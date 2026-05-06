# Java moderno (21+/25) — boas práticas

Diretrizes para aproveitar recursos modernos do Java sem cair em "dialetos" obscuros.

## Records — use por padrão para DTOs imutáveis

```java
public record AuthenticateRecord(String cpf, String password, String authorizedToken) {}

public record UserCreated(UUID id, String name, Instant at) {}
```

Quando preferir record:

- Input/output simples sem mutação (chamadas de UC, payloads HTTP, eventos).
- DTOs com 3–8 campos.
- Commands do mediator.

Quando NÃO usar record:

- Entidades JPA (Hibernate exige construtor sem args + getters/setters em campos mutáveis).
- DTOs com herança comum (records são `final`).
- Quando precisa de Bean Validation com mensagens dinâmicas customizadas extensas (POJO Lombok ainda é mais ergonômico).

Records suportam validação no canonical constructor:

```java
public record Cep(String value) {
    public Cep {
        if (!value.matches("\\d{8}")) throw new ValidatorException("CEP inválido");
    }
}
```

## Pattern matching

`instanceof` com binding:

```java
if (entityFieldValue instanceof Collection<?> coll) {
    coll.forEach(...);
}
```

`switch` expressions e pattern matching (preview/estável conforme versão):

```java
String label = switch (status) {
    case ACTIVE -> "Ativo";
    case INACTIVE -> "Inativo";
    case PENDING -> "Pendente";
};
```

## Sealed types

Modelagem fechada de variantes (úteis para resultados, comandos polimórficos):

```java
public sealed interface Result<T> permits Result.Ok, Result.Error {
    record Ok<T>(T value) implements Result<T> {}
    record Error<T>(String message) implements Result<T> {}
}
```

Combine com `switch` exaustivo para garantir tratamento de todos os casos em compile time.

## Streams e collections

- `.toList()` (Java 16+) em vez de `.collect(Collectors.toList())`.
- `Stream.of(...)` para construir streams pequenas.
- Evite `parallelStream()` em código de UC — efeitos colaterais ocultos e custo de fork/join raramente compensam.
- `Collectors.toUnmodifiableList()` quando o resultado não deve ser mutado.

## Optional

- Use **somente como tipo de retorno**, nunca como campo ou parâmetro.
- Não chame `.get()` sem `isPresent()` — prefira `orElseThrow(...)` com exceção de domínio:

  ```java
  userRepository.findByCpf(cpf)
      .orElseThrow(() -> new AppException(EnumAppException.USER_NOT_FOUND));
  ```

## Datas

- `Instant` para timestamps (UTC), `LocalDate` para data sem hora, `LocalDateTime` apenas quando timezone for sempre conhecido.
- Evite `java.util.Date`/`Calendar` em código novo. Quando interagir com APIs antigas (JPA, Lombok existente), encapsule a conversão num util.
- Datas serializadas em ISO-8601 por padrão (Jackson). Para campos com formato fixo, anote:

  ```java
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "America/Sao_Paulo")
  private LocalDate birthDate;
  ```

## Virtual threads (Java 21+)

- Quarkus suporta virtual threads em endpoints (`@RunOnVirtualThread`) e em handlers de mensageria.
- Use para I/O bloqueante de alta concorrência (HTTP outbound, JDBC, leituras de arquivo).
- Não use para CPU-bound — não traz ganho.
- Não combine virtual threads com `synchronized` em hotspots — pode "pinar" a thread carrier (use `ReentrantLock`).

## `var` (inferência local)

- OK em variáveis locais quando o tipo é óbvio do RHS:

  ```java
  var users = userRepository.findAll();   // List<UserEntity>
  ```

- Evite quando esconde o tipo:

  ```java
  var x = svc.process();                  // o que é x?
  ```

- Não use `var` em assinaturas/return types — não é permitido para campos/parâmetros mesmo se fosse.

## Texto

- Text blocks (`"""..."""`) para SQL/JSON inline:

  ```java
  String json = """
      { "id": "%s", "name": "%s" }
      """.formatted(id, name);
  ```

## Exceções

- Sem `throws Exception` em assinaturas de domínio. Use exceções não-checadas (`AppException`, `ValidatorException`).
- `try-with-resources` para qualquer `AutoCloseable` (streams, conexões manuais, arquivos).

## Lombok — uso responsável

| Anotação                            | Use quando                                          |
| ----------------------------------- | --------------------------------------------------- |
| `@RequiredArgsConstructor`          | Sempre em beans CDI (com campos `final`).           |
| `@Data`                             | DTOs / value objects mutáveis. Não em entidades JPA (gera `equals/hashCode` perigosos com lazy collections). |
| `@Getter` / `@Setter` (seletivo)    | Entidades JPA — só onde precisa.                    |
| `@EqualsAndHashCode(of = "id")`     | Entidades JPA — evita StackOverflow em ciclos.      |
| `@ToString(of = "id")`              | Entidades JPA — evita lazy load acidental.          |
| `@Slf4j`                            | Em qualquer classe que loga.                        |
| `@Builder`                          | DTOs com muitos campos opcionais (events).          |

Não use `@SneakyThrows`, `@NonNull` automatizado, ou `@Cleanup` — preferimos código explícito para essas situações.

## Antipadrões

- Misturar `Date` e `Instant` no mesmo módulo sem util de conversão.
- `null` como sinal de "não encontrado" em retorno público (use `Optional` ou exceção de domínio).
- `String` para IDs onde já se sabe que é UUID (perde tipo).
- `equals`/`hashCode` em entidade baseado em todos os campos (provoca lazy loading).
- `parallelStream` em código transacional.
- Concatenação repetida de string em loop quente — use `StringBuilder` ou `Stream.collect(Collectors.joining(...))`.
