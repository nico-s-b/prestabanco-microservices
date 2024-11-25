FROM openjdk:21
ARG JAR_FILE=target/ms-creditcalculate-service.jar
COPY ${JAR_FILE} ms-creditcalculate-service.jar
ENTRYPOINT ["java","-jar","/ms-creditcalculate-service.jar"]