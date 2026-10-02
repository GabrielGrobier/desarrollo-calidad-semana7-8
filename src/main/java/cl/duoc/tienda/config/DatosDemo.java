package cl.duoc.tienda.config;

import cl.duoc.tienda.model.*;
import cl.duoc.tienda.repository.*;
import java.math.BigDecimal;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;

/** Datos sintéticos, sólo para aprender. También carga las cuentas en la BD de test. */
@Configuration
public class DatosDemo {
    @Bean CommandLineRunner datosIniciales(CuentaUsuarioRepository usuarios,
                                            ProductoRepository productos,
                                            PasswordEncoder encoder) {
        return args -> {
            crearUsuarioSiFalta(usuarios, encoder, "admin", "Admin123!", Rol.ADMIN);
            crearUsuarioSiFalta(usuarios, encoder, "vendedor", "Vendedor123!", Rol.VENDEDOR);
            crearUsuarioSiFalta(usuarios, encoder, "auditor", "Auditor123!", Rol.AUDITOR);
            if (productos.count() == 0) {
                productos.save(new Producto("ARR-001", "Arroz 1 kg", "Despensa", new BigDecimal("1490.00"), 20));
                productos.save(new Producto("LEC-001", "Leche 1 L", "Lácteos", new BigDecimal("1190.00"), 12));
            }
        };
    }
    private static void crearUsuarioSiFalta(CuentaUsuarioRepository repo, PasswordEncoder encoder,
                                             String nombre, String claveDemo, Rol rol) {
        if (repo.findByUsername(nombre).isEmpty()) {
            repo.save(new CuentaUsuario(nombre, encoder.encode(claveDemo), rol));
        }
    }
}
