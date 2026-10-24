-- Datos de prueba mínimos (usuarios, proyectos y monitoreos)
-- Ejecutar DESPUÉS de schema.sql. Contraseña de todos los usuarios: Ambiental2026
-- (Si ya corrieron "npm run seed" en la API, estos datos ya existen y no hace falta.)
USE impacto_ambiental_db;

INSERT INTO usuarios (id, nombre, correo, password_hash, rol) VALUES
(1, 'Administrador General', 'admin@plataforma.com', '$2a$10$om2680UiGcSQaJgIu9yUJ.v51dLLzajcR./vHWK2oLq0F7GDLDMHy', 'admin'),
(2, 'Roycer Cervantes', 'roycer.cervantes@plataforma.com', '$2a$10$om2680UiGcSQaJgIu9yUJ.v51dLLzajcR./vHWK2oLq0F7GDLDMHy', 'gestor'),
(3, 'Sebastián Rodríguez', 'sebastian.rodriguez@plataforma.com', '$2a$10$om2680UiGcSQaJgIu9yUJ.v51dLLzajcR./vHWK2oLq0F7GDLDMHy', 'gestor'),
(4, 'Consultor Externo', 'consultor@plataforma.com', '$2a$10$om2680UiGcSQaJgIu9yUJ.v51dLLzajcR./vHWK2oLq0F7GDLDMHy', 'consultor');

INSERT INTO proyectos (id, codigo, nombre, descripcion, tipo, municipio, departamento, latitud, longitud, fecha_inicio, fecha_fin, presupuesto, estado, responsable_id) VALUES
(1, 'PIA-2026-0001', 'Restauración de manglar en la Ciénaga Grande de Santa Marta', 'Siembra de propágulos de mangle rojo y recuperación de caños para mejorar el intercambio de agua dulce y salada.', 'restauracion', 'Pueblo Viejo', 'Magdalena', '10.991000', '-74.282000', '2026-02-01', '2027-01-31', '450000000.00', 'ejecucion', 2),
(2, 'PIA-2026-0002', 'Planta de tratamiento de aguas residuales del sector Gaira', 'Construcción de una PTAR con lagunas de estabilización para reducir vertimientos directos al mar.', 'saneamiento', 'Santa Marta', 'Magdalena', '11.183000', '-74.226000', '2026-05-10', '2027-12-15', '1800000000.00', 'planeacion', 3),
(3, 'PIA-2026-0003', 'Monitoreo de calidad del agua del río Manzanares', 'Campañas de muestreo para evaluar la calidad del río a su paso por la ciudad.', 'conservacion', 'Santa Marta', 'Magdalena', '11.237000', '-74.188000', '2026-01-15', '2026-12-15', '85000000.00', 'ejecucion', 2);

-- Monitoreos de ejemplo
INSERT INTO monitoreos (proyecto_id, parametro, valor, unidad, limite_permisible, tipo_limite, cumple_norma, punto_muestreo, fecha_muestreo, observaciones) VALUES
(1, 'Salinidad', '28.5000', 'PSU', '35.0000', 'maximo', 1, 'Caño Clarín', '2026-08-20', NULL),
(1, 'Oxígeno disuelto', '3.1000', 'mg/L', '4.0000', 'minimo', 0, 'Boca de la Barra', '2026-08-20', 'Valor bajo, posible proliferación de algas'),
(2, 'DBO5 (agua cruda)', '320.0000', 'mg/L', '90.0000', 'maximo', 0, 'Colector principal', '2026-07-12', 'Línea base antes del tratamiento'),
(3, 'pH', '7.4000', 'unidades', '9.0000', 'maximo', 1, 'Puente de Bavaria', '2026-03-18', NULL),
(3, 'Coliformes totales', '24000.0000', 'NMP/100mL', '20000.0000', 'maximo', 0, 'Desembocadura', '2026-03-18', NULL);
