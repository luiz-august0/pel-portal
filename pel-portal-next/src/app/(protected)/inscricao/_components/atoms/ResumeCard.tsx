import { Card, CardContent } from "@/components/ui/card";
import { CountryFlag } from "@/components/customized/CountryFlag";
import { DataSection } from "@/components/customized/DataSection";
import dayjs from "dayjs";
import { ActualLevel, AvailableClass } from "@/types/domains/inscription";
import { formatDayOfWeek } from "../inscriptionUtils";
import { formatTime } from "@/helpers/formatters";
import { ReactNode } from "react";

export function ResumeCard({
  actualLevel,
  selectedClass,
  setShowClassDetails,
  badge,
}: {
  actualLevel: ActualLevel;
  selectedClass: AvailableClass;
  setShowClassDetails: (show: boolean) => void;
  badge?: ReactNode;
}) {
  return (
    <Card className="bg-gray-50">
      <CardContent className="p-4">
        {/* Header com bandeira e nome do curso */}
        <div className="mb-4 flex items-center justify-between">
          <div className="flex items-center gap-3">
            <CountryFlag courseName={actualLevel.course.courseName} />
            <span className="text-sm font-medium">
              {actualLevel.course.courseName}
            </span>
          </div>
          {badge ? (
            badge
          ) : (
            <div className="inline-flex rounded-full bg-blue-600 px-3 py-1 text-sm font-medium text-white">
              {`Inicia ${dayjs(selectedClass.plannedStartDate).format(
                "DD/MM/YYYY"
              )}`}
            </div>
          )}
        </div>

        {/* Dados da turma */}
        <DataSection
          values={[
            {
              label: "Nível",
              value: actualLevel.levelName,
            },
            {
              label: "Horário",
              value: `${formatDayOfWeek(selectedClass.dayOfWeek)}\n${formatTime(
                selectedClass.startTime
              )} - ${formatTime(selectedClass.endTime)}`,
            },
            {
              label: "Turma",
              value: selectedClass.className,
            },
          ]}
        />

        {/* Link para mais detalhes */}
        <button
          onClick={() => setShowClassDetails(true)}
          className="mt-4 text-blue-600 hover:text-blue-700 text-sm font-medium cursor-pointer"
        >
          Mais detalhes
        </button>
      </CardContent>
    </Card>
  );
}
