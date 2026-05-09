"use client";

import { useState } from "react";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { toast } from "sonner";
import { useRouter } from "next/navigation";
import { Mail } from "lucide-react";
import { BackButton } from "@/components/customized/BackButton";
import { useMutation } from "@tanstack/react-query";

import { Button } from "@/components/ui/button";
import Input from "@/components/customized/Input";

import { PasswordRecoveryRequestType, passwordRecoveryRequestSchema } from "@/types/domains/auth";
import { requestPasswordRecovery } from "@/core/services/auth/recoveryService";
import { applyCpfMask } from "@/helpers/masks";
import { HttpError } from "@/core/http/httpError";

export default function PasswordRecoveryRequestPage() {
  const router = useRouter();
  const [isSuccess, setIsSuccess] = useState(false);
  const [countdown, setCountdown] = useState(0);

  const {
    handleSubmit,
    formState: { errors },
    setValue,
    watch,
    getValues
  } = useForm<PasswordRecoveryRequestType>({
    resolver: zodResolver(passwordRecoveryRequestSchema),
    defaultValues: {
      cpf: ""
    }
  });

  const recoveryMutation = useMutation({
    mutationFn: requestPasswordRecovery,
    onSuccess: () => {
      setIsSuccess(true);
      setCountdown(30);
      toast.success("Instruções enviadas para seu email!");
      
      // Inicia o countdown
      const timer = setInterval(() => {
        setCountdown((prev) => {
          if (prev <= 1) {
            clearInterval(timer);
            return 0;
          }
          return prev - 1;
        });
      }, 1000);
    },
    onError: (error: HttpError | Error) => {
      console.log(error)
      toast.error(error instanceof HttpError ? error.message : error.message || "Erro ao solicitar recuperação de senha");
    },
  });

  const onSubmit = (data: PasswordRecoveryRequestType) => {
    recoveryMutation.mutate({
      cpf: data.cpf.replace(/\D/g, '')
    });
  };

  const handleGoBack = () => {
    router.push("/login");
  };

  const handleResend = () => {
    const currentData = getValues();
    recoveryMutation.mutate({
      cpf: currentData.cpf.replace(/\D/g, '')
    });
  };

  const handleCpfChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const maskedValue = applyCpfMask(e.target.value);
    setValue('cpf', maskedValue);
  };

  if (isSuccess) {
    return (
      <div className="min-h-screen flex items-center justify-center p-4">
        <div className="bg-white rounded-lg shadow-lg max-w-md w-full p-6">
          {/* Header */}
          <div className="flex items-center mb-8">
            <BackButton onClick={handleGoBack} size="lg" className="mr-4" />
          </div>

          {/* Título */}
          <h1 className="text-2xl font-bold text-center mb-8 text-gray-900">
            Recuperação de senha
          </h1>

          {/* CPF (readonly)*/}
          <Input
            id="cpf"
            label="CPF"
            value={watch("cpf")}
            className="mb-6 bg-gray-50"
            readOnly
          />

          {/* Mensagem de sucesso */}
          <div className="bg-green-50 border border-green-200 rounded-lg p-4 mb-6">
            <div className="flex items-start">
              <Mail className="w-5 h-5 text-green-500 mt-0.5 mr-3 flex-shrink-0" />
              <div>
                <h3 className="text-sm font-medium text-green-800 mb-1">Instruções enviadas!</h3>
                <p className="text-sm text-green-700">Verifique seu email e clique no link enviado.</p>
              </div>
            </div>
          </div>

          {/* Botão de reenvio */}
          <div className="mb-6">
            <Button
              onClick={handleResend}
              disabled={countdown > 0 || recoveryMutation.isPending}
              className={`w-full py-3 px-4 rounded-lg font-medium transition-colors ${
                countdown > 0 || recoveryMutation.isPending
                  ? "bg-gray-100 text-gray-500 cursor-not-allowed hover:bg-gray-100"
                  : "bg-primary/10 text-primary hover:bg-primary/20 cursor-pointer"
              }`}
            >
              {countdown > 0 
                ? `Aguarde para reenviar ${countdown}s`
                : recoveryMutation.isPending
                ? "Reenviando..."
                : "Reenviar instruções"
              }
            </Button>
          </div>

          {/* Botão principal */}
          <Button
            onClick={() => router.push("/login")}
            className="w-full bg-primary text-white py-3 px-4 rounded-lg font-medium hover:bg-primary/90 focus:ring-2 focus:ring-primary focus:ring-offset-2 transition-colors cursor-pointer"
          >
            Voltar ao login
          </Button>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen flex items-center justify-center p-4">
      <div className="bg-white rounded-lg shadow-lg max-w-md w-full p-6">
        {/* Header */}
        <div className="flex items-center justify-between mb-6">
          <BackButton onClick={handleGoBack} />
        </div>

        {/* Título */}
        <h1 className="text-2xl font-bold text-center mb-8 text-gray-900">
          Recuperação de senha
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

          {/* Botão de envio */}
          <Button
            type="submit"
            disabled={recoveryMutation.isPending}
            className="w-full bg-primary text-white py-3 px-4 rounded-lg font-medium hover:bg-primary/90 focus:ring-2 focus:ring-primary focus:ring-offset-2 transition-colors disabled:opacity-50 cursor-pointer"
          >
            {recoveryMutation.isPending ? "Enviando..." : "Receber instruções"}
          </Button>
        </form>
      </div>
    </div>
  );
}
