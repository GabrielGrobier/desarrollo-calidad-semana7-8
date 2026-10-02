# Evidencia técnica para el laboratorio

La actividad oficial de CDY2203 exige documentar clases y métodos validados; este ejemplo
sirve para aprender cómo producir evidencia propia, sin usar el caso veterinaria.

| Evidencia | Qué demuestra | Fuente sugerida |
|---|---|---|
| `java -version`, `mvn -version` | Entorno local | PowerShell |
| `mvn clean verify` | Ejecución de suite | Terminal |
| `target/surefire-reports/` | Resultados por clase/test | XML/TXT generado |
| `target/site/jacoco/index.html` | Cobertura medida | JaCoCo |
| `ProductoModelTest` | Reglas unitarias | Código fuente |
| `ProductoServiceTest` | Aislamiento de repo | Código fuente |
| `ProductoControllerWebTest` | Contrato HTTP simulado | Código fuente |
| `SeguridadHttpIntegrationTest` | 401, 403 y permisos reales | Código y salida |
| `docker compose ps` | Estado de servicios | Consola Docker |

## Cómo diagnosticar resultados

- `Failure`: una aserción no coincide con el resultado real.
- `Error`: fallo al ejecutar el escenario (configuración, Spring, base de datos, etc.).
- `Skipped`: test omitido. No lo presentes como ejecutado correctamente.
- `BUILD SUCCESS`: Maven terminó sus metas exitosamente; no es garantía absoluta.

## Plantilla de análisis (ejemplo)

| Clase | Método | Arrange | Act | Assert | Resultado |
|---|---|---|---|---|---|
| `ProductoModelTest` | `descontarStock_cantidadValida_disminuyeElInventario` | 10 unidades | vender 3 | quedan 7 | completar tras ejecutar |

Asegúrate de que el resultado informado corresponda a la ejecución real del equipo.
No inventar capturas ni marcar tests como pasados sin comprobarlos.
