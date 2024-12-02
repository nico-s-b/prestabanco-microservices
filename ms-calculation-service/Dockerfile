FROM openjdk:21
ARG JAR_FILE=target/ms-calculation-service.jar
COPY ${JAR_FILE} ms-calculation-service.jar
ENTRYPOINT ["java","-jar","/ms-calculation-service.jar"]