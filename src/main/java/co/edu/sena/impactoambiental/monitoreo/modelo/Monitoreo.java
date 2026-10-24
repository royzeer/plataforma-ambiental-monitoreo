package co.edu.sena.impactoambiental.monitoreo.modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Medición de un parámetro ambiental en un proyecto (tabla "monitoreos").
 *
 * Ejemplos: pH = 7.2 unidades, DBO5 = 35 mg/L, oxígeno disuelto = 3.2 mg/L.
 * El campo cumpleNorma lo calcula el servicio comparando el valor con el
 * límite permisible; queda en null cuando no hay límite de referencia.
 *
 * @author Roycer Cervantes
 * @author Sebastián Rodríguez
 */
@Entity
@Table(name = "monitoreos")
public class Monitoreo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "proyecto_id", nullable = false)
    private Proyecto proyecto;

    @Column(nullable = false, length = 80)
    private String parametro;

    @Column(nullable = false, precision = 12, scale = 4)
    private BigDecimal valor;

    @Column(nullable = false, length = 30)
    private String unidad;

    @Column(name = "limite_permisible", precision = 12, scale = 4)
    private BigDecimal limitePermisible;

    @Column(name = "tipo_limite", nullable = false)
    private TipoLimite tipoLimite = TipoLimite.MAXIMO;

    @Column(name = "cumple_norma")
    private Boolean cumpleNorma;

    @Column(name = "punto_muestreo", length = 120)
    private String puntoMuestreo;

    @Column(name = "fecha_muestreo", nullable = false)
    private LocalDate fechaMuestreo;

    @Column(columnDefinition = "TEXT")
    private String observaciones;

    /** Lo llena MySQL con DEFAULT CURRENT_TIMESTAMP. */
    @Column(name = "creado_en", insertable = false, updatable = false)
    private LocalDateTime creadoEn;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Proyecto getProyecto() {
        return proyecto;
    }

    public void setProyecto(Proyecto proyecto) {
        this.proyecto = proyecto;
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

    public Boolean getCumpleNorma() {
        return cumpleNorma;
    }

    public void setCumpleNorma(Boolean cumpleNorma) {
        this.cumpleNorma = cumpleNorma;
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

    public LocalDateTime getCreadoEn() {
        return creadoEn;
    }

    /** Texto para la etiqueta de estado en las vistas. */
    public String getEstadoNorma() {
        if (cumpleNorma == null) {
            return "Sin referencia";
        }
        return cumpleNorma ? "Cumple" : "No cumple";
    }
}
