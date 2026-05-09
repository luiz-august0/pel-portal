"use client";

import { HeaderPage } from "@/components/customized/HeaderPage";
import { useParams } from "next/navigation";
import { useRouter } from "next/navigation";
import { TransferReviewStep } from "../../_components/organisms/TransferReviewStep";
import { Button } from "@/components/ui/button";
import { useQuery } from "@tanstack/react-query";
import { getTransferDetails } from "@/core/services/transfer/transferService";

export default function TransferDetailsPage() {
  const params = useParams();
  const router = useRouter();
  const transferId = Number(params.id);

  // Query para buscar detalhes da inscrição
  const { data: transfer, isLoading: isLoadingTransfer } = useQuery({
    queryKey: ["transfer-details", transferId],
    queryFn: () => getTransferDetails(transferId),
    enabled: !!transferId,
  });

  if (isLoadingTransfer) {
    return (
      <div className="container mx-auto max-w-md p-4">
        <div className="animate-pulse space-y-4">
          <div className="h-8 w-48 bg-gray-200 rounded"></div>
          <div className="h-64 bg-gray-200 rounded"></div>
        </div>
      </div>
    );
  }

  if (!transfer) {
    return (
      <div className="container mx-auto max-w-4xl p-4">
        <div className="text-center">
          <h1 className="text-2xl font-bold text-gray-900">
            Transferência não encontrada
          </h1>
          <Button onClick={() => router.push("/")} className="mt-4">
            Voltar
          </Button>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gray-50 p-4">
      <div className="flex flex-col gap-4 mx-auto max-w-md">
        {/* Header */}
        <HeaderPage title="Transferência" onBack={() => router.push("/")} />
        <TransferReviewStep
          transfer={transfer}
          onlyView
          inscription={transfer.sourceInscription}
          selectedClass={transfer.destinationClass}
        />
      </div>
    </div>
  );
}
