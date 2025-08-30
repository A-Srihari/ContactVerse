# Build stage
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Runtime stage
FROM openjdk:21-jdk-slim
WORKDIR /app
COPY --from=build /app/target/ContactVerse-0.0.1-SNAPSHOT.jar ContactVerse.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","ContactVerse.jar"]
