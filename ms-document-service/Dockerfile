FROM openjdk:21
ARG JAR_FILE=target/ms-document-service-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} ms-document-service.jar
ENTRYPOINT ["java","-jar","/ms-document-service.jar"]