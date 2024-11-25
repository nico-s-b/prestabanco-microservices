FROM openjdk:21
ARG JAR_FILE=target/ms-credit-service.jar
COPY ${JAR_FILE} ms-credit-service.jar
ENTRYPOINT ["java","-jar","/ms-credit-service.jar"]