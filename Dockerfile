FROM node:lts-alpine as build-stage
COPY frontend/package*.json ./
RUN npm install
COPY ./frontend .
RUN npm run build

FROM openjdk:8-jdk-alpine as builder
COPY --from=build-stage /node_modules /src/main/resources/dist/node_modules
COPY --from=build-stage /dist /src/main/resources/dist
COPY ./backend/gradlew .
COPY ./backend/gradle gradle
COPY ./backend/build.gradle .
COPY ./backend/settings.gradle . 
COPY ./backend/src src
RUN chmod +x ./gradlew
RUN ./gradlew clean build

FROM openjdk:8-jdk-alpine
COPY --from=builder build/libs/ssafy-fifth-web-common-project-6.16.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]



