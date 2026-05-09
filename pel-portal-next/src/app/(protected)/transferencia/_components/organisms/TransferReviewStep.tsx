"use client";

import { useMutation, useQueryClient } from "@tanstack/react-query";
import { useRouter } from "next/navigation";
import { useState } from "react";
import { toast } from "sonner";

import { Button } from "@/components/ui/button";

import { ResumeCard } from "@/app/(protected)/inscricao/_components/atoms/ResumeCard";
import { ClassDetailsModal } from "@/app/(protected)/inscricao/_components/molecules/ClassDetailsModal";
import {
  InscriptionDetails,
  type ActualLevel,
  type AvailableClass,
} from "@/types/domains/inscription";
import { requestTransfer } from "@/core/services/transfer/transferService";
import { handlerHttpError } from "@/helpers/toast";
import { Separator } from "@/components/ui/separator";
import { Card, CardContent } from "@/components/ui/card";
import { getStatusBadge } from "../transferUtils";
import { Transfer } from "@/types/domains/transfer";

type TransferReviewStepProps = {
  inscription: InscriptionDetails;
  selectedClass: AvailableClass;
  onlyView?: boolean;
  transfer?: Transfer;
};

type ShowClassDetails = {
  class?: AvailableClass;
  isOpen: boolean;
};

export function TransferReviewStep({
  inscription,
  selectedClass,
  onlyView = false,
  transfer,
}: TransferReviewStepProps) {
  const router = useRouter();
  const queryClient = useQueryClient();
  const [showClassDetails, setShowClassDetails] = useState<ShowClassDetails>({
    isOpen: false,
  });

  // Mutation para solicitar transferência
  const transferMutation = useMutation({
    mutationFn: () => requestTransfer(inscription.id, selectedClass.id),
    onSuccess: (transferId) => {
      toast.success("Solicitação de transferência enviada com sucesso!");
      queryClient.invalidateQueries({
        queryKey: ["transfers-grouped-by-year"],
      });
      router.push(`/transferencia/${transferId}/detalhes`);
    },
    onError: (error) => {
      handlerHttpError(error);
    },
  });

  const onSubmit = () => {
    transferMutation.mutate();
  };

  return (
    <div className="space-y-6">
      {transfer && (
        <>
          <Card className="px-2 py-3">
            <CardContent className="flex px-2 justify-between items-center">
              <span className="text-sm font-semibold">Status</span>
              {getStatusBadge(transfer)}
            </CardContent>
          </Card>
          <Separator />
        </>
      )}
      <div className="space-y-2">
        <h2 className="text-sm font-semibold">Resumo</h2>
        <ResumeCard
          actualLevel={inscription.clazz?.level as ActualLevel}
          selectedClass={inscription.clazz}
          setShowClassDetails={() =>
            setShowClassDetails({ class: inscription.clazz, isOpen: true })
          }
          badge={
            <div className="inline-flex rounded-full bg-blue-600 px-3 py-1 text-sm font-medium text-white">
              Turma atual
            </div>
          }
        />
        <ResumeCard
          actualLevel={inscription.clazz?.level as ActualLevel}
          selectedClass={selectedClass}
          setShowClassDetails={() =>
            setShowClassDetails({ class: selectedClass, isOpen: true })
          }
          badge={
            <div className="inline-flex rounded-full bg-green-600 px-3 py-1 text-sm font-medium text-white">
              Nova turma
            </div>
          }
        />
      </div>

      {!onlyView ? (
        <Button
          onClick={onSubmit}
          className="w-full cursor-pointer"
          disabled={transferMutation.isPending}
        >
          {transferMutation.isPending
            ? "Processando..."
            : "Concluir solicitação"}
        </Button>
      ) : (
        <Button
          onClick={() => router.push("/")}
          className="w-full cursor-pointer"
        >
          OK
        </Button>
      )}

      {/* Modal de detalhes da turma */}
      <ClassDetailsModal
        isOpen={showClassDetails.isOpen}
        onClose={() => setShowClassDetails({ isOpen: false })}
        classData={showClassDetails.class as AvailableClass}
        actualLevel={inscription.clazz?.level as ActualLevel}
        isLoading={false}
        disableInscribe
        disableStartDateBadge
      />
    </div>
  );
}
