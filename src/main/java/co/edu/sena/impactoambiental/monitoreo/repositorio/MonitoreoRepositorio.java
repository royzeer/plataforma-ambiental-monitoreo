package co.edu.sena.impactoambiental.monitoreo.repositorio;

import co.edu.sena.impactoambiental.monitoreo.modelo.Monitoreo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Acceso a datos de monitoreos.
 * JpaRepository ya trae save, findById, findAll y deleteById (el CRUD básico).
 *
 * @author Roycer Cervantes
 * @author Sebastián Rodríguez
 */
public interface MonitoreoRepositorio extends JpaRepository<Monitoreo, Integer> {

    /**
     * Busca monitoreos con filtros opcionales: si un filtro llega en null no se aplica.
     *
     * @param proyectoId  proyecto (null = todos)
     * @param texto       parte del nombre del parámetro (null = todos)
     * @param estadoNorma "cumple", "incumple", "sin_referencia" o "todos"
     * @return monitoreos del más reciente al más antiguo
     */
    @Query("""
            SELECT m FROM Monitoreo m JOIN FETCH m.proyecto p
            WHERE (:proyectoId IS NULL OR p.id = :proyectoId)
              AND (:texto IS NULL OR LOWER(m.parametro) LIKE LOWER(CONCAT('%', :texto, '%')))
              AND (:estadoNorma = 'todos'
                   OR (:estadoNorma = 'cumple' AND m.cumpleNorma = true)
                   OR (:estadoNorma = 'incumple' AND m.cumpleNorma = false)
                   OR (:estadoNorma = 'sin_referencia' AND m.cumpleNorma IS NULL))
            ORDER BY m.fechaMuestreo DESC, m.id DESC
            """)
    List<Monitoreo> buscarConFiltros(@Param("proyectoId") Integer proyectoId,
                                     @Param("texto") String texto,
                                     @Param("estadoNorma") String estadoNorma);
}
