# Ruta docente: una clase práctica con el proyecto

Este laboratorio es un **ejemplo independiente** de la actividad formativa de veterinaria.
No se debe presentar como una solución ni pedir a los alumnos entregar estas mismas clases.

## Antes de la sesión

Ejecuta `java -version`, `mvn -version` y `mvn clean verify` para comprobar el entorno.
La primera ejecución puede necesitar Internet para obtener artefactos Maven. Si Maven no
está instalado, instala Maven 3.9+ o utiliza el de tu IDE. Docker solo es necesario para la
fase final de despliegue.

## Bloque 1. Qué es una prueba unitaria (10 min)

Abre `service/CalculadoraVenta.java` y su test. Recorre Arrange, Act y Assert. Enseña
`@Test`, `@ParameterizedTest`, `@CsvSource`, `assertEquals` y `assertThrows`. Ejecuta:

```powershell
mvn "-Dtest=CalculadoraVentaTest" test
```

Cambiar temporalmente un valor esperado permite producir una falla real; restáuralo luego.

## Bloque 2. Reglas de negocio (10 min)

Abre `model/Producto.java` y `model/ProductoModelTest.java`. Enseña escenarios felices,
stock insuficiente, valor cero, y protección contra stock negativo. No participa la BD.

## Bloque 3. Mockito y aislamiento de dependencias (20 min)

Abre `ProductoServiceTest.java` y `VentaServiceTest.java`. Explica:
- `@Mock`: dependencia de prueba controlada.
- `@InjectMocks`: instancia la unidad bajo prueba e inyecta mocks.
- `when(...).thenReturn(...)`: comportamiento preparado.
- `verify(...)`: comprueba una interacción.
- `ArgumentCaptor`: inspecciona el objeto enviado a una dependencia.
- `never()` y `verifyNoInteractions(...)`: escenarios negativos.

Estas clases no contactan MySQL y no confirman SQL ni cableado de Spring.

## Bloque 4. Capa HTTP simulada (15 min)

Abre `ProductoControllerWebTest.java`. Enseña `@WebMvcTest`, MockMvc, GET, POST, JSON,
200, 201, 400 y 404. `addFilters=false` aísla el contrato web; NO demuestra seguridad.

## Bloque 5. Integración real con H2 temporal (15 min)

Abre `ContextoApplicationTest`, `ProductoRepositoryJpaTest`, `VentaJpaIntegrationTest`
y `SeguridadHttpIntegrationTest`. Las pruebas cargan Spring, JPA y/o los filtros JWT.
Explica por qué no se clasifican automáticamente como unitarias. La versión con
`RANDOM_PORT` realiza HTTP real hacia el servidor embebido.

## Bloque 6. Evidencias y despliegue (15 min)

Ejecuta `mvn clean verify`. Interpreta `Tests run`, `Failures`, `Errors`, `Skipped` y
`BUILD SUCCESS`/`BUILD FAILURE`. Revisa Surefire y JaCoCo. Luego, de forma opcional,
levanta MySQL + backend con Docker Compose. El Dockerfile de MySQL configura una
infraestructura real; las pruebas aisladas no necesitan esa infraestructura.

## Cierre

SCA (Semana 6) revisa vulnerabilidades conocidas de dependencias. JUnit/Mockito/MockMvc
(Semana 7) comprueban comportamientos. No confundir cobertura de código con garantía de
correctitud, ni que una prueba pase con ausencia de todos los defectos.
