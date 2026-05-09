"use client";

import { useState } from "react";
import { ChevronDown, ChevronUp } from "lucide-react";
import { InscriptionsByYear } from "@/types/domains/inscription";
import { useRouter } from "next/navigation";

import dayjs from "dayjs";
import { InscriptionCourseCard } from "../molecules/InscriptionCourseCard";

type InscriptionYearSectionProps = {
  yearData: InscriptionsByYear;
};

export function InscriptionYearSection({ yearData }: InscriptionYearSectionProps) {  
  const [isExpanded, setIsExpanded] = useState(yearData.year === dayjs().year().toString());
  const router = useRouter();

  const handleInscriptionClick = (inscriptionId: number) => {
    router.push(`/inscricao/${inscriptionId}/detalhes`);
  };

  return (
    <div className="bg-white rounded-lg shadow-sm overflow-hidden">
      {/* Year Header */}
      <button
        className="w-full p-4 flex items-center justify-between bg-white hover:bg-gray-50 transition-colors cursor-pointer"
        onClick={() => setIsExpanded(!isExpanded)}
      >
        <h2 className="text-lg font-semibold text-gray-900">{yearData.year}</h2>
        {isExpanded ? (
          <ChevronUp className="w-5 h-5 text-gray-500" />
        ) : (
          <ChevronDown className="w-5 h-5 text-gray-500" />
        )}
      </button>

      {/* Inscription Cards */}
      {isExpanded && (
        <div className="border-t border-gray-100 p-2">
          {yearData.inscriptions.length > 0 ? (
            <div className="space-y-3">
              {yearData.inscriptions.map((inscription, index) => (
                <div key={inscription.id}>
                  <InscriptionCourseCard
                    inscription={inscription}
                    onClick={() => handleInscriptionClick(inscription.id)}
                  />
                  {index < yearData.inscriptions.length - 1 && (
                    <div className="border-t border-gray-100" />
                  )}
                </div>
              ))}
            </div>
          ) : (
            <div className="p-4 text-center text-gray-500">
              Nenhuma inscrição encontrada para este ano
            </div>
          )}
        </div>
      )}
    </div>
  );
}
