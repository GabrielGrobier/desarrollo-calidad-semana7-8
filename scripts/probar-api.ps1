# Requiere primero `docker compose up -d --build` o backend+MySQL ejecutándose.
$ErrorActionPreference = 'Stop'
$base = 'http://localhost:8080'
Write-Host 'Salud pública:'
Invoke-RestMethod "$base/api/salud"

$credenciales = @{ username='vendedor'; password='Vendedor123!' } | ConvertTo-Json
$login = Invoke-RestMethod "$base/api/auth/login" -Method Post -ContentType 'application/json' -Body $credenciales
# NUNCA imprimas ni subas el JWT del usuario a una captura pública.
$headers = @{ Authorization = "Bearer $($login.accessToken)" }
Write-Host 'Listado con JWT:'
Invoke-RestMethod "$base/api/productos" -Headers $headers

Write-Host 'Registrar venta:'
$productos = Invoke-RestMethod "$base/api/productos" -Headers $headers
$arroz = $productos | Where-Object sku -eq 'ARR-001' | Select-Object -First 1
$venta = @{ productoId=$arroz.id; cantidad=1 } | ConvertTo-Json
Invoke-RestMethod "$base/api/ventas" -Method Post -Headers $headers -ContentType 'application/json' -Body $venta
