# Injeção de dependência (genérico — Quarkus/CDI)

## Regra única

**Use injeção por construtor + `private final`.** Nunca use `@Inject` em campo mutável, nunca use setter injection, nunca anote `@Autowired`.

```java
@ApplicationScoped
@RequiredArgsConstructor      // gera o construtor com os finais
public class FooUC {
    private final BarRepository barRepository;
    private final BazGTW bazGTW;
}
```

## Por quê

- **Imutabilidade**: campos `final` impedem reatribuição acidental e tornam o objeto thread-safe por design.
- **Falha rápida**: a ausência de uma dependência quebra na construção do bean (boot), não em runtime.
- **Testabilidade**: instanciar a classe em testes unitários é apenas `new FooUC(mockBar, mockBaz)` — sem mexer em CDI.
- **Sem ciclos surpresa**: dependência circular vira erro de compilação/inicialização em vez de NPE intermitente.
- **Sem proxy nas dependências**: campos `final` evitam interceptação acidental em campos mutáveis.

## Quando precisar de qualifier (`@Channel`, `@Any`, `@Named`, ...)

`@RequiredArgsConstructor` não consegue colocar anotações nos parâmetros gerados. Nesses casos declare o construtor manualmente:

```java
@ApplicationScoped
public class KafkaProducerService implements EventProducerGTW {
    private final Emitter<String> emitter;
    private final ObjectMapper objectMapper;

    public KafkaProducerService(@Channel("kafka-producer") Emitter<String> emitter,
                                ObjectMapper objectMapper) {
        this.emitter = emitter;
        this.objectMapper = objectMapper;
    }
}
```

Para coleções de beans use `Instance<T>` + `@Any` (resolução manual em runtime — é o que `MediatorImpl` faz).

## Escopos CDI — quando usar cada um

| Escopo                  | Use para                                                      |
| ----------------------- | ------------------------------------------------------------- |
| `@ApplicationScoped`    | Padrão. Stateless ou estado compartilhado seguro (cache de leitura). |
| `@RequestScoped`        | Estado por requisição HTTP (auth context, correlation id).    |
| `@SessionScoped`        | Raramente. Quase sempre indica modelagem incorreta em API REST stateless. |
| `@Dependent` (default sem anotação) | Beans utilitários sem ciclo de vida próprio que herdam o do consumidor. Evite — prefira `@ApplicationScoped`. |

> Regra prática: comece com `@ApplicationScoped`. Só promova para `@RequestScoped` se houver estado por request.

## Cuidados específicos

- **Não injete bean de escopo menor em bean de escopo maior por construtor** sem proxy. Em CDI, `@RequestScoped` injetado num `@ApplicationScoped` funciona porque CDI cria proxy — mas evite passar essa referência para threads de fundo (o request já terá terminado).
- **Não use `new`** para instanciar serviços CDI; eles precisam ser resolvidos pelo container.
- **Lombok + Quarkus**: `@Slf4j` funciona; `@RequiredArgsConstructor` funciona com CDI 3+. Configure o annotation processor no `pom.xml`.
- **`Mediator`/handlers** (e qualquer abstração resolvida por reflection genérica) devem ser registrados via CDI normalmente — o lookup via `Instance<T>` evita ter que cadastrar manualmente.

## Checklist em revisão de PR

- [ ] Todo bean tem `@RequiredArgsConstructor` (ou construtor manual com qualifier) e dependências `private final`.
- [ ] Nenhum `@Inject` em campo.
- [ ] Nenhum `@Autowired` em código novo.
- [ ] `@ApplicationScoped` por padrão; promoção a `@RequestScoped` justificada.
- [ ] Nenhum `new ServicoCDI()` no código.
