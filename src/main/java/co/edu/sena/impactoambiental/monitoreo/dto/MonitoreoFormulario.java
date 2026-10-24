package co.edu.sena.impactoambiental.monitoreo.dto;

import co.edu.sena.impactoambiental.monitoreo.modelo.Monitoreo;
import co.edu.sena.impactoambiental.monitoreo.modelo.TipoLimite;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Datos que llegan del formulario de monitoreo, con sus reglas de validación.
 *
 * Usamos un DTO aparte de la entidad para no exponer campos que el usuario
 * no debe tocar (id, cumpleNorma, creadoEn) y para tener las validaciones
 * en un solo lugar. Spring las revisa con @Valid en el controlador.
 *
 * @author Roycer Cervantes
 * @author Sebastián Rodríguez
 */
public class MonitoreoFormulario {

    /** Letras con tildes, números, espacios y . , ( ) / % - (ej. "PM2.5", "DBO5", "Oxígeno disuelto"). */
    public static final String PATRON_TEXTO = "^[A-Za-zÁÉÍÓÚÜÑáéíóúüñ0-9 .,()/%\\-]*$";

    /*
     * Nota: los patrones aceptan la cadena vacía a propósito. Si el campo llega vacío,
     * solo se muestra el mensaje de @NotBlank y no tres mensajes repetidos.
     */

    /** Unidades: mg/L, µg/m³, NMP/100 mL, °C, unidades de pH, %. */
    public static final String PATRON_UNIDAD = "^[A-Za-z0-9µ/%°³² .]*$";

    @NotNull(message = "Seleccione el proyecto.")
    private Integer proyectoId;

    @NotBlank(message = "El parámetro es obligatorio.")
    @Size(max = 80, message = "El parámetro no puede pasar de 80 caracteres.")
    @Pattern(regexp = "^(|.{2,})$", message = "El parámetro debe tener al menos 2 caracteres.")
    @Pattern(regexp = PATRON_TEXTO, message = "El parámetro tiene caracteres no permitidos.")
    private String parametro;

    @NotNull(message = "El valor medido es obligatorio.")
    @DecimalMin(value = "0.0", message = "El valor no puede ser negativo.")
    @DecimalMax(value = "99999999.9999", message = "El valor es demasiado grande.")
    @Digits(integer = 8, fraction = 4, message = "Máximo 8 enteros y 4 decimales.")
    private BigDecimal valor;

    @NotBlank(message = "La unidad es obligatoria.")
    @Size(max = 30, message = "La unidad no puede pasar de 30 caracteres.")
    @Pattern(regexp = PATRON_UNIDAD, message = "La unidad tiene caracteres no permitidos (ej. válidos: mg/L, µg/m³, °C).")
    private String unidad;

    @DecimalMin(value = "0.0", inclusive = false, message = "El límite debe ser mayor que cero.")
    @Digits(integer = 8, fraction = 4, message = "Máximo 8 enteros y 4 decimales.")
    private BigDecimal limitePermisible;

    @NotNull(message = "Indique si el límite es máximo o mínimo.")
    private TipoLimite tipoLimite = TipoLimite.MAXIMO;

    @Size(max = 120, message = "El punto de muestreo no puede pasar de 120 caracteres.")
    @Pattern(regexp = PATRON_TEXTO, message = "El punto de muestreo tiene caracteres no permitidos.")
    private String puntoMuestreo;

    @NotNull(message = "La fecha de muestreo es obligatoria.")
    @PastOrPresent(message = "La fecha de muestreo no puede ser futura.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate fechaMuestreo;

    @Size(max = 500, message = "Las observaciones no pueden pasar de 500 caracteres.")
    private String observaciones;

    /** Llena el formulario con los datos de un monitoreo existente (para editar). */
    public static MonitoreoFormulario desde(Monitoreo monitoreo) {
        MonitoreoFormulario formulario = new MonitoreoFormulario();
        formulario.setProyectoId(monitoreo.getProyecto().getId());
        formulario.setParametro(monitoreo.getParametro());
        formulario.setValor(monitoreo.getValor().stripTrailingZeros());
        formulario.setUnidad(monitoreo.getUnidad());
        formulario.setLimitePermisible(monitoreo.getLimitePermisible() == null
                ? null : monitoreo.getLimitePermisible().stripTrailingZeros());
        formulario.setTipoLimite(monitoreo.getTipoLimite());
        formulario.setPuntoMuestreo(monitoreo.getPuntoMuestreo());
        formulario.setFechaMuestreo(monitoreo.getFechaMuestreo());
        formulario.setObservaciones(monitoreo.getObservaciones());
        return formulario;
    }

    public Integer getProyectoId() {
        return proyectoId;
    }

    public void setProyectoId(Integer proyectoId) {
        this.proyectoId = proyectoId;
    }

    public String getParametro() {
        return parametro;
    }

    public void setParametro(String parametro) {
        this.parametro = parametro;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getUnidad() {
        return unidad;
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }

    public BigDecimal getLimitePermisible() {
        return limitePermisible;
    }

    public void setLimitePermisible(BigDecimal limitePermisible) {
        this.limitePermisible = limitePermisible;
    }

    public TipoLimite getTipoLimite() {
        return tipoLimite;
    }

    public void setTipoLimite(TipoLimite tipoLimite) {
        this.tipoLimite = tipoLimite;
    }

    public String getPuntoMuestreo() {
        return puntoMuestreo;
    }

    public void setPuntoMuestreo(String puntoMuestreo) {
        this.puntoMuestreo = puntoMuestreo;
    }

    public LocalDate getFechaMuestreo() {
        return fechaMuestreo;
    }

    public void setFechaMuestreo(LocalDate fechaMuestreo) {
        this.fechaMuestreo = fechaMuestreo;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}
