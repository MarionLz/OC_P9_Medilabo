# Étape 1 : build
FROM maven:3.9.6-eclipse-temurin AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Étape 2 : runtime
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

ENV LOG_DIR=/diabetesReportService/logs
RUN mkdir -p /diabetesReportService/logs

COPY --from=build /app/target/diabetesReportService-0.0.1-SNAPSHOT.jar diabetesReportService.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "diabetesReportService.jar"]