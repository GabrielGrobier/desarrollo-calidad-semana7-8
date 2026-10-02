package cl.duoc.tienda.service;
import java.math.BigDecimal;

/** Clase pura: perfecta para comenzar con JUnit sin mocks ni Spring. */
public final class CalculadoraVenta {
    private CalculadoraVenta() { }
    public static BigDecimal total(BigDecimal precio, int cantidad) {
        if (precio == null || precio.signum() < 0 || cantidad <= 0) {
            throw new IllegalArgumentException("Precio o cantidad inválidos");
        }
        return precio.multiply(BigDecimal.valueOf(cantidad));
    }
}
