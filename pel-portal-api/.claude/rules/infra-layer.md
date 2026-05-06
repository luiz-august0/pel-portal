# Camada `infra` — genérico

A camada `infra` contém **detalhes de implementação** (frameworks, drivers, SDKs) que o `core` não pode tocar diretamente.

## O que vai aqui

| Subpacote                | Conteúdo                                                       |
| ------------------------ | -------------------------------------------------------------- |
| `config/`                | Producers CDI (`@Produces`), config de serializadores (Jackson/Gson) |
| `constants/`             | Constantes de transporte/infra (paths, headers, content-types)  |
| `context/`               | Beans `@RequestScoped` (auth, correlation id)                   |
| `dto/`                   | DTOs **específicos do transporte** (campos diferentes do core) |
| `dto/mapper/`            | Mappers entre DTO core ↔ DTO transporte                         |
| `exception/classes/`     | `ErrorResponse` e classes auxiliares de mapeamento de erro      |
| `exception/handlers/`    | `ExceptionMapper`s JAX-RS                                       |
| `interceptor/`           | Filtros JAX-RS (`ContainerRequestFilter`)                       |
| `mediator/`              | `MediatorImpl`                                                  |
| `mediator/handler/`      | Handlers concretos (`*CommandHandler`)                          |
| `service/<categoria>/`   | Implementações de gateways (`*Service implements *GTW`)         |
| `util/`                  | Helpers de infra (token util, file util)                        |

## Princípios

1. **Implementa, não decide.** Toda decisão de negócio fica no `core`. `infra` só faz a coisa acontecer (criptografar, persistir, enviar).
2. **Pode importar do core.** O contrário não vale.
3. **Encapsula SDKs.** Tipos da AWS/Kafka/Mailer/JWT só aparecem aqui; nunca em assinaturas do `core`.
4. **DTOs próprios quando precisar.** Se a representação externa diverge da do core (ex.: campos achatados, naming snake_case), crie um DTO em `infra/dto/` e um `*Mapper`.

## Producers e configuração CDI

```java
@ApplicationScoped
public class JacksonConfig {
    @Produces
    @ApplicationScoped
    public ObjectMapper objectMapper() {
        ObjectMapper om = new ObjectMapper();
        om.registerModule(new JavaTimeModule());
        om.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return om;
    }
}
```

- Use `@Produces` para fornecer beans não-CDI nativos (ObjectMapper, Gson, S3Client custom).
- Mantenha um `ObjectMapper` único por aplicação — múltiplas instâncias fragmentam configuração.

## Interceptors / Filters JAX-RS

```java
@Provider
@Priority(Priorities.AUTHENTICATION)
@ApplicationScoped
@RequiredArgsConstructor
public class AuthInterceptor implements ContainerRequestFilter {
    private final TokenService tokenService;
    private final UserRepository userRepository;
    private final AuthContext authContext;
    // ...
}
```

- Anote com `@Provider` para registro automático.
- Defina `@Priority` adequada (`AUTHENTICATION` < `AUTHORIZATION` < `HEADER_DECORATOR`).
- Mantenha listas de "paths excluídos" como constantes nomeadas — não strings espalhadas.
- Use `ctx.abortWith(Response.status(...).build())` para curto-circuito; **não** lance exception (perde informação contextual).

## Exception handlers (JAX-RS)

Centralize em uma classe com mappers internos para evitar dispersão:

```java
public class ExceptionHandler {

    @Provider
    public static class AppExceptionMapper implements ExceptionMapper<AppException> { ... }

    @Provider
    public static class ValidatorExceptionMapper implements ExceptionMapper<ValidatorException> { ... }

    @Provider
    public static class RuntimeExceptionMapper implements ExceptionMapper<RuntimeException> { ... }
}
```

- Sempre retorne um `ErrorResponse` consistente (timestamp, status, error, message, path).
- **Não** vaze stacktrace para o cliente em produção.
- Registre log no nível adequado (warn para erros de cliente, error para 5xx).

## Mappers

- Stateless. Métodos `static` quando não precisam de dependência.
- Um mapper por par `<Origem, Destino>`. Não componha mapeadores generalistas que cobrem o app inteiro.
- Para conversão estrutural simples, ferramenta de reflection (ConverterEntityToDTO) é aceitável; para conversões com regra (achatar, renomear), prefira mapper explícito.

## O que NÃO vai em `infra`

- Regra de negócio.
- Validação de invariantes de domínio.
- Definições de contrato (interfaces de gateway) — vivem no `core`.
- Entidades JPA — vivem no `core/domain/entity`.
