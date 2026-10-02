package cl.duoc.tienda.service;

import cl.duoc.tienda.dto.VentaRequest;
import cl.duoc.tienda.model.Producto;
import cl.duoc.tienda.repository.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.*;

/** INTEGRACIÓN REAL: se guarda una venta en H2 y se confirma el descuento de stock. */
@SpringBootTest
@ActiveProfiles("test")
@Transactional // Spring revierte los cambios al terminar la prueba; no contamina otras pruebas.
class VentaJpaIntegrationTest {
    @Autowired VentaService ventas;
    @Autowired ProductoRepository productos;
    @Autowired VentaRepository repositorioVentas;

    @Test void registrarVenta_persisteYActualizaStock() {
        Producto arroz = productos.findBySkuIgnoreCase("ARR-001").orElseThrow();
        int inicial = arroz.getStock();
        long cantidadInicial = repositorioVentas.count();
        var resultado = ventas.registrar(new VentaRequest(arroz.getId(), 2));
        productos.flush(); // Fuerza sincronización con la BD antes de verificar.
        assertThat(resultado.total()).isEqualByComparingTo("2980.00");
        assertThat(productos.findById(arroz.getId()).orElseThrow().getStock()).isEqualTo(inicial - 2);
        assertThat(repositorioVentas.count()).isEqualTo(cantidadInicial + 1);
    }
}
