# Extraer los servicios seleccionados correctamente
param (
    [Parameter(Mandatory = $false)]
    [string[]]$select
)

$dockerUser = "nicolassepulvedab"

$microserviceFolders = @(
    "config-service",
    "eureka-service",
    "gateway-service",
    "ms-calculation-service",
    "ms-credit-service",
    "ms-document-service",
    "ms-evaluation-service",
    "ms-tracking-service",
    "ms-user-service"
)

$frontendFolder = "prestabanco-frontend"

# Función para verificar si un servicio debe ser procesado
function ShouldProcessService($service) {
    if ($selectedServices -contains "all") {
        return $true
    }
    foreach ($selected in $selectedServices) {
        if ($service -like "*$selected*") {
            return $true
        }
    }
    return $false
}

$selectedServices = if ($select) {
    $select
} else {
    @()
}

# Validar si hay argumentos y si son válidos
if (-not $selectedServices) {
    Write-Host "Modo de uso: Ingrese como argumentos partes de los nombres de los microservicios deseados" -ForegroundColor Cyan
    Write-Host "./deploy_prestabanco.ps1 -select [microservicios] # para procesar deseados" -ForegroundColor Green
    Write-Host "./deploy_prestabanco.ps1 -select all # para procesar todos" -ForegroundColor Green
    Write-Host "`nMicroservicios disponibles:" -ForegroundColor Cyan
    foreach ($service in $microserviceFolders) {
        Write-Host "- $service" -ForegroundColor Yellow
    }
    Write-Host "- frontend" -ForegroundColor Yellow
    exit 1
}

# Verificar si los servicios seleccionados son válidos
if ($selectedServices -notcontains "all" -and -not (@($microserviceFolders + @("frontend") | Where-Object { ShouldProcessService $_ }) -ne $null)) {
    Write-Host "No se encontraron microservicios válidos para procesar." -ForegroundColor Red
    Write-Host "Use el flag '-select all' o elija microservicios válidos de la lista disponible." -ForegroundColor Cyan
    exit 1
}

# Procesar los servicios seleccionados
foreach ($service in $microserviceFolders) {
    if (ShouldProcessService $service) {
        Write-Host "Procesando microservicio: $service" -ForegroundColor Cyan

        Set-Location $service

        Write-Host "Empaquetando $service con Maven..." -ForegroundColor Magenta
        mvn clean package -DskipTests

        $imageName = "nicolassepulvedab/$service"

        Write-Host "Construyendo imagen para $imageName..." -ForegroundColor Yellow
        docker build -t $imageName .

        Write-Host "Subiendo imagen $imageName a Docker Hub..." -ForegroundColor Green
        docker push $imageName

        Set-Location ..
    }
}
if ($selectedServices -contains "all" -or ($selectedServices | Where-Object { $_ -like "*frontend*" })) {
    Write-Host "Procesando microservicio de Frontend" -ForegroundColor Cyan

    Set-Location ..
    Set-Location $frontendFolder

    Write-Host "Creando build del Frontend" -ForegroundColor Green
    npm run build

    Write-Host "Construyendo imagen para prestabanco-frontend..." -ForegroundColor Yellow
    docker build -t nicolassepulvedab/prestabanco-frontend .

    Write-Host "Subiendo imagen prestabanco-frontend a Docker Hub..." -ForegroundColor Green
    docker push nicolassepulvedab/prestabanco-frontend

    Set-Location ..
    Set-Location "microservices"
}

Write-Host "Proceso completado para todos los argumentos." -ForegroundColor Magenta
