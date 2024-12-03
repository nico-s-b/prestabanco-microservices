FROM openjdk:21
ARG JAR_FILE=target/ms-client-service.jar
COPY ${JAR_FILE} ms-client-service.jar
ENTRYPOINT ["java","-jar","/ms-client-service.jar"]