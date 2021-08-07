FROM openjdk:8-jdk-alpine as builder
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle . 
COPY src src
COPY dummy dummy
RUN chmod +x ./gradlew
RUN ./gradlew clean build

FROM openjdk:8-jdk-alpine
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]

