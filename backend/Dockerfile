FROM openjdk:8-jdk-alpine as builder
COPY --from=frontend /usr/share/nginx/html/homepage /src/main/resources/dist
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle . 
COPY src src
RUN chmod +x ./gradlew
RUN ./gradlew clean build

FROM openjdk:8-jdk-alpine
COPY --from=builder build/libs/ssafy-fifth-web-common-project-6.16.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]

