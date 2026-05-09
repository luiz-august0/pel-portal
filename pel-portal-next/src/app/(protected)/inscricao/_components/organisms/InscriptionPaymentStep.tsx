"use client";

import { useState } from "react";
import { useForm, UseFormReturn } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { useMutation } from "@tanstack/react-query";
import { toast } from "sonner";

import { Button } from "@/components/ui/button";
import { Card, CardContent } from "@/components/ui/card";
import { CountryFlag } from "@/components/customized/CountryFlag";
import { DataSection } from "@/components/customized/DataSection";
import { ClassDetailsModal } from "../molecules/ClassDetailsModal";
import { PaymentFormSelector } from "../molecules/PaymentFormSelector";

import {
  InscriptionStep2Schema,
  type InscriptionStep2,
  type AvailableClass,
  type ActualLevel,
} from "@/types/domains/inscription";
import { addPaymentFormToInscription } from "@/core/services/inscription/inscriptionService";
import { handlerHttpError } from "@/helpers/toast";
import { ResumeCard } from "../atoms/ResumeCard";

type InscriptionPaymentStepProps = {
  formPayment: UseFormReturn<InscriptionStep2>;
  inscriptionId: number;
  selectedClass: AvailableClass;
  actualLevel: ActualLevel;
  onNext: () => void;
  onContractReceived: (contract: string) => void;
};

export function InscriptionPaymentStep({
  formPayment,
  inscriptionId,
  selectedClass,
  actualLevel,
  onNext,
  onContractReceived,
}: InscriptionPaymentStepProps) {
  const [showClassDetails, setShowClassDetails] = useState(false);

  const {
    watch,
    setValue,
    handleSubmit,
    formState: { isValid },
  } = formPayment;
  const selectedPaymentForm = watch("paymentForm");

  // Mutation para adicionar forma de pagamento
  const addPaymentMutation = useMutation({
    mutationFn: (paymentForm: string) =>
      addPaymentFormToInscription(inscriptionId, paymentForm),
    onSuccess: (contract) => {
      onContractReceived(contract);
      onNext();
    },
    onError: (error) => {
      handlerHttpError(error);
    },
  });

  const onSubmit = (data: InscriptionStep2) => {
    addPaymentMutation.mutate(data.paymentForm);
  };

  const handlePaymentFormChange = (paymentForm: string) => {
    setValue("paymentForm", paymentForm as "B1x" | "B2x" | "B3x", {
      shouldValidate: true,
    });
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
          <h3 className="text-sm font-medium">Forma de pagamento</h3>
          <PaymentFormSelector
            selectedPaymentForm={selectedPaymentForm}
            onPaymentFormChange={handlePaymentFormChange}
          />
        </div>

        <Button
          type="submit"
          className="w-full cursor-pointer"
          disabled={!isValid || addPaymentMutation.isPending}
        >
          {addPaymentMutation.isPending ? "Processando..." : "Próximo"}
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
