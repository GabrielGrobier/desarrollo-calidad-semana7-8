package cl.duoc.tienda.controller;

import cl.duoc.tienda.dto.*;
import cl.duoc.tienda.service.ProductoService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/** Controlador: transforma solicitudes HTTP en llamadas al servicio. */
@RestController
@RequestMapping("/api/productos")
public class ProductoController {
    private final ProductoService servicio;
    public ProductoController(ProductoService servicio) { this.servicio = servicio; }

    @GetMapping public List<ProductoResponse> listar() { return servicio.listar(); }
    @GetMapping("/{id}") public ProductoResponse buscar(@PathVariable Long id) { return servicio.buscar(id); }
    @PostMapping public ResponseEntity<ProductoResponse> crear(@Valid @RequestBody ProductoRequest entrada) {
        return ResponseEntity.status(HttpStatus.CREATED).body(servicio.crear(entrada));
    }
    @PatchMapping("/{id}/stock")
    public ProductoResponse ajustarStock(@PathVariable Long id, @Valid @RequestBody StockRequest entrada) {
        return servicio.ajustarStock(id, entrada.stock());
    }
}
