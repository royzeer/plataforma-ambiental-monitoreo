package co.edu.sena.impactoambiental.monitoreo.dto;

import co.edu.sena.impactoambiental.monitoreo.modelo.TipoLimite;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Pruebas de las validaciones del formulario (fechas, números, textos,
 * caracteres especiales y longitudes) sin levantar Spring.
 *
 * @author Roycer Cervantes
 * @author Sebastián Rodríguez
 */
class MonitoreoFormularioValidacionTest {

    private Validator validador;

    @BeforeEach
    void preparar() {
        validador = Validation.buildDefaultValidatorFactory().getValidator();
    }

    private MonitoreoFormulario crearFormularioValido() {
        MonitoreoFormulario f = new MonitoreoFormulario();
        f.setProyectoId(1);
        f.setParametro("Oxígeno disuelto");
        f.setValor(new BigDecimal("5.4"));
        f.setUnidad("mg/L");
        f.setLimitePermisible(new BigDecimal("4"));
        f.setTipoLimite(TipoLimite.MINIMO);
        f.setPuntoMuestreo("Río Manzanares - puente Avenida del Río");
        f.setFechaMuestreo(LocalDate.now().minusDays(2));
        return f;
    }

    private Set<String> camposConError(MonitoreoFormulario f) {
        return validador.validate(f).stream()
                .map(ConstraintViolation::getPropertyPath)
                .map(Object::toString)
                .collect(Collectors.toSet());
    }

    @Test
    @DisplayName("Un formulario correcto no tiene errores")
    void formularioValido() {
        assertTrue(validador.validate(crearFormularioValido()).isEmpty());
    }

    @Test
    @DisplayName("Fecha futura es rechazada")
    void fechaFutura() {
        MonitoreoFormulario f = crearFormularioValido();
        f.setFechaMuestreo(LocalDate.now().plusDays(1));
        assertEquals(Set.of("fechaMuestreo"), camposConError(f));
    }

    @Test
    @DisplayName("Valor negativo y con más de 4 decimales es rechazado")
    void valoresNumericos() {
        MonitoreoFormulario f = crearFormularioValido();
        f.setValor(new BigDecimal("-1"));
        assertEquals(Set.of("valor"), camposConError(f));
        f.setValor(new BigDecimal("1.12345"));
        assertEquals(Set.of("valor"), camposConError(f));
        f.setValor(new BigDecimal("5.4"));
        f.setLimitePermisible(BigDecimal.ZERO);
        assertEquals(Set.of("limitePermisible"), camposConError(f));
    }

    @Test
    @DisplayName("Textos vacíos, muy cortos o muy largos son rechazados")
    void longitudes() {
        MonitoreoFormulario f = crearFormularioValido();
        f.setParametro("p");
        f.setObservaciones("x".repeat(501));
        f.setPuntoMuestreo("a".repeat(121));
        assertEquals(Set.of("parametro", "observaciones", "puntoMuestreo"), camposConError(f));
    }

    @Test
    @DisplayName("Caracteres especiales peligrosos son rechazados")
    void caracteresEspeciales() {
        MonitoreoFormulario f = crearFormularioValido();
        f.setParametro("<script>alert(1)</script>");
        f.setUnidad("mg/L; DROP");
        assertEquals(Set.of("parametro", "unidad"), camposConError(f));
    }
}
