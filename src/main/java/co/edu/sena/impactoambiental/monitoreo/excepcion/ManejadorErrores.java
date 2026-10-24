package co.edu.sena.impactoambiental.monitoreo.excepcion;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Atrapa los errores de todos los controladores para que el usuario vea un
 * mensaje claro en vez de una página de error técnica.
 *
 * @author Roycer Cervantes
 * @author Sebastián Rodríguez
 */
@ControllerAdvice
public class ManejadorErrores {

    /** Monitoreo o proyecto inexistente: vuelve al listado con un aviso. */
    @ExceptionHandler(RecursoNoEncontradoException.class)
    public String manejarNoEncontrado(RecursoNoEncontradoException ex, RedirectAttributes atributos) {
        atributos.addFlashAttribute("mensajeError", ex.getMessage());
        return "redirect:/monitoreos";
    }

    /** Alguien escribió /monitoreos/abc en la URL. */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public String manejarIdInvalido(RedirectAttributes atributos) {
        atributos.addFlashAttribute("mensajeError", "El identificador de la URL no es válido.");
        return "redirect:/monitoreos";
    }
}
