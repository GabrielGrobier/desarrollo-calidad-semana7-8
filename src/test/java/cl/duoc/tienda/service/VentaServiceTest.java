package cl.duoc.tienda.service;

import cl.duoc.tienda.dto.*;
import cl.duoc.tienda.model.*;
import cl.duoc.tienda.repository.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.util.Optional;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/** NIVEL: UNITARIO; simula las dos dependencias de persistencia. */
@ExtendWith(MockitoExtension.class)
class VentaServiceTest {
    @Mock ProductoRepository productos;
    @Mock VentaRepository ventas;
    @InjectMocks VentaService servicio;

    @Test
    void registrar_conStock_calculaTotalYDescuenta() {
        Producto arroz = new Producto("ARR-001", "Arroz", "Despensa", new BigDecimal("1490"), 20);
        when(productos.bloquearPorId(1L)).thenReturn(Optional.of(arroz));
        when(ventas.save(any(Venta.class))).thenAnswer(inv -> inv.getArgument(0));
        VentaResponse respuesta = servicio.registrar(new VentaRequest(1L, 2));
        assertThat(respuesta.total()).isEqualByComparingTo("2980");
        assertThat(arroz.getStock()).isEqualTo(18);
        verify(productos).bloquearPorId(1L);
        verify(ventas).save(any(Venta.class));
    }

    @Test
    void registrar_stockInsuficiente_noGuardaVenta() {
        Producto leche = new Producto("LEC-001", "Leche", "Lácteos", new BigDecimal("1190"), 1);
        when(productos.bloquearPorId(2L)).thenReturn(Optional.of(leche));
        assertThatThrownBy(() -> servicio.registrar(new VentaRequest(2L, 2)))
                .isInstanceOf(IllegalStateException.class).hasMessageContaining("Stock insuficiente");
        assertThat(leche.getStock()).isEqualTo(1);
        verify(ventas, never()).save(any());
    }

    @Test
    void registrar_productoInexistente_noGuarda() {
        when(productos.bloquearPorId(500L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> servicio.registrar(new VentaRequest(500L, 1)))
                .isInstanceOf(RecursoNoEncontradoException.class);
        verifyNoInteractions(ventas);
    }

    @Test
    void registrar_cantidadCero_noConsultaRepositorio() {
        assertThatThrownBy(() -> servicio.registrar(new VentaRequest(1L, 0)))
                .isInstanceOf(IllegalArgumentException.class);
        verifyNoInteractions(productos, ventas);
    }
}
