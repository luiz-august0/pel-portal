# Gateways (porta-adaptador) — genérico

Toda integração com o **mundo externo** (banco, fila, e-mail, S3, API HTTP, JWT, hash, etc.) é representada como uma **porta** (interface no `core`) com um **adaptador** (implementação no `infra`).

## Estrutura

```
core/gateway/<categoria>/<Nome>GTW.java        (interface)
infra/service/<categoria>/<Nome>Service.java   (implementação)
```

- Sufixo da interface: `GTW` (gateway).
- Sufixo da implementação: `Service`.
- A categoria agrupa por domínio externo: `auth`, `crypt`, `event`, `file`, `mail`, `token`, `aws/s3`, ...

## Contrato

A interface define o contrato em **termos de domínio**. Não vaze tipos do framework de I/O:

```java
// BOM — vocabulário de domínio
public interface AuthorizationGTW {
    AuthorizedUserRecord authorize(AuthenticateRecord authenticateRecord);
}

// RUIM — vaza tipos JAX-RS / Hibernate
public interface AuthorizationGTW {
    Response authorize(HttpServletRequest req);
}
```

Se a operação pode falhar de forma esperada, declare uma exceção de domínio (`AppException`) — **não** uma exceção do driver/sdk externo.

## Implementação

```java
@ApplicationScoped
@RequiredArgsConstructor
public class AuthorizationService implements AuthorizationGTW {
    private final TokenService tokenService;
    private final UserRepository userRepository;

    @Override
    public AuthorizedUserRecord authorize(AuthenticateRecord rec) {
        // detalhes externos (BCrypt, JWT, SDK) ficam aqui
    }
}
```

A implementação pode depender livremente de SDKs/clients (BCrypt, AWS SDK, java-jwt, Mailer) — esse é o ponto de encapsulamento.

## Por quê

- **`core` permanece testável** sem container/banco/Kafka. Em testes unitários você faz `new FooUC(mockGTW)`.
- **Trocar fornecedor é local** — passar de S3 para GCS é editar um único `*Service`, sem mexer em UCs.
- **Inversão de dependência** real: a regra de negócio decide a interface; o detalhe a implementa.

## Quando criar um GTW

Crie sempre que houver:

1. Comunicação síncrona com sistema externo (HTTP, gRPC, banco SQL/NoSQL via cliente customizado, S3, ...).
2. Operação dependente de tempo/relógio/entropia (geração de token, datas) — facilita teste determinístico.
3. Operação criptográfica (hash, encrypt, JWT).
4. Publicação/leitura de eventos (Kafka, SQS, RabbitMQ).

> Repositórios JPA já são uma forma de gateway (Spring Data abstrai). Não duplique criando `*GTW` em cima de `JpaRepository` sem ganho.

## Antipadrões

- **Interface gateway com método para cada query** — vira proxy do repositório. Use o repositório direto.
- **Vazamento de tipos do SDK** na assinatura (`PutObjectRequest`, `Response`, `KafkaRecord`, ...).
- **Gateway sem implementação** (interface "futurista"). Crie o contrato junto com o primeiro consumidor real.
- **Mais de uma implementação ativa do mesmo GTW sem qualifier** — ambíguo no CDI.
