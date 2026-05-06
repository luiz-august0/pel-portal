# AGENTS.md — Portal PEL API

Documento guia para agentes de IA (Claude, Cursor, Copilot, etc.) trabalhando neste repositório. Leia antes de qualquer alteração.

> Regras genéricas reutilizáveis (Clean Arch + DDD + Quarkus) estão em `.claude/rules/`. Este arquivo cobre apenas o que é **específico do Portal PEL API**.

---

## 1. Visão geral do projeto

API backend do **Portal PEL** — sistema de cadastro/gestão de usuários (e dependentes/menores de idade com responsável legal) que se integra a outros serviços via Kafka, envia e-mails transacionais e armazena documentos no AWS S3.

- **Stack**: Java 25, Quarkus 3.35.1, PostgreSQL 16, Flyway, Hibernate ORM (com `quarkus-spring-data-jpa`), SmallRye Reactive Messaging (Kafka), Quarkus Mailer, AWS S3, Auth0 java-jwt, BCrypt, Lombok, ModelMapper, Gson.
- **Testes**: JUnit 5, RestAssured, Mockito (`@InjectMock`), AssertJ, Testcontainers (Postgres) e SmallRye in-memory connector para Kafka.
- **Build**: Maven Wrapper (`./mvnw`).
- **Runtime**: container Java 25 (Eclipse Temurin Alpine). Porta `8081`. Path base `/api/v1`.

---

## 2. Estrutura de pacotes

Pacote raiz: `com.almeja.pel.portal`.

```
core/                          # Núcleo de domínio (sem dependência de framework de transporte)
  annotation/                  # Anotações próprias (ex.: @ObjectFieldsOnly)
  domain/
    entity/      + base/       # Entidades JPA (BaseEntity com createdAt/updatedAt)
    enums/       + base/       # Enums + IEnum/IEnumConverter (AttributeConverter genérico)
    factory/                   # Factories de agregados (UserFactory)
    service/                   # Domain services (validators, orquestração de regra de domínio)
    usecase/<bounded-context>/ # Casos de uso (sufixo "UC")
  dto/                         # DTOs de entrada/saída do core (POJOs Lombok)
    base/                      # BaseDTO (Serializable)
    record/                    # DTOs imutáveis (Java records) p/ inputs/outputs simples
  event/         + dto/        # Publicação de eventos de domínio
  exception/     + enums/      # AppException, ValidatorException, EnumAppException
  gateway/<categoria>/         # Interfaces de gateway (sufixo "GTW") — porta de saída
  mail/          + builders/   # MailBuilder<P> abstrato + builders concretos por template
  mediator/      + command/    # Mediator (Command/CommandHandler/AsyncCommandHandler)
  repository/                  # Spring Data JPA repositories (interfaces)
  util/          + enums/      # Helpers puros (StringUtil, DateUtil, CpfUtil, etc.)

inbound/                       # Pontos de entrada (controllers/listeners). Só chamam UC.
  http/                        # JAX-RS Controllers (sufixo "Controller")
  event/                       # Kafka listeners (sufixo "KafkaListener")

infra/                         # Implementações de gateways e detalhes de framework
  config/                      # Producers / config CDI (ex.: JacksonConfig)
  constants/                   # Constantes de infra (ex.: PrefixPathConstant)
  context/                     # AuthContext (RequestScoped)
  dto/         + mapper/       # DTOs específicos da camada de transporte + mappers
  exception/   + classes/handlers/  # ExceptionMappers JAX-RS + ErrorResponse
  interceptor/                 # Filtros JAX-RS (ex.: AuthInterceptor)
  mediator/    + handler/      # MediatorImpl + handlers concretos (sufixo "CommandHandler")
  service/<categoria>/         # Implementações dos GTW (sufixo "Service")
  util/                        # Helpers de infra (TokenUtil, FileUtil)
```

Convenções de sufixo (importantes — siga ao adicionar arquivos):

| Sufixo            | Camada     | Exemplo                                  |
| ----------------- | ---------- | ---------------------------------------- |
| `*Entity`         | core       | `UserEntity`                             |
| `*Repository`     | core       | `UserRepository extends JpaRepository`   |
| `*UC`             | core       | `RegisterUC`                             |
| `*Factory`        | core       | `UserFactory`                            |
| `*Service` (core) | core       | `UserValidatorService` (domain service)  |
| `*GTW`            | core       | `AuthorizationGTW` (interface — porta)   |
| `*Command`        | core       | `RegisterUserCommand` (record)           |
| `Enum*`           | core       | `EnumDocumentType`                       |
| `*DTO`            | core/infra | `UserDTO`, `DependentDTO`                |
| `*Record`         | core       | `AuthenticateRecord` (input/output imut) |
| `*Controller`     | inbound    | `UserController`                         |
| `*KafkaListener`  | inbound    | `PreRegistrationReviewKafkaListener`     |
| `*CommandHandler` | infra      | `AuthenticateCommandHandler`             |
| `*Service`(infra) | infra      | `AuthorizationService implements *GTW`   |
| `*Mapper`         | infra      | `DependentsLinkedListMapper`             |

Direção de dependência: `inbound` → `core` ← `infra`. **`core` nunca importa `infra` nem `inbound`.**

---

## 3. Convenções específicas do projeto

### 3.1 Injeção de dependência

**Sempre via construtor**, com `@RequiredArgsConstructor` (Lombok) + `private final`. Nunca use `@Inject` em campo mutável. Beans em `@ApplicationScoped` por padrão; `AuthContext` é `@RequestScoped`.

```java
@ApplicationScoped
@RequiredArgsConstructor
public class RegisterUC {
    private final UserRepository userRepository;
    private final UserCryptPasswordGTW userCryptPasswordGTW;
    private final UserFactory userFactory;
    private final Mediator mediator;
}
```

Quando precisar de `@Channel`, `@Any`, ou outro qualifier que `@RequiredArgsConstructor` não cobre, declare construtor explícito (ver [`KafkaEventProducerService`](src/main/java/com/almeja/pel/portal/infra/service/event/KafkaEventProducerService.java) e [`MediatorImpl`](src/main/java/com/almeja/pel/portal/infra/mediator/MediatorImpl.java)).

### 3.2 Mediator (commands)

Pipeline próprio em [`core/mediator`](src/main/java/com/almeja/pel/portal/core/mediator). Use quando um UC dispara efeitos colaterais coesos que merecem encapsulamento (e/ou execução assíncrona):

- `Command<T>` — record marcador (sem campos obrigatórios na interface).
- `CommandHandler<C, T>` — handler síncrono. Implementação **na camada `infra`** (pode chamar outros UCs).
- `AsyncCommandHandler<C, T>` — handler assíncrono (ex.: envio de e-mail).
- `Mediator` — interface no core; `MediatorImpl` resolve handler por reflection do tipo genérico.

Convenção de transação dos handlers síncronos: `@Transactional(Transactional.TxType.MANDATORY)` (participam da transação do UC chamador).

```java
mediator.send(new RegisterUserCommand(user, token, generateLink));
mediator.sendAsync(new SendMailCommand(to, subject, html));
```

### 3.3 Gateways (porta/adapter)

- Interface em `core/gateway/<categoria>/<Nome>GTW.java`.
- Implementação em `infra/service/<categoria>/<Nome>Service.java` com `implements <Nome>GTW`.
- Interface define **apenas o contrato do domínio** (não exponha tipos do framework).

Exemplos: `AuthorizationGTW` ↔ `AuthorizationService`, `EventProducerGTW` ↔ `KafkaEventProducerService`, `FileHandlerGTW` ↔ `FileHandlerService`.

### 3.4 Camada inbound

Controllers e listeners **apenas adaptam transporte e chamam UCs**. Sem regra de negócio, sem acesso a repositório direto, sem cálculo. Conversão `Entity → DTO` via `ConverterEntityToDTOUtil.convert(...)` ou `*Mapper`.

```java
@POST @Path("/login")
public AuthenticatedRecord authenticate(AuthenticateRecord record) {
    return authenticateUC.execute(record);   // só delega
}
```

Path base: `PrefixPathConstant.PREFIX_PATH` (`/api/v1`). Sempre prefixe.

### 3.5 Autenticação

`AuthInterceptor` (`@Provider` + `ContainerRequestFilter`, prioridade `AUTHENTICATION`) valida JWT no header `Authorization: Bearer <token>`, busca o usuário e popula `AuthContext` (`@RequestScoped`). Controllers acessam o usuário logado via `authContext.getUser()`.

Paths excluídos: `/auth`, `/v3/api-docs`, `/swagger-ui`, `/openapi`, `/q/health`, `/q/openapi`. Para liberar novo path público, edite `EXCLUDED_PATHS` em `AuthInterceptor`.

### 3.6 Exceções

- `AppException` (negócio recuperável) → HTTP 400. Use `EnumAppException` para mensagens reutilizáveis.
- `ValidatorException` (validação) → HTTP 401. Use livremente em domain services e entidades.
- Tudo o resto → HTTP 500 via `RuntimeExceptionMapper`.

Mensagens em **pt-BR**. Adicione novos códigos em `EnumAppException` em vez de strings literais quando a mensagem se repetir.

### 3.7 Enums persistidos

Enum implementa `IEnum` (`getKey()` / `getValue()`) e tem **classe interna** `Converter implements IEnumConverter<E, String>` anotada com `@jakarta.persistence.Converter(autoApply = true)`. Garante mapeamento O/R consistente sem precisar declarar `@Convert` em cada coluna.

### 3.8 Entidades

- Todas estendem `BaseEntity` (UUID `id`, `createdAt`/`updatedAt` automáticos via `@PrePersist`/`@PreUpdate`).
- `@Getter` + `@Setter` **seletivo** (apenas em campos que devem ser mutáveis fora do agregado). Mutações coesas como métodos de negócio (`updateBasicInfo`, `updateAddress`, `validateReviewed`).
- `@EqualsAndHashCode(of = "id", callSuper = false)` e `@ToString(of = "id")` para evitar lazy-loading acidental.
- Construtor sem args (`@NoArgsConstructor`) + construtor de criação semântico.
- IDs `UUID` gerados via `GenerationType.UUID` no Postgres.

### 3.9 Factories e Domain Services

- **Factory** (`UserFactory`): cria/atualiza agregados aplicando invariantes via domain services.
- **Domain Service** (`UserValidatorService`, `VerifyDependentService`, `DocumentValidatorService`, ...): regras de domínio que não pertencem a uma única entidade. Métodos puramente estáticos quando não precisam de repositório; instância (`@ApplicationScoped`) quando precisam.

### 3.10 DTOs

- POJO Lombok (`@Data` + `extends BaseDTO`) para inputs/outputs com mais campos ou que exigem mutação.
- **Java records** (`core/dto/record/`) para inputs/outputs imutáveis e simples (auth, tokens). Prefira records para entradas novas quando não precisar de Bean Validation customizado.
- DTOs específicos de transporte/infra ficam em `infra/dto/` com `*Mapper` em `infra/dto/mapper/`.

### 3.11 Migrations Flyway

`src/main/resources/db/migration/V<N>__<descrição>.sql`. Sempre incremente `N`. Migrations rodam no startup (`quarkus.flyway.migrate-at-start=true`). **Nunca edite migrations já versionadas/aplicadas em produção** — crie uma nova.

### 3.12 Eventos (Kafka)

- Saída: implementar via `EventProducerGTW.send(topic, payload)` (canal interno `kafka-producer`).
- Entrada: classe em `inbound/event` com método anotado `@Incoming("<topic>")`. Payload chega como `String` JSON (deserializa com Gson ou Jackson). Listeners só chamam UC.
- Tópicos atuais: `portal-update-create-user` (out), `pre-registration-review` (in).

### 3.13 Mail

`MailBuilder<P>` abstrato carrega o template via `TemplateEmailRepository` (templates em banco, enum `EnumTemplateEmail`). Builders concretos em `core/mail/builders/`. Envio assíncrono via `mediator.sendAsync(new SendMailCommand(...))`.

---

## 4. Comandos úteis

```bash
# Dev
./mvnw quarkus:dev                        # hot reload, porta 8081

# Build
./mvnw clean package                      # com testes
./mvnw clean package -DskipTests          # sem testes (Dockerfile usa este)

# Testes
./mvnw test                               # todos
./mvnw test -Dtest=AuthenticateIntegrationTest    # único
./mvnw test -Dtest=AuthenticateIntegrationTest#shouldLoginNormallyWithoutToken
```

> Testes de integração sobem um Postgres real via Testcontainers (`PostgresTestResource`). Docker precisa estar rodando.

---

## 5. Como adicionar um caso de uso novo (checklist)

1. **Entidade/migration** — se houver mudança de schema, crie `V<N+1>__*.sql` e ajuste/crie a `*Entity` em `core/domain/entity`.
2. **Repository** — interface `extends JpaRepository<E, UUID>` em `core/repository`.
3. **Domain service / factory** — regras invariantes vão em `core/domain/service` ou `core/domain/factory`. Validações que falham com `ValidatorException`.
4. **DTO/Record** — input/output em `core/dto` (POJO) ou `core/dto/record` (record).
5. **Gateway (se precisar de I/O externo)** — interface em `core/gateway/<cat>` + impl em `infra/service/<cat>`.
6. **UseCase** — `*UC` em `core/domain/usecase/<contexto>/`. `@ApplicationScoped` + `@Transactional` + `@RequiredArgsConstructor`. Método público único `execute(...)`.
7. **Command + Handler (se aplicável)** — `core/mediator/command/*Command.java` (record) e `infra/mediator/handler/*CommandHandler.java`. Use `MANDATORY` no handler síncrono.
8. **Controller** — endpoint em `inbound/http/*Controller.java` apenas delegando ao UC. Path com `PREFIX_PATH`.
9. **Teste de integração** — em `src/test/java/.../integration/`, estender `BaseIntegrationTest`, anotar `@QuarkusTest`. Mockar bordas externas (Kafka producer, S3 etc.) com `@InjectMock`. Cenários no padrão Given/When/Then com `@DisplayName` em pt-BR.

---

## 6. Coisas que você **não** deve fazer

- Não importe pacote `infra` ou `inbound` a partir de `core`.
- Não acesse repositório direto do controller — sempre via UC.
- Não use `@Inject` em campo mutável; nunca `@Autowired`.
- Não ponha lógica de domínio em controller, listener, handler de exceção, mapper ou util.
- Não mocke o banco em testes de integração — use `Testcontainers` (já configurado no `BaseIntegrationTest`).
- Não rode `flyway.clean` fora dos testes; em produção é desativado.
- Não exponha `Entity` em endpoint — sempre converta para DTO/Record.
- Não use Spring (`@Service`, `@Autowired`, `@Component`); use CDI/Jakarta. A única dependência Spring permitida é `quarkus-spring-data-jpa` para repositórios.
- Não escreva mensagens/comentários em inglês quando o restante do código está em pt-BR — mantenha consistência (mensagens de erro pt-BR, identificadores em inglês).
- Não edite migrations já aplicadas; sempre crie nova versão.

---

## 7. Variáveis de ambiente principais

Ver `.env-example`. Resumo das mais importantes:

| Variável                                    | Uso                                       |
| ------------------------------------------- | ----------------------------------------- |
| `PORTAL_PEL_API_DB_URL` / `_USERNAME` / `_PASSWORD` | Datasource Postgres                |
| `PORTAL_PEL_API_TOKEN_SECRET`               | Segredo JWT (HMAC)                        |
| `PORTAL_PEL_API_TOKEN_*_ISSUER`             | Issuers dos tokens (sessão / recovery / link autorizado) |
| `PORTAL_PEL_API_CORS_ALLOWED_ORIGINS`       | Lista de origens permitidas (CORS)        |
| `PORTAL_PEL_API_MAIL_SENDER_EMAIL` / `_PASSWORD` | Credenciais SMTP                     |
| `PORTAL_PEL_API_AWS_*`                      | Credenciais S3                            |
| `PORTAL_PEL_API_FILE_S3_ENABLED`            | `true` para usar S3, `false` para upload local |
| `kafka.bootstrap.servers`                   | Cluster Kafka                             |

Defaults de dev em `application.properties`. Em testes (`src/test/resources/application.properties`) o mailer está mockado e Kafka usa `smallrye-in-memory`.

---

## 8. Regras genéricas (reutilizáveis)

Aplicáveis a qualquer backend Quarkus com Clean Arch + DDD. Mantidas em `.claude/rules/` para facilitar reúso entre projetos:

- [`architecture.md`](.claude/rules/architecture.md) — Clean Architecture + DDD: camadas, dependências, sufixos, agregados.
- [`dependency-injection.md`](.claude/rules/dependency-injection.md) — Construtor + `@RequiredArgsConstructor`, escopos CDI.
- [`mediator.md`](.claude/rules/mediator.md) — Padrão Mediator com Command/Handler sync e async.
- [`gateways.md`](.claude/rules/gateways.md) — Porta-adaptador (interface no core, impl em infra).
- [`inbound.md`](.claude/rules/inbound.md) — Controllers/listeners finos: só adaptam e delegam.
- [`infra-layer.md`](.claude/rules/infra-layer.md) — Conteúdo da camada infra (DTOs, handlers, interceptors, services).
- [`exceptions.md`](.claude/rules/exceptions.md) — Hierarquia de exceções e exception mappers.
- [`flyway-migrations.md`](.claude/rules/flyway-migrations.md) — Versionamento, imutabilidade e boas práticas.
- [`testing.md`](.claude/rules/testing.md) — F.I.R.S.T, integração com Testcontainers, mocks/stubs/spies/fakes, Quarkus Test.
- [`quarkus-best-practices.md`](.claude/rules/quarkus-best-practices.md) — Boas práticas Quarkus (transações, configuração, reactive messaging, testes nativos).
- [`java-modern.md`](.claude/rules/java-modern.md) — Java 21+/25 (records, pattern matching, sealed, virtual threads).

---

## 9. Memória e contexto

Memórias persistentes do agente Claude estão em `~/.claude/projects/.../memory/MEMORY.md`. Antes de propor mudanças que reincidam em padrões já estabelecidos (DI por construtor, mediator, etc.), assuma que essas regras valem.
