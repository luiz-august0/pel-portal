"use client";

import { useState } from "react";
import { useQuery } from "@tanstack/react-query";
import { ChevronUp, ChevronDown } from "lucide-react";
import {
  ResponsiveDialog,
  ResponsiveDialogContent,
  ResponsiveDialogFooter,
  ResponsiveDialogHeader,
  ResponsiveDialogTitle,
} from "@/components/customized/ResponsiveDialog";
import { Button } from "@/components/ui/button";
import { Card } from "@/components/ui/card";
import { getInscriptionGrades } from "@/core/services/inscription/inscriptionService";
import { type QuarterGrade } from "@/types/domains/inscription";
import { cn } from "@/helpers/cn";
import { ScrollArea } from "@/components/ui/scroll-area";

type InscriptionGradesModalProps = {
  inscriptionId: number;
  annualAverage?: number;
  open: boolean;
  onOpenChange: (open: boolean) => void;
};

const getQuarterName = (quarter: number): string => {
  const quarterNames = {
    1: "1º Trimestre",
    2: "2º Trimestre",
    3: "3º Trimestre",
    4: "4º Trimestre",
  };
  return (
    quarterNames[quarter as keyof typeof quarterNames] ||
    `${quarter}º Trimestre`
  );
};

const QuarterSection = ({ quarter }: { quarter: QuarterGrade }) => {
  const [isExpanded, setIsExpanded] = useState(quarter.quarter === 1);

  const hasGrades = quarter.grades && quarter.grades.length > 0;
  const displayGrade = quarter.grade > 0 ? quarter.grade.toFixed(2) : "--";

  return (
    <div className="space-y-2">
      <button
        onClick={() => setIsExpanded(!isExpanded)}
        className="w-full flex items-center justify-between py-4"
        disabled={!hasGrades}
      >
        <span className="font-medium text-left text-base">
          {getQuarterName(quarter.quarter)}
        </span>
        <div className="flex items-center gap-2">
          <span className="text-sm font-medium">Total {displayGrade}</span>
          {hasGrades &&
            (isExpanded ? (
              <ChevronUp className="h-4 w-4" />
            ) : (
              <ChevronDown className="h-4 w-4" />
            ))}
        </div>
      </button>

      {isExpanded && hasGrades && (
        <div className="space-y-2">
          {/* Header da tabela */}
          <div className="flex justify-between items-center px-4 py-2 bg-white border-b font-medium text-sm">
            <span>Prova</span>
            <span>Nota</span>
          </div>

          {/* Linhas das provas */}
          {quarter.grades.map((grade) => (
            <div
              key={grade.id}
              className="flex justify-between items-center px-4 py-3 bg-white text-sm border-b"
            >
              <span className="text-gray-900">
                {grade.courseEvaluation.courseEvaluationName}
              </span>
              <span className="text-gray-900">{grade.gradeValue.toFixed(2)}</span>
            </div>
          ))}

          {/* Total do trimestre */}
          <div className="flex justify-between items-center px-4 py-3 bg-gray-50 font-medium text-sm">
            <span>Total</span>
            <span>{quarter.grade.toFixed(2)}</span>
          </div>
        </div>
      )}
    </div>
  );
};

export const InscriptionGradesModal = ({
  inscriptionId,
  annualAverage,
  open,
  onOpenChange,
}: InscriptionGradesModalProps) => {
  const { data: grades, isLoading } = useQuery({
    queryKey: ["inscription-grades", inscriptionId],
    queryFn: () => getInscriptionGrades(inscriptionId),
    enabled: open && !!inscriptionId,
  });

  return (
    <ResponsiveDialog open={open} onOpenChange={onOpenChange}>
      <ResponsiveDialogContent className="max-w-md mx-auto">
        <ResponsiveDialogHeader>
          <ResponsiveDialogTitle>Notas</ResponsiveDialogTitle>
        </ResponsiveDialogHeader>

        <ScrollArea className={cn("overflow-y-auto max-md:p-4")}>
          <div className="space-y-4">
            {/* Média Anual */}
            {annualAverage && (
              <Card className="p-4 bg-gray-50">
                <div className="flex justify-between items-center">
                  <span className="font-medium text-sm">Média Anual</span>
                  <span className="font-medium text-sm">
                    Total {annualAverage.toFixed(2)}
                  </span>
                </div>
              </Card>
            )}

            {/* Loading state */}
            {isLoading && (
              <div className={cn("text-center py-8")}>
                <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-primary mx-auto"></div>
                <p className="text-gray-600 mt-2">Carregando notas...</p>
              </div>
            )}

            {/* Trimestres */}
            {grades && grades.length > 0 && (
              <div className="space-y-3">
                {grades.map((quarter) => (
                  <QuarterSection key={quarter.quarter} quarter={quarter} />
                ))}
              </div>
            )}

            {/* Estado vazio */}
            {grades && grades.length === 0 && (
              <div className="flex justify-center py-8">
                <div className="text-gray-500">Nenhum resultado encontrado</div>
              </div>
            )}
          </div>
        </ScrollArea>

        <ResponsiveDialogFooter>
          <Button onClick={() => onOpenChange(false)} className="w-full">
            OK
          </Button>
        </ResponsiveDialogFooter>
      </ResponsiveDialogContent>
    </ResponsiveDialog>
  );
};
