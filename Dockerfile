# Stage 1: Build the application
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copia apenas o pom.xml para baixar dependencias e otimizar cache do Docker
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copia as fontes e gera o build do pacote
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Imagem leve de execucao
FROM eclipse-temurin:17-jre
WORKDIR /app

# Copia o jar gerado no estagio anterior
COPY --from=build /app/target/portfolio-manager-0.0.1-SNAPSHOT.jar app.jar

# Expose port
EXPOSE 80

# Comando de inicializacao
ENTRYPOINT ["java", "-jar", "app.jar"]
