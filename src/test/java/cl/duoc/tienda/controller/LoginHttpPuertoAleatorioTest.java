package cl.duoc.tienda.controller;

import cl.duoc.tienda.dto.LoginRequest;
import cl.duoc.tienda.dto.LoginResponse;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.*;

/** INTEGRACIÓN HTTP REAL: RANDOM_PORT levanta servidor embebido (distinto de MockMvc). */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class LoginHttpPuertoAleatorioTest {
    @LocalServerPort int puerto; // El puerto es asignado por Spring; evitamos conflictos con 8080.
    @Autowired TestRestTemplate cliente;

    private String url(String ruta) { return "http://localhost:" + puerto + ruta; }

    @Test void loginYConsultaAutenticada_porHttpReal() {
        // Arrange: credenciales sintéticas cargadas por DatosDemo sobre la H2 temporal.
        LoginRequest credenciales = new LoginRequest("vendedor", "Vendedor123!");
        // Act: esto sí hace una petición HTTP real al servidor embebido.
        ResponseEntity<LoginResponse> login = cliente.postForEntity(
                url("/api/auth/login"), credenciales, LoginResponse.class);
        // Assert: el contrato entrega un token y es posible utilizarlo después.
        assertThat(login.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(login.getBody()).isNotNull();
        String token = login.getBody().accessToken();
        assertThat(token).isNotBlank();

        HttpHeaders encabezados = new HttpHeaders();
        encabezados.setBearerAuth(token);
        ResponseEntity<String> respuesta = cliente.exchange(url("/api/productos"), HttpMethod.GET,
                new HttpEntity<>(encabezados), String.class);
        assertThat(respuesta.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(respuesta.getBody()).contains("ARR-001");
    }

    @Test void contraseñaIncorrecta_porHttpRealDevuelve401() {
        ResponseEntity<String> respuesta = cliente.postForEntity(url("/api/auth/login"),
                new LoginRequest("vendedor", "clave-erronea"), String.class);
        assertThat(respuesta.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }
}
