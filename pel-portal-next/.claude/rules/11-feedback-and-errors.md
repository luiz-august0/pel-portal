# User feedback and error handling

## Toasts (sonner)

- A single `<Toaster richColors position="top-center" />` is mounted once in the root layout.
- Use `toast.success(...)`, `toast.error(...)`, `toast.warning(...)`, `toast.info(...)`. Don't render custom alert components for transient feedback.
- Messages are **user-facing** — write them in the project's UI language and avoid technical jargon ("Erro 500" → "Não foi possível concluir a operação. Tente novamente.").

## Mutation feedback

Every mutation must surface result feedback:

```ts
useMutation({
  mutationFn: ...,
  onSuccess: () => toast.success("Operação realizada com sucesso!"),
  onError: (error) => handlerHttpError(error), // or a custom message
});
```

- `handlerHttpError` (from `helpers/toast.ts`) maps `HttpError`/standardized API errors to the right toast severity (e.g. `401` → warning, others → error) and reads `response.data.message` when available.

## Loading states

- Components that consume `useQuery` must render a meaningful loading state (skeleton, spinner, placeholder text). Don't render an empty page while data is loading.
- Use `Skeleton` from `components/ui/skeleton.tsx` (or the customized variant) to keep loading UI consistent.
- Disable submit buttons on `mutation.isPending`. Show "Salvando…" / "Carregando…" labels rather than dual-state spinners when possible.

## Confirmation flows

- For destructive or irreversible actions, use the project's `useConfirmation` hook tied to `LayoutProvider` instead of building ad-hoc `<Dialog>`s. This keeps copy and styling consistent.

## Empty states

- A list query with `data?.length === 0` should render an empty-state UI explaining the absence and (when applicable) offering an action ("Cadastrar primeiro item").

## Don't

- ❌ Don't `console.log` errors instead of toasting/displaying them.
- ❌ Don't swallow errors in `try/catch` without surfacing feedback to the user.
- ❌ Don't `alert()` or `confirm()` — use the toast and confirmation patterns.
