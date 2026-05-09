# Domain types & Zod schemas

## Single source of truth

- Every domain entity, request body, response payload and form schema lives in **`src/types/domains/<domain>.ts`**.
- One file per domain (`auth.ts`, `user.ts`, `inscription.ts`, `address.ts`, ...).
- **Types are derived from Zod schemas**, not declared independently:
  ```ts
  export const userDataSchema = z.object({
    cpf: z.string().refine(validateBr.cpf, "CPF inválido"),
    name: z.string().min(1, "Nome é obrigatório"),
    email: z.email("E-mail inválido"),
  });
  export type UserDataType = z.infer<typeof userDataSchema>;
  ```

## When to add a schema

- A new entity returned by the API → schema + `z.infer` type.
- A new form → schema in the same domain file (e.g. `loginSchema` in `auth.ts`).
- A new step in a multi-step form → step-scoped schema (`InscriptionStep1Schema`, ...).
- A composed schema → use `.merge()`, `.extend()`, `.refine()`. Don't recreate fields.

## Naming

- Schemas: `<thing>Schema` (camelCase prefix, `Schema` suffix).
- Inferred types: `<Thing>Type` or a semantic name (`InscriptionDetails`) — be consistent within a domain file.
- Enums-as-zod: `z.enum([...])` instead of TS `enum` whenever the value is also serialized.

## Validation conventions

- Error messages are **user-facing** — write them in the project's UI language (pt-BR in this template).
- Use `.refine()` for cross-field validations (`password === confirmPassword`).
- Use library-backed validators where they exist (`js-brasil` for CPF/CNPJ/CEP/phone).
- Date fields: store as `string` (ISO/`YYYY-MM-DD`) and validate with `dayjs(value).isValid()`. Format only at the UI/API boundary.

## Don't

- ❌ Don't use `any`. If a value is truly unknown at compile time, use `unknown` and narrow.
- ❌ Don't define a TS interface that mirrors a Zod schema — derive with `z.infer` instead.
- ❌ Don't define schemas inside components — they belong in `types/domains/`.
- ❌ Don't reuse a schema for both API response and form input if the shapes diverge — declare separate schemas in the same file.
