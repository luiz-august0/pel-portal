# Camada inbound — controllers e listeners (genérico)

A camada `inbound` é o **adaptador de entrada**. Sua única responsabilidade é traduzir transporte (HTTP, Kafka, CLI) para chamadas de UC.

## Regra de ouro

> Inbound só faz três coisas: (1) receber input, (2) chamar `useCase.execute(...)`, (3) responder/serializar.

Tudo o que não couber nessas três etapas mora em outra camada.

## Controllers (HTTP / JAX-RS)

```java
@ApplicationScoped
@RequiredArgsConstructor
@Path(PREFIX_PATH + "/user")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserController {

    private final GetCurrentUserUC getCurrentUserUC;
    private final UpdateUserUC updateUserUC;
    private final AuthContext authContext;

    @GET @Path("/current")
    public UserDTO getCurrentUser() {
        return ConverterEntityToDTOUtil.convert(
            getCurrentUserUC.execute(), UserDTO.class
        );
    }

    @PUT @Path("/update")
    public void updateUser(UserUpdateDTO dto) {
        updateUserUC.execute(authContext.getUser(), dto);
    }
}
```

Convenções:

- `@ApplicationScoped` (controllers Quarkus podem ser singletons; estado por requisição vai em `@RequestScoped` separado).
- `@RequiredArgsConstructor` + `private final` para todos os UCs e contextos.
- `@Path` com prefixo padronizado por constante (ex.: `PrefixPathConstant.PREFIX_PATH`).
- Status HTTP explícito quando não for 200/204 (`Response.status(Response.Status.CREATED).build()`).
- Conversão `Entity → DTO` em mapper/util — nunca exponha entidade direto.
- **Sem** `@Transactional` em controller. Transação é responsabilidade do UC.
- **Sem** acesso a `*Repository` direto.

## Listeners (Kafka / mensageria)

```java
@Slf4j
@ApplicationScoped
@RequiredArgsConstructor
public class PreRegistrationReviewKafkaListener {

    private final SubmitUserReviewUC submitUserReviewUC;

    @Incoming("pre-registration-review")
    public void execute(String payload) {
        var dto = new Gson().fromJson(payload, EventPreRegistrationReviewDTO.class);
        log.info("Pré-cadastro com cpf {} recebido", dto.getCpf());
        submitUserReviewUC.execute(dto.getCpf().replaceAll("[^0-9]", ""), dto.getApproved());
    }
}
```

Convenções:

- Sufixo `*KafkaListener` (ou `*Listener` por canal).
- Deserializa o payload (Gson/Jackson) e delega ao UC.
- Não trate regra de negócio — só normalização superficial (trim, máscara).
- Log informativo no início; falhas devem propagar para a infraestrutura de retry/DLQ.

## O que NUNCA fazer no inbound

- Ler/escrever direto em repositório.
- Chamar gateway de domínio direto (`*GTW`) — passe pelo UC.
- Aplicar regra de negócio (cálculos, decisões, validações além de presença/formato trivial).
- Iniciar transação manualmente.
- Captar `Exception` genérico para virar resposta — deixe o `ExceptionMapper` cuidar.
- Misturar mais de um caso de uso por endpoint.

## Tamanho saudável

- Um controller deve ter idealmente entre **3 e 10 endpoints** coesos por contexto. Acima disso, considere quebrar por subcontexto.
- Cada método tipicamente cabe em **3-6 linhas** (1 linha de delegação + retorno/conversão). Linhas a mais geralmente sinalizam regra que vazou da camada errada.

## Bean Validation

Para validações de formato (anotações `@NotNull`, `@Size`, regex), use Bean Validation no DTO de entrada e `@Valid` no parâmetro do controller. Validações de negócio ficam no domain service / entidade.
