# Módulo de Monitoreo Ambiental – Spring Boot

[![Build](https://github.com/royzeer/plataforma-ambiental-monitoreo/actions/workflows/build.yml/badge.svg)](https://github.com/royzeer/plataforma-ambiental-monitoreo/actions/workflows/build.yml)
![Java 17](https://img.shields.io/badge/Java_17-ED8B00?logo=openjdk&logoColor=white)
![Spring Boot 3](https://img.shields.io/badge/Spring_Boot_3-6DB33F?logo=springboot&logoColor=white)
![Thymeleaf](https://img.shields.io/badge/Thymeleaf-005F0F?logo=thymeleaf&logoColor=white)
![MySQL 8](https://img.shields.io/badge/MySQL_8-4479A1?logo=mysql&logoColor=white)
![JUnit 5](https://img.shields.io/badge/JUnit_5-25A162?logo=junit5&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-C71A36?logo=apachemaven&logoColor=white)
![licencia MIT](https://img.shields.io/badge/licencia_MIT-blue?logo=opensourceinitiative&logoColor=white)

> *English summary at the [end of this file](#english-summary).*

**Plataforma Digital Integral para la Gestión de Proyectos de Impacto Ambiental**

| | |
|---|---|
| **Programa** | Análisis y Desarrollo de Software (228118) – SENA |
| **Evidencias** | GA7-220501096-AA3-EV01 (codificación) y AA3-EV02 (codificado y probado) |
| **Aprendices** | Roycer Cervantes · Sebastián Rodríguez |
| **Tipo de aplicación** | Web |
| **Tecnologías** | Java 17 · Spring Boot 3.3 (Web MVC, Data JPA, Validation) · Thymeleaf · MySQL 8 · JUnit 5 · Maven · Git |

## ¿Qué hace este módulo?

En los proyectos ambientales se toman muestras de agua, aire o suelo (pH, oxígeno disuelto, DBO5, coliformes, PM10...). Este módulo permite **registrar esas mediciones** y **compararlas automáticamente con su límite permisible** para saber si el proyecto está cumpliendo la norma. Es una parte del sistema que antes se llevaba en hojas de Excel sueltas y donde los incumplimientos se veían tarde.

Funciones (historias de usuario):

| HU | Como... | quiero... | para... |
|---|---|---|---|
| HU-01 | gestor ambiental | registrar un monitoreo con su valor, unidad y límite | dejar soporte de cada medición |
| HU-02 | gestor ambiental | que el sistema calcule si la medición cumple la norma | detectar incumplimientos sin hacer cálculos a mano |
| HU-03 | gestor o consultor | consultar y filtrar monitoreos por proyecto, parámetro y resultado | encontrar rápido una medición |
| HU-04 | gestor o consultor | ver el detalle de un monitoreo | revisar todos sus datos |
| HU-05 | gestor ambiental | editar un monitoreo | corregir errores de digitación |
| HU-06 | gestor ambiental | eliminar un monitoreo con confirmación | quitar registros duplicados sin borrar por accidente |
| HU-07 | coordinador | ver un resumen de cumplimiento por proyecto y las alertas | tomar decisiones a tiempo |

Usa la **misma base de datos** (`impacto_ambiental_db`, tabla `monitoreos`) que la API REST y el front en React del proyecto.

## Arquitectura (MVC por capas)

```
src/main/java/co/edu/sena/impactoambiental/monitoreo/
├── MonitoreoAplicacion.java        # arranque de Spring Boot
├── controlador/                    # recibe las peticiones HTTP (GET/POST)
│   ├── MonitoreoControlador.java
│   └── InicioControlador.java
├── servicio/MonitoreoServicio.java # reglas de negocio (cumplimiento, fechas, resumen)
├── repositorio/                    # Spring Data JPA (consultas a MySQL)
├── modelo/                         # entidades JPA: Monitoreo, Proyecto, TipoLimite
├── dto/                            # MonitoreoFormulario (validaciones), ResumenProyecto
└── excepcion/                      # manejo centralizado de errores
src/main/resources/
├── application.properties          # conexión a MySQL, puerto 8081
├── messages.properties             # mensajes de conversión en español
├── templates/                      # vistas Thymeleaf
└── static/css/estilos.css
src/test/java/...                   # 9 pruebas unitarias (JUnit 5)
```

## Estándares de codificación aplicados

- Paquetes en minúscula: `co.edu.sena.impactoambiental.monitoreo.servicio`
- Clases en PascalCase con sufijo según su capa: `MonitoreoControlador`, `MonitoreoServicio`, `MonitoreoRepositorio`
- Métodos y variables en camelCase con verbos: `evaluarCumplimiento`, `buscarConFiltros`, `fechaMuestreo`
- Constantes en MAYÚSCULA: `PATRON_TEXTO`, `VISTA_FORMULARIO`
- Inyección de dependencias por constructor, comentarios Javadoc en cada clase y método público
- Nombres en español, igual que en la API y el front del proyecto

## Cómo ejecutarlo

1. Tener la base de datos creada (`database/schema.sql` de la API) y los datos de prueba (`npm run seed` en la API, o `database/datos_prueba.sql`).
2. Poner usuario y contraseña de MySQL en `src/main/resources/application.properties`.
3. Ejecutar:
   ```bash
   mvn spring-boot:run
   ```
   (o desde IntelliJ / NetBeans / VS Code: ejecutar `MonitoreoAplicacion`).
4. Abrir **http://localhost:8081**

Pruebas unitarias: `mvn test` → 9 pruebas, 0 fallas.

## Repositorio

https://github.com/royzeer/plataforma-ambiental-monitoreo

---

## English summary

Web module (Spring Boot MVC) to **record environmental monitoring data** (pH, dissolved oxygen, BOD5, coliforms, PM10…) and automatically check each measurement against its **regulatory limit**, so non-compliance is detected on time.

**Features:** full CRUD with server-side validation, filters by project, parameter and result, delete confirmation, and a per-project compliance summary with alerts.

**Tech stack:** Java 17 · Spring Boot 3.3 (Web MVC, Data JPA, Validation) · Thymeleaf · MySQL 8 · JUnit 5 · Maven · GitHub Actions.

**Architecture:** layered MVC (controllers → services → Spring Data JPA repositories), DTOs for forms and a centralized error handler. It shares the same database as the project's REST API and React frontend.

**Run it:** create the database with `database/schema.sql` and `database/datos_prueba.sql`, set your MySQL credentials in `application.properties`, then `mvn spring-boot:run` and open http://localhost:8081. Tests: `mvn test`.
