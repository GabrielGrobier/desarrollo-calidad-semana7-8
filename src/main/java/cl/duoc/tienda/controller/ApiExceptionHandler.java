package cl.duoc.tienda.controller;

import cl.duoc.tienda.service.*;
import org.springframework.http.*;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

/** Traduce excepciones de negocio a estados HTTP verificables con MockMvc. */
@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<Map<String, String>> noEncontrado(Exception ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", ex.getMessage()));
    }
    @ExceptionHandler(ConflictoException.class)
    public ResponseEntity<Map<String, String>> conflicto(Exception ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", ex.getMessage()));
    }
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, String>> stock(Exception ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", ex.getMessage()));
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> argumento(Exception ex) {
        return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
    }
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String, String>> credenciales(Exception ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Credenciales inválidas"));
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> validacion(MethodArgumentNotValidException ex) {
        String detalle = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage()).findFirst().orElse("Datos inválidos");
        return ResponseEntity.badRequest().body(Map.of("error", detalle));
    }
}
