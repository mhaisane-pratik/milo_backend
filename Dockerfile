# ------------------------------------------------------------------------------
# 1. BUILD STAGE: Maven & JDK 17
# ------------------------------------------------------------------------------
FROM maven:3.9.6-eclipse-temurin-17-alpine AS build
WORKDIR /app

# Copy pom.xml and source code
COPY pom.xml .
COPY src ./src

# Build production JAR (skip unit tests during build)
RUN mvn clean package -DskipTests

# ------------------------------------------------------------------------------
# 2. RUNTIME STAGE: Lightweight JRE 17
# ------------------------------------------------------------------------------
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copy compiled JAR from build stage
COPY --from=build /app/target/*.jar app.jar

# Render default port mapping
EXPOSE 8080

# Run Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]
