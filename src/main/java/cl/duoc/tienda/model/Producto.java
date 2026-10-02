package cl.duoc.tienda.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

/** Entidad de persistencia: representa una fila de la tabla productos. */
@Entity
@Table(name = "productos")
public class Producto {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true, length = 30)
    private String sku;
    @Column(nullable = false, length = 120)
    private String nombre;
    @Column(nullable = false, length = 60)
    private String categoria;
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal precio;
    @Column(nullable = false)
    private int stock;

    protected Producto() { }
    public Producto(String sku, String nombre, String categoria, BigDecimal precio, int stock) {
        this.sku = sku; this.nombre = nombre; this.categoria = categoria;
        this.precio = precio; actualizarStock(stock);
    }
    public void actualizarStock(int nuevoStock) {
        if (nuevoStock < 0) throw new IllegalArgumentException("El stock no puede ser negativo");
        this.stock = nuevoStock;
    }
    /** Regla de negocio real: no se puede vender más de lo disponible. */
    public void descontarStock(int cantidad) {
        if (cantidad <= 0) throw new IllegalArgumentException("La cantidad debe ser positiva");
        if (cantidad > stock) throw new IllegalStateException("Stock insuficiente");
        stock -= cantidad;
    }
    public Long getId() { return id; }
    public String getSku() { return sku; }
    public String getNombre() { return nombre; }
    public String getCategoria() { return categoria; }
    public BigDecimal getPrecio() { return precio; }
    public int getStock() { return stock; }
}
