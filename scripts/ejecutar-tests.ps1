# Ejecuta todas las pruebas y el informe de cobertura desde la raíz del proyecto.
# No borra bases de datos, no instala software y no necesita Docker.
$ErrorActionPreference = 'Stop'
Set-Location (Split-Path $PSScriptRoot -Parent)
Write-Host 'Java:'; java -version
Write-Host 'Maven:'; mvn -version
mvn clean verify
if ($LASTEXITCODE -ne 0) { throw 'Fallaron las pruebas o el build. Revisar target/surefire-reports y consola.' }
Write-Host 'Revisar target/surefire-reports y target/site/jacoco/index.html'
