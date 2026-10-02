package cl.duoc.tienda.ejemplos;

/**
 * EJEMPLO DIDÁCTICO 1: clase sin ninguna dependencia externa.
 * Sirve para introducir JUnit puro: Arrange/Act/Assert, assertThrows y @ParameterizedTest.
 */
public class Calculadora {

    public int sumar(int a, int b) {
        return a + b;
    }

    public int restar(int a, int b) {
        return a - b;
    }

    public int dividir(int dividendo, int divisor) {
        if (divisor == 0) {
            throw new IllegalArgumentException("No se puede dividir por cero");
        }
        return dividendo / divisor;
    }
}
