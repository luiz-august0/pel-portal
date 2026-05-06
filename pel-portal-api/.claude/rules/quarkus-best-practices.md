# Quarkus — boas práticas (genérico)

## Configuração

- Toda configuração via `application.properties` (ou `application.yaml`). Variáveis sensíveis vêm de **env**, com default seguro:

  ```properties
  quarkus.datasource.jdbc.url=${APP_DB_URL:jdbc:postgresql://localhost:5432/app}
  api.security.token.secret=${APP_TOKEN_SECRET:dev-only-secret}
  ```

- `application.properties` em `src/test/resources/` sobrescreve no perfil `test`. Para perfis específicos use `%dev.`, `%prod.`, `%test.` como prefixo.
- Acesse config com `@ConfigProperty(name="...")` em CDI ou `ConfigProvider.getConfig()` em código estático. Prefira o primeiro.
- **Nunca commite secrets reais**. `.env-example` documenta as chaves; `.env` é gitignored.

## Transações

- `@Transactional` no UC (boundary). Handlers síncronos chamados pelo UC: `@Transactional(MANDATORY)`.
- Métodos chamados em background (async handler, listener) abrem própria transação se precisarem (`REQUIRES_NEW` ou outro `@Transactional`).
- Não anote `@Transactional` em controllers — boundary deve ser o UC.
- Cuidado: `@Transactional` em método chamado **dentro da mesma classe** não cria proxy CDI — chame via outro bean.
- `LazyInitializationException` em DTOs: ou inicialize relação dentro da transação, ou use `Hibernate.unproxy(...)`, ou faça projeção via JPQL/`@EntityGraph`.

## Persistência

- **Spring Data JPA** (`quarkus-spring-data-jpa`) é aceitável quando o time já vem desse ecossistema. Alternativa: Panache (`PanacheRepository<T>`), mais idiomática em Quarkus.
- Repositories são interfaces com métodos derivados (`findByCpf`, `existsByEmailAndIdIsNot`) — privilegie isso sobre JPQL manual quando der.
- IDs `UUID` para tudo exposto externamente; `BIGINT` só para tabelas internas.
- Entidade base com `createdAt`/`updatedAt` automatizados via `@PrePersist`/`@PreUpdate`.
- `@OneToMany`/`@ManyToOne` sempre `LAZY` por padrão. Use `@EntityGraph` ou `JOIN FETCH` quando precisar carregar.
- Cuidado com `CascadeType.ALL` — propaga delete em todos os filhos. Use deliberadamente.

## Reactive Messaging (Kafka, AMQP, ...)

- Outgoing: `Emitter<String>` injetado via `@Channel("nome")`. Envie `Message.of(json).addMetadata(...)`.
- Incoming: método anotado `@Incoming("nome")`. Payload tipicamente `String` (JSON) — deserialize na entrada.
- Em `application.properties`:

  ```properties
  mp.messaging.outgoing.<canal>.connector=smallrye-kafka
  mp.messaging.outgoing.<canal>.value.serializer=org.apache.kafka.common.serialization.StringSerializer
  mp.messaging.outgoing.<canal>.acks=all
  mp.messaging.outgoing.<canal>.retries=3
  mp.messaging.incoming.<canal>.connector=smallrye-kafka
  mp.messaging.incoming.<canal>.auto.offset.reset=earliest
  mp.messaging.incoming.<canal>.enable.auto.commit=false
  ```

- Em testes, troque o connector para `smallrye-in-memory`.
- DLQ + retry: configure no nível do connector; trate exceção no listener com cuidado para não consumir loop infinito.

## Health, observabilidade, OpenAPI

- `quarkus-smallrye-health` expõe `/q/health/live` e `/q/health/ready`. Adicione `HealthCheck` customizado para dependências críticas (banco, fila externa).
- `quarkus-smallrye-openapi` gera `/q/openapi`. Anote endpoints com `@Operation`, `@APIResponse` quando o cliente externo depender de spec rica.
- Logging: `@Slf4j` (Lombok). Quarkus usa JBoss LogManager; configure níveis por categoria:

  ```properties
  quarkus.log.category."com.exemplo.app".level=DEBUG
  quarkus.log.level=INFO
  ```

- Não logue PII (senha, token, CPF/CNPJ inteiro). Mascarar.

## CORS

```properties
quarkus.http.cors=true
quarkus.http.cors.origins=${APP_CORS_ALLOWED_ORIGINS:http://localhost:3000}
quarkus.http.cors.headers=*
quarkus.http.cors.methods=GET,POST,PUT,PATCH,DELETE,OPTIONS
quarkus.http.cors.access-control-max-age=3600
```

Restrinja `origins` em produção — `*` é raramente apropriado.

## Build & runtime

- Maven Wrapper (`./mvnw`): garante versão fixa do Maven.
- Dev mode: `./mvnw quarkus:dev` (hot reload, dev UI em `/q/dev`).
- JVM build: `./mvnw clean package` → `target/quarkus-app/quarkus-run.jar`.
- Native build: `./mvnw package -Pnative` (requer GraalVM ou `quarkus.native.container-build=true`).
- Dockerfile multi-stage: builda no Maven image, copia artefatos para JRE slim. Não rode como root.

## CDI específico do Quarkus

- Build-time scanning: classes só viram beans se descobertas no build. Adicione `META-INF/beans.xml` (vazio) se precisar habilitar scan em jar externo.
- `@Startup` para executar lógica no boot (cuidado com health checks falsos).
- `@Scheduled(every="...")` para tarefas recorrentes — não use para SLA crítico (use scheduler externo).
- `@Singleton` ≠ `@ApplicationScoped`: o primeiro não cria proxy. Use `@ApplicationScoped` por padrão.

## Performance e produção

- `quarkus.hibernate-orm.log.sql=false` em produção (custo alto).
- `quarkus.hibernate-orm.database.generation=none` em produção; deixa Flyway no controle.
- Pool HikariCP é default — ajuste `quarkus.datasource.jdbc.max-size` por carga real.
- Profile `prod` sem `quarkus.flyway.clean-at-start=true`. Em alguns ambientes, configure `quarkus.flyway.clean-disabled=true` por segurança.
- Habilite compressão HTTP se respostas grandes: `quarkus.http.enable-compression=true`.

## Antipadrões

- Catch genérico em controller para "tratar tudo".
- `@Transactional` em método privado (proxy CDI não intercepta).
- Injetar `EntityManager` diretamente em UC (use repositório).
- Lógica em listener Kafka.
- Gravar arquivo no FS local em ambiente serverless/container imutável.
- Usar `System.out.println` em vez de logger.
- Configurar `quarkus.hibernate-orm.database.generation=update` em produção.
