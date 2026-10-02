# Contextualización de los 6 criterios, sin reproducir la actividad formativa

Este repositorio fue creado para mostrar las **habilidades** de la pauta Semana 7. El ejemplo oficial trabaja con una veterinaria y pruebas entregadas; nuestro ejemplo trabaja con una tienda de abarrotes e incluye pruebas diseñadas para docencia.

| Criterio formativo | Cómo se enseña en nuestro ejemplo | Evidencia de demostración |
|---|---|---|
| Backend configurado y ejecutable | Spring Boot, endpoints, JWT y capas | `GET /api/salud` responde tras `docker compose up -d --build` |
| Cadenas de conexión correctas | `application.yml`, `application-test.yml`, `compose.yaml` | Host `mysql` dentro de Compose y `localhost:3307` desde Windows |
| Ejecutar y explicar pruebas | 10 clases con JUnit/Mockito/MockMvc/integ. | Maven Surefire y mapa de métodos |
| Dockerfile funcional de BD | `mysql/Dockerfile`, SQL init y healthcheck | `docker compose ps` y consulta SQL en MySQL |
| Entrega estructurada y documentada | Paquetes src/main, src/test, README y docs | ZIP y árbol de carpetas |
| Documento de evidencia | `docs/04_MODELO_DE_EVIDENCIAS.md` | Capturas propias de ejecución, no simuladas |

**Aclaración de alcance:** una prueba con H2 verifica el comportamiento JPA; no reemplaza la evidencia de conectividad con MySQL en Docker pedida en el trabajo oficial. Por eso se entregan ambas rutas. No se inventa un umbral obligatorio de JaCoCo: se incluye como recurso adicional de aprendizaje.
