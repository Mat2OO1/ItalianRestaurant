FROM maven:3.9.1-eclipse-temurin-17-alpine AS maven
WORKDIR /app
COPY pom.xml ./
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn package -Dmaven.test.skip

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=maven /app/target/ItalianRestaurantBackend-0.0.1-SNAPSHOT.jar ./
COPY certs ./certs
EXPOSE 8443
CMD ["java", "-jar", "ItalianRestaurantBackend-0.0.1-SNAPSHOT.jar"]