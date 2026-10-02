package cl.duoc.tienda.service;
/** Error de dominio: el recurso solicitado no existe. */
public class RecursoNoEncontradoException extends RuntimeException {
    public RecursoNoEncontradoException(String mensaje) { super(mensaje); }
}
