
# Pokémon SOAP API

## Overview
This project provides a simple SOAP API that retrieves Pokémon data from an external REST API (`https://pokeapi.co/api/v2/pokemon/`) and presents the information via a SOAP endpoint. The API exposes methods like abilities, base experience, held items, ID, name, and location areas.

## Prerequisites
To run this project, make sure you have the following installed:

- **Java 21**
- **Docker**
- **Maven** (Optional: If Maven is not installed, you can use the Maven wrapper included in the project.)

## Author
Adrián Moreno - [i13120427@gmail.com](mailto:i13120427@gmail.com)

## Checklist
The project implements:

- ✅ SOAP service consuming a REST API from `https://pokeapi.co/api/v2/pokemon/`
- ✅ H2 Database integration for logging requests
- ✅ SonarQube analysis: [Sonar URL](https://sonarcloud.io/summary/new_code?id=Monoradioactivo_bnky-test)
- ✅ Unit tests
- ✅ Cucumber tests
- ❌ Swagger integration (Pending)

## Running the Application

### Using Maven

To run the application with Maven, execute the following command:

```bash
./mvnw spring-boot:run
```

### Using Docker

Alternatively, you can run the application using Docker with the provided `Dockerfile`:

1. **Build the Docker image:**
   ```bash
   docker build -t pokemon-soap-api .
   ```

2. **Run the Docker container:**
   ```bash
   docker run -p 8080:8080 pokemon-soap-api
   ```

## Application Details

- The application runs on **port 8080**.
- SOAP service is available at: `http://localhost:8080/ws`

### Example SOAP Request

```xml
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                  xmlns:pok="http://simio.mx/pokemon">
    <soapenv:Header/>
    <soapenv:Body>
        <pok:getPokemonRequest>
            <name>ditto</name>
        </pok:getPokemonRequest>
    </soapenv:Body>
</soapenv:Envelope>
```

### Example SOAP Response

```xml
<SOAP-ENV:Envelope xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/">
    <SOAP-ENV:Header/>
    <SOAP-ENV:Body>
        <ns3:getPokemonResponse xmlns:ns3="http://simio.mx/pokemon">
            <abilities>
                <ability>
                    <name>limber</name>
                    <hidden>false</hidden>
                    <slot>1</slot>
                </ability>
                <ability>
                    <name>imposter</name>
                    <hidden>true</hidden>
                    <slot>3</slot>
                </ability>
            </abilities>
            <baseExperience>101</baseExperience>
            <heldItems>
                <heldItem>
                    <name>metal-powder</name>
                </heldItem>
                <heldItem>
                    <name>quick-powder</name>
                </heldItem>
            </heldItems>
            <id>132</id>
            <name>ditto</name>
            <locationAreas>
                <locationArea>
                    <name>sinnoh-route-218-area</name>
                </locationArea>
                <locationArea>
                    <name>johto-route-34-area</name>
                </locationArea>
                <locationArea>
                    <name>johto-route-35-area</name>
                </locationArea>
                <locationArea>
                    <name>johto-route-47-area</name>
                </locationArea>
                <locationArea>
                    <name>kanto-route-13-area</name>
                </locationArea>
                <locationArea>
                    <name>kanto-route-14-area</name>
                </locationArea>
                <locationArea>
                    <name>kanto-route-15-area</name>
                </locationArea>
                <locationArea>
                    <name>cerulean-cave-1f</name>
                </locationArea>
                <locationArea>
                    <name>cerulean-cave-2f</name>
                </locationArea>
                <locationArea>
                    <name>cerulean-cave-b1f</name>
                </locationArea>
                <locationArea>
                    <name>kanto-route-23-area</name>
                </locationArea>
                <locationArea>
                    <name>pokemon-mansion-b1f</name>
                </locationArea>
                <locationArea>
                    <name>desert-underpass-area</name>
                </locationArea>
                <locationArea>
                    <name>giant-chasm-forest</name>
                </locationArea>
                <locationArea>
                    <name>giant-chasm-forest-cave</name>
                </locationArea>
                <locationArea>
                    <name>pokemon-village-area</name>
                </locationArea>
                <locationArea>
                    <name>johto-safari-zone-zone-wetland</name>
                </locationArea>
                <locationArea>
                    <name>alola-route-9-police-station</name>
                </locationArea>
                <locationArea>
                    <name>konikoni-city-area</name>
                </locationArea>
                <locationArea>
                    <name>mount-hokulani-main</name>
                </locationArea>
                <locationArea>
                    <name>mount-hokulani-east</name>
                </locationArea>
                <locationArea>
                    <name>mount-hokulani-west</name>
                </locationArea>
            </locationAreas>
        </ns3:getPokemonResponse>
    </SOAP-ENV:Body>
</SOAP-ENV:Envelope>
```

## SonarQube Analysis

You can check the SonarQube analysis of the project at the following URL:  
[SonarQube Project](https://sonarcloud.io/summary/new_code?id=Monoradioactivo_bnky-test)

## Dockerfile

Below is the `Dockerfile` used to build the Docker image:

```dockerfile
FROM eclipse-temurin:21-jdk-alpine
LABEL authors="amoreno"

WORKDIR /app

COPY target/pokemon-api-soap-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
```
