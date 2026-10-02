# EDICIÓN REHECHA (2026): Inicio rápido en INICIO_AQUI.txt; ruta de clase en docs/07_RECORRIDO_DE_CLASE.md.

# Tienda de Abarrotes — laboratorio pedagógico de testing (Semana 7)

**Proyecto original de demostración, NO la actividad formativa de veterinaria.** Está diseñado a partir de los temas de CDY2203: JUnit, Mockito, MockMvc, Spring Boot, pruebas de integración, autenticación, MySQL y evidencias. Incluye comentarios en el código, ejemplos negativos y documentación docente.

## Requisitos

Java 21, Maven 3.9+ y, para el despliegue completo, Docker Desktop o Docker Engine + Compose. Internet para descargar dependencias en el primer build. No hace falta NVD ni API Key ni ejecutar OWASP Dependency-Check para usar este repositorio.

## 1. Ejecutar TODOS los tests (no necesitas Docker)

```powershell
cd CDY2203_Laboratorio_Java_Testing_2026
java -version
mvn -version
mvn clean verify
```

El perfil de prueba (`@ActiveProfiles("test")`) utiliza una base de datos temporal H2 en memoria. Los tests unitarios con Mockito ni siquiera requieren BD. `verify` genera además un reporte JaCoCo sin imponer un mínimo artificial.

**Resultados reales:** `target/surefire-reports/` y `target/site/jacoco/index.html`.

```powershell
start .\target\site\jacoco\index.html
Get-ChildItem .\target\surefire-reports\
```

Para ejecutar una única clase en PowerShell:

```powershell
mvn "-Dtest=CalculadoraVentaTest" test
mvn "-Dtest=VentaServiceTest" test
mvn "-Dtest=ProductoControllerWebTest" test
mvn "-Dtest=SeguridadHttpIntegrationTest" test
```

## 2. Levantar MySQL + backend en Docker (entorno de aplicación)

```powershell
Copy-Item .env.example .env
# Edita .env para cambiar credenciales demo si lo deseas.
docker compose up -d --build
docker compose ps
docker compose logs -f backend
```

MySQL dentro de la red Docker es `mysql:3306`; expuesto al PC es `localhost:3307`. API: http://localhost:8080. Salud pública: http://localhost:8080/api/salud. La creación de tablas de dominio la realiza JPA al inicio; `mysql/Dockerfile` copia el SQL inicial de infraestructura. Si modificas `01-laboratorio.sql`, recuerda que los scripts de `/docker-entrypoint-initdb.d` sólo corren con volumen de datos vacío: `docker compose down -v` **borra los datos locales**.

Consulta de evidencia de BD:

```powershell
docker compose exec mysql mysql -utienda_app -pSoloDemo123! tienda_abarrotes_lab -e "SHOW TABLES; SELECT * FROM instalacion_laboratorio;"
```

En ejecución Java fuera de Docker, primero levanta sólo la BD: `docker compose up -d --build mysql`, luego ejecuta `mvn spring-boot:run`. Desde el host el puerto es 3307 por configuración `application.yml`.

## 3. Acceso y roles de laboratorio

| Usuario sintético | Contraseña DEMO | Permisos |
|---|---|---|
| admin | Admin123! | Productos: leer/crear/stock; ventas: crear/listar |
| vendedor | Vendedor123! | Productos: leer; ventas: crear |
| auditor | Auditor123! | Productos: leer; ventas: listar |

Las claves se guardan con BCrypt en la BD. **Son credenciales exclusivamente docentes**, nunca para Internet/producción. El secret JWT demo se debe sustituir con `APP_JWT_SECRET`; no subir tokens, secretos ni `.env` a Git.

### Login (PowerShell)

```powershell
$body = @{ username='vendedor'; password='Vendedor123!' } | ConvertTo-Json
$login = Invoke-RestMethod 'http://localhost:8080/api/auth/login' -Method Post -ContentType 'application/json' -Body $body
$headers = @{ Authorization = "Bearer $($login.accessToken)" }
Invoke-RestMethod 'http://localhost:8080/api/productos' -Headers $headers
```

Ejemplo con script: `.\scripts\probar-api.ps1` (requiere servicio levantado).

### Rutas

| Método y ruta | Rol |
|---|---|
| GET `/api/salud` | Pública |
| POST `/api/auth/login` | Pública |
| GET `/api/productos` y `/{id}` | ADMIN, VENDEDOR, AUDITOR |
| POST `/api/productos` | ADMIN |
| PATCH `/api/productos/{id}/stock` | ADMIN |
| POST `/api/ventas` | ADMIN, VENDEDOR |
| GET `/api/ventas` | ADMIN, AUDITOR |

## 4. Cómo estudiar el proyecto (orden recomendado)

1. `service/CalculadoraVenta.java`, `CalculadoraVentaTest` y `ProductoModelTest`: JUnit y Arrange/Act/Assert.
2. `ProductoServiceTest` y `VentaServiceTest`: mocks, `when`, `verify`, `never`, escenarios negativos.
3. `ProductoControllerWebTest`: MockMvc, HTTP y JSON sin filtros.
4. `ContextoApplicationTest`: arranque del contexto Spring.
5. `ProductoRepositoryJpaTest` y `VentaJpaIntegrationTest`: integración real con H2.
6. `SeguridadHttpIntegrationTest`: filtros de seguridad activos, JWT, 401 y 403.
7. `LoginHttpPuertoAleatorioTest`: petición HTTP real con servidor embebido en puerto aleatorio.

Ver documentación: `docs/01_GUIA_DOCENTE.md`, `docs/02_MAPA_DE_PRUEBAS.md`, `docs/03_COBERTURA_DE_LA_PAUTA_COMO_CONTEXTO.md`, `docs/04_MODELO_DE_EVIDENCIAS.md`, `docs/05_CONCEPTOS_Y_FUENTES.md` y `docs/06_ANOTACIONES_PARA_EXPLICAR.md`.

## 5. Distinguir herramientas y objetivos

`mvn test`: ejecuta pruebas. `mvn clean verify`: ejecuta pruebas + genera cobertura JaCoCo. `mvn dependency-check:check` era el análisis SCA de Semana 6; este laboratorio NO requiere NVD y NO confunde vulnerabilidades de dependencias con fallos funcionales.

## 6. Límites explícitos del ejemplo

- H2 para pruebas automatizadas, MySQL para levantar el backend: **no son la misma evidencia**.
- El test MockMvc aislado no prueba permisos HTTP; esa responsabilidad queda en `SeguridadHttpIntegrationTest`.
- 100 % de cobertura no significa ausencia de bugs o CVE.
- Credenciales/clave JWT y `ddl-auto=update` son sólo de desarrollo docente.
- Spring Boot 3.5.16 es continuidad pedagógica, pero llegó al final de soporte OSS 3.5; en producción planificar migración a la rama vigente con soporte.

## 7. Validación que debe hacer el docente antes de usar en clase

```powershell
mvn clean verify
docker compose up -d --build
docker compose ps
.\scripts\probar-api.ps1
```

Los resultados y capturas reales deben obtenerse en tu PC. No se incluyen capturas ficticias.
