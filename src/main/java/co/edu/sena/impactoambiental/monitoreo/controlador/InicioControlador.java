package co.edu.sena.impactoambiental.monitoreo.controlador;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Redirige la raíz del sitio al resumen del módulo.
 *
 * @author Roycer Cervantes
 * @author Sebastián Rodríguez
 */
@Controller
public class InicioControlador {

    @GetMapping("/")
    public String inicio() {
        return "redirect:/monitoreos/resumen";
    }
}
