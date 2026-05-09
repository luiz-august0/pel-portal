"use client";

import { useState } from "react";
import { Card, CardContent } from "@/components/ui/card";
import { CountryFlag } from "@/components/customized/CountryFlag";
import { DataSection } from "@/components/customized/DataSection";
import { Badge } from "@/components/ui/badge";
import dayjs from "dayjs";
import "dayjs/locale/pt-br";
import { formatTime } from "@/helpers/formatters";
import { type InscriptionDetails } from "@/types/domains/inscription";
import { formatDayOfWeek, getStatusBadge } from "../inscriptionUtils";
import { cn } from "@/helpers/cn";
import { Button } from "@/components/ui/button";
import { InscriptionAttendanceModal } from "./InscriptionAttendanceModal";
import { InscriptionGradesModal } from "./InscriptionGradesModal";

dayjs.locale("pt-br");

type InscriptionCourseCardProps = {
  inscription: InscriptionDetails;
  onClick?: () => void;
};

export function InscriptionCourseCard({
  inscription,
  onClick,
}: InscriptionCourseCardProps) {
  const [isGradesModalOpen, setIsGradesModalOpen] = useState(false);
  const [isAttendanceModalOpen, setIsAttendanceModalOpen] = useState(false);

  const openGrades = () => {
    setIsGradesModalOpen(true);
  };

  const openAttendance = () => {
    setIsAttendanceModalOpen(true);
  };

  return (
    <Card className="bg-gray-50">
      <CardContent className="p-4">
        {/* Header com bandeira, nome do curso e status */}
        <div className="mb-4 flex items-center justify-between">
          <div className="flex items-center gap-3">
            <CountryFlag courseName={inscription.clazz.course.courseName} />
            <span className="text-sm font-medium">
              {inscription.clazz.course.courseName}
            </span>
          </div>
          {getStatusBadge(inscription)}
        </div>

        {/* Dados da turma */}
        <DataSection
          values={[
            {
              label: "Nível",
              value: inscription.clazz.level.levelName,
            },
            {
              label: "Horário",
              value: `${formatDayOfWeek(
                inscription.clazz.dayOfWeek
              )}\n${formatTime(inscription.clazz.startTime)} - ${formatTime(
                inscription.clazz.endTime
              )}`,
            },
            {
              label: "Turma",
              value: inscription.clazz.className,
            },
            {
              label: "Média",
              value: (
                <div className="flex items-center gap-3">
                  <p className="text-gray-900 font-medium">
                    {inscription.finalGrade > 0
                      ? inscription.finalGrade.toFixed(2)
                      : "-"}
                  </p>
                  <Button
                    variant="link"
                    onClick={openGrades}
                    className="h-auto p-0 text-blue-600 justify-start"
                  >
                    Notas
                  </Button>
                </div>
              ),
            },
            {
              label: "Presença",
              value: (
                <div className="flex items-center gap-3">
                  <p className="text-gray-900 font-medium">
                    {inscription.attendancePercentage > 0
                      ? `${inscription.attendancePercentage.toFixed(0)}%`
                      : "-"}
                  </p>
                  <Button
                    variant="link"
                    onClick={openAttendance}
                    className="h-auto p-0 text-blue-600 justify-start"
                  >
                    Faltas
                  </Button>
                </div>
              ),
            },
          ]}
        />
        {onClick && (
          <Button
            variant="link"
            onClick={onClick}
            className="mt-2 p-0 text-blue-600 w-full"
          >
            Mais detalhes
          </Button>
        )}

        {/* Modal de presença */}
        <InscriptionAttendanceModal
          inscriptionId={inscription.id}
          open={isAttendanceModalOpen}
          onOpenChange={setIsAttendanceModalOpen}
        />
        <InscriptionGradesModal
          annualAverage={inscription.finalGrade}
          inscriptionId={inscription.id}
          open={isGradesModalOpen}
          onOpenChange={setIsGradesModalOpen}
        />
      </CardContent>
    </Card>
  );
}
