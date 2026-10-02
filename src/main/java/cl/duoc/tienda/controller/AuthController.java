package cl.duoc.tienda.controller;
import cl.duoc.tienda.dto.*;
import cl.duoc.tienda.security.JwtService;
import jakarta.validation.Valid;
import org.springframework.security.authentication.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwt;
    public AuthController(AuthenticationManager authenticationManager, JwtService jwt) {
        this.authenticationManager = authenticationManager; this.jwt = jwt;
    }
    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest entrada) {
        // El provider compara la contraseña recibida con el hash BCrypt de la BD.
        var auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(entrada.username(), entrada.password()));
        return jwt.emitir(auth);
    }
}
