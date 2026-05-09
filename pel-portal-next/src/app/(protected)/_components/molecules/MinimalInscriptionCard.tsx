"use client";

import { CountryFlag } from "@/components/customized/CountryFlag";
import { Button } from "@/components/ui/button";
import { Card, CardContent } from "@/components/ui/card";
import { formatTime } from "@/helpers/formatters";
import { type InscriptionDetails } from "@/types/domains/inscription";
import dayjs from "dayjs";
import "dayjs/locale/pt-br";
import { ChevronRight } from "lucide-react";
import { formatDayOfWeek, getStatusBadge } from "../../inscricao/_components/inscriptionUtils";

dayjs.locale("pt-br");

type MinimalInscriptionCardProps = {
  inscription: InscriptionDetails;
  onClick?: () => void;
};

export function MinimalInscriptionCard({
  inscription,
  onClick,
}: MinimalInscriptionCardProps) {
  return (
    <Card className="bg-gray-50 p-2">
      <CardContent className="px-1">
        {/* Header com bandeira, nome do curso e status */}
        <div className="mb-4 flex items-center justify-between">
          <div className="flex items-center gap-3">
            <CountryFlag courseName={inscription.clazz.course.courseName} />
            <div className="flex flex-col">
              <span className="text-sm font-medium">
                {inscription.clazz.course.courseName}
              </span>
              <span className="text-xs text-gray-500">
                {inscription.clazz.level.levelName}
              </span>
            </div>
          </div>
          <div className="flex items-center gap-1">
            {getStatusBadge(inscription)}
            <Button variant="ghost" onClick={onClick}>
              <ChevronRight className="w-5 h-5 text-gray-400" />
            </Button>
          </div>
        </div>
        <div className="flex items-center gap-1">
          <Card className="bg-gray-50 p-2 min-w-[40%]">
            <CardContent className="flex flex-col items-start px-2">
              <span className="text-sm font-semibold">
                {formatDayOfWeek(inscription.clazz.dayOfWeek)}
              </span>
              <span className="text-sm font-medium">
                {`${formatTime(inscription.clazz.startTime)} - ${formatTime(
                  inscription.clazz.endTime
                )}`}
              </span>
            </CardContent>
          </Card>
          <Card className="bg-gray-50 p-2 w-full items-center">
            <CardContent className="flex flex-col items-center px-2">
              <span className="text-sm font-medium">
                {inscription.finalGrade > 0
                  ? inscription.finalGrade.toString()
                  : "-"}
              </span>
              <span className="text-sm text-gray-500">Média</span>
            </CardContent>
          </Card>
          <Card className="bg-gray-50 p-2 w-full items-center">
            <CardContent className="flex flex-col items-center px-2 ">
              <span className="text-sm font-medium">
                {inscription.attendancePercentage > 0
                  ? `${inscription.attendancePercentage}%`
                  : "-"}
              </span>
              <span className="text-sm text-gray-500">Presença</span>
            </CardContent>
          </Card>
        </div>
      </CardContent>
    </Card>
  );
}
