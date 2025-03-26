# Stage 1: Use Maven for building
FROM maven:3.8.5-openjdk-17-slim as build
WORKDIR /app

COPY ./bookingservice ./bookingservice

WORKDIR /app/bookingservice
RUN mvn clean package -DskipTests

# Stage 2: use OpenJDK for running
FROM openjdk:21-jdk-slim
WORKDIR /app
COPY --from=build /app/bookingservice/target/*.jar bookingservice.jar
EXPOSE 9003
ENTRYPOINT ["java","-jar","bookingservice.jar", "--spring.profiles.active=docker"]