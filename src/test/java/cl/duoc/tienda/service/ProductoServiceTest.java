package cl.duoc.tienda.service;

import cl.duoc.tienda.dto.*;
import cl.duoc.tienda.model.Producto;
import cl.duoc.tienda.repository.ProductoRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.util.Optional;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/** NIVEL: UNITARIO del service. El repositorio se reemplaza por un mock. */
@ExtendWith(MockitoExtension.class)
class ProductoServiceTest {
    @Mock ProductoRepository repositorio;
    @InjectMocks ProductoService servicio;

    @Test
    void buscar_idExistente_devuelveProducto() {
        // ARRANGE: DB simulada; ningún SELECT real se ejecutará.
        Producto arroz = new Producto("ARR-001", "Arroz", "Despensa", new BigDecimal("1490"), 10);
        when(repositorio.findById(1L)).thenReturn(Optional.of(arroz));
        // ACT
        ProductoResponse resultado = servicio.buscar(1L);
        // ASSERT: verificamos salida y colaboración con la dependencia.
        assertThat(resultado.nombre()).isEqualTo("Arroz");
        assertThat(resultado.stock()).isEqualTo(10);
        verify(repositorio, times(1)).findById(1L);
    }

    @Test
    void buscar_idInexistente_lanzaExcepcion() {
        when(repositorio.findById(99L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> servicio.buscar(99L))
                .isInstanceOf(RecursoNoEncontradoException.class)
                .hasMessageContaining("99");
    }

    @Test
    void crear_skuLibre_guardaProducto() {
        ProductoRequest entrada = new ProductoRequest("AZU-001", "Azúcar", "Despensa",
                new BigDecimal("1200"), 7);
        when(repositorio.existsBySkuIgnoreCase("AZU-001")).thenReturn(false);
        when(repositorio.save(any(Producto.class))).thenAnswer(inv -> inv.getArgument(0));
        ProductoResponse respuesta = servicio.crear(entrada);
        assertThat(respuesta.nombre()).isEqualTo("Azúcar");
        // ArgumentCaptor revisa qué objeto se entregó a la dependencia simulada.
        ArgumentCaptor<Producto> captor = ArgumentCaptor.forClass(Producto.class);
        verify(repositorio).save(captor.capture());
        assertThat(captor.getValue().getStock()).isEqualTo(7);
    }

    @Test
    void crear_skuDuplicado_noDebeGuardar() {
        ProductoRequest entrada = new ProductoRequest("ARR-001", "Arroz", "Despensa",
                new BigDecimal("1490"), 10);
        when(repositorio.existsBySkuIgnoreCase("ARR-001")).thenReturn(true);
        assertThatThrownBy(() -> servicio.crear(entrada)).isInstanceOf(ConflictoException.class);
        verify(repositorio, never()).save(any());
    }

    @Test
    void ajustarStock_actualizaEntidad() {
        Producto arroz = new Producto("ARR-001", "Arroz", "Despensa", new BigDecimal("1490"), 10);
        when(repositorio.findById(1L)).thenReturn(Optional.of(arroz));
        ProductoResponse respuesta = servicio.ajustarStock(1L, 25);
        assertThat(respuesta.stock()).isEqualTo(25);
        assertThat(arroz.getStock()).isEqualTo(25);
    }
}
