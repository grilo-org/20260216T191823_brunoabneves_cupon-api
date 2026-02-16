# 📦 Cupon API

API REST para gerenciamento de cupons, construída com **Spring Boot 4.0.2**, **Java 21**, **Spring Data JPA**, **H2 em memória**, **MapStruct**, **Lombok** e documentação automática via **SpringDoc OpenAPI (Swagger UI)**.

---

## 🚀 Tecnologias utilizadas
- **Java 21**
- **Spring Boot 4.0.2**
- **Spring Web**
- **Spring Data JPA**
- **Spring Validation**
- **H2 Database (em memória)**
- **MapStruct** (DTO ↔ Entidade)
- **Lombok**
- **SpringDoc OpenAPI** (Swagger UI)
- **JUnit e Mockito**
- **Docker**

---

## ⚙️ Requisitos
Para rodar a aplicação, você precisa de **uma das opções abaixo**:

- [Docker Desktop](https://www.docker.com/products/docker-desktop) instalado e rodando  
  ou
- [Java 21](https://adoptium.net/) + [Maven](https://maven.apache.org/) instalados

---

## 🐳 Rodando com Docker

### 1. Build da imagem
Na raiz do projeto (onde está o `Dockerfile`):
```bash
docker build -t cupon-api .
```

### 2. Subir o container com docker-compose
Na raiz do projeto (onde está o arquivo `docker-compose.yml`), execute:
    ```bash
    docker-compose up -d
    ```
### 3. Swagger
Após subir o container, o swagger iterativo pode ser acessado via:
```http://localhost:8080/swagger-ui/index.html ```