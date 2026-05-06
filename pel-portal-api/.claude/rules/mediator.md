# Mediator + Commands (genérico — Quarkus)

Padrão para desacoplar caso de uso de efeitos colaterais coesos. Inspirado em CQRS/MediatR (.NET). Útil quando:

- O efeito colateral é coeso e nomeável (envio de e-mail, geração de token, sincronização externa).
- O UC quer disparar mais de um efeito sem virar um *god method*.
- Há necessidade de execução assíncrona desacoplada (ex.: e-mail).

> **Não use mediator para tudo.** Se um UC só chama um repositório, basta o repositório. Mediator é para isolar efeitos colaterais que envolvem múltiplos colaboradores ou async.

## Contratos no `core`

```java
public interface Command<T> { }

public interface CommandHandler<C extends Command<T>, T> {
    T handle(C command);
}

public interface AsyncCommandHandler<C extends Command<T>, T> {
    void handleAsync(C command);
}

public interface Mediator {
    <T> T send(Command<T> command);
    <T> void sendAsync(Command<T> command);
}
```

## Implementação no `infra`

`MediatorImpl` resolve handler pelo tipo genérico do `Command` usando `Instance<CommandHandler<?, ?>>` (CDI) + cache:

```java
@ApplicationScoped
public class MediatorImpl implements Mediator {
    private final Instance<CommandHandler<?, ?>> handlers;
    private final Instance<AsyncCommandHandler<?, ?>> asyncHandlers;
    private final Map<Class<?>, CommandHandler<?, ?>> cache = new ConcurrentHashMap<>();
    // ...
}
```

Cuidado: ao iterar `Instance<>` em busca do handler correto, **ignore proxies CDI** (`_Subclass`/`$$`) lendo `getSuperclass()` antes de inspecionar `getGenericInterfaces()`.

## Convenções

- **Command** é `record` em `core/mediator/command/`. Nome no padrão `<Verbo><Sujeito>Command`. Implementa `Command<Tipo>` (`Command<Void>` quando não há retorno).
- **Handler síncrono** em `infra/mediator/handler/`, sufixo `*CommandHandler`, `implements CommandHandler<XCommand, T>`. Anotado `@ApplicationScoped` + `@RequiredArgsConstructor`.
- **Handler assíncrono** segue o mesmo padrão mas implementa `AsyncCommandHandler`.
- Um command tem **exatamente um handler**.
- Handlers podem chamar UCs, repositórios, gateways. Não chamam controllers nem outros handlers diretamente (use `mediator.send` se precisar encadear).

## Transações

- O UC que dispara o command já abriu transação com `@Transactional`. O handler síncrono deve ser `@Transactional(Transactional.TxType.MANDATORY)` para garantir que **sempre roda dentro** da transação do chamador.
- Handlers assíncronos costumam **não** participar da transação do UC (executam em outro contexto). Não dependa de entidades gerenciadas vivas — passe IDs/snapshots no command.

## Quando usar `send` vs `sendAsync`

| Cenário                                           | Recomendação |
| ------------------------------------------------- | ------------ |
| Efeito faz parte da consistência do UC            | `send`       |
| Efeito pode falhar/repetir sem afetar consistência | `sendAsync`  |
| I/O lento e independente (e-mail, push)           | `sendAsync`  |
| Mutação no banco que precisa do mesmo commit      | `send`       |

## Antipadrões

- **Command "saco-de-getters"** sem semântica clara — prefira nomes orientados a intenção (`SubmitOrderCommand`, não `OrderDataCommand`).
- **Handler que chama outro handler diretamente** — encadeie via `mediator.send`.
- **Lógica de negócio no handler** — handlers orquestram; regras vivem em domain services / agregados.
- **Command com referência mutável de entidade num async handler** — capture estado imutável (UUID, DTO record).
- **Múltiplos handlers para o mesmo Command** — quebra a unicidade do contrato; use eventos de domínio em vez disso.
