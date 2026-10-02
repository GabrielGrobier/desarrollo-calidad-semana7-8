package cl.duoc.tienda.repository;
import cl.duoc.tienda.model.CuentaUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface CuentaUsuarioRepository extends JpaRepository<CuentaUsuario, Long> {
    Optional<CuentaUsuario> findByUsername(String username);
}
