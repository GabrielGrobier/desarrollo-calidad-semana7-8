package cl.duoc.tienda.repository;

import cl.duoc.tienda.model.Producto;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

/** Spring Data genera el acceso a datos; no escribimos SQL para cada operación. */
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    boolean existsBySkuIgnoreCase(String sku);
    Optional<Producto> findBySkuIgnoreCase(String sku);

    /** Para registrar ventas, bloqueamos la fila mientras se comprueba y descuenta stock. */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from Producto p where p.id = :id")
    Optional<Producto> bloquearPorId(@Param("id") Long id);
}
