package cl.duoc.tienda.ejemplos;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullSource;
import static org.junit.jupiter.api.Assertions.*;

/**
 * NIVEL: UNITARIO PURO. Refuerza assertTrue/assertFalse y casos límite (boundary testing):
 * ¿qué pasa justo en el borde de una regla (largo mínimo, null, vacío)?
 */
@DisplayName("Reglas de una clave válida")
class ValidadorClaveTest {

    private final ValidadorClave validador = new ValidadorClave();

    @Test
    void esValida_claveConLetraNumeroYLargoSuficiente_devuelveTrue() {
        assertTrue(validador.esValida("Duoc2026!"));
    }

    @ParameterizedTest(name = "\"{0}\" no es válida")
    @NullSource
    @CsvSource({"corta1A", "sinnumeroymayus", "SINMINUSCULASNI1"})
    void esValida_casosInvalidos_devuelveFalse(String clave) {
        assertFalse(validador.esValida(clave));
    }
}
