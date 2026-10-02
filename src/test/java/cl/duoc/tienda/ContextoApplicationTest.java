package cl.duoc.tienda;

import cl.duoc.tienda.controller.ProductoController;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.assertThat;

/** SMOKE / CONTEXTO SPRING: carga beans, JPA, seguridad y H2. No es test unitario puro. */
@SpringBootTest
@ActiveProfiles("test")
class ContextoApplicationTest {
    @Autowired ProductoController controlador;

    @Test void contextLoads_yControladorDisponible() {
        assertThat(controlador).isNotNull();
    }
}
