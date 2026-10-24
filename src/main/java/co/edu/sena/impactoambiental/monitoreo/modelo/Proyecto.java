package co.edu.sena.impactoambiental.monitoreo.modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.Immutable;

import java.time.LocalDate;

/**
 * Proyecto ambiental (tabla "proyectos").
 *
 * En este módulo el proyecto solo se consulta: se crea y edita desde el
 * módulo de proyectos o desde la API. Por eso la entidad es @Immutable
 * y solo mapeamos las columnas que necesitamos mostrar.
 *
 * @author Roycer Cervantes
 * @author Sebastián Rodríguez
 */
@Entity
@Immutable
@Table(name = "proyectos")
public class Proyecto {

    @Id
    private Integer id;

    @Column(length = 20)
    private String codigo;

    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(nullable = false, length = 80)
    private String municipio;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @Column(nullable = false)
    private String estado;

    protected Proyecto() {
        // Requerido por JPA
    }

    public Proyecto(Integer id, String codigo, String nombre, String municipio,
                    LocalDate fechaInicio, String estado) {
        this.id = id;
        this.codigo = codigo;
        this.nombre = nombre;
        this.municipio = municipio;
        this.fechaInicio = fechaInicio;
        this.estado = estado;
    }

    public Integer getId() {
        return id;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public String getMunicipio() {
        return municipio;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public String getEstado() {
        return estado;
    }

    /** Texto corto para listas desplegables: "PIA-2026-0001 · Nombre". */
    public String getEtiqueta() {
        return codigo + " · " + nombre;
    }
}
