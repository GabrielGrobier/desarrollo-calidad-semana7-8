package cl.duoc.tienda.repository;

import cl.duoc.tienda.model.Producto;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import java.math.BigDecimal;
import static org.assertj.core.api.Assertions.*;

/** INTEGRACIÓN DE PERSISTENCIA: aquí sí actúan JPA/Hibernate y H2 en memoria. */
@DataJpaTest
@ActiveProfiles("test")
class ProductoRepositoryJpaTest {
    @Autowired ProductoRepository repositorio;
    @Test
    void guardar_yBuscarPorSku() {
        Producto nuevo = new Producto("HAR-001", "Harina", "Despensa", new BigDecimal("990"), 4);
        repositorio.saveAndFlush(nuevo);
        assertThat(repositorio.findBySkuIgnoreCase("har-001"))
                .isPresent().get().extracting(Producto::getNombre).isEqualTo("Harina");
    }
}
