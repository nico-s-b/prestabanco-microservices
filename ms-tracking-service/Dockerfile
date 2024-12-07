FROM openjdk:21
ARG JAR_FILE=target/ms-tracking-service-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} ms-tracking-service.jar
ENTRYPOINT ["java","-jar","/ms-tracking-service.jar"]