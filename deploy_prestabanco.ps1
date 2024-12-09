# Aplicar Config-Service
kubectl apply -f deployment/config-deployment-service.yaml
Write-Host "1/7 Esperando a que config-service este listo..." -ForegroundColor Cyan
kubectl rollout status deployment/config-service-deployment
Write-Host " "

# Aplicar Eureka-Service
kubectl apply -f deployment/eureka-deployment-service.yaml
Write-Host "2/7 Esperando a que eureka-service este listo..." -ForegroundColor Cyan
kubectl rollout status deployment/eureka-service-deployment
Write-Host " "

# Aplicar Gateway-Service
kubectl apply -f deployment/gateway-deployment-service.yaml
Write-Host "3/7 Esperando a que gateway-service este listo..." -ForegroundColor Cyan
kubectl rollout status deployment/gateway-service-deployment
Write-Host " "

# Aplicar ConfigMaps y Secrets
kubectl apply -f deployment/postgres-config-map.yaml
kubectl apply -f deployment/postgres-secrets.yaml
Write-Host "4/7 ConfigMaps y Secrets aplicados..." -ForegroundColor Cyan
Write-Host " "

# Aplicar Base de Datos
kubectl apply -f deployment/postgres/prestabanco-calculations-deployment-service.yaml
kubectl rollout status deployment/db-calculations
Write-Host "DB 1/6" -ForegroundColor Green
kubectl apply -f deployment/postgres/prestabanco-credits-deployment-service.yaml
kubectl rollout status deployment/db-credits
Write-Host "DB 2/6" -ForegroundColor Green
kubectl apply -f deployment/postgres/prestabanco-documents-deployment-service.yaml
kubectl rollout status deployment/db-documents
Write-Host "DB 3/6" -ForegroundColor Green
kubectl apply -f deployment/postgres/prestabanco-evaluations-deployment-service.yaml
kubectl rollout status deployment/db-evaluations
Write-Host "DB 4/6" -ForegroundColor Green
kubectl apply -f deployment/postgres/prestabanco-tracking-deployment-service.yaml
kubectl rollout status deployment/db-tracking
Write-Host "DB 5/6" -ForegroundColor Green
kubectl apply -f deployment/postgres/prestabanco-users-deployment-service.yaml
kubectl rollout status deployment/db-users
Write-Host "DB 6/6" -ForegroundColor Green
Write-Host "5/7 Todas las bases de datos se han aplicado..." -ForegroundColor Cyan
Write-Host " "

# Aplicar otros microservicios
kubectl apply -f deployment/prestabanco_ms/
Write-Host "6/7 Todos los servicios se han aplicado." -ForegroundColor Cyan
Write-Host " "

# Aplicar frrontend
kubectl apply -f deployment/frontend-deployment-service.yaml
Write-Host "7/7 Esperando a que frontend este listo..." -ForegroundColor Cyan
kubectl rollout status deployment/frontend-deployment
Write-Host " "

Write-Host "Despliegue finalizado." -ForegroundColor Green
Write-Host " "