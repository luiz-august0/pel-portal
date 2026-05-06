# Clean Architecture + DDD (genérico — Quarkus)

Regras de organização aplicáveis a qualquer backend Java/Quarkus que adote Clean Architecture com toques de DDD. Independentes de domínio.

## Camadas

```
core/      <- domínio puro. Sem dependência de frameworks de transporte.
inbound/   <- entradas (HTTP, Kafka, CLI, etc.). Adapta e delega ao core.
infra/     <- detalhes externos. Implementa portas (gateways) do core.
```

**Direção da dependência:** `inbound → core ← infra`. `core` **nunca** importa `infra` nem `inbound`. `inbound` não conhece `infra` (a comunicação é via gateway/UC). Quebrar essa regra é o sinal mais comum de erosão arquitetural.

## Estrutura recomendada de `core`

| Subpacote     | Responsabilidade                                              | Sufixo            |
| ------------- | ------------------------------------------------------------- | ----------------- |
| `domain/entity`  | Entidades JPA / agregados; invariantes via métodos coesos | `*Entity`         |
| `domain/entity/base` | `BaseEntity` (id, audit) compartilhado                | —                 |
| `domain/enums`   | Enums de domínio (com converter JPA quando persistidos)   | `Enum*`           |
| `domain/factory` | Construção de agregados aplicando invariantes             | `*Factory`        |
| `domain/service` | Domain services — regras que não pertencem a uma entidade | `*Service`/`*Validator*` |
| `domain/usecase/<contexto>/` | Casos de uso aplicacionais (1 método público `execute`) | `*UC`     |
| `dto/`        | DTOs do core (POJOs Lombok extends `BaseDTO`)               | `*DTO`            |
| `dto/record/` | DTOs imutáveis (Java records)                                | `*Record`         |
| `event/`      | Publicação de eventos de domínio (orquestração de envio)     | `*Event`          |
| `exception/`  | `AppException`, `ValidatorException`, `Enum*Exception`       | —                 |
| `gateway/<categoria>/` | Interfaces de portas de saída                       | `*GTW`            |
| `mediator/`   | `Mediator`, `Command`, `CommandHandler`, `AsyncCommandHandler` | —              |
| `mediator/command/` | Definições dos commands (records)                      | `*Command`        |
| `repository/` | Spring Data JPA repositories                                 | `*Repository`     |
| `util/`       | Funções puras / helpers sem estado                           | `*Util`           |

## Estrutura recomendada de `inbound`

| Subpacote | Responsabilidade                | Sufixo            |
| --------- | ------------------------------- | ----------------- |
| `http/`   | JAX-RS controllers              | `*Controller`     |
| `event/`  | Listeners de mensageria (Kafka) | `*KafkaListener`  |

## Estrutura recomendada de `infra`

| Subpacote                | Responsabilidade                                         |
| ------------------------ | -------------------------------------------------------- |
| `config/`                | Producers CDI, configuração de serializadores, etc.       |
| `constants/`             | Constantes de transporte/infra (paths, headers)           |
| `context/`               | Beans `@RequestScoped` (auth context, correlation id)     |
| `dto/`                   | DTOs específicos de transporte/representação externa      |
| `dto/mapper/`            | Mappers entre DTO core ↔ DTO transporte                   |
| `exception/handlers/`    | `ExceptionMapper`s JAX-RS                                 |
| `interceptor/`           | Filtros JAX-RS (auth, logging, correlação)                |
| `mediator/`              | `MediatorImpl`                                            |
| `mediator/handler/`      | Handlers de command (sufixo `*CommandHandler`)            |
| `service/<categoria>/`   | **Implementações** dos `*GTW` (sufixo `*Service`)         |
| `util/`                  | Helpers de infra (token, file)                            |

## Princípios de design

1. **Single entry per use case.** Um UC tem um método público `execute(...)`. Sobrecargas só quando representam o mesmo caso com inputs alternativos.
2. **Agregado é a unidade transacional.** A raiz do agregado controla os filhos; mutações coesas viram métodos no agregado, não setters espalhados.
3. **Invariante perto do dado.** Validações que dependem só do estado da entidade ficam na entidade (ex.: `validateReviewed`). Validações que dependem de repositório ficam no domain service.
4. **Use `record` para input/output imutável.** Use POJO Lombok quando precisar de mutabilidade ou herança.
5. **Não exponha `Entity` em controllers.** Sempre converta para DTO/Record antes de responder.
6. **Sem regra de negócio em mapper, util, exception handler ou interceptor.**
7. **Sem dependência circular entre UCs.** Se A precisa de B e B precisa de A, extraia uma colaboração (domain service ou command).

## Sinais de problema (checklist de revisão)

- `import com.<...>.infra.*` dentro de `core`: violação.
- `@Inject` em controller sem ser via construtor: violação.
- Controller chamando `*Repository` direto: violação.
- Lógica de negócio em `*Mapper` ou `ExceptionHandler`: violação.
- UC com mais de uma classe de "execute" pública não relacionadas: violação (quebrar em UCs distintos).
- Entidade com setters públicos para campos de invariante: violação (usar método de domínio).
- DTO de saída expondo `password`, `token`, `responsibleToken` ou similares: violação (filtrar campos).
