# Use Eclipse Temurin as the base image
#FROM eclipse-temurin:17-jdk-alpine
#WORKDIR /app
#COPY build/libs/*.jar app.jar
#EXPOSE 8080
#ENTRYPOINT ["java", "-jar", "/app/app.jar"]

FROM maven:3.9.4-eclipse-temurin-17 AS builder
WORKDIR /app

# Preload dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -DskipTests

# Copy source and build
COPY . .
RUN mvn clean package -DskipTests

# Stage 2: Lightweight JDK image
FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
COPY --from=builder /app/target/*.jar app.jar

# ✅ Activate the 'production' profile
ENTRYPOINT ["java", "-jar", "/app.jar", "--spring.profiles.active=prod"]

# ✅ Make port visible to Render
EXPOSE 8083