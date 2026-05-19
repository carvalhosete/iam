# IAM Service (Identity and Access Management)

Este é o microsserviço de **Autenticação e Gestão de Usuários** do ecossistema Helpdesk. Ele atua como a porta de entrada segura da aplicação, sendo o responsável direto por validar credenciais, emitir tokens JWT assinados e publicar eventos de notificação de forma assíncrona em filas de mensageria na AWS.

## Tecnologias Utilizadas
* **Java 21** (Uso nativo de Records, Pattern Matching e Otimizações de JVM)
* **Spring Boot 4.0.5**
* **Spring Security** + **JWT (JSON Web Token)** para autenticação stateless.
* **PostgreSQL** rodando via container Docker para persistência relacional.
* **Spring Cloud AWS (SQS)** para integração com filas de mensageria.
* **Swagger / OpenAPI 3** para documentação interativa das rotas.
* **JUnit 5 & MockMvc** para testes de integração automatizados.

## Arquitetura e Fluxo de Negócio
1. **Autenticação:** O cliente envia as credenciais via `POST /login`. O IAM valida o hash no PostgreSQL usando a criptografia **BCrypt**. Se os dados coincidirem, um Token JWT assinado e com tempo de expiração é retornado.
2. **Cadastro & Evento:** Ao registrar um novo usuário (`POST /usuarios`), o serviço salva o registro no banco de dados local e atua imediatamente como um **Producer (Produtor)**, disparando um payload estruturado para a fila `fila-notificacao-boas-vinda` no **AWS SQS** de forma assíncrona e desacoplada.

## Como Executar o Projeto Localmente

### Pré-requisitos
* Java 21 ou superior instalado.
* Docker Desktop ativo.
* Credenciais válidas da AWS com as filas SQS criadas.

### 1. Inicializando a Infraestrutura (Banco de Dados)
Na raiz do projeto, execute o comando do Docker Compose para subir a instância isolada do PostgreSQL:
```bash
  docker-compose up -d
```

### 2. Configurando as Variáveis de Ambiente
Para rodar a aplicação com total segurança, configure as variáveis de ambiente abaixo na sua IDE (ex: IntelliJ em *Edit Configurations -> Environment Variables*) ou no seu terminal de execução:

| Variável | Descrição | Exemplo Local |
| :--- | :--- | :--- |
| `DB_URL` | URL de conexão JDBC para o PostgreSQL | `jdbc:postgresql://localhost:5432/iam_db` |
| `DB_USER` | Usuário do banco de dados | `postgres` |
| `DB_PASSWORD` | Senha do banco de dados | `sua_senha_real_aqui` |
| `JWT_SECRET` | Chave secreta de assinatura dos tokens JWT | `SuaChaveUltraSecretaEComplexa2026!` |
| `AWS_ACCESS_KEY` | Access Key de um usuário IAM na AWS | `AKIAIOSFODNN7EXAMPLE` |
| `AWS_SECRET_KEY` | Secret Key do usuário IAM na AWS | `wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY` |
| `AWS_REGION` | Região AWS onde as filas residem | `sa-east-1` |

*(Nota: O arquivo `application.properties.example` na raiz serve como modelo limpo para novos ambientes).*

### 3. Executando a API
Inicie a aplicação utilizando o wrapper do Maven:
```bash
  ./mvnw spring-boot:run
```

## Documentação da API (Swagger)
Com a aplicação em execução, acesse o painel interativo do Swagger para validar e testar as rotas e DTOs em tempo real:
* **Mapeamento:** `http://localhost:8080/swagger-ui/index.html`

## Tratamento Global de Exceções
A API implementa uma classe anotada com `@RestControllerAdvice` que intercepta falhas e devolve estruturas limpas e padronizadas em formato JSON para o cliente, ocultando detalhes internos do Java:
* `400 Bad Request`: Disparado automaticamente por violações de integridade de DTOs mapeados com `@Valid`. Retorna o campo específico e o erro de preenchimento.
* `401 Unauthorized`: Disparado por credenciais inválidas ou senhas incorretas via `BadCredentialsException`.
* `403 Forbidden`: Bloqueio nativo do filtro de segurança para acessos ou requisições sem token JWT válido em rotas protegidas.
* `404 Not Found`: Disparado quando o Hibernate ou as buscas por ID não localizam o registro correspondente.
* `500 Internal Server Error`: Captura genérica para ocultar stack traces do Java em ambiente de produção, exibindo uma mensagem amigável.

## Testes Automatizados
O microsserviço conta com testes de integração utilizando `MockMvc` para simular o comportamento de requisições HTTP reais (como cenários de falhas de validação de DTO e segurança) diretamente nos Controllers, sem a necessidade de subir um servidor web físico.
* **Para executar a suíte de testes:**
```bash
  ./mvnw test
```
*(Lembrete: Para rodar os testes na IDE, certifique-se de preencher as mesmas Environment Variables na configuração do JUnit).*