# TypeScript rules

## Hard rules

- **`strict: true`** must be enabled in `tsconfig.json`.
- **Never use `any`.** When a type isn't known at compile time, use `unknown` and narrow with type guards or `zod.parse`.
- **Don't use `@ts-ignore` / `@ts-expect-error` / `eslint-disable`** without a comment explaining why and a follow-up plan.
- **Don't cast to bypass errors** (`as any`, double-cast `as unknown as T`). Fix the type instead.

## Type sources

- Domain shapes come from Zod schemas via `z.infer<typeof schema>` — see `src/types/domains/`.
- Backend response types live next to the schema, not in a separate `models.ts`.
- Component props are declared inline (`interface Props { ... }` or `type Props = ...`) above the component.

## Style

- Prefer **type inference** for local variables and return types. Only annotate when the inferred type is wrong, ambiguous, or part of a public contract (exported function, hook, service).
- Prefer `type` aliases for unions/intersections; `interface` for object shapes you may want to extend.
- Public exported functions in `core/services/` always declare an explicit return type — this stabilizes the API contract.
- Use **discriminated unions** (`{ kind: "ok"; data } | { kind: "error"; error }`) instead of optional/optional pairs.

## Generics

- Provide generics where the consumer benefits (`HttpClient.get<T>`). Don't over-genericize internal helpers for hypothetical reuse.

## Imports

- Use `import type { ... }` for type-only imports to avoid bundling.
- Use the `@/*` path alias for everything inside `src/`. No deep relative imports (`../../../`).

## Common pitfalls

- ❌ `function fetchUser(id: any)` — use `string | number` or a domain-specific type.
- ❌ `const data: any = await res.json()` — use `unknown` + Zod parse.
- ❌ Defining a TS interface that mirrors a Zod schema by hand — derive it.
- ❌ Returning `Promise<void>` from a function that actually returns data.
