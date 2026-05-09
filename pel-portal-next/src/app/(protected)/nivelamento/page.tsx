"use client";

import { useRouter } from "next/navigation";
import { HeaderPage } from "@/components/customized/HeaderPage";
import { useQuery } from "@tanstack/react-query";
import { getLevelingGroupedByYear } from "@/core/services/leveling/levelingService";
import { LevelingYearSection } from "./_components/organisms/LevelingYearSection";
import { Button } from "@/components/ui/button";
import { Plus } from "lucide-react";

export default function LevelingPage() {
  const router = useRouter();

  const { data: levelingData, isLoading } = useQuery({
    queryKey: ["leveling-grouped-by-year"],
    queryFn: getLevelingGroupedByYear,
  });

  const handleGoBack = () => {
    router.push("/");
  };

  const hasAnyLeveling = levelingData && levelingData.length > 0;

  return (
    <div className="min-h-screen bg-gray-50">
      <div className="max-w-md mx-auto p-4">
        {/* Header */}
        <HeaderPage title="Nivelamento" onBack={handleGoBack} />

        {/* Loading State */}
        {isLoading && (
          <div className="text-center py-8">
            <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-primary mx-auto"></div>
          </div>
        )}

        {/* Content */}
        {!isLoading && (
          <>
            {hasAnyLeveling ? (
              <div className="space-y-4">
                {levelingData?.map((yearData) => (
                  <LevelingYearSection
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

            {/* Floating Action Button */}

            <Button
              className="w-full mt-4"
              onClick={() => {
                router.push("/nivelamento/agendar");
              }}
            >
              Agendar nivelamento
            </Button>
          </>
        )}
      </div>
    </div>
  );
}
