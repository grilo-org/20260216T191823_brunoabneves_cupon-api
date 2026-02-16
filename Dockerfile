# Etapa de build usando Maven
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Copia pom.xml e baixa dependências
COPY pom.xml .
RUN mvn dependency:go-offline

# Copia código fonte e compila
COPY src ./src
RUN mvn clean package -DskipTests

# Etapa de runtime
FROM eclipse-temurin:21-jdk
WORKDIR /app

# Copia o jar gerado
COPY --from=build /app/target/cupon-api-0.0.1-SNAPSHOT.jar app.jar

# Porta exposta
EXPOSE 8080

# Comando de inicialização
ENTRYPOINT ["java","-jar","app.jar"]