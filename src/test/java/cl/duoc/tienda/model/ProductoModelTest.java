package cl.duoc.tienda.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

/**
 * PRUEBA UNITARIA PURA: se comprueban reglas del dominio Producto.
 * Esta clase no requiere Spring, repositorio, Docker ni una base de datos.
 */
@DisplayName("Reglas de inventario de Producto")
class ProductoModelTest {

    /** Caso positivo: una venta válida disminuye las unidades disponibles. */
    @Test
    void descontarStock_cantidadValida_disminuyeElInventario() {
        // Arrange: preparar producto con 10 unidades.
        Producto producto = new Producto("ARR-001", "Arroz", "Despensa", new BigDecimal("1490"), 10);
        // Act: ejecutar la regla real del dominio.
        producto.descontarStock(3);
        // Assert: comprobar el nuevo stock; 10 - 3 = 7.
        assertEquals(7, producto.getStock());
    }

    /** Caso negativo: se rechaza una venta superior al stock disponible. */
    @Test
    void descontarStock_stockInsuficiente_lanzaExcepcionSinModificarStock() {
        Producto producto = new Producto("LEC-001", "Leche", "Lacteos", new BigDecimal("1190"), 2);
        IllegalStateException error = assertThrows(IllegalStateException.class,
                () -> producto.descontarStock(3));
        assertEquals("Stock insuficiente", error.getMessage());
        assertEquals(2, producto.getStock(), "Una operación rechazada no debe descontar unidades");
    }

    /** Comprueba el límite: no se admite vender cero unidades. */
    @Test
    void descontarStock_cantidadCero_lanzaExcepcion() {
        Producto producto = new Producto("AZU-001", "Azucar", "Despensa", new BigDecimal("1200"), 4);
        assertThrows(IllegalArgumentException.class, () -> producto.descontarStock(0));
        assertEquals(4, producto.getStock());
    }

    /** Comprueba que tampoco se admita asignar stock negativo. */
    @Test
    void actualizarStock_valorNegativo_lanzaExcepcion() {
        Producto producto = new Producto("HAR-001", "Harina", "Despensa", new BigDecimal("990"), 4);
        assertThrows(IllegalArgumentException.class, () -> producto.actualizarStock(-1));
        assertEquals(4, producto.getStock());
    }
}
