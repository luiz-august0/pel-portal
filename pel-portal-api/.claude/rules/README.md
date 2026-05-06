# Rules — backend Quarkus + Clean Arch + DDD

Conjunto de regras **genéricas** que podem ser copiadas para qualquer backend Java/Quarkus que adote Clean Architecture com toques de DDD. Cada arquivo é independente e auto-contido — copie só o que se aplicar ao novo projeto.

| Arquivo                                                  | Tema                                                  |
| -------------------------------------------------------- | ----------------------------------------------------- |
| [`architecture.md`](architecture.md)                     | Camadas, sufixos, agregados, direção de dependência   |
| [`dependency-injection.md`](dependency-injection.md)     | Construtor + `@RequiredArgsConstructor`, escopos CDI  |
| [`mediator.md`](mediator.md)                             | Padrão Mediator (Command/Handler sync e async)        |
| [`gateways.md`](gateways.md)                             | Porta-adaptador (interface no core, impl em infra)    |
| [`inbound.md`](inbound.md)                               | Controllers/listeners finos, só delegam               |
| [`infra-layer.md`](infra-layer.md)                       | Conteúdo da camada infra                              |
| [`exceptions.md`](exceptions.md)                         | Hierarquia + ExceptionMappers + EnumAppException      |
| [`flyway-migrations.md`](flyway-migrations.md)           | Migrations imutáveis, naming, boas práticas SQL       |
| [`testing.md`](testing.md)                               | F.I.R.S.T, Testcontainers, doubles, integração        |
| [`quarkus-best-practices.md`](quarkus-best-practices.md) | Config, transações, mensageria, observabilidade       |
| [`java-modern.md`](java-modern.md)                       | Records, pattern matching, virtual threads, Lombok    |

## Como usar em outro projeto

1. Copie o diretório `.claude/rules/` inteiro para a raiz do novo projeto Quarkus.
2. Crie um `AGENTS.md` na raiz desse projeto com o que é **específico** dele (estrutura de pacotes real, comandos, env vars, fluxos do domínio). Referencie as rules genéricas no final, como neste repo.
3. Ajuste convenções pontuais (sufixos, perfis, prefixos de path) onde divergir.

## Princípios que atravessam todas as rules

- **`core` puro** — nunca depende de `infra` nem `inbound`.
- **DI por construtor + `final`** — sempre.
- **Use case = boundary transacional** — `@Transactional` aqui, não em controller nem em handler.
- **Inbound só delega** — sem regra de negócio em controller/listener.
- **Gateways escondem SDKs** — vocabulário externo nunca vaza para o core.
- **Testes de integração com Testcontainers** — banco real, sempre.
- **Migrations imutáveis** — corrija criando nova versão.
- **Mensagens de erro consistentes** — padronize em `EnumAppException` e mantenha o idioma do produto.
