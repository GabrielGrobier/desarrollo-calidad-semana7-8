package cl.duoc.tienda.controller;

import cl.duoc.tienda.dto.*;
import cl.duoc.tienda.service.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.*;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.math.BigDecimal;
import java.util.List;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/** TEST DE CAPA WEB: MockMvc simula HTTP, NO levanta servidor TCP ni MySQL. */
@WebMvcTest(ProductoController.class)
@AutoConfigureMockMvc(addFilters = false) // Aquí se prueba el contrato HTTP; seguridad se prueba aparte.
class ProductoControllerWebTest {
    @Autowired MockMvc mvc;
    @MockitoBean ProductoService servicio; // Mock del service inyectado en el controller.

    @Test
    void getProductos_retorna200YJson() throws Exception {
        ProductoResponse arroz = new ProductoResponse(1L, "ARR-001", "Arroz", "Despensa",
                new BigDecimal("1490"), 10);
        when(servicio.listar()).thenReturn(List.of(arroz));
        mvc.perform(get("/api/productos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].sku").value("ARR-001"))
                .andExpect(jsonPath("$[0].stock").value(10));
        verify(servicio).listar();
    }

    @Test
    void getProductoInexistente_retorna404() throws Exception {
        when(servicio.buscar(99L)).thenThrow(new RecursoNoEncontradoException("Producto no encontrado"));
        mvc.perform(get("/api/productos/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Producto no encontrado"));
    }

    @Test
    void postProductoValido_retorna201() throws Exception {
        when(servicio.crear(any(ProductoRequest.class))).thenReturn(
                new ProductoResponse(9L, "AZU-001", "Azúcar", "Despensa", new BigDecimal("1200"), 5));
        mvc.perform(post("/api/productos").contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"sku":"AZU-001","nombre":"Azúcar","categoria":"Despensa","precio":1200,"stock":5}
                    """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(9))
                .andExpect(jsonPath("$.nombre").value("Azúcar"));
    }

    @Test
    void postProductoInvalido_retorna400YNoInvocaServicio() throws Exception {
        mvc.perform(post("/api/productos").contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"sku":"","nombre":"","categoria":"Despensa","precio":-1,"stock":-3}
                    """))
                .andExpect(status().isBadRequest());
        verifyNoInteractions(servicio);
    }
}
