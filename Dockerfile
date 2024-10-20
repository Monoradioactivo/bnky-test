FROM eclipse-temurin:21-jdk-alpine
LABEL authors="amoreno"

WORKDIR /app

COPY target/pokemon-api-soap-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]