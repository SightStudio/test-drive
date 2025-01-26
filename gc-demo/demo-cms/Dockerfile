# Stage 1: Build the application using Gradle
FROM eclipse-temurin:11-alpine as builder

# Set the working directory
WORKDIR /app

# Copy Gradle wrapper and necessary files for dependency resolution
COPY gradle gradle
COPY gradlew .
COPY settings.gradle .
COPY build.gradle .

# Download Gradle dependencies
RUN ./gradlew dependencies --no-daemon

# Copy the rest of the application source code
COPY src src

# Build the application
RUN ./gradlew bootJar --no-daemon

# Stage 2: Create a minimal runtime image
FROM eclipse-temurin:11-alpine

# Set the working directory
WORKDIR /app
RUN mkdir /app/gc-log

# Copy the built application JAR from the builder stage
COPY --from=builder /app/build/libs/*.jar cms-app.jar

EXPOSE 8080
EXPOSE 9010

# Run the application
ENTRYPOINT ["java", "-jar", "cms-app.jar"]
