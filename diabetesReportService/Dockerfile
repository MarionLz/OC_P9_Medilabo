# Étape 1 : build
FROM maven:3.9.6-eclipse-temurin AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Étape 2 : runtime
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=builder /app/target/diabetesReportService-0.0.1-SNAPSHOT.jar diabetesReportService.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "diabetesReportService.jar"]