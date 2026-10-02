package cl.duoc.tienda.ejemplos;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.*;

/**
 * NIVEL: UNITARIO PURO. Sin Spring, sin mocks, sin base de datos.
 * Objetivo pedagógico: patrón Arrange/Act/Assert y las aserciones básicas de JUnit 5.
 */
@DisplayName("Calculadora básica")
class CalculadoraTest {

    private final Calculadora calculadora = new Calculadora();

    @Test
    void sumar_dosNumerosPositivos_devuelveElTotal() {
        // Arrange: los datos de entrada ya están definidos (2 y 3).
        // Act: se ejecuta únicamente el método bajo prueba.
        int resultado = calculadora.sumar(2, 3);
        // Assert: se compara el resultado con el valor esperado.
        assertEquals(5, resultado);
    }

    @Test
    void restar_numeroMayorMenosMenor_devuelvePositivo() {
        int resultado = calculadora.restar(10, 4);
        assertEquals(6, resultado);
    }

    @ParameterizedTest(name = "{0} / {1} = {2}")
    @CsvSource({"10, 2, 5", "9, 3, 3", "0, 5, 0"})
    void dividir_casosValidos_calculaCorrectamente(int dividendo, int divisor, int esperado) {
        // Un solo método, tres ejecuciones distintas gracias a @CsvSource.
        assertEquals(esperado, calculadora.dividir(dividendo, divisor));
    }

    @Test
    void dividir_porCero_lanzaExcepcion() {
        // assertThrows: el caso negativo, la excepción ES el resultado esperado.
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> calculadora.dividir(8, 0));
        assertEquals("No se puede dividir por cero", error.getMessage());
    }
}
