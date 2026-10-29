# ---------- Etapa 1: compilar con Maven ----------
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# Primero solo el pom para aprovechar la cache de dependencias
COPY pom.xml .
RUN mvn -B -q dependency:go-offline

COPY src ./src
RUN mvn -B -q clean package -DskipTests

# ---------- Etapa 2: imagen final liviana ----------
FROM eclipse-temurin:17-jre
WORKDIR /app

COPY --from=build /app/target/reporteservice-0.0.1-SNAPSHOT.jar app.jar

# Render inyecta PORT; la aplicacion lo lee con server.port=${PORT:8080}
EXPOSE 8080
ENV JAVA_OPTS="-XX:MaxRAMPercentage=75.0"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
