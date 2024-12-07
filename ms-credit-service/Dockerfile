FROM openjdk:21
ARG JAR_FILE=target/ms-credit-service-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} ms-credit-service.jar
ENTRYPOINT ["java","-jar","/ms-credit-service.jar"]