"use client";

import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { toast } from "sonner";
import { useRouter, useParams } from "next/navigation";
import { BackButton } from "@/components/customized/BackButton";
import { useMutation } from "@tanstack/react-query";

import { Button } from "@/components/ui/button";
import PasswordInput from "@/components/customized/PasswordInput";

import { PasswordResetType, passwordResetSchema } from "@/types/domains/auth";
import { resetPassword } from "@/core/services/auth/recoveryService";

export default function PasswordResetPage() {
  const router = useRouter();
  const params = useParams();
  const token = params.token as string;

  const {
    register,
    handleSubmit,
    formState: { errors, isValid },
    watch
  } = useForm<PasswordResetType>({
    resolver: zodResolver(passwordResetSchema),
    defaultValues: {
      token,
      password: "",
      confirmPassword: ""
    },
    mode: "onChange"
  });

  const resetMutation = useMutation({
    mutationFn: resetPassword,
    onSuccess: () => {
      toast.success("Senha redefinida com sucesso!");
      router.push("/login");
    },
    onError: (error: Error) => {
      toast.error(error.message || "Erro ao redefinir senha");
    },
  });

  const onSubmit = (data: PasswordResetType) => {
    resetMutation.mutate({
      token,
      password: data.password
    });
  };

  const handleGoBack = () => {
    router.push("/login");
  };

  const password = watch("password");
  const confirmPassword = watch("confirmPassword");

  return (
    <div className="min-h-screen flex items-center justify-center p-4">
      <div className="bg-white rounded-lg shadow-lg max-w-md w-full p-6">
        {/* Header */}
        <div className="flex items-center justify-between mb-6">
          <BackButton onClick={handleGoBack} />
        </div>

        {/* Título */}
        <h1 className="text-2xl font-bold text-center mb-8 text-gray-900">
          Agora, defina uma nova senha
        </h1>

        {/* Formulário */}
        <form onSubmit={handleSubmit(onSubmit)} className="space-y-6">
          {/* Senha */}
          <PasswordInput
            id="password"
            label="Senha"
            showValidation={true}
            value={password}
            {...register("password")}
          />

          {/* Confirmação de senha */}
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

          {/* Botão de envio */}
          <Button
            type="submit"
            disabled={resetMutation.isPending || !isValid}
            className="w-full bg-primary text-white py-3 px-4 rounded-lg font-medium hover:bg-primary/90 focus:ring-2 focus:ring-primary focus:ring-offset-2 transition-colors disabled:opacity-50 cursor-pointer"
          >
            {resetMutation.isPending ? "Redefinindo..." : "Redefinir senha"}
          </Button>
        </form>
      </div>
    </div>
  );
}
