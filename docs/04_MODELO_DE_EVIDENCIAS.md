# Ejemplo de estructura para documentar pruebas (máximo 5 páginas si se adaptara a una entrega)

**Esta es una guía metodológica; NO contiene resultados ni capturas ficticias.**

1. Identificación y objetivo: qué proyecto se probó, versión de Java/Maven, qué se buscaba demostrar.
2. Entorno: Spring Boot, conexión al backend, MySQL Docker (captura `docker compose ps`), y perfil `test` con H2 temporal.
3. Cuadro de pruebas: clase, método `@Test`, escenario de entrada, resultado esperado, resultado observado y nivel (unitario, web, integración).
4. Captura de ejecución real `mvn clean verify`: total de ejecuciones, failures, errors, skipped y BUILD SUCCESS/FAILURE.
5. Conclusión: fallos encontrados, correcciones realizadas, limitaciones del alcance; adjuntar `target/surefire-reports` cuando corresponda.

Para capturas: NO exponer JWT completos, contraseñas, hashes ni variables sensibles. Mostrar resultados reales y nombres de métodos.

Comandos útiles:

```powershell
mvn clean verify
Get-ChildItem .\target\surefire-reports\
start .\target\site\jacoco\index.html
```

JaCoCo muestra proporción de instrucciones/ramas ejecutadas; 100 % no demuestra corrección completa, seguridad ni cobertura de todos los escenarios.
