# Stage 1: Use Maven for building
FROM maven:3.8.5-openjdk-17-slim as build
WORKDIR /app
COPY ./authenticationservice ./authenticationservice
WORKDIR /app/authenticationservice
RUN mvn clean package -DskipTests

# Stage 2: Use OpenJDK for running
FROM openjdk:21-jdk-slim
WORKDIR /app
COPY --from=build /app/authenticationservice/target/*.jar authenticationservice.jar
EXPOSE 9001

# Chạy với profile "docker"
ENTRYPOINT ["java", "-jar", "authenticationservice.jar", "--spring.profiles.active=docker"]
