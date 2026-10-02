package cl.duoc.tienda.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

/** Venta histórica: conserva el precio al momento de comprar. */
@Entity
@Table(name = "ventas")
public class Venta {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;
    @Column(nullable = false)
    private int cantidad;
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal precioUnitario;
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal total;
    @Column(nullable = false)
    private Instant creadaEn;

    protected Venta() { }
    public Venta(Producto producto, int cantidad, BigDecimal total) {
        this.producto = producto; this.cantidad = cantidad;
        this.precioUnitario = producto.getPrecio(); this.total = total;
        this.creadaEn = Instant.now();
    }
    public Long getId() { return id; }
    public Producto getProducto() { return producto; }
    public int getCantidad() { return cantidad; }
    public BigDecimal getPrecioUnitario() { return precioUnitario; }
    public BigDecimal getTotal() { return total; }
    public Instant getCreadaEn() { return creadaEn; }
}
