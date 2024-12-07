FROM openjdk:21
ARG JAR_FILE=target/ms-user-service-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} ms-user-service.jar
ENTRYPOINT ["java","-jar","/ms-user-service.jar"]