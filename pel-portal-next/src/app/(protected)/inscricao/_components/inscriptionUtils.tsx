import { Badge } from "@/components/ui/badge";
import { InscriptionDetails } from "@/types/domains/inscription";
import { cn } from "@/helpers/cn";
import dayjs from "dayjs";

export const formatDayOfWeek = (dayOfWeek: string) => {
  const dayMap: Record<string, string> = {
    SEG: "Segunda",
    TER: "Terça",
    QUA: "Quarta",
    QUI: "Quinta",
    SEX: "Sexta",
    SAB: "Sábado",
    DOM: "Domingo",
  };

  // Se não contém ponto e vírgula, é um dia único
  if (!dayOfWeek.includes(";")) {
    return dayMap[dayOfWeek] || dayOfWeek;
  }

  // Separar os dias e mapear para nomes completos
  const days = dayOfWeek
    .split(";")
    .map((day) => dayMap[day.trim()] || day.trim());

  // Se tem apenas um dia (não deveria acontecer com ;, mas por segurança)
  if (days.length === 1) {
    return days[0];
  }

  // Se tem dois dias: "Terça e Quinta"
  if (days.length === 2) {
    return `${days[0]} e ${days[1]}`;
  }

  // Se tem três ou mais dias: "Terça, Quinta e Sexta"
  const lastDay = days.pop(); // Remove e pega o último dia
  return `${days.join(", ")} e ${lastDay}`;
};

export const getStatusBadge = (inscription: InscriptionDetails) => {
  type Variant =
    | "default"
    | "secondary"
    | "destructive"
    | "outline"
    | "success";

  if (inscription?.result && inscription?.result.length > 0) {
    const statusMap: {
      [key: string]: {
        label: string;
        variant: Variant;
      };
    } = {
      A: { label: "Aprovado", variant: "success" },
      R: { label: "Reprovado", variant: "destructive" },
      RF: { label: "Reprovado por falta", variant: "destructive" },
    };

    const status = statusMap[inscription.status] || {
      label: inscription.status,
      variant: "outline",
    };

    return <Badge variant={status.variant}>{status.label}</Badge>;
  }

  const alreadyStarted = dayjs(inscription.clazz.plannedStartDate).isBefore(
    dayjs()
  );

  const statusMap: {
    [key: string]: {
      label: string;
      variant: Variant;
    };
  } = {
    A: {
      label: alreadyStarted
        ? "Ativo"
        : `Inicia ${dayjs(inscription.clazz.plannedStartDate).format(
            "DD/MM/YYYY"
          )}`,
      variant: "success",
    },
    P: { label: "Pendente", variant: "destructive" },
    D: { label: "Desistente", variant: "destructive" },
    T: { label: "Transferido", variant: "outline" },
    C: { label: "Cancelado", variant: "destructive" },
  };

  const status = statusMap[inscription.status] || {
    label: inscription.status,
    variant: "outline",
  };

  return (
    <Badge
      className={cn(
        inscription.status == "A" && !alreadyStarted && "bg-blue-600 text-white"
      )}
      variant={status.variant}
    >
      {status.label}
    </Badge>
  );
};
