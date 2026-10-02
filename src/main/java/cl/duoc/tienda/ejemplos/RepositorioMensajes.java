package cl.duoc.tienda.ejemplos;

/**
 * EJEMPLO DIDÁCTICO 3: dependencia "cara" (podría representar una BD o una API externa).
 * En el test de ServicioSaludo esta interfaz se reemplaza por un mock de Mockito.
 */
public interface RepositorioMensajes {

    /** Devuelve la plantilla para el idioma dado, o null si no está soportado. */
    String buscarSaludoPorIdioma(String idioma);

    /** Registra (por ejemplo, en una bitácora o base de datos) el saludo enviado. */
    void registrarSaludoEnviado(String usuario, String mensaje);
}
