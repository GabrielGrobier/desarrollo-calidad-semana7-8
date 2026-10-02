# Dockerfile del BACKEND. Alternativa: mvn spring-boot:run desde la máquina anfitriona.
FROM maven:3.9.11-eclipse-temurin-21 AS builder
WORKDIR /workspace
COPY pom.xml .
COPY src ./src
# Se empaqueta. Los tests se ejecutan por separado con `mvn clean verify`.
RUN mvn -B -DskipTests package

FROM eclipse-temurin:21-jre
WORKDIR /app
RUN useradd --system --uid 10001 appuser
COPY --from=builder /workspace/target/laboratorio-java-testing-s7-2.0.0.jar app.jar
USER appuser
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
