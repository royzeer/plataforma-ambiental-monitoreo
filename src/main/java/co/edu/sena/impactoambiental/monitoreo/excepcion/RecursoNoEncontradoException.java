package co.edu.sena.impactoambiental.monitoreo.excepcion;

/**
 * Se lanza cuando se pide un monitoreo o proyecto que no existe.
 *
 * @author Roycer Cervantes
 * @author Sebastián Rodríguez
 */
public class RecursoNoEncontradoException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public RecursoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
