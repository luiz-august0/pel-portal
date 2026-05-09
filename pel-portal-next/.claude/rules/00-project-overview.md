# Project overview (generic Next.js + shadcn template)

Apply these rules to any Next.js 14+ project that uses TypeScript, Tailwind, shadcn/ui, TanStack Query, react-hook-form and Zod.

## Canonical folder layout

```
src/
├── app/                       # Next.js App Router
│   ├── layout.tsx             # Root layout — global providers only
│   ├── api/                   # Route handlers
│   ├── (protected)/           # Route group — requires auth
│   │   ├── layout.tsx         # Auth + app-shell providers
│   │   └── <feature>/
│   │       ├── page.tsx
│   │       ├── [id]/page.tsx
│   │       └── _components/{atoms,molecules,organisms,pages}/
│   └── (unprotected)/         # Route group — public (login, signup, etc.)
├── components/
│   ├── ui/                    # shadcn primitives (do NOT hand-edit)
│   └── customized/            # Project-wide composed components
├── core/
│   ├── http/                  # HttpClient + types + errors
│   └── services/<domain>/     # API service layer (one folder per domain)
├── types/
│   ├── api.ts                 # Generic HTTP/error types
│   ├── general.ts             # Shared constants/regex
│   └── domains/<domain>.ts    # Zod schemas + inferred types per domain
├── helpers/                   # cn, formatters, masks, toast, ...
├── hooks/                     # Reusable hooks (useConfirmation, useMobile, ...)
├── providers/                 # Auth/Layout/QueryClient/Theme providers
└── modules/                   # Adapters / type augmentations for libs
```

## Non-negotiable principles

- **Single source of truth per concern.** Domain types/schemas live in `src/types/domains/`. API calls live in `src/core/services/`. UI primitives live in `src/components/ui/`. Never duplicate.
- **Keep components dumb.** Business rules and integrations belong in services + hooks, not inside JSX.
- **Prefer Server Components by default.** Add `"use client"` only when the component truly needs hooks/events/browser APIs.
- **Co-locate by feature.** A page-specific component goes in `app/<route>/_components/`, not in `src/components/`.

## Aliases

- Use `@/*` (mapped to `src/*`) — never relative chains like `../../../`.
