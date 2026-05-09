"use client";

import { Suspense } from "react";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { toast } from "sonner";
import { useRouter, useSearchParams } from "next/navigation";
import { BackButton } from "@/components/customized/BackButton";
import Link from "next/link";
import { signIn } from "next-auth/react";
import { useMutation } from "@tanstack/react-query";

import { Button } from "@/components/ui/button";
import Input from "@/components/customized/Input";
import PasswordInput from "@/components/customized/PasswordInput";

import { LoginType, loginSchema } from "@/types/domains/auth";
import { applyCpfMask } from "@/helpers/masks";

function LoginForm() {
  const router = useRouter();
  const searchParams = useSearchParams();
  const storedCpf = localStorage.getItem('cpf');

  const {
    register,
    handleSubmit,
    formState: { errors },
    setValue,
    watch
  } = useForm<LoginType>({
    resolver: zodResolver(loginSchema),
    defaultValues: {
      cpf: storedCpf ? applyCpfMask(storedCpf) : "",
      password: ""
    }
  });

  const loginMutation = useMutation({
    mutationFn: async (data: LoginType) => {
      const result = await signIn("credentials", {
        cpf: data.cpf.replace(/\D/g, ''),
        password: data.password,
        authorizedToken: searchParams.get('token') ?? "",
        redirect: false,
      });
      if (result?.error) {
        const errorData = JSON.parse(result.error);
        throw new Error(errorData?.message || "Erro ao fazer login");
      }

      if (!result?.ok) {
        throw new Error("Erro ao fazer login");
      }
      localStorage.setItem('cpf', data.cpf.replace(/\D/g, ''));
      return result;
    },
    onSuccess: () => {
      toast.success("Login realizado com sucesso!");
    },
    onError: (error: Error) => {
      toast.error(error.message || "Erro inesperado ao fazer login");
    },
  });

  const onSubmit = (data: LoginType) => {
    loginMutation.mutate(data);
  };

  const handleGoBack = () => {
    router.push(`/onboarding?${searchParams.toString()}`);
  };

  const handleCpfChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const maskedValue = applyCpfMask(e.target.value);
    setValue('cpf', maskedValue);
  };

  return (
    <div className="min-h-screen flex items-center justify-center p-4">
      <div className="bg-white rounded-lg shadow-lg max-w-md w-full p-6">
        {/* Header */}
        <div className="flex items-center justify-between mb-6">
          <BackButton onClick={handleGoBack} />
        </div>

        {/* Título */}
        <h1 className="text-2xl font-bold text-center mb-8 text-gray-900">
          Login
        </h1>

        {/* Formulário */}
        <form onSubmit={handleSubmit(onSubmit)} className="space-y-6">
          {/* CPF */}
          <Input
            id="cpf"
            label="CPF"
            placeholder="000.000.000-00"
            value={watch("cpf") || ''}
            onChange={handleCpfChange}
            error={!!errors.cpf}
            errorMessage={errors.cpf?.message}
          />

          {/* Senha */}
          <div>
            <div className="flex justify-between items-center mb-2">
              <span className="text-sm font-medium text-foreground">Senha</span>
              <Link
                href="/login/solicitar-recuperacao"
                className="text-sm text-blue-600 hover:text-blue-800"
              >
                Esqueci minha senha
              </Link>
            </div>
            <PasswordInput
              id="password"
              error={!!errors.password}
              errorMessage={errors.password?.message}
              value={watch("password")}
              {...register("password")}
            />
          </div>

          {/* Botão de envio */}
          <Button
            type="submit"
            disabled={loginMutation.isPending}
            className="w-full bg-primary text-white py-3 px-4 rounded-lg font-medium hover:bg-primary/90 focus:ring-2 focus:ring-primary focus:ring-offset-2 transition-colors disabled:opacity-50"
          >
            {loginMutation.isPending ? "Acessando..." : "Acessar"}
          </Button>
        </form>
      </div>
    </div>
  );
}

export default function LoginPage() {
  return (
    <Suspense fallback={<div className="min-h-screen flex items-center justify-center p-4"><div className="bg-white rounded-lg shadow-lg max-w-md w-full p-6 text-center">Carregando...</div></div>}>
      <LoginForm />
    </Suspense>
  );
}
