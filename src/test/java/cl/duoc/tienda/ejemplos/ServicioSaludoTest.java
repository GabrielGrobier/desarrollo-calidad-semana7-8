package cl.duoc.tienda.ejemplos;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * NIVEL: UNITARIO con Mockito. RepositorioMensajes se reemplaza por un mock:
 * no hay base de datos, no hay red, solo se simula su comportamiento con when/thenReturn.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("ServicioSaludo con RepositorioMensajes simulado")
class ServicioSaludoTest {

    @Mock
    RepositorioMensajes repositorio;

    @InjectMocks
    ServicioSaludo servicio;

    @Test
    void saludar_usuarioValido_devuelveMensajePersonalizado() {
        // Arrange: le enseñamos al mock cómo responder.
        when(repositorio.buscarSaludoPorIdioma("es")).thenReturn("Hola {usuario}!");
        // Act
        String mensaje = servicio.saludar("Ana", "es");
        // Assert: el resultado y que sí hubo colaboración con la dependencia.
        assertEquals("Hola Ana!", mensaje);
        verify(repositorio).registrarSaludoEnviado("Ana", "Hola Ana!");
    }

    @Test
    void saludar_usuarioVacio_lanzaExcepcionYNoConsultaRepositorio() {
        // La validación ocurre ANTES de tocar la dependencia "cara".
        assertThatThrownBy(() -> servicio.saludar("", "es"))
                .isInstanceOf(IllegalArgumentException.class);
        verifyNoInteractions(repositorio);
    }

    @Test
    void saludar_idiomaNoSoportado_lanzaExcepcionYNoRegistraNada() {
        when(repositorio.buscarSaludoPorIdioma("xx")).thenReturn(null);
        assertThatThrownBy(() -> servicio.saludar("Ana", "xx"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("xx");
        verify(repositorio, never()).registrarSaludoEnviado(any(), any());
    }

    @Test
    void saludar_capturaElMensajeRealmenteRegistrado() {
        when(repositorio.buscarSaludoPorIdioma("en")).thenReturn("Hi {usuario}");
        servicio.saludar("Bob", "en");
        // ArgumentCaptor: inspecciona el valor real que se envió al mock.
        ArgumentCaptor<String> mensajeCapturado = ArgumentCaptor.forClass(String.class);
        verify(repositorio).registrarSaludoEnviado(eq("Bob"), mensajeCapturado.capture());
        assertEquals("Hi Bob", mensajeCapturado.getValue());
    }
}
