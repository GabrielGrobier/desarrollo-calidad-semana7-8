package cl.duoc.tienda.controller;
import cl.duoc.tienda.dto.*;
import cl.duoc.tienda.service.VentaService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/ventas")
public class VentaController {
    private final VentaService servicio;
    public VentaController(VentaService servicio) { this.servicio = servicio; }
    @PostMapping public ResponseEntity<VentaResponse> registrar(@Valid @RequestBody VentaRequest entrada) {
        return ResponseEntity.status(HttpStatus.CREATED).body(servicio.registrar(entrada));
    }
    @GetMapping public List<VentaResponse> listar() { return servicio.listar(); }
}
