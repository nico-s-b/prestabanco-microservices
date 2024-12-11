# Prestabanco - Microservicios

Para el despliegue desde cero, seguir los siguientes pasos:
1. Descargar repositorio de microservicios
2. Descargar/clonar repositorio del frontend
3. Ubicar ambos directorios en una misma carpeta raíz.
4. Hacer login en dockers.
5. Ejecutar ./docker_build_push.ps1 en la carpeta de microservicios. Esto creará los archivos .jar y build del front, construirá las imágenes de docker y hará push al repositorio de dockers
```
./docker_build_push.ps1
```
5. Iniciar minikube. En este caso, se usó el driver de Dockers
```
minikube start --driver=docker
```
6. Ejecutar ./deploy_prestabanco.ps1 en la carpeta raíz de microservicios. Esto aplicará todos los manifiestos de deploy, service, pv, pvc, config-map y secrets en el orden correcto. Esperar hasta que el despliegue esté completo.
```
./docker_build_push.ps1
```
7. Abrir un tunel de minikube
```
minikube tunnel
```
8. Una vez completo este paso, debiera ser posible acceder al frontend, backend, bases de datos y servidor de eureka mediante las siguientes direcciones y puertos:
-Frontend: 127.0.0.1:80
-Backend: 127.0.0.1:8082
-Bases de datos: 127.0.0.1:5432
-Eureka: 127.0.0.1:8761
