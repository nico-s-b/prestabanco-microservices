FROM openjdk:21
ARG JAR_FILE=target/ms-calculation-service-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} ms-calculation-service.jar
ENTRYPOINT ["java","-jar","/ms-calculation-service.jar"]