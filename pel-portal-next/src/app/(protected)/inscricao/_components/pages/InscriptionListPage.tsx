import { HeaderPage } from "@/components/customized/HeaderPage";
import { InscriptionYearSection } from "../organisms/InscriptionYearSection";
import { Button } from "@/components/ui/button";
import { useRouter } from "next/navigation";
import { InscriptionsGroupedByYearResponse } from "@/types/domains/inscription";

export function InscriptionListPage({
  isLoading,
  inscriptionData,
  handleGoBack,
  showNewInscriptionButton,
}: {
  isLoading: boolean;
  inscriptionData?: InscriptionsGroupedByYearResponse;
  handleGoBack: () => void;
  showNewInscriptionButton?: boolean;
}) {
  const router = useRouter();
  const hasAnyInscription = inscriptionData && inscriptionData.length > 0;

  return (
    <div className="min-h-screen bg-gray-50">
      <div className="max-w-md mx-auto p-4">
        {/* Header */}
        <HeaderPage title="Cursos" onBack={handleGoBack} />

        {/* Loading State */}
        {isLoading && (
          <div className="text-center py-8">
            <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-primary mx-auto"></div>
          </div>
        )}

        {/* Content */}
        {!isLoading && (
          <>
            {hasAnyInscription ? (
              <div className="space-y-4">
                {inscriptionData?.map((yearData) => (
                  <InscriptionYearSection
                    key={yearData.year}
                    yearData={yearData}
                  />
                ))}
              </div>
            ) : (
              <div className="text-center py-16">
                <p className="text-gray-500 mb-8">Nenhum resultado</p>
              </div>
            )}

            {/* New Inscription Button */}
            {showNewInscriptionButton && (
              <Button
                className="w-full mt-4"
                onClick={() => {
                  router.push("/inscricao/nova");
                }}
              >
                Realizar nova inscrição
              </Button>
            )}
          </>
        )}
      </div>
    </div>
  );
}
