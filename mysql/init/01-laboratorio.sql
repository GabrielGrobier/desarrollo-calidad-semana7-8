-- Evidencia de inicialización de BD desde Dockerfile. No almacenar secretos aquí.
CREATE DATABASE IF NOT EXISTS tienda_abarrotes_lab
  CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE tienda_abarrotes_lab;
-- Hibernate crea las tablas de entidades cuando arranca la app (ddl-auto=update, solo demo).
CREATE TABLE IF NOT EXISTS instalacion_laboratorio (
  id INT PRIMARY KEY,
  descripcion VARCHAR(100) NOT NULL
);
INSERT IGNORE INTO instalacion_laboratorio (id, descripcion)
VALUES (1, 'Base de datos inicializada mediante imagen MySQL personalizada');
