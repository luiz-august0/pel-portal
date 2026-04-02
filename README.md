# PEL Portal

Sistema de portal educacional composto por microsservicos para gestao de alunos, inscricoes, cursos e documentos. A arquitetura segue principios de **Domain-Driven Design (DDD)** e **Clean Architecture**, com comunicacao assincrona via **Apache Kafka** e integracao com um sistema legado.

## Arquitetura

```
                        ┌──────────────────────────┐
                        │   PEL Portal FrontEnd     │
                        └────────────┬─────────────┘
                                     │
                                     ▼
                        ┌──────────────────────────┐
                        │   API Gateway (Kong)      │
                        │                           │
                        │  - Autorizacao JWT (HS256) │
                        │  - Roteamento: /portal,   │
                        │    /gestao, /portal/auth  │
                        └──────┬──────────┬─────────┘
                               │          │
                ┌──────────────┘          └──────────────┐
                ▼                                        ▼
  ┌──────────────────────────┐          ┌──────────────────────────┐
  │    PEL Portal API        │          │    PEL Gestao API        │
  │    (porta 8081)          │          │    (porta 8082)          │
  │                          │          │                          │
  │  - Registro, Login       │          │  - Inscricao, Nivelamento│
  │  - Dependentes, Perfil   │          │  - Transferencia, Cursos │
  │  - Documentos            │          │  - Notas, Frequencia     │
  │  - BD independente       │          │  - Ligado ao BD legado   │
  │    (PostgreSQL)          │          │    (PostgreSQL)          │
  └────────────┬─────────────┘          └──────────┬───────────────┘
               │                                   │
               │         ┌──────────────┐          │
               └────────►│    Kafka     │◄─────────┘
                         └──────┬───────┘
                                │
                                ▼
                       ┌────────────────┐
                       │    Legado      │
                       └────────────────┘
```

### Fluxo de Inscricao

1. **PEL Portal API** registra a inscricao e, ao finalizar, envia evento via Kafka para o legado
2. **Legado** gera boletos e PDF do contrato e envia mensagem para PEL Gestao API
3. **PEL Gestao API** recebe a mensagem, salva contrato no servidor e registra no banco de dados

### Fluxos de Integracao Legado via Kafka

| Fluxo | Direcao | Descricao |
|-------|---------|-----------|
| Validacao de cadastro | Legado → Portal API | Legado valida pre-cadastro e comunica resultado ao Portal |
| Finalizacao de turma | Legado → Gestao API | Legado comunica finalizacao para salvar PDF do certificado |
| Geracao de declaracao | Gestao API ↔ Legado | Gestao solicita e recebe PDF de declaracao do legado |
| Criacao/atualizacao de usuario | Portal API → Gestao API | Portal publica evento de usuario para sincronizar dados |

## Stack Tecnologica

| Componente | Tecnologia | Versao |
|-----------|-----------|--------|
| Linguagem | Java | 21 |
| Framework | Spring Boot | 3.5.4 |
| Build | Maven | 3.9.5 |
| Banco de Dados | PostgreSQL | - |
| ORM | Hibernate / JPA | - |
| Migracao de BD | Flyway | - |
| Mensageria | Apache Kafka | 7.4.0 (Confluent) |
| API Gateway | Kong Gateway | 3.11.0.3 |
| Autenticacao | JWT (Auth0 java-jwt) | 4.4.0 |
| Armazenamento | AWS S3 | Spring Cloud AWS 2.4.4 |
| Documentacao API | SpringDoc OpenAPI | 2.7.0 |
| Email | Spring Mail (Gmail SMTP) | - |
| Containerizacao | Docker | Multi-stage build |

## Estrutura do Projeto

```
pel-portal/
├── pel-portal-api/      # API do portal (registro, login, perfil, dependentes, documentos)
├── pel-gestao-api/      # API de gestao (inscricoes, cursos, turmas, notas, nivelamento)
└── pel-gateway/         # API Gateway Kong + Kafka (infraestrutura)
```

## Servicos

### PEL Portal API (porta 8081)

API responsavel pelo cadastro e gerenciamento de usuarios do portal.

**Funcionalidades:**
- Registro e autenticacao de usuarios (JWT)
- Recuperacao de senha por e-mail
- Gerenciamento de perfil e endereco
- Cadastro e vinculacao de dependentes
- Upload/download de documentos (S3 ou local)
- Geracao de link de autorizacao para responsaveis de menores
- Notificacoes por e-mail

**Endpoints principais** (`/api/v1`):

| Grupo | Metodo | Endpoint | Descricao |
|-------|--------|----------|-----------|
| Auth | POST | `/auth/register` | Registro de usuario |
| Auth | POST | `/auth/login` | Login |
| Auth | POST | `/auth-recovery/generate` | Gerar token de recuperacao |
| Auth | POST | `/auth-recovery/confirm` | Confirmar recuperacao de senha |
| User | GET | `/user/current` | Usuario autenticado |
| User | PUT | `/user/update` | Atualizar usuario |
| User | POST | `/user/address` | Criar/atualizar endereco |
| User | POST | `/user/change-password` | Alterar senha |
| User | GET | `/user/status` | Status do usuario |
| Dependent | POST | `/dependent/create` | Criar dependente |
| Dependent | GET | `/dependent/list` | Listar dependentes |
| Dependent | GET | `/dependent/{id}/info` | Info do dependente |
| Dependent | PUT | `/dependent/{id}/update` | Atualizar dependente |
| Document | POST | `/document/upload` | Upload de documento |
| Document | GET | `/document/download` | Download de documento |
| Document | DELETE | `/document/delete` | Remover documento |

**Topicos Kafka:**

| Topico | Direcao | Descricao |
|--------|---------|-----------|
| `portal-update-create-user` | Producer | Notifica criacao/atualizacao de usuario |
| `pre-registration-review` | Consumer | Recebe resultado da revisao de pre-cadastro |

**Banco de dados:** PostgreSQL independente (`pelportal`)

Tabelas: `portal_user`, `portal_user_details`, `portal_address`, `portal_user_dependent`, `portal_document`, `portal_template_email`

---

### PEL Gestao API (porta 8082)

API responsavel pela gestao academica: inscricoes, cursos, turmas, notas e frequencia. Conectada diretamente ao banco de dados do sistema legado.

**Funcionalidades:**
- Inscricao em cursos e turmas
- Nivelamento (agendamento e registro)
- Transferencia entre turmas
- Consulta de notas e frequencia
- Download de PDFs (contrato, certificado, declaracao)
- Cancelamento automatico de inscricoes pendentes (job horario)
- Sincronizacao de usuarios vindos do Portal API

**Endpoints principais** (`/api/v1`):

| Grupo | Metodo | Endpoint | Descricao |
|-------|--------|----------|-----------|
| Inscription | GET | `/inscription/grouped-by-year` | Inscricoes agrupadas por ano |
| Inscription | POST | `/inscription/register?classId=x` | Registrar inscricao |
| Inscription | POST | `/inscription/{id}/finalize-register` | Finalizar inscricao |
| Inscription | POST | `/inscription/{id}/cancel` | Cancelar inscricao |
| Inscription | GET | `/inscription/{id}/grades` | Notas |
| Inscription | GET | `/inscription/{id}/attendance` | Frequencia |
| Inscription | GET | `/inscription/{id}/contract-pdf` | PDF do contrato |
| Inscription | GET | `/inscription/{id}/certificate-pdf` | PDF do certificado |
| Course | GET | `/course/list-active` | Cursos ativos |
| Course | GET | `/course/{id}/actual-level` | Nivel atual do curso |
| Class | GET | `/class/list-available` | Turmas disponiveis |
| Class | GET | `/class/{id}/details` | Detalhes da turma |
| Leveling | POST | `/leveling/register` | Registrar nivelamento |
| Leveling | DELETE | `/leveling/{id}/cancel` | Cancelar nivelamento |
| Transfer | POST | `/transfer/?inscriptionId=x&classId=x` | Registrar transferencia |
| Transfer | GET | `/transfer/grouped-by-year` | Transferencias por ano |

**Topicos Kafka:**

| Topico | Direcao | Descricao |
|--------|---------|-----------|
| `portal-update-create-user` | Consumer | Recebe dados de usuario do Portal |
| `finalize-inscription-register` | Producer | Notifica finalizacao de inscricao |
| `inscription-contract-pdf-generated` | Consumer | Recebe PDF de contrato gerado |
| `inscription-certificate-pdf-generated` | Consumer | Recebe PDF de certificado gerado |
| `person-declaration-pdf-ask` | Producer | Solicita geracao de PDF de declaracao |
| `person-declaration-pdf-generated` | Consumer | Recebe PDF de declaracao gerado |

**Banco de dados:** PostgreSQL do sistema legado

---

### PEL Gateway

API Gateway baseado no **Kong Gateway 3.11.0.3** em modo **DB-less** (configuracao declarativa via YAML).

**Roteamento:**

| Rota publica | Servico destino | Porta | JWT |
|-------------|-----------------|-------|-----|
| `/portal/auth/*` | pel-portal-api → `/api/v1/auth` | 8081 | Nao |
| `/portal/*` | pel-portal-api → `/api/v1` | 8081 | Sim |
| `/gestao/*` | pel-gestao-api → `/api/v1` | 8082 | Sim |

**Autenticacao JWT:**
- Algoritmo: HS256
- Claim verificado: `exp` (expiracao)
- Localizacao do token: header `Authorization` ou query param `?jwt=`
- Consumer unico: `pel-consumer`
- Endpoint publico (sem JWT): `/portal/auth/*` (login e registro)

**Infraestrutura Kafka** (docker-compose separado):
- Zookeeper (porta 2181)
- Kafka Broker (porta 9092)
- Kafka UI (porta 9090)

## Arquitetura de Pacotes (Clean Architecture)

Ambas as APIs seguem a mesma estrutura de pacotes:

```
com.almeja.pel.{portal|gestao}/
├── core/                          # Camada de dominio (sem dependencias de framework)
│   ├── domain/
│   │   ├── entity/                # Entidades JPA
│   │   ├── enums/                 # Enumeracoes de dominio
│   │   ├── factory/               # Factories para criacao de entidades
│   │   ├── service/               # Servicos de dominio (validacoes)
│   │   └── usecase/               # Casos de uso (logica de negocio)
│   ├── dto/                       # Data Transfer Objects
│   ├── event/                     # Eventos de dominio
│   ├── exception/                 # Excecoes customizadas
│   ├── gateway/                   # Interfaces de abstracoes externas
│   └── mediator/                  # Padrao Command/Mediator
├── inbound/                       # Camada de entrada
│   ├── http/                      # Controllers REST
│   │   └── interfaces/            # Interfaces dos controllers
│   ├── event/                     # Kafka listeners
│   └── job/                       # Jobs agendados (apenas gestao-api)
└── infra/                         # Camada de infraestrutura
    ├── config/                    # Configuracoes Spring (Security, CORS, Swagger)
    ├── repository/                # Repositorios JPA
    ├── service/                   # Servicos de infra (Mail, Kafka, S3, Token)
    ├── gateway/                   # Implementacoes dos gateways
    ├── security/                  # Configuracao de seguranca
    ├── exception/                 # Handler global de excecoes
    └── mediator/                  # Implementacao do Mediator e handlers
```

## Pre-requisitos

- **Java 21**
- **Maven 3.9.5+**
- **Docker e Docker Compose**
- **PostgreSQL**

## Como Executar

### 1. Infraestrutura (Gateway + Kafka)

```bash
cd pel-gateway

# Iniciar Kafka e Zookeeper
docker compose -f docker-compose-kafka.yml up -d

# Iniciar Kong Gateway
docker compose -f docker-compose-gateway.yaml up -d
```

**Portas expostas:**

| Servico | Porta |
|---------|-------|
| Kong Proxy (HTTP) | 8000 |
| Kong Proxy (HTTPS) | 8443 |
| Kong Admin API | 8001 |
| Kong Manager UI | 8002 |
| Kafka Broker | 9092 |
| Kafka UI | 9090 |

### 2. PEL Portal API

```bash
cd pel-portal-api

# Copiar e configurar variaveis de ambiente
cp .env-example .env

# Executar com Docker
docker compose up -d

# OU executar localmente com Maven
./mvnw spring-boot:run
```

**Variaveis de ambiente necessarias:**

| Variavel | Descricao |
|----------|-----------|
| `PORTAL_PEL_API_DB_URL` | URL do PostgreSQL (ex: `jdbc:postgresql://localhost:5432/pelportal`) |
| `PORTAL_PEL_API_DB_USERNAME` | Usuario do banco |
| `PORTAL_PEL_API_DB_PASSWORD` | Senha do banco |
| `PORTAL_PEL_API_TOKEN_SECRET` | Secret para assinatura JWT |
| `PORTAL_PEL_API_TOKEN_SESSION_ISSUER` | Issuer do token de sessao |
| `PORTAL_PEL_API_MAIL_SENDER_EMAIL` | E-mail do remetente (Gmail) |
| `PORTAL_PEL_API_MAIL_SENDER_PASSWORD` | Senha de app do Gmail |
| `PORTAL_PEL_API_CORS_ALLOWED_ORIGINS` | Origens CORS permitidas |
| `PORTAL_PEL_API_AWS_ACCESS_KEY` | Chave de acesso AWS |
| `PORTAL_PEL_API_AWS_SECRET_KEY` | Chave secreta AWS |
| `PORTAL_PEL_API_AWS_S3_BUCKET_NAME` | Nome do bucket S3 |
| `PORTAL_PEL_API_FILE_S3_ENABLED` | Habilitar S3 (`true`/`false`) |

### 3. PEL Gestao API

```bash
cd pel-gestao-api

# Copiar e configurar variaveis de ambiente
cp .env-example .env

# Executar com Docker
docker compose up -d

# OU executar localmente com Maven
./mvnw spring-boot:run
```

**Variaveis de ambiente necessarias:**

| Variavel | Descricao |
|----------|-----------|
| `GESTAO_PEL_API_DB_URL` | URL do PostgreSQL do legado |
| `GESTAO_PEL_API_DB_USERNAME` | Usuario do banco |
| `GESTAO_PEL_API_DB_PASSWORD` | Senha do banco |
| `GESTAO_PEL_API_CORS_ALLOWED_ORIGINS` | Origens CORS permitidas |
| `GESTAO_PEL_API_AWS_ACCESS_KEY` | Chave de acesso AWS |
| `GESTAO_PEL_API_AWS_SECRET_KEY` | Chave secreta AWS |
| `GESTAO_PEL_API_AWS_S3_BUCKET_NAME` | Nome do bucket S3 |
| `GESTAO_PEL_API_FILE_S3_ENABLED` | Habilitar S3 (`true`/`false`) |

## Documentacao da API

Com os servicos em execucao, a documentacao OpenAPI (Swagger UI) esta disponivel em:

- **Portal API:** http://localhost:8081/swagger-ui.html
- **Gestao API:** http://localhost:8082/swagger-ui.html

## Topicos Kafka

| Topico | Producer | Consumer | Descricao |
|--------|----------|----------|-----------|
| `portal-update-create-user` | Portal API | Gestao API | Sincronizacao de dados de usuario |
| `pre-registration-review` | Legado | Portal API | Resultado da revisao de pre-cadastro |
| `finalize-inscription-register` | Gestao API | Legado | Evento de finalizacao de inscricao |
| `inscription-contract-pdf-generated` | Legado | Gestao API | PDF de contrato gerado |
| `inscription-certificate-pdf-generated` | Legado | Gestao API | PDF de certificado gerado |
| `person-declaration-pdf-ask` | Gestao API | Legado | Solicitacao de geracao de declaracao |
| `person-declaration-pdf-generated` | Legado | Gestao API | PDF de declaracao gerado |

## Banco de Dados

### PEL Portal API (banco independente)

Migracoes gerenciadas pelo **Flyway**. Tabelas:

| Tabela | Descricao |
|--------|-----------|
| `portal_user` | Usuarios (CPF, email, senha, status) |
| `portal_user_details` | Detalhes (data nascimento, telefone, necessidades especiais) |
| `portal_address` | Enderecos |
| `portal_user_dependent` | Vinculo usuario-dependente |
| `portal_document` | Documentos (foto, laudo medico, comprovantes) |
| `portal_template_email` | Templates de e-mail |

### PEL Gestao API (banco do legado)

Conectado diretamente ao banco do sistema legado. Principais entidades:

| Entidade | Descricao |
|----------|-----------|
| `pessoa` | Pessoas (alunos/usuarios) |
| `matricula` | Inscricoes/matriculas |
| Cursos, Turmas, Niveis | Estrutura academica |
| Notas, Frequencia | Desempenho academico |
| Nivelamento, Transferencia | Processos academicos |
| Arquivos, Duplicatas | Documentos e boletos |
