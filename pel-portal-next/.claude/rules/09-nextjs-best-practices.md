# Next.js best practices

## Server vs. Client Components

- The App Router renders **Server Components by default**. Only opt into client rendering when you actually need it.
- Add `"use client"` (top of file) **only** when the component uses one or more of:
  - React hooks (`useState`, `useEffect`, `useMemo`, ...)
  - Event handlers (`onClick`, `onChange`, ...)
  - Browser APIs (`window`, `document`, `localStorage`, `IntersectionObserver`, ...)
  - Context consumers from client-only providers (e.g. `useSession` from `next-auth/react`, `useQuery` from React Query)
- Push `"use client"` **down the tree** as far as possible. A static section inside a page should stay a Server Component even if the parent has interactive children.

## Prefer SSR / RSC where possible

- For data that doesn't change per-user-interaction, fetch inside Server Components and pass the result as props.
- Use `next/cache` (`unstable_cache`, `revalidateTag`) for cacheable backend reads on the server.
- For per-user data behind auth in this template, the session is read on the client (`useSession`) — keep that in a client island, not in a global "use client" page.

## Hydration safety

- Don't read `window` or `document` during render — wrap in `useEffect` or `typeof window !== "undefined"` checks.
- Set `suppressHydrationWarning` on `<html>` only when intentional (theme attribute injection).
- Use `next/font` for fonts (already done with `Inter`) — don't add `<link>` font tags manually.

## Routing

- Use `next/link` for in-app navigation, never raw `<a href>` for internal routes.
- Use `useRouter` from `next/navigation` (App Router), not `next/router`.
- For programmatic redirects in Server Components/Route Handlers, use `redirect()` from `next/navigation`.
- Wrap pages that read `useSearchParams` in `<Suspense>` with a fallback.

## Metadata

- Set `export const metadata: Metadata = { ... }` from layout/page files. Don't manipulate `<head>` manually.

## Don't

- ❌ Don't add `"use client"` to a layout/page just because one descendant needs it — extract the descendant.
- ❌ Don't import server-only modules (e.g. `fs`, `next/headers`) in client components.
- ❌ Don't import client-only hooks (`useSession`, `useQuery`) in Server Components.
- ❌ Don't use `getServerSideProps` / `getStaticProps` — those belong to the Pages Router.
