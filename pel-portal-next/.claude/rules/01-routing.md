# Routing rules (Next.js App Router)

## Route groups

- Use `(protected)` for routes that require an authenticated session and `(unprotected)` for public routes (login, signup, password recovery, onboarding).
- Each group **must** have its own `layout.tsx`:
  - `(protected)/layout.tsx` mounts auth + app-shell providers (sidebar, theme, etc.) and gates rendering on session readiness.
  - `(unprotected)/layout.tsx` redirects authenticated users to `/`.
- Do **not** introduce additional route groups without explicit alignment — the two above cover the common case.

## `_components` folders (route-private)

- Folders prefixed with `_` are **ignored by the App Router** and are the canonical place for components that belong to a single route.
- Structure them with Atomic Design:
  ```
  <route>/_components/
    atoms/        # smallest building blocks (Button wrappers, cards, badges)
    molecules/    # composed atoms with light logic
    organisms/    # heavy components: modals, multi-step forms, sections with queries
    pages/        # optional — page-level views decoupled from page.tsx
  ```
- Components shared **across multiple routes of the same group** go in `app/(<group>)/_components/{atoms,molecules,organisms}/`.
- Components shared **across groups / across the whole app** go in `src/components/customized/`.

## Pages

- `page.tsx` should be thin. When the view has non-trivial composition or state, delegate to `_components/pages/<View>Page.tsx` and let `page.tsx` only fetch top-level data and pass it down.
- Use dynamic segments `[id]`, `[slug]`, `[cpf]` for parameterized routes.
- Wrap pages that read `useSearchParams` in `<Suspense>` to satisfy Next.js requirements.

## Layouts

- Keep `app/layout.tsx` minimal: html/body shell + truly global providers (QueryClient, Session, Toaster, Theme).
- Per-group layouts handle group-specific concerns (auth gating, sidebar, etc.).
