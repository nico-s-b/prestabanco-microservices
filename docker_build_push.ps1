$dockerUser = "nicolassepulvedab"

$microserviceFolders = @(
    "config-service",
    "eureka-service",
    "gateway-service",
    "ms-calculation-service",
    "ms-credit-service",
    "ms-document-service",
    "ms-tracking-service",
    "ms-user-service"
)

foreach ($service in $microserviceFolders) {
    Write-Host "Procesando microservicio: $service" -ForegroundColor Cyan

    Set-Location $service

    $imageName = "$dockerUser/$service"

    Write-Host "Construyendo imagen para $imageName..." -ForegroundColor Yellow
    docker build -t $imageName .

    Write-Host "Subiendo imagen $imageName a Docker Hub..." -ForegroundColor Green
    docker push $imageName

    Set-Location ..
}

Write-Host "Proceso completado para todos los microservicios." -ForegroundColor Magenta
