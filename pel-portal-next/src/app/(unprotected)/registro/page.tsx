"use client";

import { useState, useEffect, Suspense } from "react";
import { useMutation } from "@tanstack/react-query";
import { toast } from "sonner";
import { useRouter, useSearchParams } from "next/navigation";
import { BackButton } from "@/components/customized/BackButton";

import { PersonalDataForm } from "./_components/organisms/PersonalDataForm";
import { SpecialNeedsForm } from "./_components/organisms/SpecialNeedsForm";
import { ProgramKnowledgeForm } from "./_components/organisms/ProgramKnowledgeForm";
import { PasswordForm } from "./_components/organisms/PasswordForm";

import {
  PersonalDataType,
  SpecialNeedsType,
  ProgramKnowledgeType,
  PasswordType,
  RegisterType,
} from "@/types/domains/register";
import dayjs from "dayjs";
import { signIn } from "next-auth/react";
import { HttpError } from "@/core/http/httpError";
import { handlerHttpError } from "@/helpers/toast";
import { register } from "@/core/services/register/registerService";

function RegisterForm() {
  const router = useRouter();
  const searchParams = useSearchParams();
  const [currentStep, setCurrentStep] = useState(1);
  const [authorizedToken, setAuthorizedToken] = useState<string | null>(null);
  const [formData, setFormData] = useState<{
    personalData?: PersonalDataType;
    specialNeeds?: SpecialNeedsType;
    programKnowledge?: ProgramKnowledgeType;
    password?: PasswordType;
  }>({});

  // Verifica se há token de autorização na URL
  useEffect(() => {
    const token = searchParams.get("token");
    if (token) {
      setAuthorizedToken(token);
    }
  }, [searchParams]);

  const registerMutation = useMutation({
    mutationFn: async (data: RegisterType) => {
      await register(data)
        .then(async () => {
          const result = await signIn("credentials", {
            cpf: data.cpf.replace(/\D/g, ""),
            password: data.password,
            redirect: false,
          });

          if (result?.error) {
            const errorData = JSON.parse(result.error);
            throw new Error(errorData?.message || "Erro ao fazer login");
          }

          if (!result?.ok) {
            throw new Error("Erro ao fazer login");
          }

          toast.success("Cadastro realizado com sucesso!");
        })
        .catch((error: HttpError | Error) => {
          handlerHttpError(error);
        });
    },
  });

  const handlePersonalDataNext = (data: PersonalDataType) => {
    setFormData((prev) => ({ ...prev, personalData: data }));
    setCurrentStep(2);
  };

  const handleSpecialNeedsNext = (data: SpecialNeedsType) => {
    setFormData((prev) => ({ ...prev, specialNeeds: data }));
    // Se tem token de autorização, pula o step 3 (ProgramKnowledge)
    setCurrentStep(authorizedToken ? 4 : 3);
  };

  const handleProgramKnowledgeNext = (data: ProgramKnowledgeType) => {
    setFormData((prev) => ({ ...prev, programKnowledge: data }));
    setCurrentStep(4);
  };

  const handlePasswordNext = async (data: PasswordType) => {
    setFormData((prev) => ({ ...prev, password: data }));

    // Monta o payload final para envio
    const payload: RegisterType = {
      name: formData.personalData!.name,
      email: formData.personalData!.email,
      password: data.password,
      birthDate: dayjs(formData.personalData!.birthDate).format("YYYY-MM-DD"),
      phone: formData.personalData!.phone?.replace(/\D/g, ""),
      cpf: formData.personalData!.cpf.replace(/\D/g, ""),
      specialNeeds: formData.specialNeeds!.specialNeeds,
      // Se tem token, não precisa dos dados de programKnowledge
      programKnowledgeSource: authorizedToken
        ? ("OUTRO" as any)
        : formData.programKnowledge!.programKnowledgeSource,
      programKnowledgeSourceOther: authorizedToken
        ? "Autorizado via token"
        : formData.programKnowledge!.programKnowledgeSourceOther,
      authorizedToken: authorizedToken || undefined,
    };

    registerMutation.mutate(payload);
  };

  const handleBack = () => {
    if (currentStep > 1) {
      // Se tem token e está no step 4 (senha), volta para step 2 (necessidades especiais)
      if (authorizedToken && currentStep === 4) {
        setCurrentStep(2);
      } else {
        setCurrentStep(currentStep - 1);
      }
    }
  };

  const handleGoBack = () => {
    if (currentStep === 1) {
      router.push(`/onboarding?${searchParams.toString()}`);
    } else {
      handleBack();
    }
  };

  const getProgressPercentage = () => {
    if (authorizedToken) {
      // Com token: steps 1, 2, 4 (3 steps efetivos)
      const stepMapping = { 1: 1, 2: 2, 4: 3 };
      const effectiveStep =
        stepMapping[currentStep as keyof typeof stepMapping] || currentStep;
      return (effectiveStep / 3) * 100;
    } else {
      // Sem token: steps 1, 2, 3, 4 (4 steps)
      return (currentStep / 4) * 100;
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center p-4">
      <div className="bg-white rounded-lg shadow-lg max-w-sm w-full p-8">
        {/* Header */}
        <div className="flex items-center justify-between mb-6">
          <BackButton onClick={handleGoBack} />
        </div>

        {/* Progress Bar */}
        <div className="mb-6">
          <div className="w-full bg-gray-200 rounded-full h-2">
            <div
              className="bg-primary h-2 rounded-full transition-all duration-300"
              style={{ width: `${getProgressPercentage()}%` }}
            />
          </div>
        </div>

        {/* Content */}
        <div>
          {currentStep === 1 && (
            <PersonalDataForm
              onNext={handlePersonalDataNext}
              initialData={formData.personalData}
            />
          )}

          {currentStep === 2 && (
            <SpecialNeedsForm
              onNext={handleSpecialNeedsNext}
              onBack={handleBack}
              initialData={formData.specialNeeds}
            />
          )}

          {currentStep === 3 && (
            <ProgramKnowledgeForm
              onNext={handleProgramKnowledgeNext}
              onBack={handleBack}
              initialData={formData.programKnowledge}
            />
          )}

          {currentStep === 4 && (
            <PasswordForm
              onNext={handlePasswordNext}
              onBack={handleBack}
              initialData={formData.password}
              isLoading={registerMutation.isPending}
            />
          )}
        </div>
      </div>
    </div>
  );
}

export default function RegisterPage() {
  return (
    <Suspense
      fallback={
        <div className="min-h-screen flex items-center justify-center p-4">
          <div className="bg-white rounded-lg shadow-lg max-w-sm w-full p-8 text-center">
            Carregando...
          </div>
        </div>
      }
    >
      <RegisterForm />
    </Suspense>
  );
}
