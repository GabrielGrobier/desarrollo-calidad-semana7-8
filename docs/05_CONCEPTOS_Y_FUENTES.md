# Diccionario breve y fuentes para profundizar

**JUnit Jupiter**: motor/API para definir y ejecutar casos de prueba Java con `@Test`, `@ParameterizedTest` y assertions. Un test verifica un contrato observable.

**Mockito**: reemplaza dependencias por dobles controlados, permitiendo pruebas aisladas (`@Mock`, `@InjectMocks`, `when`, `verify`).

**MockMvc**: ejecuta el tratamiento Spring MVC con objetos request/response simulados, sin servidor HTTP real. No equivale por definición a prueba unitaria; importa cuánto contexto se carga.

**@SpringBootTest**: levanta contexto de aplicación para pruebas integradas.

**@WebMvcTest**: carga una parte limitada relacionada con la capa MVC. Se simula el servicio para aislar el controlador.

**@DataJpaTest**: prueba una parte de JPA, repositorios y mapeos; en este laboratorio utiliza H2.

**Cobertura**: código ejecutado durante pruebas; no mide por sí sola la calidad de las aserciones ni elimina riesgo de seguridad.

**401 vs 403**: 401 es ausencia o invalidez de autenticación; 403 es identidad autenticada sin autorización suficiente.

**Fuentes oficiales (consultables desde navegador):**

- Spring Boot 3.5 — Testing: https://docs.spring.io/spring-boot/3.5/reference/testing/index.html
- Spring Framework — MockMvc: https://docs.spring.io/spring-framework/reference/testing/mockmvc.html
- Spring Security — JWT Resource Server: https://docs.spring.io/spring-security/reference/6.5/servlet/oauth2/resource-server/jwt.html
- JUnit 5 User Guide: https://docs.junit.org/5.13.4/user-guide/
- Mockito — documentación: https://site.mockito.org/
- Maven Surefire: https://maven.apache.org/surefire/maven-surefire-plugin/
- JaCoCo documentación: https://www.jacoco.org/jacoco/trunk/doc/
- Docker — inicialización de MySQL imagen oficial: https://hub.docker.com/_/mysql

**Alcance de versiones:** se usa Spring Boot 3.5.16 por continuidad con las clases y guía de Spring Boot 3; esta es la última versión OSS 3.5. Para proyectos nuevos en producción elegir una rama con soporte actual según matriz oficial. Véase https://spring.io/blog/2026/06/25/spring-boot-3-5-16-available-now/.
