FROM openjdk:21
ARG JAR_FILE=target/ms-evaluation-service-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} ms-evaluation-service.jar
ENTRYPOINT ["java","-jar","/ms-evaluation-service.jar"]