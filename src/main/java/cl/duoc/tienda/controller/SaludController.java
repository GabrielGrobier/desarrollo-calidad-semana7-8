package cl.duoc.tienda.controller;
import java.util.Map;
import org.springframework.web.bind.annotation.*;
/** Ruta pública sencilla para comprobar que el backend responde. */
@RestController
public class SaludController {
    @GetMapping("/api/salud") public Map<String, String> estado() { return Map.of("estado", "operativo"); }
}
