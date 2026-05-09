"use client";

import dayjs from "dayjs";
import "dayjs/locale/pt-br";
import { Button } from "@/components/ui/button";
import { Card, CardContent } from "@/components/ui/card";
import { CountryFlag } from "@/components/customized/CountryFlag";
import { DataSection } from "@/components/customized/DataSection";
import {
  type AvailableClass,
  type ActualLevel,
} from "@/types/domains/inscription";
import {
  ResponsiveDialog,
  ResponsiveDialogContent,
  ResponsiveDialogHeader,
  ResponsiveDialogTitle,
} from "@/components/customized/ResponsiveDialog";
import { formatDayOfWeek } from "../inscriptionUtils";
import { formatTime } from "@/helpers/formatters";
import { cn } from "@/helpers/cn";
import { ScrollArea } from "@/components/ui/scroll-area";

dayjs.locale("pt-br");

type ClassDetailsModalProps = {
  isOpen: boolean;
  onClose: () => void;
  classData?: AvailableClass;
  actualLevel?: ActualLevel;
  onInscribe?: () => void;
  isLoading?: boolean;
  disableInscribe?: boolean;
  transfer?: boolean;
  disableStartDateBadge?: boolean;
};

export function ClassDetailsModal({
  isOpen,
  onClose,
  classData,
  actualLevel,
  onInscribe,
  isLoading = false,
  disableInscribe = false,
  transfer = false,
  disableStartDateBadge = false,
}: ClassDetailsModalProps) {
  const formatDate = (dateString: string) => {
    return dayjs(dateString).format("DD/MM/YYYY");
  };

  const getStartDateBadge = () => {
    const startDate = dayjs(classData?.plannedStartDate);
    const today = dayjs();

    if (startDate.isAfter(today)) {
      return (
        <div className="inline-flex rounded-full bg-blue-600 px-3 py-1 text-sm font-medium text-white">
          Inicia {formatDate(classData?.plannedStartDate || "")}
        </div>
      );
    }

    return null;
  };

  return (
    <ResponsiveDialog open={isOpen} onOpenChange={onClose}>
      <ResponsiveDialogContent className="max-w-md">
        <ResponsiveDialogHeader>
          <ResponsiveDialogTitle>Detalhes do curso</ResponsiveDialogTitle>
        </ResponsiveDialogHeader>

        <ScrollArea className={cn("overflow-y-auto max-md:p-4")}>
          <div className="space-y-6">
            {/* Card Principal */}
            <Card className="bg-gray-50">
              <CardContent className="p-4">
                {/* Header com bandeira e nome do curso */}
                <div className="mb-4 flex items-center justify-between">
                  <div className="flex items-center gap-3">
                    <CountryFlag
                      courseName={actualLevel?.course.courseName || ""}
                    />
                    <span className="text-sm font-medium">
                      {actualLevel?.course.courseName}
                    </span>
                  </div>
                  {!disableStartDateBadge && getStartDateBadge()}
                </div>

                {/* Dados da turma */}
                <DataSection
                  values={[
                    {
                      label: "Nível",
                      value: actualLevel?.levelName,
                    },
                    {
                      label: "Horário",
                      value: `${formatDayOfWeek(
                        classData?.dayOfWeek || ""
                      )}\n${formatTime(
                        classData?.startTime || ""
                      )} - ${formatTime(classData?.endTime || "")}`,
                    },
                    {
                      label: "Turma",
                      value: classData?.className || "",
                    },
                    {
                      label: "Vagas",
                      value: `${
                        (classData?.availableSlots ?? 0) -
                        (classData?.subscribers ?? 0)
                      }/${classData?.availableSlots}`,
                    },
                    {
                      label: "Professor",
                      value: classData?.professor?.name || classData?.professorName,
                    },
                    {
                      label: "Data início",
                      value: formatDate(classData?.plannedStartDate || ""),
                    },
                    {
                      label: "Data fim",
                      value: formatDate(classData?.plannedEndDate || ""),
                    },
                  ]}
                />
              </CardContent>
            </Card>

            {/* Botão de Inscrição */}
            {!disableInscribe && !transfer && (
              <Button
                onClick={onInscribe}
                disabled={isLoading}
                className="w-full"
              >
                {isLoading ? "Inscrevendo..." : "Fazer minha inscrição"}
              </Button>
            )}

            {transfer && (
              <Button
                onClick={onInscribe}
                disabled={isLoading}
                className="w-full"
              >
                {isLoading ? "Transferindo..." : "Solicitar transferência"}
              </Button>
            )}
          </div>
        </ScrollArea>
      </ResponsiveDialogContent>
    </ResponsiveDialog>
  );
}
