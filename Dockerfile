# Use a Maven image to build the application
FROM maven:3.9.1-eclipse-temurin-17-alpine AS maven

# Set working directory
WORKDIR /app

# Copy the pom.xml file to the container
COPY pom.xml ./

# Download the project dependencies and cache them in the Docker image
RUN mvn dependency:go-offline

# Copy the application source code to the container
COPY src ./src

# Build the application
RUN mvn package

# Use a JRE image to run the application
FROM eclipse-temurin:17-jre-alpine

# Set working directory
WORKDIR /app

# Copy the application JAR file from the Maven image to the JRE image
COPY --from=maven /app/target/ItalianRestaurant-0.0.1-SNAPSHOT.jar ./

# Expose port 8080 for the container
EXPOSE 8080

# Start the Spring Boot application
CMD ["java", "-jar", "ItalianRestaurant-0.0.1-SNAPSHOT.jar"]