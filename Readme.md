# Forum Hub 🗣️

Este é um projeto de um **Fórum Hub** desenvolvido em **Java 21** utilizando **Spring Boot**, que permite a criação de usuários, tópicos e respostas, com operações **CRUD** completas para as principais entidades. A aplicação é organizada de forma modular e utiliza tecnologias modernas como **Spring Security**, **Spring Data JPA**, **Spring Validation**, e **Springdoc OpenAPI**.

---

## ✨ Tecnologias Utilizadas

- **Java 21**
- **Spring Boot**
- **Spring Security**
- **Spring Data JPA**
- **Spring Validation**
- **Springdoc OpenAPI**
- **MySql**
- **Flyway**

---

## ⚙️ Funcionalidades

### Autenticação e Autorização
- Registro de novos usuários.
- Login com geração de **JWT Token**.
- Proteção de endpoints com **Spring Security**.

### Operações de Fórum
- **CRUD** completo para as entidades:
  - **Usuários**
  - **Tópicos**
  - **Respostas**

### Validações
- Validação de dados de entrada utilizando **Spring Validation**.

### Migrations
- Uso do **Flyway** para migrações de banco de dados, garantindo versionamento e integridade dos dados.

### Documentação da API
- Documentação interativa gerada automaticamente com **Springdoc OpenAPI**.

---

## 🚀 Como Executar o Projeto

### Pré-requisitos

- **Java 21** instalado.
- **Maven** configurado.
- Banco de dados **MySQL** (ou outro de sua preferência).

### Passo a Passo

1. Clone o repositório:
   ```bash
   git clone https://github.com/Felipebert/forum-hub.git
   cd forum-hub

2. Configure o banco de dados no arquivo src/main/resources/application.properties:
   ```bash
    spring.datasource.url=jdbc:mysql://localhost:3306/forum_hub
    spring.datasource.username=seu_usuario
    spring.datasource.password=sua_senha
    spring.jpa.hibernate.ddl-auto=none
    flyway.enabled=true

3. Execute as migrations:
  ```bash
  ./mvnw flyway:migrate
  ```

4. Execute o projeto:
  ```bash
  ./mvnw spring-boot:run
  ```

Após executar o projeto, você pode acessar a documentação da API em:
  ```bash
  http://localhost:8080/swagger-ui.html
  ```