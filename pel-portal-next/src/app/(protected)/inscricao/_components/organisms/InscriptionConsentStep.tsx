"use client";

import { useForm, UseFormReturn } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { useQuery } from "@tanstack/react-query";

import { Button } from "@/components/ui/button";
import { Card, CardContent } from "@/components/ui/card";
import RadioButton from "@/components/customized/RadioButton";

import {
  InscriptionStep3Schema,
  type InscriptionStep3,
} from "@/types/domains/inscription";
import { getImageTerm } from "@/core/services/inscription/inscriptionService";

type InscriptionConsentStepProps = {
  formConsent: UseFormReturn<InscriptionStep3>;
  inscriptionId: number;
  contract: string;
  onNext: () => void;
};

export function InscriptionConsentStep({
  formConsent,
  inscriptionId,
  contract,
  onNext,
}: InscriptionConsentStepProps) {
  const {
    watch,
    setValue,
    handleSubmit,
    formState: { isValid },
  } = formConsent;
  const acceptContract = watch("acceptContract");
  const acceptImageTerm = watch("acceptImageTerm");

  // Query para buscar termo de autorização de uso de imagem
  const { data: imageTerm, isLoading: isLoadingImageTerm } = useQuery({
    queryKey: ["image-term", inscriptionId],
    queryFn: () => getImageTerm(inscriptionId),
    enabled: !!inscriptionId,
  });

  const onSubmit = () => {
    onNext();
  };

  const handleImageTermAcceptance = (value: string) => {
    const accepted = value === "true";
    setValue("acceptImageTerm", accepted, { shouldValidate: true });
  };

  if (isLoadingImageTerm) {
    return (
      <div className="flex flex-col items-center justify-center py-8">
        <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-primary mx-auto"></div>
        <p className="text-gray-600 mt-2">Carregando termos...</p>
      </div>
    );
  }

  return (
    <div className="space-y-6">
      <div className="text-center">
        <p className="text-medium">
          Leia e aceite o contrato para prosseguir.
        </p>
        <p className="text-sm text-gray-500 mt-1">
          A autorização de uso de imagem é opcional.
        </p>
      </div>

      <form onSubmit={handleSubmit(onSubmit)} className="space-y-6">
        {/* Contrato de Prestação de Serviços */}
        <div className="space-y-4">
          <h3 className="text-sm font-medium">
            Contrato de Prestação de Serviços
          </h3>

          <Card className="bg-pink-50 border-pink-200">
            <CardContent className="p-4">
              <div
                className="prose prose-sm max-w-none text-sm text-gray-700 max-h-64 overflow-y-auto"
                dangerouslySetInnerHTML={{ __html: contract }}
              />
            </CardContent>
          </Card>

          <div className="flex items-center space-x-2">
            <input
              type="checkbox"
              id="acceptContract"
              checked={acceptContract}
              onChange={(e) =>
                setValue("acceptContract", e.target.checked, {
                  shouldValidate: true,
                })
              }
              className="w-4 h-4 text-primary focus:ring-primary border-gray-300 rounded cursor-pointer"
            />
            <label
              htmlFor="acceptContract"
              className="text-sm text-gray-700 cursor-pointer"
            >
              Confirmo que li e aceito o Contrato de Prestação de Serviços.
            </label>
          </div>
        </div>

        {/* Termo de Autorização do Uso de Imagem */}
        <div className="space-y-4">
          <h3 className="text-sm font-medium">
            Termo de Autorização do Uso de Imagem
          </h3>

          <Card className="bg-pink-50 border-pink-200">
            <CardContent className="p-4">
              <div
                className="prose prose-sm max-w-none text-sm text-gray-700 max-h-64 overflow-y-auto"
                dangerouslySetInnerHTML={{ __html: imageTerm || "" }}
              />
            </CardContent>
          </Card>

          <div className="space-y-3">
            <p className="text-sm text-gray-600">
              Você aceita o Termo de Autorização do Uso de Imagem?
            </p>

            <div className="space-y-2">
              <RadioButton
                name="imageTermAcceptance"
                value="true"
                checked={acceptImageTerm === true}
                onChange={handleImageTermAcceptance}
              >
                Sim
              </RadioButton>

              <RadioButton
                name="imageTermAcceptance"
                value="false"
                checked={acceptImageTerm === false}
                onChange={handleImageTermAcceptance}
              >
                Não
              </RadioButton>
            </div>
          </div>
        </div>

        <Button
          type="submit"
          className="w-full cursor-pointer"
          disabled={!isValid}
        >
          Próximo
        </Button>
      </form>
    </div>
  );
}
