# Authentication & security

## NextAuth wiring

- Auth is delegated to **NextAuth** with a credentials/SSO provider exposed under `src/app/api/auth/[...nextauth]/`.
- `NextSessionProvider` wraps the app at the root layout so `useSession()` works anywhere on the client.
- Session shape is augmented in `src/modules/next-auth.d.ts` — extend it (don't fork the Session type) when new fields are needed.

## Route gating

- Use **route groups** to gate pages:
  - `(protected)/layout.tsx` is the only place that enforces "must be authenticated". It mounts `AuthProvider`, which:
    1. Reads `useSession()`.
    2. On `authenticated`, fetches the latest user data via the service layer.
    3. On `unauthenticated`, redirects to the public entry route.
    4. Uses a `useRef(hasInitialized)` guard so the bootstrap runs once.
  - `(unprotected)/layout.tsx` redirects authenticated users away from public-only pages.
- Don't gate auth ad-hoc inside `page.tsx` files — let the group layout do it.

## Token handling

- The access token is stored in a **cookie** (set by the auth flow) and is read by the HttpClient when building requests.
- Components and hooks **never read tokens directly**. Always go through the service layer.
- Don't put tokens in `localStorage` or in React state. Don't pass tokens as URL params.

## Session updates

- After a mutation that changes user data, propagate to the session:
  ```ts
  await update({ ...session, user: { ...session?.user, ...newData } });
  ```
- For changes that invalidate the session (CPF change, password rotation), call `signOut({ redirect: false })` and route the user back to the public entry.

## Sensitive data

- Never log credentials, tokens, CPF/CNPJ, addresses, or PII to the console.
- Don't include sensitive data in `queryKey`s — they may end up in DevTools.
- When transmitting to the backend, strip masks (`replace(/\D/g, "")`) so the server stores normalized values.

## Don't

- ❌ Don't bypass `(protected)/(unprotected)` to "quickly" build a new auth flow.
- ❌ Don't roll your own token refresh in a component — push it into the auth provider/HttpClient.
- ❌ Don't expose env vars that aren't `NEXT_PUBLIC_*` to client code.
