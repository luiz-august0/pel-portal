# Forms

## Mandatory pattern

Every form uses **`useForm` + `zodResolver`** with a Zod schema from `src/types/domains/<domain>.ts`.

```ts
const {
  register,
  handleSubmit,
  formState: { errors, isValid },
  setValue,
  watch,
  trigger,
  reset,
} = useForm<UserDataType>({
  resolver: zodResolver(userDataSchema),
  defaultValues: { name: "", email: "", ... },
  mode: "onChange",
});

const mutation = useMutation({
  mutationFn: (data: UserDataType) => updateUser(data),
  onSuccess: () => { toast.success(...); onClose(); },
  onError: () => toast.error(...),
});

return (
  <form onSubmit={handleSubmit((data) => mutation.mutate(data))}>...</form>
);
```

## Rules

- **Schema first.** If a schema doesn't exist for the form's shape, add it to the domain file before writing JSX.
- Reuse existing domain schemas when the form maps 1:1 to a domain entity. Compose with `.extend()` / `.merge()` when adding fields (e.g. password confirmation).
- Use the project's customized form controls (`Input`, `PasswordInput`, `DatePicker`, `Select`, `RadioButton`, `Switch` from `components/customized/`). They already accept `error` and `errorMessage` props compatible with `errors.<field>?.message`.
- Disable the submit button on `mutation.isPending` and (optionally) `!isValid`.
- Reset the form via `reset(defaultValues)` when reopening modals so stale values don't leak between sessions.
- For controlled fields with masks (CPF, phone, CEP):
  ```ts
  const handleCpfChange = (e) => {
    setValue("cpf", applyCpfMask(e.target.value));
    trigger("cpf");
  };
  ```
- Normalize before sending to the backend: strip masks (`replace(/\D/g, "")`), format dates (`dayjs(...).format("YYYY-MM-DD")`).

## Multi-step forms

- One Zod schema per step (`Step1Schema`, `Step2Schema`, ...) in the same domain file.
- Each step is its own organism component; `page.tsx` owns the step state and the cumulative form data.
- Validate per step with `trigger()` before allowing "Next".

## Don't

- ❌ Don't write ad-hoc `useState` validation — always Zod.
- ❌ Don't disable Zod errors with empty `.refine()` to "make the form pass" — fix the data instead.
- ❌ Don't call services directly from `onSubmit`; wrap them in `useMutation` so loading/error states are first-class.
- ❌ Don't keep form schemas inside the component file — promote to `types/domains/`.
