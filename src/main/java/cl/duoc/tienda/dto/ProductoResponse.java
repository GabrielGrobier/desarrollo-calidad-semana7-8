package cl.duoc.tienda.dto;
import cl.duoc.tienda.model.Producto;
import java.math.BigDecimal;

/** DTO de salida: no devolvemos directamente entidades JPA al cliente. */
public record ProductoResponse(Long id, String sku, String nombre, String categoria,
                               BigDecimal precio, int stock) {
    public static ProductoResponse desde(Producto p) {
        return new ProductoResponse(p.getId(), p.getSku(), p.getNombre(),
                p.getCategoria(), p.getPrecio(), p.getStock());
    }
}
