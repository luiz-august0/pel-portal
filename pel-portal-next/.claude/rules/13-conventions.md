# General conventions

## Naming

- **Components & files:** `PascalCase.tsx` (`EditUserDataModal.tsx`). One component per file unless tightly coupled.
- **Hooks:** `useXxx.ts`, exported as `useXxx`. The hook function name matches the file name.
- **Services:** descriptive verbs — `loginService.ts`, `getCurrentUserService.ts`, or a single `<domain>Service.ts` aggregating related operations.
- **Schemas:** `<thing>Schema` (camelCase), inferred type `<Thing>Type` or a semantic domain name.
- **Folders:** kebab-case for routes (`solicitar-recuperacao/`), camelCase or kebab-case for shared modules, but match what's already in the project.

## Imports

- Always use the `@/*` alias for cross-feature imports — never `../../../`.
- Group imports: external libs first, then `@/...`, then relative — separated by blank lines.
- Use `import type` for type-only imports.

## Comments

- Default to **no comments**. Names should explain what; comments only explain *why* when non-obvious.
- Don't write comments that restate the code (`// fetch user`).
- Don't leave commented-out code. If it's worth keeping, put it behind a feature flag or remove it (git keeps history).

## Language

- UI text (labels, placeholders, toasts, validation messages) in the **product's language** (pt-BR for this template).
- Code identifiers and inline comments preferably in **English** unless the surrounding code is already in another language — match the local convention.

## File size

- If a component file passes ~300 lines, look for an extraction (smaller atoms/molecules, dedicated hook).
- If a service file passes ~200 lines, split by sub-domain.

## Misc

- Prefer **small pure functions** over deeply nested logic.
- Prefer **early returns** over `if/else` pyramids.
- Prefer **`async/await`** over chained `.then()`.
- Don't introduce new dependencies without a clear reason — check `package.json` for an existing alternative first.
