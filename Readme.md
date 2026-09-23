# Forum Hub 🗣️

Este é o repositório do **Fórum Hub**, uma API REST desenvolvida em **Java 21** e **Spring Boot** para gestão de usuários, tópicos e respostas. O foco principal deste projeto é a sua **Arquitetura Cloud e Pipeline de CI/CD**, garantindo entregas contínuas, análise de qualidade de código e deploy automatizado na AWS.

---

## 🛠️ Stack Tecnológica

- **Backend:** Java 21, Spring Boot, Spring Security (JWT), Spring Data JPA, Flyway, MySQL.
- **DevOps & Cloud:** Jenkins, AWS (EC2, RDS, S3), SonarQube, IAM Roles, Bash.
- **Integração:** Webhooks (GitHub & SonarQube), Notificações no Slack (ChatOps).

---

## ☁️ Arquitetura Cloud e CI/CD (DevOps)

O projeto conta com um pipeline de CI/CD 100% automatizado, com infraestrutura provisionada na **AWS** focada em segurança, isolamento de rede e performance.

### 🏗️ Infraestrutura AWS
- **EC2 (Jenkins Controller):** Instância dedicada ao gerenciamento do pipeline, plugins e recebimento de Webhooks.
- **EC2 (Jenkins Agent - `jdk21-agent`):** Máquina isolada com Java 21, Maven e AWS CLI. Executa os builds pesados para não sobrecarregar o Controller.
- **EC2 (SonarQube):** Servidor dedicado para análise estática de código e métricas de qualidade.
- **Amazon RDS (MySQL):** Banco de dados relacional gerenciado. O Security Group só aceita conexões na porta 3306 originadas do Jenkins Agent.
- **Amazon S3:** Bucket com versionamento para armazenamento dos artefatos (`.jar`) compilados.
- **IAM Roles:** Permissões nativas da AWS (`s3:PutObject`) anexadas diretamente à EC2 do Agent, eliminando chaves estáticas expostas no código.

### 🔄 Pipeline Jenkins (Declarative)
O fluxo contínuo é orquestrado pelo `Jenkinsfile`. As credenciais (banco de dados, Slack, endpoints) são gerenciadas pelo Jenkins e injetadas dinamicamente no bloco `environment`.

1. **Fetch Code:** Clonagem automática via Webhook a partir de push na branch `main`.
2. **Checkstyle Analysis:** Validação de padrões de codificação (`mvn checkstyle:checkstyle`).
3. **Sonar Code Analysis:** Análise estática enviada ao servidor SonarQube.
4. **Quality Gate:** Timeout de segurança aguardando o webhook do SonarQube. Se o código falhar nas métricas, o build é abortado (*Fail Fast*).
5. **Build:** Empacotamento da aplicação (`-DskipTests` para performance) e arquivamento do `.jar`.
6. **Publish Artifact to S3:** Upload seguro do artefato versionado (`${BUILD_NUMBER}`) para o S3.
7. **Apply Database Migrations:** Execução remota do Flyway diretamente no RDS para aplicar os scripts SQL em produção.
8. **Slack Notifications:** Envio de alertas automáticos com status da execução e links de log para o canal de engenharia.

---

## 🚀 Executando Localmente

1. Clone o repositório:
   ```bash
   git clone [https://github.com/Felipebert/forum-hub.git](https://github.com/Felipebert/forum-hub.git)
   cd forum-hub
   ```
   Configure o banco de dados local (application.properties):
   ```
   spring.datasource.url=jdbc:mysql://localhost:3306/forum_hub
   spring.datasource.username=root
   spring.datasource.password=sua_senha
   spring.jpa.hibernate.ddl-auto=none
   flyway.enabled=true
   ```
   Suba o projeto (O Flyway criará as tabelas automaticamente):
   ```
   ./mvnw spring-boot:run
   ```
A documentação interativa da API (Swagger) ficará disponível em: http://localhost:8080/swagger-ui.html
