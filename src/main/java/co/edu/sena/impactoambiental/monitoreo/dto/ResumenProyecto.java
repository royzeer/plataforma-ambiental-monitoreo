package co.edu.sena.impactoambiental.monitoreo.dto;

/**
 * Totales de monitoreos de un proyecto para la pantalla de resumen.
 *
 * @param proyectoId     id del proyecto
 * @param codigo         código del proyecto (PIA-2026-0001)
 * @param nombre         nombre del proyecto
 * @param total          monitoreos registrados
 * @param cumplen        monitoreos dentro de la norma
 * @param incumplen      monitoreos fuera de la norma
 * @param sinReferencia  monitoreos sin límite para comparar
 *
 * @author Roycer Cervantes
 * @author Sebastián Rodríguez
 */
public record ResumenProyecto(Integer proyectoId, String codigo, String nombre,
                              long total, long cumplen, long incumplen, long sinReferencia) {

    /** Porcentaje de cumplimiento sobre los monitoreos que sí tienen límite. */
    public int porcentajeCumplimiento() {
        long evaluados = cumplen + incumplen;
        return evaluados == 0 ? 100 : (int) Math.round(cumplen * 100.0 / evaluados);
    }
}
