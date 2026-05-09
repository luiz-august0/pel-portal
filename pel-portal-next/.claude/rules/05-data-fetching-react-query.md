# Data fetching (TanStack Query)

## When to use what

- **Listings, details, searches, any GET** → `useQuery`.
- **Any write to the backend** (POST/PUT/PATCH/DELETE) → `useMutation`.
- Don't fetch in `useEffect` + `useState`. Don't store server data in Context.

## `useQuery`

```ts
const { data, isLoading, isError } = useQuery({
  queryKey: ["inscriptions-grouped-by-year", session?.user?.id],
  queryFn: getInscriptionsGroupedByYear,
  enabled: !!session?.user?.id,
});
```

- **Query keys** are arrays starting with the domain/feature name + identifying params.
  - ✅ `["user-status", userId]`
  - ✅ `["inscription", inscriptionId]`
  - ❌ `["data"]` / `["fetch"]` — too generic.
- Use `enabled` to gate queries on dependencies (session ready, route param present).
- `queryFn` should call a function from `src/core/services/...`, not inline `fetch`.
- Don't catch errors inside `queryFn` — let them propagate so React Query reports `isError`.

## `useMutation`

```ts
const updateMutation = useMutation({
  mutationFn: (data: UserDataType) => updateUser(data),
  onSuccess: () => {
    toast.success("Dados atualizados com sucesso!");
    queryClient.invalidateQueries({ queryKey: ["user-status"] });
  },
  onError: (error) => {
    toast.error("Erro ao atualizar dados. Tente novamente.");
  },
});
```

- **Always provide `onSuccess` and `onError`** with a user-visible toast.
- For standardized HTTP error toasts use the project helper (e.g. `handlerHttpError`).
- After a successful mutation, invalidate or update the affected queries (`queryClient.invalidateQueries({ queryKey: [...] })`).
- Submit forms by calling `mutation.mutate(data)` from `handleSubmit`.

## QueryClient configuration

- One `QueryClient` instance per app, mounted in a top-level provider with sane defaults:
  ```ts
  defaultOptions: {
    queries: { staleTime: 1000 * 60 * 5, refetchOnWindowFocus: false, retry: 1 },
  }
  ```
- Don't recreate the client on every render — wrap with `useState(() => new QueryClient(...))`.

## Don't

- ❌ Don't mix React Query with manual `useEffect` data loading for the same resource.
- ❌ Don't pass mutable objects (component state) into `queryKey` without `JSON.stringify`-stable shape.
- ❌ Don't hide loading states — components must render skeletons or fallback UI while `isLoading`.
