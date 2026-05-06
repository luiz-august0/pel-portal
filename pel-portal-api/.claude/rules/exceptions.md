# Exceções e tratamento de erros (genérico)

## Hierarquia recomendada

```
RuntimeException
├── AppException          (negócio recuperável → 4xx tipicamente 400)
└── ValidatorException    (entrada inválida → 4xx tipicamente 400/401/422)
```

Sem exceções *checked* (`extends Exception`) em código de domínio. Quarkus/JAX-RS lida melhor com `RuntimeException`, e código com `throws` espalha boilerplate de `try/catch` que polui regra de negócio.

## Convenções de uso

- **`AppException`**: erro de negócio recuperável (sessão expirada, credencial errada, recurso não encontrado, link expirado). Mensagem em pt-BR (ou idioma do produto), pronta para o usuário final.
- **`ValidatorException`**: violação de invariante / formato (e-mail inválido, CPF inválido, campo obrigatório, valor fora do range).
- **`RuntimeException` (genérico)**: bug. Vira 500 e deve aparecer no log de erro.

## Mensagens reutilizáveis: enum de exceções

Para mensagens repetidas em múltiplos pontos, centralize em um enum:

```java
@Getter
public enum EnumAppException {
    USER_NOT_FOUND("Usuário não encontrado"),
    WRONG_CREDENTIALS("E-mail ou senha incorretos"),
    EXPIRED_SESSION("Sessão expirada, realize o login novamente");

    private final String message;
    EnumAppException(String message) { this.message = message; }
}

throw new AppException(EnumAppException.WRONG_CREDENTIALS);
```

Vantagens: mensagens consistentes, fácil i18n futuro, *grep-friendly*.

## ExceptionMapper (JAX-RS)

Em `infra/exception/handlers/`, com `ErrorResponse` padrão:

```java
@Provider
public static class AppExceptionMapper implements ExceptionMapper<AppException> {
    @Context UriInfo uriInfo;

    @Override
    public Response toResponse(AppException e) {
        ErrorResponse err = new ErrorResponse(
            DateUtil.getDate(),
            Response.Status.BAD_REQUEST.getStatusCode(),
            e.getClass().getName(),
            e.getMessage(),
            uriInfo.getPath()
        );
        return Response.status(Response.Status.BAD_REQUEST).entity(err).build();
    }
}
```

`ErrorResponse` deve ter: `timestamp`, `status`, `error` (classe ou código), `message`, `path`. Mantenha o formato estável — clientes dependem dele.

Importante:

- Nunca exponha stacktrace ao cliente.
- Não exponha mensagens de exceções de baixo nível (`SQLException`, `KafkaException`, ...) — capture e converta em `AppException`/`ValidatorException` no ponto de origem.
- Logue no nível adequado: `warn` para 4xx (problema do cliente, vale métrica), `error` para 5xx (problema nosso, dispara alerta).

## Lançamento de exceções: onde

| Onde                    | Tipo                                | Exemplo                                    |
| ----------------------- | ----------------------------------- | ------------------------------------------ |
| Entidade (invariante)   | `ValidatorException`                | "Informações já revisadas, sem alteração"  |
| Domain service          | `ValidatorException` / `AppException` | "CPF já está em uso", "Usuário não encontrado" |
| UseCase                 | `AppException`                      | "Usuário inativo"                          |
| Gateway (impl)          | `AppException`                      | "Erro ao enviar e-mail: ..."               |
| Controller / listener   | nunca (deixe propagar)              | —                                          |

Não capture e re-empacote a mesma exceção sem agregar contexto. Se for capturar, adicione informação útil (ID do recurso, operação).

## Antipadrões

- `throw new RuntimeException("...")` em código de negócio (use `AppException`).
- `catch (Exception e) { ... }` em controller para "tratar tudo" (deixe o mapper).
- `throw e` dentro de `catch` que não acrescenta nada.
- Concatenar mensagens externas sem sanitizar (`AppException("Erro: " + e.getMessage())`) — pode vazar interno; prefira mensagem fixa e log do erro original.
- Mensagens em idiomas misturados (pt + en) — mantenha consistência por produto.
