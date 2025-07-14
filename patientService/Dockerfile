# Étape 1 : Builder le jar avec Maven + Java 21
FROM maven:3.9.6-eclipse-temurin AS builder

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

# Étape 2 : Image finale avec Java 21 JRE
FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=builder /app/target/patientService-0.0.1-SNAPSHOT.jar patientService.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "patientService.jar"]

