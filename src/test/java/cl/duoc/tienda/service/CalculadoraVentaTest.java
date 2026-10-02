package cl.duoc.tienda.service;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

/** NIVEL: UNITARIO PURO. No hay Spring, red, MySQL ni mocks. */
@DisplayName("Cálculo del total de la venta")
class CalculadoraVentaTest {
    @ParameterizedTest(name = "{0} x {1} = {2}")
    @CsvSource({"1490.00, 2, 2980.00", "1190.00, 1, 1190.00", "0.00, 5, 0.00"})
    void total_calculaCorrectamente(String precio, int cantidad, String esperado) {
        // ARRANGE: prepararnos los datos y el valor que esperamos.
        BigDecimal valor = new BigDecimal(precio);
        // ACT: se ejecuta únicamente el método bajo prueba.
        BigDecimal actual = CalculadoraVenta.total(valor, cantidad);
        // ASSERT: compareTo evita falsas diferencias por escala decimal (2.0 versus 2.00).
        assertEquals(0, new BigDecimal(esperado).compareTo(actual));
    }

    @Test
    void total_rechazaCantidadCero() {
        // assertThrows prueba un escenario negativo: la excepción ES lo esperado.
        assertThrows(IllegalArgumentException.class,
                () -> CalculadoraVenta.total(new BigDecimal("1490"), 0));
    }

    @Test
    void total_rechazaPrecioNegativo() {
        assertThrows(IllegalArgumentException.class,
                () -> CalculadoraVenta.total(new BigDecimal("-1"), 1));
    }
}
