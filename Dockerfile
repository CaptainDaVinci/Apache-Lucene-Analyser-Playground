# ---- Build Stage ----
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copy only pom to leverage Docker layer caching for dependencies
COPY pom.xml ./

# Pre-fetch dependencies; uses BuildKit cache to avoid re-downloading between builds
RUN --mount=type=cache,target=/root/.m2 mvn -B dependency:go-offline

# Copy the source code
COPY src ./src

# Package the application (skip tests for faster build)
RUN --mount=type=cache,target=/root/.m2 mvn -B package -DskipTests

# ---- Runtime Stage ----
FROM eclipse-temurin:17-jre
WORKDIR /app

# Copy compiled jar from build stage
COPY --from=build /app/target/lucene-analyzer-api-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

# Run the application
ENTRYPOINT ["java","-jar","app.jar"] 