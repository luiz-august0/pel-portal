"use client";

import { useQuery } from "@tanstack/react-query";
import { useRouter } from "next/navigation";
import { Button } from "@/components/ui/button";
import { TransferYearSection } from "./_components/organisms/TransferYearSection";
import { getTransfersGroupedByYear } from "@/core/services/transfer/transferService";
import { useSession } from "next-auth/react";
import { HeaderPage } from "@/components/customized/HeaderPage";

export default function TransferPage() {
  const router = useRouter();
  const { data: session } = useSession();

  // Query para buscar transferências agrupadas por ano
  const { data: transfersData, isLoading } = useQuery({
    queryKey: ["transfers-grouped-by-year", session?.user?.id],
    queryFn: getTransfersGroupedByYear,
    enabled: !!session?.user?.id,
  });

  const handleBack = () => {
    router.back();
  };

  const handleTransferClick = (transferId: number) => {
    router.push(`/transferencia/${transferId}/detalhes`);
  };

  return (
    <div className="min-h-screen bg-gray-50">
      <div className="max-w-md mx-auto p-4">
        {/* Header */}
        <HeaderPage title="Transferência" onBack={handleBack} />

        {/* Loading State */}
        {isLoading && (
          <div className="text-center py-8">
            <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-primary mx-auto"></div>
          </div>
        )}

        {/* Content */}
        {!isLoading && (
          <>
            {transfersData && transfersData.length > 0 ? (
              <div className="space-y-4">
                {transfersData?.map((yearData) => (
                  <TransferYearSection
                    key={yearData.year}
                    yearData={yearData}
                    onTransferClick={handleTransferClick}
                  />
                ))}
              </div>
            ) : (
              <div className="text-center py-16">
                <p className="text-gray-500 mb-8">Nenhum resultado</p>
              </div>
            )}

            <Button
              className="w-full mt-4"
              onClick={() => {
                router.push("/transferencia/nova");
              }}
            >
              Solicitar transferência
            </Button>
          </>
        )}
      </div>
    </div>
  );
}
