"use client";

import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { PasswordType, passwordSchema } from "@/types/domains/register";
import { Button } from "@/components/ui/button";
import PasswordInput from "@/components/customized/PasswordInput";

interface PasswordFormProps {
  onNext: (data: PasswordType) => void;
  onBack: () => void;
  initialData?: Partial<PasswordType>;
  isLoading?: boolean;
}

export function PasswordForm({ onNext, onBack, initialData, isLoading }: PasswordFormProps) {
  const {
    register,
    handleSubmit,
    formState: { errors, isValid },
    watch
  } = useForm<PasswordType>({
    resolver: zodResolver(passwordSchema),
    defaultValues: initialData,
    mode: "onChange"
  });

  const password = watch("password");
  const confirmPassword = watch("confirmPassword");

  const onSubmit = (data: PasswordType) => {
    onNext(data);
  };

  return (
    <div className="space-y-8">
      <div className="text-center">
        <h1 className="text-xl font-semibold text-gray-800 mb-8">
          Agora, é só criar sua senha
        </h1>
      </div>

      <form onSubmit={handleSubmit(onSubmit)} className="space-y-6">
        <PasswordInput
          id="password"
          label="Senha"
          showValidation={true}
          value={password}
          {...register("password")}
        />

        <PasswordInput
          id="confirmPassword"
          label="Confirmação de senha"
          value={confirmPassword}
          error={!!errors.confirmPassword || (!!confirmPassword && password !== confirmPassword)}
          errorMessage={
            errors.confirmPassword?.message ||
            (confirmPassword && password !== confirmPassword && !errors.confirmPassword
              ? "As senhas não coincidem"
              : undefined)
          }
          {...register("confirmPassword")}
        />

        <div className="flex gap-4 pt-4">
          <Button
            type="button"
            variant="outline"
            onClick={onBack}
            disabled={isLoading}
            className="flex-1 py-3 px-6 rounded-lg transition-colors"
          >
            Voltar
          </Button>
          <Button
            type="submit"
            disabled={!isValid || isLoading}
            className="flex-1 bg-primary hover:bg-primary/90 text-white font-medium py-3 px-6 rounded-lg transition-colors"
          >
            {isLoading ? "Cadastrando..." : "Concluir"}
          </Button>
        </div>
      </form>
    </div>
  );
}
