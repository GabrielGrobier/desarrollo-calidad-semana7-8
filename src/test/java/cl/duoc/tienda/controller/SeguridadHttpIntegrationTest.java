package cl.duoc.tienda.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import cl.duoc.tienda.repository.ProductoRepository;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/** INTEGRACIÓN: MockMvc con filtros REALES de seguridad, JWT + usuarios persistidos en H2. */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SeguridadHttpIntegrationTest {
    @Autowired MockMvc mvc;
    @Autowired ObjectMapper json;
    @Autowired ProductoRepository productos;
    @Autowired JwtDecoder decoder;

    private String token(String usuario, String clave) throws Exception {
        MvcResult respuesta = mvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.writeValueAsString(new Credenciales(usuario, clave))))
                .andExpect(status().isOk()).andReturn();
        return json.readTree(respuesta.getResponse().getContentAsString())
                .get("accessToken").asText();
    }
    private record Credenciales(String username, String password) { }

    @Test void salud_esPublica() throws Exception {
        mvc.perform(get("/api/salud")).andExpect(status().isOk());
    }

    @Test void loginValido_emiteJwtConRol() throws Exception {
        String jwt = token("admin", "Admin123!");
        assertThat(jwt.split("\\.")).hasSize(3); // JWT firmado: header.payload.signature.
        assertThat(decoder.decode(jwt).getClaimAsString("role")).isEqualTo("ADMIN");
    }

    @Test void loginClaveIncorrecta_devuelve401() throws Exception {
        mvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"admin\",\"password\":\"incorrecta\"}"))
                .andExpect(status().isUnauthorized());
    }

    @Test void productosSinToken_devuelve401() throws Exception {
        mvc.perform(get("/api/productos")).andExpect(status().isUnauthorized());
    }

    @Test void vendedorNoPuedeCrearProducto_devuelve403() throws Exception {
        mvc.perform(post("/api/productos")
                .header("Authorization", "Bearer " + token("vendedor", "Vendedor123!"))
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"sku\":\"AZU-008\",\"nombre\":\"Azúcar\",\"categoria\":\"Despensa\",\"precio\":1200,\"stock\":5}"))
                .andExpect(status().isForbidden());
    }

    @Test void auditorNoPuedeRegistrarVenta_devuelve403() throws Exception {
        mvc.perform(post("/api/ventas")
                .header("Authorization", "Bearer " + token("auditor", "Auditor123!"))
                .contentType(MediaType.APPLICATION_JSON).content("{\"productoId\":1,\"cantidad\":1}"))
                .andExpect(status().isForbidden());
    }

    @Test void tokenManipulado_devuelve401() throws Exception {
        String jwt = token("admin", "Admin123!");
        mvc.perform(get("/api/productos")
                .header("Authorization", "Bearer " + jwt + "alterado"))
                .andExpect(status().isUnauthorized());
    }

    @Test void adminPuedeCrearProducto_devuelve201() throws Exception {
        mvc.perform(post("/api/productos")
                .header("Authorization", "Bearer " + token("admin", "Admin123!"))
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"sku\":\"DEMO-ADM-001\",\"nombre\":\"Sal\",\"categoria\":\"Despensa\",\"precio\":900,\"stock\":7}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.sku").value("DEMO-ADM-001"));
    }

    @Test void vendedorPuedeRegistrarVenta_devuelve201() throws Exception {
        Long id = productos.findBySkuIgnoreCase("ARR-001").orElseThrow().getId();
        mvc.perform(post("/api/ventas")
                .header("Authorization", "Bearer " + token("vendedor", "Vendedor123!"))
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"productoId\":" + id + ",\"cantidad\":1}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.cantidad").value(1));
    }

    @Test void vendedorPuedeConsultarProductos_devuelve200() throws Exception {
        mvc.perform(get("/api/productos")
                .header("Authorization", "Bearer " + token("vendedor", "Vendedor123!")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }
}
