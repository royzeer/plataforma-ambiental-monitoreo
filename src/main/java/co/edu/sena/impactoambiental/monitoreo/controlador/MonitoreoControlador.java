package co.edu.sena.impactoambiental.monitoreo.controlador;

import co.edu.sena.impactoambiental.monitoreo.dto.MonitoreoFormulario;
import co.edu.sena.impactoambiental.monitoreo.dto.ResumenProyecto;
import co.edu.sena.impactoambiental.monitoreo.modelo.Monitoreo;
import co.edu.sena.impactoambiental.monitoreo.modelo.TipoLimite;
import co.edu.sena.impactoambiental.monitoreo.servicio.MonitoreoServicio;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

/**
 * Controlador web del módulo de monitoreo ambiental (patrón MVC).
 *
 * Rutas:
 *   GET  /monitoreos/resumen          resumen de cumplimiento por proyecto
 *   GET  /monitoreos                  listado con filtros
 *   GET  /monitoreos/nuevo            formulario de registro
 *   POST /monitoreos                  guardar nuevo
 *   GET  /monitoreos/{id}             detalle
 *   GET  /monitoreos/{id}/editar      formulario de edición
 *   POST /monitoreos/{id}             guardar cambios
 *   GET  /monitoreos/{id}/eliminar    confirmación
 *   POST /monitoreos/{id}/eliminar    eliminar
 *
 * @author Roycer Cervantes
 * @author Sebastián Rodríguez
 */
@Controller
@RequestMapping("/monitoreos")
public class MonitoreoControlador {

    private static final String VISTA_FORMULARIO = "monitoreos/formulario";

    private final MonitoreoServicio monitoreoServicio;

    public MonitoreoControlador(MonitoreoServicio monitoreoServicio) {
        this.monitoreoServicio = monitoreoServicio;
    }

    /** Estos datos los necesitan varias vistas, se agregan siempre al modelo. */
    @ModelAttribute("tiposLimite")
    public TipoLimite[] tiposLimite() {
        return TipoLimite.values();
    }

    // ---------------------------------------------------------- Consultas

    @GetMapping("/resumen")
    public String resumen(Model modelo) {
        List<ResumenProyecto> resumen = monitoreoServicio.resumenPorProyecto();
        long total = resumen.stream().mapToLong(ResumenProyecto::total).sum();
        long cumplen = resumen.stream().mapToLong(ResumenProyecto::cumplen).sum();
        long incumplen = resumen.stream().mapToLong(ResumenProyecto::incumplen).sum();

        modelo.addAttribute("resumen", resumen);
        modelo.addAttribute("total", total);
        modelo.addAttribute("cumplen", cumplen);
        modelo.addAttribute("incumplen", incumplen);
        modelo.addAttribute("porcentaje", (cumplen + incumplen) == 0 ? 100
                : Math.round(cumplen * 100.0 / (cumplen + incumplen)));
        modelo.addAttribute("alertas", monitoreoServicio.buscar(null, null, "incumple"));
        return "monitoreos/resumen";
    }

    @GetMapping
    public String listar(@RequestParam(required = false) Integer proyectoId,
                         @RequestParam(required = false) String buscar,
                         @RequestParam(defaultValue = "todos") String norma,
                         Model modelo) {
        modelo.addAttribute("monitoreos", monitoreoServicio.buscar(proyectoId, buscar, norma));
        modelo.addAttribute("proyectos", monitoreoServicio.listarProyectos());
        modelo.addAttribute("proyectoId", proyectoId);
        modelo.addAttribute("buscar", buscar);
        modelo.addAttribute("norma", norma);
        return "monitoreos/lista";
    }

    @GetMapping("/{id}")
    public String detalle(@PathVariable Integer id, Model modelo) {
        modelo.addAttribute("monitoreo", monitoreoServicio.obtener(id));
        return "monitoreos/detalle";
    }

    // ---------------------------------------------------------- Registro

    @GetMapping("/nuevo")
    public String nuevo(@RequestParam(required = false) Integer proyectoId, Model modelo) {
        MonitoreoFormulario formulario = new MonitoreoFormulario();
        formulario.setProyectoId(proyectoId);
        return mostrarFormulario(modelo, formulario, null);
    }

    @PostMapping
    public String crear(@Valid @ModelAttribute("formulario") MonitoreoFormulario formulario,
                        BindingResult resultado, Model modelo, RedirectAttributes atributos) {
        agregarErroresDeNegocio(formulario, resultado);
        if (resultado.hasErrors()) {
            modelo.addAttribute("mensajeError", "Revise los campos marcados en rojo.");
            return mostrarFormulario(modelo, formulario, null);
        }
        Monitoreo guardado = monitoreoServicio.crear(formulario);
        atributos.addFlashAttribute("mensajeExito", mensajeGuardado("registrado", guardado));
        return "redirect:/monitoreos/" + guardado.getId();
    }

    // ---------------------------------------------------------- Edición

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Integer id, Model modelo) {
        Monitoreo monitoreo = monitoreoServicio.obtener(id);
        return mostrarFormulario(modelo, MonitoreoFormulario.desde(monitoreo), id);
    }

    @PostMapping("/{id}")
    public String actualizar(@PathVariable Integer id,
                             @Valid @ModelAttribute("formulario") MonitoreoFormulario formulario,
                             BindingResult resultado, Model modelo, RedirectAttributes atributos) {
        monitoreoServicio.obtener(id); // si no existe, el manejador de errores redirige
        agregarErroresDeNegocio(formulario, resultado);
        if (resultado.hasErrors()) {
            modelo.addAttribute("mensajeError", "Revise los campos marcados en rojo.");
            return mostrarFormulario(modelo, formulario, id);
        }
        Monitoreo actualizado = monitoreoServicio.actualizar(id, formulario);
        atributos.addFlashAttribute("mensajeExito", mensajeGuardado("actualizado", actualizado));
        return "redirect:/monitoreos/" + id;
    }

    // ---------------------------------------------------------- Eliminación

    @GetMapping("/{id}/eliminar")
    public String confirmarEliminar(@PathVariable Integer id, Model modelo) {
        modelo.addAttribute("monitoreo", monitoreoServicio.obtener(id));
        return "monitoreos/eliminar";
    }

    @PostMapping("/{id}/eliminar")
    public String eliminar(@PathVariable Integer id, RedirectAttributes atributos) {
        Monitoreo monitoreo = monitoreoServicio.obtener(id);
        monitoreoServicio.eliminar(id);
        atributos.addFlashAttribute("mensajeExito", "Se eliminó el monitoreo de "
                + monitoreo.getParametro() + " del " + monitoreo.getFechaMuestreo() + ".");
        return "redirect:/monitoreos";
    }

    // ---------------------------------------------------------- Apoyo

    private String mostrarFormulario(Model modelo, MonitoreoFormulario formulario, Integer idEdicion) {
        modelo.addAttribute("formulario", formulario);
        modelo.addAttribute("idEdicion", idEdicion);
        modelo.addAttribute("proyectos", monitoreoServicio.listarProyectos());
        return VISTA_FORMULARIO;
    }

    private void agregarErroresDeNegocio(MonitoreoFormulario formulario, BindingResult resultado) {
        Map<String, String> errores = monitoreoServicio.validarReglasDeNegocio(formulario);
        errores.forEach((campo, mensaje) -> {
            if (!resultado.hasFieldErrors(campo)) {
                resultado.rejectValue(campo, "negocio." + campo, mensaje);
            }
        });
    }

    private String mensajeGuardado(String accion, Monitoreo monitoreo) {
        String estado = switch (monitoreo.getEstadoNorma()) {
            case "Cumple" -> "Cumple la norma.";
            case "No cumple" -> "ATENCIÓN: el valor NO cumple el límite permisible.";
            default -> "No tiene límite de referencia para comparar.";
        };
        return "Monitoreo de " + monitoreo.getParametro() + " " + accion + " correctamente. " + estado;
    }
}
