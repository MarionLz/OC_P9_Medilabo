# Étape 1 : build
FROM maven:3.9.6-eclipse-temurin AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:resolve
COPY src ./src
RUN mvn clean package -DskipTests

# Étape 2 : runtime
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/notesService-0.0.1-SNAPSHOT.jar notesService.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "notesService.jar"]
