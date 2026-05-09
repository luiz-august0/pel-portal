# AGENTS.md — pel-portal-next

Guia de contexto para agentes de IA (Claude Code) trabalharem neste repositório.
Para regras genéricas reutilizáveis em outros projetos Next.js, veja `.claude/rules/` (importadas pelo `CLAUDE.md` na raiz).

---

## 1. Visão geral

- **Projeto:** Portal do aluno (PEL).
- **Stack:** Next.js 14 (App Router) + React 18 + TypeScript estrito + Tailwind v4 + shadcn/ui (style "new-york") + TanStack Query v5 + react-hook-form + Zod v4 + NextAuth v4.
- **Idioma do produto:** pt-BR (mensagens de UI, toasts e validações são em português).
- **Path alias:** `@/*` aponta para `src/*`.
- **Gerenciador:** `yarn` (há `yarn.lock`). Scripts: `yarn dev`, `yarn build`, `yarn start`, `yarn lint`.

---

## 2. Estrutura de pastas

```
src/
├── app/                          # App Router do Next.js
│   ├── layout.tsx                # Root layout (providers globais)
│   ├── globals.css               # Tailwind + tokens
│   ├── api/auth/[...nextauth]/   # NextAuth handler
│   ├── (protected)/              # Route group — exige sessão
│   │   ├── layout.tsx            # AuthProvider + LayoutProvider + Sidebar
│   │   ├── _components/          # Componentes compartilhados ENTRE páginas protegidas
│   │   │   ├── atoms/
│   │   │   ├── molecules/
│   │   │   └── organisms/
│   │   ├── perfil/
│   │   │   ├── page.tsx
│   │   │   └── _components/organisms/...
│   │   ├── inscricao/
│   │   │   ├── page.tsx
│   │   │   ├── nova/page.tsx
│   │   │   ├── [id]/page.tsx
│   │   │   └── _components/{atoms,molecules,organisms,pages}/
│   │   └── ...                   # dependentes/, transferencia/, nivelamento/
│   └── (unprotected)/            # Route group — público (login, registro, onboarding)
│       └── ...
├── components/
│   ├── ui/                       # shadcn primitives — NÃO modificar à mão fora do CLI shadcn
│   └── customized/               # Wrappers/compostos do projeto (Input, DatePicker, DataTable, etc.)
├── core/
│   ├── http/                     # HttpClientImpl (fetch + Bearer token via cookie)
│   └── services/<dominio>/       # Service Layer — única ponte com o backend
├── types/
│   ├── api.ts                    # tipos genéricos de erro/response HTTP
│   ├── general.ts                # constantes (regex, etc.)
│   └── domains/<dominio>.ts      # Zod schemas + tipos de domínio (z.infer)
├── helpers/                      # cn, formatters, masks, toast, converters, general
├── hooks/                        # useConfirmation, useMobile
├── modules/                      # adapters/declarações para libs externas (js-brasil, next-auth.d.ts)
└── providers/                    # AuthProvider, LayoutProvider, QueryClientProvider, ThemeProvider
```

---

## 3. Roteamento (App Router)

- **Route groups** `(protected)` e `(unprotected)` separam páginas que exigem sessão NextAuth. **Toda nova página privada vai dentro de `(protected)/`.**
- **Pastas `_components`** dentro de qualquer rota são ignoradas pelo roteador do Next (prefixo `_`). Use-as para componentes locais àquela rota.
- **Componentes locais a uma rota** seguem **Atomic Design** dentro de `_components/`:
  - `atoms/` — botões/cards mínimos sem regras de domínio.
  - `molecules/` — composição de atoms com pouca lógica.
  - `organisms/` — modais, steps, seções com lógica e queries/mutations.
  - `pages/` (opcional) — composição "view" desacoplada do `page.tsx` (ver `inscricao/_components/pages/InscriptionListPage.tsx`).
- **Componentes compartilhados entre rotas do mesmo grupo** ficam em `app/(protected)/_components/` na mesma estrutura atômica.
- **Componentes globais cross-feature** ficam em `src/components/customized/`.
- Rotas dinâmicas usam `[param]` (ex.: `inscricao/[id]/page.tsx`).
- **`page.tsx` de listagens** delega para `_components/pages/<X>Page.tsx` quando há lógica de view não trivial.

---

## 4. Autenticação

- NextAuth v4 com provider `credentials`, handler em `src/app/api/auth/[...nextauth]/`.
- Sessão exposta via `useSession()` (envolvida por `NextSessionProvider` no root layout).
- O `accessToken` é lido por `httpClient` a partir do **cookie `accessToken`** (ver `core/http/httpClient.ts:4`). O HttpClient injeta automaticamente `Authorization: Bearer <token>` em chamadas autenticadas.
- `(protected)/layout.tsx` envolve com `AuthProvider`, que:
  - Busca dados frescos do usuário em `getCurrentUser()` no mount.
  - Atualiza a sessão via `update({ ...session, user: { ... } })`.
  - Faz `signOut` + redirect para `/onboarding` em sessão expirada.
  - Usa `useRef(hasInitialized)` para garantir idempotência.
- `(unprotected)/layout.tsx` redireciona para `/` se autenticado.
- Tipos da sessão estendidos em `src/modules/next-auth.d.ts`.

---

## 5. Camada de serviços (Core / API Service Layer)

**Regra:** componentes **nunca** chamam `fetch` direto. Toda integração passa por `src/core/services/<dominio>/<algo>Service.ts`.

- HttpClient configurado em `core/http/httpClient.ts`. Há clientes pré-instanciados:
  - `portalClient` — `${NEXT_PUBLIC_API}/portal` autenticado.
  - `gestaoClient` — `${NEXT_PUBLIC_API}/gestao` autenticado.
  - `portalAuthClient` — `${NEXT_PUBLIC_API}/portal` sem token (login/recuperação).
- Erros HTTP são lançados como `HttpError` (`core/http/httpError.ts`).
- **Padrão de service function:**
  ```ts
  export async function getInscriptionDetails(id: number): Promise<InscriptionDetails> {
    const { data } = await gestaoClient.get<InscriptionDetails>(`/inscription/${id}`);
    return data;
  }
  ```
- Quando a resposta vem de fonte sensível ou pouco confiável, valide com Zod:
  ```ts
  return InscriptionGradesResponseSchema.parse(data);
  ```
- Services exportam **funções puras** — sem React, sem hooks, sem `useQuery`. Hooks ficam nos componentes.

---

## 6. Tipos de domínio (`src/types/domains/`)

- **Um arquivo por domínio** (`auth.ts`, `inscription.ts`, `user.ts`, ...).
- **Padrão:** declare um Zod schema e derive o tipo TS:
  ```ts
  export const userDataSchema = z.object({ ... });
  export type UserDataType = z.infer<typeof userDataSchema>;
  ```
- Schemas de **request/response do backend** e schemas de **forms** ficam no mesmo arquivo de domínio quando se referem ao mesmo agregado (ex.: `InscriptionStep1Schema` mora em `inscription.ts`).
- Mensagens de erro de Zod são em **pt-BR**, voltadas ao usuário final.
- Validações específicas de Brasil (CPF, telefone, CEP) usam `js-brasil` via `validateBr.cpf(...)` ou helpers em `src/helpers/masks.ts`.
- **Não usar `any`.** Em casos pontuais inevitáveis, prefira `unknown` + narrowing.

---

## 7. Data fetching (TanStack Query)

- `QueryClient` é instanciado em `src/providers/QueryClientProvider.tsx` com `staleTime: 5min`, `refetchOnWindowFocus: false`, `retry: 1`.
- **Listagem/leitura** → `useQuery`. **Mutações** (POST/PUT/PATCH/DELETE) → `useMutation`.
- **Query keys:** array começando pelo domínio + identificadores. Exemplos do código:
  - `["user-status", session?.user?.id]`
  - `["inscriptions-grouped-by-year", session?.user?.id]`
  - `["documents"]`
- Use `enabled: !!session?.user?.id` (ou outra dependência) para evitar disparos prematuros.
- **Toda mutation deve ter `onSuccess` e `onError`** com toast (sonner). Padrão atual:
  ```ts
  const updateMutation = useMutation({
    mutationFn: async (data: UserDataType) => { /* call service */ },
    onSuccess: () => toast.success("Dados atualizados com sucesso!"),
    onError: () => toast.error("Erro ao atualizar dados. Tente novamente."),
  });
  ```
- Para erros HTTP padronizados use `handlerHttpError(error)` de `helpers/toast.ts`.
- Após mutações que afetam dados em cache, invalide com `queryClient.invalidateQueries({ queryKey: [...] })`.

---

## 8. Forms

- **Sempre** `react-hook-form` + `zodResolver(<schema>)`. O schema vem de `types/domains/<dominio>.ts`.
- Padrão (ver `app/(protected)/perfil/_components/organisms/EditUserDataModal.tsx`):
  ```ts
  const { register, handleSubmit, formState: { errors, isValid }, setValue, watch, trigger, reset } =
    useForm<UserDataType>({
      resolver: zodResolver(userDataSchema),
      defaultValues: { ... },
      mode: "onChange",
    });
  ```
- Submit dispara uma `useMutation`: `handleSubmit((data) => mutation.mutate(data))`.
- Inputs do projeto (`@/components/customized/Input`, `PasswordInput`, `DatePicker`, etc.) já recebem `error` e `errorMessage` — passe `errors.<campo>?.message`.
- Máscaras (CPF, telefone, CEP): use helpers de `helpers/masks.ts` em `onChange` + `setValue` + `trigger`.
- Antes de enviar ao backend, normalize: remova máscara (`replace(/\D/g, "")`), formate datas com `dayjs(date).format("YYYY-MM-DD")`.

---

## 9. Componentes UI

- **shadcn primitives** vivem em `src/components/ui/`. Foram instalados via CLI shadcn (style "new-york", `iconLibrary: lucide`). **Não edite** esses arquivos manualmente; reinstale/atualize via CLI shadcn quando precisar.
- **Wrappers/customizações do projeto** vão em `src/components/customized/` (Input, Select, DatePicker, DataTable, StandardForm, ConfirmationModal, etc.). Use-os em vez dos primitives quando existirem — eles padronizam label/erro/máscara.
- Para confirmações use o hook **`useConfirmation`** (`src/hooks/useConfirmation.ts`) acoplado ao `LayoutProvider` global, em vez de criar `Dialog`s ad-hoc.
- Estilização: **somente Tailwind** + `cn()` de `@/helpers/cn` (clsx + tailwind-merge). Não introduzir CSS modules ou styled-components fora dos imports já existentes.
- Ícones: **lucide-react** (por convenção shadcn). `react-icons` existe mas evite expandir seu uso.

---

## 10. Providers globais

`src/app/layout.tsx` (root) → `QueryClientProvider` → `NextSessionProvider` → `<Toaster />` (sonner, top-center, richColors).

`(protected)/layout.tsx` → `LayoutProvider` → `AuthProvider` → `SidebarProvider` → conteúdo.

Ao adicionar provider novo, prefira pluga-lo em um desses pontos em vez de espalhar.

---

## 11. Helpers

- `helpers/cn.ts` — merge de classes Tailwind.
- `helpers/masks.ts` — `applyCpfMask`, `applyPhoneMask`, `applyCepMask`, etc.
- `helpers/formatters.ts` / `helpers/converters.ts` / `helpers/general.ts` — utilidades genéricas (formatFileSize, etc.).
- `helpers/toast.ts` — `handlerHttpError` para erros HTTP padronizados.
- Datas: **`dayjs`** é o padrão. `date-fns` está disponível mas não introduza nova dependência sem motivo.

---

## 12. Server vs. Client Components

- O App Router renderiza Server Components **por padrão**. **Use SSR/Server Components sempre que possível** — arquivos sem hooks, eventos, ou APIs do browser não precisam de `"use client"`.
- Componentes que usam `useState`, `useEffect`, `useForm`, `useQuery`, `useSession`, `useRouter` (de `next/navigation`), browser APIs ou Context **devem** começar com `"use client"`.
- Layouts/pages que dependem de sessão NextAuth no cliente são `"use client"` (padrão atual). Para dados públicos prefira buscar no Server Component e passar como props.
- Não importe módulos client-only (ex.: `next-auth/react`) em Server Components.

---

## 13. Comandos úteis

| Comando | Descrição |
| --- | --- |
| `yarn dev` | Dev server (porta 3000 por padrão; `NEXTAUTH_URL` aponta para 8000 em local conforme `.env.example`). |
| `yarn build` | Build de produção. |
| `yarn lint` | ESLint (`eslint-config-next`). Resolva warnings antes de abrir PR. |
| `npx shadcn@latest add <componente>` | Adicionar/atualizar primitive shadcn. |

Variáveis de ambiente (`.env.example`):
- `NEXT_PUBLIC_API` — base URL da API (HttpClient prefixa com `/portal` ou `/gestao`).
- `NEXTAUTH_URL`, `NEXTAUTH_SECRET` — NextAuth.

---

## 14. Convenções de código (resumo)

- **TypeScript estrito.** `any` proibido — use `unknown` + narrowing ou genéricos.
- **Imports relativos curtos.** Use `@/...` em vez de `../../../`.
- **Mensagens ao usuário em pt-BR**, código (nomes/comentários) preferencialmente em inglês — siga o que já existe no arquivo.
- **Nomes:**
  - Componentes: `PascalCase` em arquivo `PascalCase.tsx`.
  - Hooks: `useXxx` em `useXxx.ts`.
  - Services: `<verbo><Recurso>` exportado de `<dominio>Service.ts` (ou um arquivo por operação, como em `auth/`).
  - Schemas Zod: `<algo>Schema`. Tipo derivado: `<Algo>Type` (ou nome semântico — siga o domínio existente).
- **Sem comentários óbvios.** Comente apenas o "porquê" não trivial.
- **Não criar arquivos `.md`** sem pedido explícito do usuário.

---

## 15. Para tarefas comuns

### Adicionar uma nova página protegida
1. Crie `src/app/(protected)/<rota>/page.tsx` (`"use client"` se usar hooks).
2. Componentes locais em `src/app/(protected)/<rota>/_components/{atoms,molecules,organisms,pages}/`.
3. Schema + tipos em `src/types/domains/<dominio>.ts`.
4. Funções de API em `src/core/services/<dominio>/<recurso>Service.ts` usando `gestaoClient`/`portalClient`.
5. Listagem com `useQuery`; ações com `useMutation` + toasts.

### Adicionar um campo a um form
1. Estenda o schema Zod em `types/domains/<dominio>.ts` (mantém validação + tipo TS coerentes).
2. Adicione o controle (`<Input />` customizado etc.) referenciando `errors.<campo>?.message`.
3. Se o campo precisa de máscara, use helpers em `helpers/masks.ts`.

### Trocar de mock para integração real
1. Crie/ajuste a função em `core/services/...` retornando o tipo de domínio.
2. Substitua o consumo no componente por `useQuery`/`useMutation`.
3. Garanta `enabled` apropriado e tratamento de loading/erro.

---

## 16. Não faça

- ❌ Não chame `fetch` ou `axios` fora de `src/core/`.
- ❌ Não duplique tipos que já existem em `types/domains/`.
- ❌ Não crie um schema "form-only" se já há um schema de domínio reutilizável.
- ❌ Não edite `src/components/ui/*` à mão (são gerados pelo CLI shadcn).
- ❌ Não adicione `any`. Não desabilite regras de ESLint sem comentário justificando.
- ❌ Não crie route groups novos sem alinhamento — `(protected)`/`(unprotected)` cobrem o que existe hoje.
- ❌ Não introduza state managers novos (Redux/Zustand). O projeto usa React Context + TanStack Query.
- ❌ Não armazene tokens em `localStorage` — o accessToken vive em cookie, lido pelo HttpClient.
