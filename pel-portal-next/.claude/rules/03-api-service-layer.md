# API Service Layer (`src/core/`)

The `core/` folder is the **only** place that talks to the backend. UI code consumes services; it never calls `fetch`/`axios` directly.

## `core/http/`

- A single `HttpClient` implementation wraps `fetch`. Keep it framework-free (no React).
- Pre-instantiated clients per backend prefix (e.g. `portalClient`, `gestaoClient`, `portalAuthClient`) live alongside the class. Reuse them — don't instantiate ad-hoc.
- Auth header injection happens at the client level (e.g. reading a cookie) — services don't pass tokens manually.
- HTTP errors are thrown as a typed `HttpError` class so callers can `instanceof`-check and use `handlerHttpError` from `helpers/toast.ts`.

## `core/services/<domain>/`

- One folder per domain (`auth`, `user`, `inscription`, `address`, ...).
- File granularity:
  - One file per operation (`loginService.ts`, `getCurrentUserService.ts`) **or**
  - One file per domain when functions are tightly related (`inscriptionService.ts`).
- **Service function shape:**
  ```ts
  export async function getInscriptionDetails(id: number): Promise<InscriptionDetails> {
    const { data } = await gestaoClient.get<InscriptionDetails>(`/inscription/${id}`);
    return data;
  }
  ```
- Services return **domain types** from `src/types/domains/`, never raw `Response` or `unknown`.
- When the response is sensitive or not fully trusted, validate with the domain Zod schema:
  ```ts
  return InscriptionGradesResponseSchema.parse(data);
  ```
- Services are pure async functions: no React imports, no hooks, no `useQuery`. Caching is the consumer's responsibility (TanStack Query).
- Pass query params via the HttpClient `params` option, not via string concatenation.

## Don't

- ❌ Don't call `fetch`/`axios` from components, hooks, or providers.
- ❌ Don't put data-shaping logic (mapping, filtering) inside React components when it can live in the service.
- ❌ Don't swallow errors in services — let them propagate so React Query / mutations can surface them.
- ❌ Don't read auth tokens in feature code; the HttpClient handles it.
