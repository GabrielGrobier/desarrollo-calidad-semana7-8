# Guía de anotaciones y operaciones que aparecen en el código

| Elemento | Qué hace | Dónde verlo |
|---|---|---|
| `@Test` | Marca una prueba individual | `CalculadoraVentaTest` |
| `@ParameterizedTest` / `@CsvSource` | Ejecuta el mismo método con entradas distintas | `CalculadoraVentaTest` |
| `assertEquals` / `assertThrows` | Afirma resultado o excepción esperados | `CalculadoraVentaTest` |
| `@ExtendWith(MockitoExtension.class)` | Inicializa mocks en JUnit, sin levantar Spring | `VentaServiceTest` |
| `@Mock` | Sustituye una dependencia por objeto simulado | `ProductoServiceTest` |
| `@InjectMocks` | Construye e inyecta el objeto bajo prueba | `VentaServiceTest` |
| `when` / `thenReturn` | Define qué responde el mock | `ProductoServiceTest` |
| `verify` / `never` | Comprueba una interacción o su ausencia | `VentaServiceTest` |
| `ArgumentCaptor` | Inspecciona el argumento enviado al mock | `ProductoServiceTest` |
| `@WebMvcTest` | Carga solo la capa MVC relevante | `ProductoControllerWebTest` |
| `@MockitoBean` | Reemplaza un bean de Spring por un mock | `ProductoControllerWebTest` |
| `@AutoConfigureMockMvc(addFilters=false)` | Prueba JSON y estados HTTP sin seguridad | `ProductoControllerWebTest` |
| `MockMvc.perform` / `andExpect` | Simula request y comprueba response | `ProductoControllerWebTest` |
| `@SpringBootTest` | Levanta contexto completo de la aplicación | `ContextoApplicationTest` |
| `@DataJpaTest` | Prueba repositorios y mapeos JPA | `ProductoRepositoryJpaTest` |
| `@ActiveProfiles("test")` | Selecciona configuración H2 temporal | Pruebas de integración |
| `@Transactional` en test | Revierte datos al finalizar la prueba | `VentaJpaIntegrationTest` |
| `@LocalServerPort` / `TestRestTemplate` | Usa un servidor real en puerto aleatorio | `LoginHttpPuertoAleatorioTest` |
| `JwtDecoder` | Valida firma/expiración/issuer del token | `SeguridadHttpIntegrationTest` |
| `@Valid`, `@NotBlank`, `@Min` | Validan datos de entrada HTTP | DTO y tests web |
| `@Lock(PESSIMISTIC_WRITE)` | Serializa acceso a fila durante una venta | `ProductoRepository` |

Nota: usar Mockito en el test de servicio prueba la **lógica e interacción simulada**. No confirma conectividad MySQL: esa evidencia debe realizarse con `compose.yaml` y la consulta documentada en README.
