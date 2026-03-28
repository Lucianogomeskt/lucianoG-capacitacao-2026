
# E-Commerce API | Treinamento Java & Oracle | Minsait

Este projeto é uma API REST robusta desenvolvida para o gerenciamento de um e-commerce, focada em boas práticas de desenvolvimento, persistência em banco de dados relacional e arquitetura escalável.

## 🚀 O que o sistema faz atualmente?
Com base na nossa documentação interativa (Swagger), o sistema permite:

* **Gestão de Estoque:** Registro de entradas, saídas e ajuste de saldo com rastreabilidade via métodos `PUT`.
* **Controle de Categorias:** CRUD completo para organização lógica de produtos.
* **Catálogo de Produtos:** Cadastro detalhado integrado ao estoque e categorias no Oracle XE.
* **Fluxo de Carrinho:** Gerenciamento de itens e persistência por usuário.

---

## 🛠️ Stack Tecnológica
* **Linguagem:** Java 25 (Temurin JDK)
* **Framework:** Spring Boot 3.5.11
* **Persistência:** Spring Data JPA / Hibernate
* **Banco de Dados:** Oracle XE 21c
* **Documentação:** SpringDoc OpenAPI (Swagger UI)
* **Build Tool:** Maven

---

## ⚙️ Como Executar o Projeto

### 1. Pré-requisitos
* JDK 25 instalado.
* Oracle Database XE rodando localmente.
* Maven configurado no seu ambiente/IDE.

### 2. Configuração do Banco de Dados
O projeto utiliza variáveis de ambiente para garantir a segurança das credenciais. Configure as seguintes variáveis no seu sistema ou no IntelliJ (Edit Configurations):

**Exemplo de configuração no `application.yml`:**
```yaml
spring:
  application:
    name: jp-capacitacao-2026

  datasource:
    url: ${URL_DB:jdbc:oracle:thin:@localhost:1521:xe}
    username: ${USERNAME_DB:admin}
    password: ${PASSWORD_DB:admin}
    driver-class-name: oracle.jdbc.OracleDriver

  jpa:
    show-sql: true
    hibernate:
      ddl-auto: update
    properties:
      hibernate:
        format_sql: true

server:
  port: 8080
```

---

## 📂 Estrutura do Projeto
O projeto segue o padrão de camadas para facilitar a manutenção e garantir a separação de responsabilidades:

* **`controller/`**: Exposição dos endpoints REST e tratamento de requisições.
* **`service/`**: Camada de inteligência, onde residem as regras de negócio.
* **`repository/`**: Interfaces do Spring Data JPA que gerenciam a persistência no Oracle XE.
* **`model/` ou `entity/`**: Mapeamento objeto-relacional (ORM) das tabelas do banco.
* **`dto/`**: Data Transfer Objects (Request/Response), garantindo segurança e validação na entrada de dados.
* **`enums/`**: Tipos enumerados que padronizam valores fixos do sistema (ex: Status de Pedido, Tipo de Movimentação), garantindo a integridade dos dados no banco.

---

## 📖 Documentação da API (Swagger)
A documentação detalhada dos endpoints, incluindo os formatos de JSON e parâmetros necessários, pode ser acessada via Swagger UI após rodar o projeto:

👉 `http://localhost:8080/swagger-ui.html`

Para a definição rigorosa dos códigos de retorno (Ex: **201 Created** para novos registros, **400 Bad Request** para erros de validação), foi utilizada como referência técnica a [Documentação Completa de HTTP Response Codes da Contabo](https://contabo.com/blog/http-response-codes-server-statuses/), assegurando que a API responda conforme os padrões globais da web.

---
