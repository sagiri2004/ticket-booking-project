# Stage 1: Use Maven for building
FROM maven:3.8.5-openjdk-17-slim as build
WORKDIR /app
COPY ./discoveryserver ./discoveryserver
WORKDIR /app/discoveryserver
RUN mvn clean package -DskipTests

# Stage 2: Use OpenJDK for running
FROM openjdk:21-jdk-slim
WORKDIR /app
COPY --from=build /app/discoveryserver/target/*.jar discoveryserver.jar
EXPOSE 8761

# Chạy với profile "docker"
ENTRYPOINT ["java", "-jar", "discoveryserver.jar", "--spring.profiles.active=docker"]
