package co.edu.sena.impactoambiental.monitoreo.servicio;

import co.edu.sena.impactoambiental.monitoreo.modelo.TipoLimite;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Pruebas unitarias de la regla que decide si una medición cumple la norma.
 *
 * @author Roycer Cervantes
 * @author Sebastián Rodríguez
 */
class EvaluacionNormaTest {

    private static BigDecimal n(String valor) {
        return new BigDecimal(valor);
    }

    @Test
    @DisplayName("Límite máximo: un valor por debajo o igual cumple")
    void limiteMaximoCumple() {
        assertEquals(true, MonitoreoServicio.evaluarCumplimiento(n("25"), n("30"), TipoLimite.MAXIMO));
        assertEquals(true, MonitoreoServicio.evaluarCumplimiento(n("30.0000"), n("30"), TipoLimite.MAXIMO));
    }

    @Test
    @DisplayName("Límite máximo: un valor por encima no cumple")
    void limiteMaximoNoCumple() {
        assertEquals(false, MonitoreoServicio.evaluarCumplimiento(n("30.01"), n("30"), TipoLimite.MAXIMO));
    }

    @Test
    @DisplayName("Límite mínimo (oxígeno disuelto): por debajo no cumple, igual o por encima cumple")
    void limiteMinimo() {
        assertEquals(false, MonitoreoServicio.evaluarCumplimiento(n("3.2"), n("4"), TipoLimite.MINIMO));
        assertEquals(true, MonitoreoServicio.evaluarCumplimiento(n("4"), n("4"), TipoLimite.MINIMO));
        assertEquals(true, MonitoreoServicio.evaluarCumplimiento(n("5.6"), n("4"), TipoLimite.MINIMO));
    }

    @Test
    @DisplayName("Sin límite de referencia el resultado queda en null")
    void sinLimite() {
        assertNull(MonitoreoServicio.evaluarCumplimiento(n("7.1"), null, TipoLimite.MAXIMO));
    }
}
