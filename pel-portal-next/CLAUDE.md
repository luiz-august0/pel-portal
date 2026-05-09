# CLAUDE.md

Instruções para o Claude Code trabalhar neste repositório.

Para guidance específica deste projeto (estrutura, autenticação, comandos, padrões observados nos arquivos reais), comece por:

@AGENTS.md

As regras genéricas (reaproveitáveis em qualquer projeto Next.js + shadcn + TanStack Query + RHF + Zod) estão em `.claude/rules/`. **Leia todas antes de escrever ou modificar código:**

@.claude/rules/00-project-overview.md
@.claude/rules/01-routing.md
@.claude/rules/02-components-atomic-design.md
@.claude/rules/03-api-service-layer.md
@.claude/rules/04-domain-types-zod.md
@.claude/rules/05-data-fetching-react-query.md
@.claude/rules/06-forms.md
@.claude/rules/07-styling-tailwind.md
@.claude/rules/08-typescript.md
@.claude/rules/09-nextjs-best-practices.md
@.claude/rules/10-providers-hooks-helpers.md
@.claude/rules/11-feedback-and-errors.md
@.claude/rules/12-auth-and-security.md
@.claude/rules/13-conventions.md
@.claude/rules/14-do-not.md

## Resumo operacional

- **Stack:** Next.js 14 (App Router), React 18, TS strict, Tailwind v4, shadcn/ui, TanStack Query v5, react-hook-form, Zod v4, NextAuth v4.
- **Path alias:** `@/*` → `src/*`.
- **Gerenciador:** `yarn` (`yarn dev`, `yarn build`, `yarn lint`).
- **Idioma do produto:** pt-BR (mensagens de UI/toasts/validações).

## Comportamento esperado

1. Antes de propor uma alteração, confirme que as regras acima cobrem o caso. Se houver conflito, alinhe com o usuário.
2. Para qualquer integração com backend: passe pela service layer em `src/core/services/<domain>/` e use tipos de `src/types/domains/<domain>.ts`.
3. Forms sempre com `useForm` + `zodResolver(<schemaDoDomain>)`. Submit via `useMutation` com `onSuccess`/`onError`.
4. Listagens/buscas sempre com `useQuery`. Server Components por padrão; `"use client"` só onde for necessário.
5. Componentes locais à rota em `_components/{atoms,molecules,organisms,pages}/`. Compartilhados em `src/components/customized/`. Primitivos shadcn em `src/components/ui/` (não editar à mão).
6. Estilização **só** com Tailwind + `cn()`. Sem `any` em TypeScript.
