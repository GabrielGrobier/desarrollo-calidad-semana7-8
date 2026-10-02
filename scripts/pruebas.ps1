# Ejecuta TODAS las pruebas y produce informe de cobertura. No necesita levantar Docker.
$ErrorActionPreference = 'Stop'
mvn clean verify
if ($LASTEXITCODE -ne 0) { throw 'Hay pruebas o compilación fallidas. Revisar target/surefire-reports.' }
Write-Host 'Revisar target/surefire-reports y target/site/jacoco/index.html'
