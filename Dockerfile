# ETAPA 1: Construcción (Build)
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# 1. Copiamos el pom.xml para descargar las dependencias (optimiza la caché de Docker)
COPY pom.xml .
RUN mvn dependency:go-offline

# 2. Copiamos el código fuente y compilamos el proyecto
COPY src ./src
RUN mvn clean package -DskipTests

# ETAPA 2: Ejecución (Runtime)
FROM eclipse-temurin:21-jdk-jammy
WORKDIR /app

# 3. Copiamos el .jar generado basándonos exactamente en tu pom.xml
# El nombre se construye como: artifactId-version.jar
COPY --from=build /app/target/ms-products-0.0.1-SNAPSHOT.jar app.jar

# Exponemos el puerto estándar
EXPOSE 8080

# 4. Comando para arrancar la aplicación
# Usamos parámetros de optimización para contenedores
ENTRYPOINT ["java", "-jar", "app.jar"]