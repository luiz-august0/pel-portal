# Flyway / migrations (genérico)

## Estrutura

```
src/main/resources/db/migration/
  V1__create_user_table.sql
  V2__create_address_table.sql
  V3__add_phone_to_user.sql
```

- Padrão de nome: `V<N>__<descrição_em_snake_case>.sql`. `N` estritamente crescente, sem reuso.
- Uma migration = uma intenção. Misturar 5 mudanças em `V12__various.sql` torna rollback e revisão difíceis.
- Apenas SQL para a maioria dos casos; migrations Java (`R__`, `V__`) só quando há transformação que SQL não cobre.

## Configuração Quarkus

```properties
quarkus.flyway.migrate-at-start=true     # roda no boot
quarkus.flyway.baseline-on-migrate=true  # cria baseline em bancos legados
quarkus.flyway.locations=classpath:db/migration
quarkus.flyway.schemas=public
```

Não habilite `clean-at-start` em `application.properties` de produção. **Apenas** em `src/test/resources/application.properties` ou via `BaseIntegrationTest`.

## Imutabilidade

Migrations já aplicadas em ambiente compartilhado **não podem ser editadas**. Flyway compara checksum: alterar arquivo = falha de boot. Para corrigir:

- Erro de schema → criar `V<N+1>__fix_*.sql` que ajusta.
- Dados ruins → criar migration corretiva.
- Em desenvolvimento local, se ainda **não** subiu para outros, é aceitável reescrever — mas avise o time.

## Boas práticas SQL

1. **Sempre `IF NOT EXISTS`** em `CREATE TABLE`/`CREATE INDEX` quando a migration puder rodar contra base parcialmente populada (ambientes herdados).
2. **Constraints nomeadas**:

   ```sql
   constraint user_pk primary key (id),
   constraint user_un_email unique (email),
   constraint user_address_fk foreign key (address_id) references address(id)
   ```

   Nomes explícitos facilitam debug, alteração e DROP em migrations futuras.
3. **Defaults explícitos** quando a coluna é `NOT NULL` e há dados existentes:

   ```sql
   alter table user add column active boolean not null default true;
   ```
4. **Índices em colunas de FK e busca frequente** (`WHERE`, `JOIN`). Documente o motivo no nome (`idx_user_email`, `idx_dependent_user_id`).
5. **`ON DELETE` consciente** (`CASCADE`, `SET NULL`, `RESTRICT`). Evite `CASCADE` em raízes de agregado se quiser controle do app.
6. **UUID como PK** quando IDs são expostos a clientes. `BIGSERIAL`/`IDENTITY` para tabelas internas de alto volume.
7. **Timestamps**: `created_at timestamp default current_timestamp`, `updated_at timestamp` (nullable; preencha via `@PreUpdate`).

## Operações destrutivas

- `DROP TABLE` / `DROP COLUMN` em produção: faça em **duas migrations** separadas por release: (1) tornar a coluna nullable + parar de escrever, (2) drop após confirmar que não há leitura. Backups antes.
- `ALTER TABLE` que reescreve grandes tabelas (mudança de tipo, NOT NULL com default) pode travar leitura. Para tabelas grandes, considere migration manual coordenada com janela.

## Em testes

- Padrão: `@QuarkusTestResource(PostgresTestResource.class)` sobe Postgres real (Testcontainers); o `BaseIntegrationTest` faz `flyway.clean(); flyway.migrate();` no `@BeforeAll` para começar limpo.
- Não popule dados via SQL repetível (`R__seed.sql`) que altere comportamento dos testes — use builders Java em fixtures.

## Antipadrões

- Editar `V<N>__*.sql` já aplicado.
- Pular números (`V1`, `V3`, `V4`).
- "Migration de seed" misturando DDL e dados de produção.
- `CREATE OR REPLACE` em estruturas versionadas — Flyway não reaplica mesmo se o arquivo mudar (use nova versão).
- Comentar out blocos com `--` em migration já versionada (mude o checksum).
