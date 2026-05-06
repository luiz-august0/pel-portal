# Testes — F.I.R.S.T, Quarkus, Testcontainers (genérico)

## Princípios F.I.R.S.T

| Letra | Princípio       | Significado                                                 |
| ----- | --------------- | ----------------------------------------------------------- |
| **F** | Fast            | Rodam em segundos. Lentidão erode a prática de TDD.         |
| **I** | Independent     | Nenhum teste depende da ordem ou do estado de outro.        |
| **R** | Repeatable      | Mesma entrada → mesmo resultado, em qualquer máquina.       |
| **S** | Self-validating | Passa ou falha. Sem inspeção visual de log/saída.           |
| **T** | Timely          | Escritos perto do código de produção (idealmente antes).    |

Se um teste viola um desses, é cheiro: investigue antes de mergear.

## Pirâmide

```
       ┌──────────────┐
       │  e2e (poucos)│  ← raros, fluxos críticos ponta-a-ponta
       ├──────────────┤
       │ integração   │  ← UC + banco real, gateway real (Testcontainers)
       ├──────────────┤
       │   unidade    │  ← maioria; entidades, factories, domain services, util
       └──────────────┘
```

## Testes unitários

Para classes **sem dependência de framework**:

- Entidades: invariantes (`updateBasicInfo`, `validateReviewed`).
- Domain services: regras puras (com mocks dos repositórios).
- Factories: criação válida e validação rejeita inválido.
- Utils (`StringUtil`, `CpfUtil`, `DateUtil`): casos limites.

Stack: JUnit 5 + AssertJ + Mockito (sem Quarkus).

```java
class UserValidatorServiceTest {
    private final UserRepository userRepository = mock(UserRepository.class);
    private final UserValidatorService svc = new UserValidatorService(userRepository);

    @Test
    @DisplayName("Lança ValidatorException quando e-mail está em uso")
    void emailInUse() {
        when(userRepository.existsByEmailAndIdIsNot("a@b.com", null)).thenReturn(true);

        assertThatThrownBy(() -> svc.validateEmail("a@b.com", null))
            .isInstanceOf(ValidatorException.class)
            .hasMessageContaining("já está em uso");
    }
}
```

Não use `@QuarkusTest` aqui — desnecessário e mata o "F" do F.I.R.S.T.

## Testes de integração

Para UCs, controllers e fluxos que cruzam camadas. **Sempre** com Testcontainers — **nunca** mocke o banco.

```java
@QuarkusTest
@DisplayName("Testes de integração de autenticação")
class AuthenticateIntegrationTest extends BaseIntegrationTest {

    @Inject RegisterUC registerUC;
    @Inject AuthenticateUC authenticateUC;
    @Inject UserRepository userRepository;

    @InjectMock NotifyCreateUpdatePortalUserEvent notifyEvent;  // borda externa

    @Test
    @DisplayName("Deve fazer login normal sem token")
    void shouldLoginNormallyWithoutToken() {
        // Given
        var dto = createValidUserRegisterDTO("user@example.com", "71009062026");
        doNothing().when(notifyEvent).send(any());

        // When
        registerUC.execute(dto);
        var rec = authenticateUC.execute(new AuthenticateRecord(dto.getCpf(), dto.getPassword(), null));

        // Then
        assertNotNull(rec.accessToken());
    }
}
```

Convenções:

- `@QuarkusTest` no teste; estende `BaseIntegrationTest`.
- `BaseIntegrationTest` ativa `@QuarkusTestResource(PostgresTestResource.class)` e roda `flyway.clean(); flyway.migrate()` no `@BeforeAll` para isolamento.
- `@TestInstance(TestInstance.Lifecycle.PER_CLASS)` na base permite `@BeforeAll` não-static.
- Estrutura **Given / When / Then** com comentários (revisor lê em segundos).
- `@DisplayName` em pt-BR descrevendo o comportamento esperado.
- **Mocke apenas as bordas externas** (Kafka producer, S3, Mailer) com `@InjectMock`. O banco é real.

### Setup do `BaseIntegrationTest`

```java
@QuarkusTestResource(PostgresTestResource.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class BaseIntegrationTest {
    @Inject Flyway flyway;

    @BeforeAll
    void setupDatabase() {
        flyway.clean();
        flyway.migrate();
    }
}
```

### `PostgresTestResource`

```java
public class PostgresTestResource implements QuarkusTestResourceLifecycleManager {
    private PostgreSQLContainer<?> postgres;

    @Override
    public Map<String, String> start() {
        postgres = new PostgreSQLContainer<>("postgres:16-alpine")
                .withDatabaseName("test")
                .withUsername("postgres")
                .withPassword("postgres");
        postgres.start();
        return Map.of(
            "quarkus.datasource.db-kind", "postgresql",
            "quarkus.datasource.jdbc.url", postgres.getJdbcUrl(),
            "quarkus.datasource.username", postgres.getUsername(),
            "quarkus.datasource.password", postgres.getPassword()
        );
    }

    @Override public void stop() { if (postgres != null) postgres.stop(); }
    @Override public int order() { return -1; }   // sobe antes dos demais resources
}
```

## Test doubles — escolher o certo

| Tipo   | Usa quando…                                                                 |
| ------ | --------------------------------------------------------------------------- |
| **Dummy**  | Apenas preenche assinatura; nunca é exercitado.                          |
| **Stub**   | Devolve resposta canned para guiar o caminho do teste.                   |
| **Fake**   | Implementação simplificada e funcional (ex.: in-memory repo).            |
| **Spy**    | Real, mas observa chamadas. Útil para verificar efeito sem mockar tudo.  |
| **Mock**   | Define expectativas e verifica interações. **Use com parcimônia** — testes "mock-heavy" se acoplam à implementação.|

Diretrizes:

- **Banco**: nunca mocke. Use Testcontainers.
- **Filas / SDK externo**: mock no teste de integração que não está validando a integração em si; em teste do gateway, use o real (ou Testcontainer correspondente).
- **Tempo**: prefira clock injetável a mockar `new Date()`.
- **Aleatório**: idem — gere via gateway para tornar determinístico em teste.
- **Quase nunca mocke o objeto sob teste.** Se está mockando, é um colaborador.

### Mockito + Quarkus

- `@InjectMock` substitui um bean CDI por um mock para aquele teste.
- `@InjectSpy` envolve o bean real preservando comportamento.
- Sempre verifique que o mock está sendo aplicado a um bean injetado por construtor — Quarkus suporta.

## Mensageria em teste

- Use `smallrye-reactive-messaging-in-memory` para canais de Kafka em teste:

  ```properties
  mp.messaging.outgoing.<channel>.connector=smallrye-in-memory
  mp.messaging.incoming.<channel>.connector=smallrye-in-memory
  ```

- Verifique mensagens emitidas via `InMemoryConnector.emit` e `received()`.
- Para envio externo (S3, e-mail), mocke o gateway no teste de UC; teste o gateway em isolamento contra serviço real (LocalStack, Mailpit) ou contract test.

## Cobertura

- 80%+ é meta saudável de cobertura de **linha** em domínio.
- Foque cobertura em **comportamento crítico** e **caminhos de erro**, não em getters/setters.
- Teste mutante (PIT) opcional para validar qualidade dos asserts.

## Antipadrões

- Testes de integração compartilhando estado (sem `clean`/`migrate` por classe).
- Asserts com `assertTrue(condition)` sem mensagem — use AssertJ (`assertThat(x).isTrue()`) com encadeamento legível.
- `Thread.sleep` em teste async — use `Awaitility`.
- Teste que falha 1 a cada 10 execuções: ou conserta ou deleta — *flaky* mata confiança.
- Testes ordenados (`@Order`) — viola **Independent**.
- Mockar entidades JPA — quase sempre sintoma de design ruim.
- Lógica condicional dentro de teste (`if (env == ...)`).
