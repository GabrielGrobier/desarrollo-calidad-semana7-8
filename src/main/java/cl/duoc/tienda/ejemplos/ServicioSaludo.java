package cl.duoc.tienda.ejemplos;

/**
 * EJEMPLO DIDÁCTICO 3: servicio que depende de RepositorioMensajes.
 * Se prueba con Mockito para no necesitar una implementación real de la dependencia.
 */
public class ServicioSaludo {

    private final RepositorioMensajes repositorio;

    public ServicioSaludo(RepositorioMensajes repositorio) {
        this.repositorio = repositorio;
    }

    public String saludar(String usuario, String idioma) {
        if (usuario == null || usuario.isBlank()) {
            throw new IllegalArgumentException("El usuario es obligatorio");
        }
        String plantilla = repositorio.buscarSaludoPorIdioma(idioma);
        if (plantilla == null) {
            throw new IllegalStateException("Idioma no soportado: " + idioma);
        }
        String mensaje = plantilla.replace("{usuario}", usuario);
        repositorio.registrarSaludoEnviado(usuario, mensaje);
        return mensaje;
    }
}
