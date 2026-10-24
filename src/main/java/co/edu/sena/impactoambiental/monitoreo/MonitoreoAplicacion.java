package co.edu.sena.impactoambiental.monitoreo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Punto de entrada del módulo de Monitoreo Ambiental.
 *
 * Spring Boot arranca un Tomcat embebido en el puerto 8081 y configura
 * automáticamente JPA, Thymeleaf y las validaciones.
 *
 * @author Roycer Cervantes
 * @author Sebastián Rodríguez
 */
@SpringBootApplication
public class MonitoreoAplicacion {

    public static void main(String[] args) {
        SpringApplication.run(MonitoreoAplicacion.class, args);
    }
}
