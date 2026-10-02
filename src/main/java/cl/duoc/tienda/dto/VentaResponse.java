package cl.duoc.tienda.dto;
import cl.duoc.tienda.model.Venta;
import java.math.BigDecimal;
import java.time.Instant;
public record VentaResponse(Long id, Long productoId, String producto,
                            int cantidad, BigDecimal total, Instant fecha) {
    public static VentaResponse desde(Venta v) {
        return new VentaResponse(v.getId(), v.getProducto().getId(),
                v.getProducto().getNombre(), v.getCantidad(), v.getTotal(), v.getCreadaEn());
    }
}
