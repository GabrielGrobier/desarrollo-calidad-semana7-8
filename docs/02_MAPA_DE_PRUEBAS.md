# Mapa de clases y métodos para usar en la explicación

No copiar este documento como informe de una ejecución que no ha ocurrido: los resultados reales deben salir de la terminal y `target/surefire-reports` de cada estudiante.

| Clase | Método de test | Método productivo | Qué evidencia |
|---|---|---|---|
| CalculadoraVentaTest | total_calculaCorrectamente (3 datos) | CalculadoraVenta.total | Resultado para entradas representativas |
| CalculadoraVentaTest | total_rechazaCantidadCero | CalculadoraVenta.total | Error ante cantidad cero |
| CalculadoraVentaTest | total_rechazaPrecioNegativo | CalculadoraVenta.total | Error ante precio negativo |
| ProductoServiceTest | buscar_idExistente_devuelveProducto | ProductoService.buscar | Obtención y colaboración con repo |
| ProductoServiceTest | buscar_idInexistente_lanzaExcepcion | ProductoService.buscar | Excepción de dominio |
| ProductoServiceTest | crear_skuLibre_guardaProducto | ProductoService.crear | Persistencia solicitada correctamente |
| ProductoServiceTest | crear_skuDuplicado_noDebeGuardar | ProductoService.crear | No guardar SKU duplicado |
| ProductoServiceTest | ajustarStock_actualizaEntidad | ProductoService.ajustarStock | Stock actualizado |
| VentaServiceTest | registrar_conStock_calculaTotalYDescuenta | VentaService.registrar | Precio/stock/interacción |
| VentaServiceTest | registrar_stockInsuficiente_noGuardaVenta | VentaService.registrar | Error y no persistencia |
| VentaServiceTest | registrar_productoInexistente_noGuarda | VentaService.registrar | No persiste si no existe producto |
| VentaServiceTest | registrar_cantidadCero_noConsultaRepositorio | VentaService.registrar | Validación previa |
| ProductoControllerWebTest | getProductos_retorna200YJson | ProductoController.listar | Status 200 y estructura JSON |
| ProductoControllerWebTest | getProductoInexistente_retorna404 | ProductoController.buscar | Mapeo de excepción a 404 |
| ProductoControllerWebTest | postProductoValido_retorna201 | ProductoController.crear | Creación con 201 |
| ProductoControllerWebTest | postProductoInvalido_retorna400YNoInvocaServicio | ProductoController.crear | Bean Validation con 400 |
| ContextoApplicationTest | contextLoads_yControladorDisponible | Inicio de contexto | Beans inyectables |
| ProductoRepositoryJpaTest | guardar_yBuscarPorSku | ProductoRepository | SQL/JPA real sobre H2 |
| VentaJpaIntegrationTest | registrarVenta_persisteYActualizaStock | VentaService y repositorios | Transacción+datos |
| LoginHttpPuertoAleatorioTest | loginYConsultaAutenticada_porHttpReal | AuthController + ProductoController | HTTP real con puerto aleatorio y token JWT |
| LoginHttpPuertoAleatorioTest | contraseñaIncorrecta_porHttpRealDevuelve401 | AuthController | HTTP real con login inválido |
| SeguridadHttpIntegrationTest | salud_esPublica | SeguridadConfig | Ruta permitida sin JWT |
| SeguridadHttpIntegrationTest | loginValido_emiteJwtConRol | AuthController/JwtService | Firma/token emitido |
| SeguridadHttpIntegrationTest | loginClaveIncorrecta_devuelve401 | AuthController | Credenciales inválidas |
| SeguridadHttpIntegrationTest | productosSinToken_devuelve401 | SeguridadConfig | Rechazo anónimo |
| SeguridadHttpIntegrationTest | vendedorNoPuedeCrearProducto_devuelve403 | SeguridadConfig | Permisos por rol |
| SeguridadHttpIntegrationTest | auditorNoPuedeRegistrarVenta_devuelve403 | SeguridadConfig | Permisos por rol |
| SeguridadHttpIntegrationTest | tokenManipulado_devuelve401 | JwtDecoder | Firma inválida |
| SeguridadHttpIntegrationTest | adminPuedeCrearProducto_devuelve201 | SeguridadConfig/ProductoController | Permiso positivo ADMIN |
| SeguridadHttpIntegrationTest | vendedorPuedeRegistrarVenta_devuelve201 | SeguridadConfig/VentaController | Permiso positivo VENDEDOR |
| SeguridadHttpIntegrationTest | vendedorPuedeConsultarProductos_devuelve200 | SeguridadConfig/ProductoController | Acceso autorizado |

`@ParameterizedTest` cuenta como varias ejecuciones, aunque sea un único método declarado. La cantidad real se obtiene de Surefire y no se presupone aquí.


**Ampliación de esta edición:** `ProductoModelTest` incorpora cuatro pruebas unitarias puras sobre reglas de stock y escenarios negativos.
