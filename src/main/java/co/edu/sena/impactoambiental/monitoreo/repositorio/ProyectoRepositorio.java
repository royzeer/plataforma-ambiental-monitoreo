package co.edu.sena.impactoambiental.monitoreo.repositorio;

import co.edu.sena.impactoambiental.monitoreo.modelo.Proyecto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Consultas de proyectos. Spring Data crea la implementación a partir del nombre del método.
 *
 * @author Roycer Cervantes
 * @author Sebastián Rodríguez
 */
public interface ProyectoRepositorio extends JpaRepository<Proyecto, Integer> {

    List<Proyecto> findAllByOrderByNombreAsc();
}
