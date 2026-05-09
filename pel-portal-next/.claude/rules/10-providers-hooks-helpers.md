# Providers, Hooks, Helpers, Modules

## Providers (`src/providers/`)

- One provider per file, named `<Name>Provider.tsx`. Export both the provider component and a `use<Name>()` hook that throws if used outside the provider.
- Mount global providers (Query, Session, Theme, Toaster) inside `app/layout.tsx`.
- Mount group-scoped providers (Auth, AppShell, Layout) inside `app/(<group>)/layout.tsx`.
- Avoid storing **server data** in Context. Use TanStack Query for server state; Context is for cross-cutting **UI state** (theme, sidebar open/closed, confirmation modal, ...).

## Hooks (`src/hooks/`)

- One hook per file, prefix `use`. Pure logic — no JSX.
- Hooks that depend on a provider's context belong next to the provider's `use<Name>()` exporter; cross-cutting reusable hooks (`useMobile`, `useDebounce`, `useConfirmation`) live in `src/hooks/`.
- Stable references: when a hook returns functions consumed in `useEffect` deps, wrap them in `useCallback`.

## Refs for idempotency

- Use `useRef<boolean>(false)` to prevent multi-fire effects on mount/refresh:
  ```ts
  const hasInitialized = useRef(false);
  useEffect(() => {
    if (hasInitialized.current) return;
    initialize();
    hasInitialized.current = true;
  }, []);
  ```
- Use the same pattern for "one-shot" mutations triggered from effects, network calls that must not double-fire under React 18 strict mode, or polling guards.
- Prefer **idempotent backend operations** when possible; the ref is a defense, not a substitute for backend correctness.

## Helpers (`src/helpers/`)

- Small, framework-agnostic utilities. No React. No services.
- Group by concern: `cn.ts`, `formatters.ts`, `masks.ts`, `converters.ts`, `general.ts`, `toast.ts`.
- Date utilities: pick **one** library (the project uses `dayjs`) and stick with it.
- Money/number formatting goes through helpers — don't repeat `Intl.NumberFormat` inline in components.
- Toast wrappers: a single helper (`handlerHttpError`) maps `HttpError` to the right toast severity.

## Modules (`src/modules/`)

- Adapters/shims/type augmentations for third-party libraries:
  - `next-auth.d.ts` — extends `Session`/`JWT` with project-specific fields.
  - `js-brasil.ts` — re-exports a typed wrapper around an untyped lib.
- Don't put business logic here.

## Don't

- ❌ Don't keep a giant `utils.ts` — split by concern.
- ❌ Don't export React components from `helpers/` or `hooks/`.
- ❌ Don't create a custom Context to cache server data when React Query already has it.
