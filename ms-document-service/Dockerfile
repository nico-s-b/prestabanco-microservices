FROM openjdk:21
ARG JAR_FILE=target/ms-document-service.jar
COPY ${JAR_FILE} ms-document-service.jar
ENTRYPOINT ["java","-jar","/ms-document-service.jar"]