FROM openjdk:21
ARG JAR_FILE=target/ms-user-service.jar
COPY ${JAR_FILE} ms-user-service.jar
ENTRYPOINT ["java","-jar","/ms-user-service.jar"]