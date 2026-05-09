# Don't list (quick reference)

A condensed checklist. If you find yourself doing any of these, stop and reconsider.

## Architecture
- ❌ Don't call `fetch`/`axios` outside `src/core/`.
- ❌ Don't create components in `src/components/ui/` by hand — use shadcn CLI.
- ❌ Don't put business logic in atoms/molecules.
- ❌ Don't introduce new state management libs (Redux/Zustand/Jotai). Use Context + TanStack Query.
- ❌ Don't bypass the `(protected)`/`(unprotected)` route groups.
- ❌ Don't add new route groups or top-level folders without alignment.

## Types
- ❌ Don't use `any`. Use `unknown` + Zod parsing or a proper type.
- ❌ Don't `as any` / double-cast to silence TS errors.
- ❌ Don't declare a TS interface that mirrors a Zod schema — derive it.
- ❌ Don't put domain types outside `src/types/domains/`.

## Forms & validation
- ❌ Don't validate forms with ad-hoc `useState`. Use `react-hook-form` + Zod.
- ❌ Don't keep form schemas inside the component file.
- ❌ Don't submit forms by calling services directly — wrap in `useMutation`.

## Data fetching
- ❌ Don't fetch with `useEffect` + `useState`. Use `useQuery`.
- ❌ Don't store server data in Context.
- ❌ Don't ship a mutation without `onSuccess` and `onError`.
- ❌ Don't forget to invalidate affected queries after a mutation.

## Styling
- ❌ Don't write CSS modules / styled-components / emotion.
- ❌ Don't combine inline `style={{}}` with Tailwind for layout.
- ❌ Don't hard-code hex colors when a theme token exists.

## Next.js
- ❌ Don't add `"use client"` to whole pages just because one child needs it.
- ❌ Don't use Pages-Router APIs (`getServerSideProps`, `next/router`).
- ❌ Don't read `window`/`document` during render.

## Auth
- ❌ Don't store tokens in `localStorage`.
- ❌ Don't read tokens in components — go through the service layer.
- ❌ Don't expose non-`NEXT_PUBLIC_*` env vars to the client.

## Misc
- ❌ Don't `alert()`, `confirm()`, or `console.log` for user feedback.
- ❌ Don't leave commented-out code.
- ❌ Don't create `.md` files unless asked.
