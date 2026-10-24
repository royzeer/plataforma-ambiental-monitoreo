package co.edu.sena.impactoambiental.monitoreo.servicio;

import co.edu.sena.impactoambiental.monitoreo.dto.MonitoreoFormulario;
import co.edu.sena.impactoambiental.monitoreo.dto.ResumenProyecto;
import co.edu.sena.impactoambiental.monitoreo.excepcion.RecursoNoEncontradoException;
import co.edu.sena.impactoambiental.monitoreo.modelo.Monitoreo;
import co.edu.sena.impactoambiental.monitoreo.modelo.Proyecto;
import co.edu.sena.impactoambiental.monitoreo.modelo.TipoLimite;
import co.edu.sena.impactoambiental.monitoreo.repositorio.MonitoreoRepositorio;
import co.edu.sena.impactoambiental.monitoreo.repositorio.ProyectoRepositorio;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Reglas de negocio del módulo de monitoreo.
 *
 * Aquí se decide si una medición cumple o no la norma, se revisan las reglas
 * que dependen de otros datos (como la fecha de inicio del proyecto) y se
 * arman los totales del resumen.
 *
 * @author Roycer Cervantes
 * @author Sebastián Rodríguez
 */
@Service
public class MonitoreoServicio {

    private static final Set<String> FILTROS_NORMA = Set.of("todos", "cumple", "incumple", "sin_referencia");

    private final MonitoreoRepositorio monitoreoRepositorio;
    private final ProyectoRepositorio proyectoRepositorio;

    /** Inyección de dependencias por constructor (la recomendada por Spring). */
    public MonitoreoServicio(MonitoreoRepositorio monitoreoRepositorio,
                             ProyectoRepositorio proyectoRepositorio) {
        this.monitoreoRepositorio = monitoreoRepositorio;
        this.proyectoRepositorio = proyectoRepositorio;
    }

    /**
     * Decide si un valor cumple la norma.
     *
     * @param valor  valor medido
     * @param limite límite permisible (puede ser null)
     * @param tipo   si el límite es máximo o mínimo
     * @return true si cumple, false si no, null si no hay límite para comparar
     */
    public static Boolean evaluarCumplimiento(BigDecimal valor, BigDecimal limite, TipoLimite tipo) {
        if (valor == null || limite == null) {
            return null;
        }
        int comparacion = valor.compareTo(limite);
        return tipo == TipoLimite.MINIMO ? comparacion >= 0 : comparacion <= 0;
    }

    @Transactional(readOnly = true)
    public List<Monitoreo> buscar(Integer proyectoId, String texto, String estadoNorma) {
        String textoLimpio = (texto == null || texto.isBlank()) ? null : texto.trim();
        String filtro = FILTROS_NORMA.contains(estadoNorma) ? estadoNorma : "todos";
        return monitoreoRepositorio.buscarConFiltros(proyectoId, textoLimpio, filtro);
    }

    @Transactional(readOnly = true)
    public Monitoreo obtener(Integer id) {
        return monitoreoRepositorio.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "El monitoreo #" + id + " no existe o ya fue eliminado."));
    }

    @Transactional(readOnly = true)
    public List<Proyecto> listarProyectos() {
        return proyectoRepositorio.findAllByOrderByNombreAsc();
    }

    /**
     * Reglas que no se pueden revisar con anotaciones porque dependen de la base de datos.
     *
     * @return mapa campo -> mensaje (vacío si no hay errores)
     */
    @Transactional(readOnly = true)
    public Map<String, String> validarReglasDeNegocio(MonitoreoFormulario formulario) {
        Map<String, String> errores = new LinkedHashMap<>();
        if (formulario.getProyectoId() == null) {
            return errores; // ya lo reporta @NotNull
        }
        proyectoRepositorio.findById(formulario.getProyectoId()).ifPresentOrElse(proyecto -> {
            if (formulario.getFechaMuestreo() != null
                    && formulario.getFechaMuestreo().isBefore(proyecto.getFechaInicio())) {
                errores.put("fechaMuestreo", "La fecha no puede ser anterior al inicio del proyecto ("
                        + proyecto.getFechaInicio() + ").");
            }
        }, () -> errores.put("proyectoId", "El proyecto seleccionado no existe."));
        return errores;
    }

    /** Registra un monitoreo nuevo y calcula si cumple la norma. */
    @Transactional
    public Monitoreo crear(MonitoreoFormulario formulario) {
        Monitoreo monitoreo = new Monitoreo();
        copiarDatos(formulario, monitoreo);
        return monitoreoRepositorio.save(monitoreo);
    }

    /** Actualiza un monitoreo y vuelve a evaluar la norma con los datos nuevos. */
    @Transactional
    public Monitoreo actualizar(Integer id, MonitoreoFormulario formulario) {
        Monitoreo monitoreo = obtener(id);
        copiarDatos(formulario, monitoreo);
        return monitoreoRepositorio.save(monitoreo);
    }

    @Transactional
    public void eliminar(Integer id) {
        Monitoreo monitoreo = obtener(id);
        monitoreoRepositorio.delete(monitoreo);
    }

    /** Totales por proyecto para la pantalla de resumen. */
    @Transactional(readOnly = true)
    public List<ResumenProyecto> resumenPorProyecto() {
        List<Monitoreo> todos = monitoreoRepositorio.buscarConFiltros(null, null, "todos");
        List<ResumenProyecto> resumen = new ArrayList<>();
        for (Proyecto proyecto : proyectoRepositorio.findAllByOrderByNombreAsc()) {
            List<Monitoreo> delProyecto = todos.stream()
                    .filter(m -> m.getProyecto().getId().equals(proyecto.getId()))
                    .toList();
            long cumplen = delProyecto.stream().filter(m -> Boolean.TRUE.equals(m.getCumpleNorma())).count();
            long incumplen = delProyecto.stream().filter(m -> Boolean.FALSE.equals(m.getCumpleNorma())).count();
            resumen.add(new ResumenProyecto(proyecto.getId(), proyecto.getCodigo(), proyecto.getNombre(),
                    delProyecto.size(), cumplen, incumplen, delProyecto.size() - cumplen - incumplen));
        }
        return resumen;
    }

    // ------------------------------------------------------------------
    private void copiarDatos(MonitoreoFormulario formulario, Monitoreo monitoreo) {
        Proyecto proyecto = proyectoRepositorio.findById(formulario.getProyectoId())
                .orElseThrow(() -> new RecursoNoEncontradoException("El proyecto seleccionado no existe."));

        monitoreo.setProyecto(proyecto);
        monitoreo.setParametro(formulario.getParametro().trim());
        monitoreo.setValor(formulario.getValor());
        monitoreo.setUnidad(formulario.getUnidad().trim());
        monitoreo.setLimitePermisible(formulario.getLimitePermisible());
        monitoreo.setTipoLimite(formulario.getTipoLimite());
        monitoreo.setPuntoMuestreo(textoOpcional(formulario.getPuntoMuestreo()));
        monitoreo.setFechaMuestreo(formulario.getFechaMuestreo());
        monitoreo.setObservaciones(textoOpcional(formulario.getObservaciones()));
        monitoreo.setCumpleNorma(evaluarCumplimiento(formulario.getValor(),
                formulario.getLimitePermisible(), formulario.getTipoLimite()));
    }

    private static String textoOpcional(String texto) {
        return (texto == null || texto.isBlank()) ? null : texto.trim();
    }
}
