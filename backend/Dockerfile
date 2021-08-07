<<<<<<< Updated upstream
=======
FROM openjdk:8-jdk-alpine as builder
COPY backend/gradlew .
COPY backend/gradle gradle
COPY backend/build.gradle .
COPY backend/settings.gradle . 
COPY backend/src src
RUN chmod +x ./gradlew
RUN ./gradlew clean build

>>>>>>> Stashed changes
FROM openjdk:8-jdk-alpine
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]

