import { Badge } from "@/components/ui/badge";
import { Card, CardContent } from "@/components/ui/card";
import { DataSection } from "@/components/customized/DataSection";
import { CountryFlag } from "@/components/customized/CountryFlag";
import { Transfer } from "@/types/domains/transfer";
import dayjs from "dayjs";
import "dayjs/locale/pt-br";
import { Button } from "@/components/ui/button";
import { getStatusBadge } from "../transferUtils";
import { Class } from "@/types/domains/inscription";
import { formatDayOfWeek } from "@/app/(protected)/inscricao/_components/inscriptionUtils";
import { formatTime } from "@/helpers/formatters";
import { ArrowDownUp } from "lucide-react";

dayjs.locale("pt-br");

type TransferCardProps = {
  transfer: Transfer;
  onClick?: () => void;
};

function HourSection({ label, clazz }: { label: string; clazz: Class }) {
  return (
    <div className="flex gap-3 items-center">
      <span className="text-xs text-muted-foreground font-medium w-28">{label}</span>
      <Card className="bg-gray-50 p-2 w-full">
        <CardContent className="flex flex-col items-start px-2">
          <span className="text-sm font-semibold">
            {formatDayOfWeek(clazz.dayOfWeek)}
          </span>
          <span className="text-sm font-medium">
            {`${formatTime(clazz.startTime)} - ${formatTime(clazz.endTime)}`}
          </span>
        </CardContent>
      </Card>
    </div>
  );
}

export function TransferCard({ transfer, onClick }: TransferCardProps) {
  return (
    <Card className="bg-gray-50">
      <CardContent className="p-4">
        {/* Header com bandeira, nome do curso e status */}
        <div className="mb-4 flex items-center justify-between">
          <div className="flex items-center gap-3">
            <CountryFlag
              courseName={transfer.sourceInscription.clazz.course.courseName}
            />
            <div className="flex flex-col">
              <span className="text-sm font-medium">
                {transfer.sourceInscription.clazz.course.courseName}
              </span>
              <span className="text-xs text-gray-500">
                {transfer.sourceInscription.clazz.level.levelName}
              </span>
            </div>
          </div>
          {getStatusBadge(transfer)}
        </div>

        <div className="relative flex flex-col gap-1">
          <HourSection label="Horário atual" clazz={transfer.sourceInscription.clazz} />
          <HourSection label="Horário nova turma" clazz={transfer.destinationClass} />
          <ArrowDownUp className="absolute right-4 top-1/2 -translate-y-1/2 w-4 h-4 text-blue-600" />
        </div>

        {onClick && (
          <Button
            variant="link"
            onClick={onClick}
            className="mt-2 p-0 text-blue-600 w-full"
          >
            Mais detalhes
          </Button>
        )}
      </CardContent>
    </Card>
  );
}
