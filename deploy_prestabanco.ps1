# Aplicar Config-Service
kubectl apply -f deployment/config-deployment-service.yaml
Write-Host "Esperando a que config-service este listo..."
kubectl rollout status deployment/config-service-deployment
Write-Host " "

# Aplicar Eureka-Service
kubectl apply -f deployment/eureka-deployment-service.yaml
Write-Host "Esperando a que eureka-service este listo..."
kubectl rollout status deployment/eureka-service-deployment
Write-Host " "

# Aplicar Gateway-Service
kubectl apply -f deployment/gateway-deployment-service.yaml
Write-Host "Esperando a que gateway-service este listo..."
kubectl rollout status deployment/gateway-service-deployment
Write-Host " "

# Aplicar ConfigMaps y Secrets
kubectl apply -f deployment/postgres-config-map.yaml
kubectl apply -f deployment/postgres-secrets.yaml
Write-Host " "

# Aplicar Base de Datos
kubectl apply -f deployment/postgres/prestabanco-calculations-deployment-service.yaml
kubectl rollout status deployment/prestabanco-calculations
kubectl apply -f deployment/postgres/prestabanco-credits-deployment-service.yaml
kubectl rollout status deployment/prestabanco-credits
kubectl apply -f deployment/postgres/prestabanco-documents-deployment-service.yaml
kubectl rollout status deployment/prestabanco-documents
kubectl apply -f deployment/postgres/prestabanco-evaluations-deployment-service.yaml
kubectl rollout status deployment/prestabanco-evaluations
kubectl apply -f deployment/postgres/prestabanco-tracking-deployment-service.yaml
kubectl rollout status deployment/prestabanco-tracking
kubectl apply -f deployment/postgres/prestabanco-users-deployment-service.yaml
kubectl rollout status deployment/prestabanco-users
Write-Host "Todas las bases de datos se han aplicado..."
Write-Host " "

# Aplicar otros microservicios
kubectl apply -f deployment/prestabanco_ms/
Write-Host "Todos los servicios se han aplicado."
Write-Host " "