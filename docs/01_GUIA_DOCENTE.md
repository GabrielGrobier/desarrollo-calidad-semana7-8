# Guía docente — Semana 7: de pruebas unitarias a validación de backend

**Este laboratorio es ORIGINAL y COMPLEMENTARIO.** Utiliza una tienda de abarrotes, no el caso veterinaria ni el proyecto base de la actividad formativa oficial. La pauta y las instrucciones de la unidad sirvieron únicamente para decidir qué conceptos demostrar.

## 1. Narrativa para la clase

En la Semana 6 se analizan las dependencias con SCA: «¿estoy usando un componente conocido como vulnerable?». En la Semana 7 se analiza el comportamiento con pruebas: «¿responde mi programa como acordamos ante entradas normales, inválidas y límites?». Que una prueba pase no demuestra que el sistema sea inmune a vulnerabilidades; SCA y testing se complementan.

**Caso de uso:** el administrador crea productos, el vendedor consulta el inventario y registra ventas, el auditor consulta informes. La operación de venta reduce stock y conserva el precio. No se permite vender más unidades de las disponibles.

## 2. Arquitectura a explicar

```text
POST /api/auth/login ---> AuthController ---> AuthenticationManager ---> CuentaUsuarioRepository ---> MySQL
                                |                                       |
                                +-- JwtService --> token firmado HS256 <-+

JWT Bearer ----> filtros Spring Security (firma + expiración + rol)
                      |
                      v
    ProductoController / VentaController
                      |
                      v
           ProductoService / VentaService
                      |
                      v
           ProductoRepository / VentaRepository
                      |
                      v
                    MySQL
```

## 3. Los niveles de prueba NO son sinónimos

| Prueba | Contexto | Qué comprueba | BD real |
|---|---|---|---|
| `CalculadoraVentaTest` | JUnit puro | Precio x cantidad, errores de entrada | No |
| `ProductoServiceTest` | JUnit + Mockito | Reglas y colaboración service–repo simulado | No |
| `VentaServiceTest` | JUnit + Mockito | Descuento, stock insuficiente, venta no guardada | No |
| `ProductoControllerWebTest` | `@WebMvcTest` + MockMvc | Contrato HTTP/JSON/validación | No |
| `ContextoApplicationTest` | `@SpringBootTest` | Se inicia contexto e inyectan beans | H2 temporal |
| `ProductoRepositoryJpaTest` | `@DataJpaTest` | Persistencia real de una entidad | H2 temporal |
| `VentaJpaIntegrationTest` | `@SpringBootTest` | Transacción y persistencia conjunta | H2 temporal |
| `SeguridadHttpIntegrationTest` | `@SpringBootTest` + MockMvc | Login JWT, 401, 403 y acceso permitido | H2 temporal |
| `LoginHttpPuertoAleatorioTest` | `@SpringBootTest(RANDOM_PORT)` + TestRestTemplate | Solicitud HTTP real a login/ruta JWT | H2 temporal |

**MockMvc** no abre un puerto HTTP real; simula solicitudes y respuestas dentro de Spring MVC. La presencia de MockMvc no hace que la prueba sea automáticamente «unitaria»; el nivel depende del contexto levantado y lo que se integre.

El test de puerto aleatorio sirve para explicar **la diferencia entre el MockMvc sin socket y una prueba de integración con HTTP real**. Ambos son automatizables con JUnit.

## 4. Explicar las tres A con `CalculadoraVentaTest`

- Arrange: definir precio=1490, cantidad=2, esperado=2980.
- Act: llamar `CalculadoraVenta.total(precio, cantidad)`.
- Assert: comparar resultado y esperado.

Una prueba NEGATIVA debe pasar si ocurre la excepción esperada (`assertThrows`). Prueba que la regla de validación funciona, no que «el software falló».

## 5. Mock y Mockito

`@Mock`: genera objeto simulado. `@InjectMocks`: construye la clase a probar e inyecta sus dependencias. `when(...).thenReturn(...)`: predetermina la respuesta del mock. `verify(...)`: comprueba una interacción. `never()` demuestra que no se persistió una venta cuando faltaba stock. `ArgumentCaptor` permite revisar qué objeto se pasó a save().

**No confundir:** verificar `save()` no demuestra que exista una fila en MySQL. Eso requiere prueba de integración con un repositorio real.

## 6. MockMvc

En `ProductoControllerWebTest` se apagan intencionalmente los filtros para aislar contrato HTTP. En `SeguridadHttpIntegrationTest` se dejan ACTIVOS para validar que un invitado obtiene 401 y un usuario autenticado sin permisos obtiene 403. No mezclar ambos objetivos.

- 200 OK: consulta exitosa.
- 201 Created: recurso registrado.
- 400 Bad Request: datos inválidos.
- 401 Unauthorized: falta autenticación o token válido.
- 403 Forbidden: autenticado, pero sin permisos.
- 404 Not Found: identificador inexistente.
- 409 Conflict: SKU duplicado o stock insuficiente.

## 7. Demostración propuesta (75-90 minutos)

1. Dibujar arquitectura y explicar en qué capa trabaja cada test (10 min).
2. Ejecutar `mvn test` y observar resultado (5 min).
3. Abrir `CalculadoraVentaTest`, recorrer Arrange/Act/Assert (10 min).
4. Cambiar **solamente una expectativa en el test**: de 2980 a 3000. Ejecutar la clase; observar falla y restaurar el valor (10 min).
5. Explicar `@Mock`, `@InjectMocks`, `when`, `verify` y `never` en `VentaServiceTest` (15 min).
6. Probar con MockMvc 200, 201, 400 y 404 (10 min).
7. Contrastar 401 versus 403 en `SeguridadHttpIntegrationTest` (10 min).
8. Mostrar `target/surefire-reports`, JaCoCo y matriz de evidencia (10 min).

## 8. Ejercicios adicionales NO evaluados

- Agregar caso de borde: stock exactamente igual a cantidad vendida.
- Agregar test para rechazar precio nulo.
- Agregar control de acceso para un usuario `AUDITOR` sobre ventas.
- Comprobar el efecto de `@Transactional` cuando falla el registro.

El curso indica trabajar con pruebas ya implementadas: antes de pedir nuevas pruebas, se puede analizar en grupo las existentes y documentar qué valida cada una.
