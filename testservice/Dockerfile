# Stage 1: Use Maven for building
FROM maven:3.8.5-openjdk-17-slim as build
WORKDIR /app

COPY ./testservice ./testservice

WORKDIR /app/testservice
RUN mvn clean package -DskipTests

# Stage 2: use OpenJDK for running
FROM openjdk:21-jdk-slim
WORKDIR /app
COPY --from=build /app/testservice/target/*.jar testservice.jar
EXPOSE 9003
ENTRYPOINT ["java","-jar","testservice.jar", "--spring.profiles.active=docker"]