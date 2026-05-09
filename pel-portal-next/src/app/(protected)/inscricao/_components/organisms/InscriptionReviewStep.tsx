"use client";

import { useState } from "react";
import { UseFormReturn } from "react-hook-form";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import { toast } from "sonner";
import { useRouter } from "next/navigation";

import { Button } from "@/components/ui/button";
import { Card, CardContent } from "@/components/ui/card";
import { CountryFlag } from "@/components/customized/CountryFlag";
import { DataSection } from "@/components/customized/DataSection";
import { ClassDetailsModal } from "../molecules/ClassDetailsModal";
import { PaymentFormSelector } from "../molecules/PaymentFormSelector";

import {
  InscriptionStep4Schema,
  type InscriptionStep4,
  type AvailableClass,
  type ActualLevel,
  InscriptionStep2,
} from "@/types/domains/inscription";
import { finalizeInscription } from "@/core/services/inscription/inscriptionService";
import { handlerHttpError } from "@/helpers/toast";
import { formatDayOfWeek } from "../inscriptionUtils";
import { formatTime } from "@/helpers/formatters";
import dayjs from "dayjs";
import { Check, CheckCircle, CheckCircle2, X, XCircle } from "lucide-react";
import { TermCard } from "../atoms/TermCard";
import { ResumeCard } from "../atoms/ResumeCard";

type InscriptionReviewStepProps = {
  formReview: UseFormReturn<InscriptionStep4>;
  inscriptionId: number;
  selectedClass: AvailableClass;
  actualLevel: ActualLevel;
  paymentForm: UseFormReturn<InscriptionStep2>;
  acceptContract: boolean;
  acceptImageTerm: boolean;
};

export function InscriptionReviewStep({
  formReview,
  inscriptionId,
  selectedClass,
  actualLevel,
  paymentForm,
  acceptContract,
  acceptImageTerm,
}: InscriptionReviewStepProps) {
  const router = useRouter();
  const queryClient = useQueryClient();
  const [showClassDetails, setShowClassDetails] = useState(false);
  const [showAllPaymentForms, setShowAllPaymentForms] = useState(false);

  const {
    setValue,
    handleSubmit,
    formState: { isValid },
  } = formReview;

  // Mutation para finalizar inscrição
  const finalizeInscriptionMutation = useMutation({
    mutationFn: () =>
      finalizeInscription(
        inscriptionId,
        paymentForm.watch("paymentForm"),
        acceptContract,
        acceptImageTerm
      ),
    onSuccess: () => {
      toast.success("Inscrição finalizada com sucesso!");
      queryClient.invalidateQueries({
        queryKey: ["inscriptions-grouped-by-year"],
      });
      queryClient.invalidateQueries({
        queryKey: ["last-inscription"],
      });
      router.push(`/inscricao/${inscriptionId}/detalhes`);
    },
    onError: (error) => {
      handlerHttpError(error);
    },
  });

  const onSubmit = () => {
    finalizeInscriptionMutation.mutate();
  };

  const handlePaymentFormChange = (newPaymentForm: string) => {
    paymentForm.setValue("paymentForm", newPaymentForm as "B1x" | "B2x" | "B3x", {
      shouldValidate: true,
    });
    setShowAllPaymentForms(false);
  };

  const handleToggleShowAllPaymentForms = () => {
    setShowAllPaymentForms(!showAllPaymentForms);
  };

  return (
    <div className="space-y-6">
      <ResumeCard
        actualLevel={actualLevel}
        selectedClass={selectedClass}
        setShowClassDetails={setShowClassDetails}
      />

      {/* Forma de pagamento */}
      <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
        <div className="space-y-4">
          <div className="flex items-center justify-between">
            <h3 className="text-sm font-medium">Forma de pagamento</h3>
            <button
              onClick={(e) => {
                e.preventDefault();
                handleToggleShowAllPaymentForms();
              }}
              className="text-blue-600 hover:text-blue-700 text-sm font-medium cursor-pointer"
            >
              Alterar
            </button>
          </div>
          <PaymentFormSelector
            selectedPaymentForm={paymentForm.watch("paymentForm")}
            onPaymentFormChange={handlePaymentFormChange}
            showOnlySelected={!showAllPaymentForms}
          />
        </div>

        {/* Contrato e Autorização */}
        <div className="space-y-4 mb-6">
          <h3 className="text-sm font-medium">Contrato e Autorização</h3>
          <div className="space-y-2">
            <TermCard
              title="Contrato de Prestação de Serviços"
              accept={acceptContract}
            />
            <TermCard
              title="Termo de Autorização do Uso de Imagem"
              accept={acceptImageTerm}
            />
          </div>
        </div>

        <Button
          type="submit"
          className="w-full cursor-pointer"
          disabled={finalizeInscriptionMutation.isPending}
        >
          {finalizeInscriptionMutation.isPending
            ? "Processando..."
            : "Concluir inscrição"}
        </Button>
      </form>

      {/* Modal de detalhes da turma */}
      <ClassDetailsModal
        isOpen={showClassDetails}
        onClose={() => setShowClassDetails(false)}
        classData={selectedClass}
        actualLevel={actualLevel}
        isLoading={false}
        disableInscribe
      />
    </div>
  );
}
