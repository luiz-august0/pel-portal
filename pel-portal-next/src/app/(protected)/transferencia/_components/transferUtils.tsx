import { Badge } from "@/components/ui/badge";
import { Transfer } from "@/types/domains/transfer";

export const getStatusBadge = (transfer: Transfer) => {
  type Variant =
    | "default"
    | "secondary"
    | "destructive"
    | "outline"
    | "success";

  const statusMap: {
    [key: string]: {
      label: string;
      variant: Variant;
    };
  } = {
    P: { label: "Pendente", variant: "destructive" },
    A: { label: "Aprovado", variant: "success" },
    R: { label: "Recusado", variant: "destructive" },
  };

  const status = statusMap[transfer.status] || {
    label: transfer.status,
    variant: "outline",
  };

  return <Badge variant={status.variant}>{status.label}</Badge>;
};
