package cl.duoc.tienda.service;

import cl.duoc.tienda.dto.*;
import cl.duoc.tienda.model.*;
import cl.duoc.tienda.repository.*;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;

@Service
public class VentaService {
    private final ProductoRepository productos;
    private final VentaRepository ventas;
    public VentaService(ProductoRepository productos, VentaRepository ventas) {
        this.productos = productos; this.ventas = ventas;
    }

    /** @Transactional: descuento y venta forman una sola unidad de trabajo. */
    @Transactional
    public VentaResponse registrar(VentaRequest entrada) {
        if (entrada.cantidad() <= 0) throw new IllegalArgumentException("Cantidad inválida");
        Producto p = productos.bloquearPorId(entrada.productoId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto no encontrado"));
        p.descontarStock(entrada.cantidad()); // Puede fallar por stock insuficiente.
        BigDecimal total = CalculadoraVenta.total(p.getPrecio(), entrada.cantidad());
        Venta guardada = ventas.save(new Venta(p, entrada.cantidad(), total));
        return VentaResponse.desde(guardada);
    }
    @Transactional(readOnly = true)
    public List<VentaResponse> listar() {
        return ventas.findAll(Sort.by(Sort.Direction.DESC, "creadaEn"))
                .stream().map(VentaResponse::desde).toList();
    }
}
