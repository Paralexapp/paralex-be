# Use Eclipse Temurin as the base image
#FROM eclipse-temurin:17-jdk-alpine
#WORKDIR /app
#COPY build/libs/*.jar app.jar
#EXPOSE 8080
#ENTRYPOINT ["java", "-jar", "/app/app.jar"]

# Stage 1: Build with Gradle
FROM gradle:8.7-jdk17 AS builder
WORKDIR /app

# Preload dependencies
COPY build.gradle settings.gradle gradle.properties ./
COPY gradle ./gradle
RUN gradle build --no-daemon -x test || true

# Copy the full source code and build
COPY . .
RUN gradle clean bootJar --no-daemon -x test

# Stage 2: Lightweight runtime image
FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
COPY --from=builder /app/build/libs/*.jar app.jar

# ✅ Activate the 'production' profile
ENTRYPOINT ["java", "-jar", "/app.jar", "--spring.profiles.active=prod"]

# ✅ Make port visible
EXPOSE 8083
