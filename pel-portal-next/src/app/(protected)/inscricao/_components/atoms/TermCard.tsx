import { Card, CardContent } from "@/components/ui/card";
import { CheckCircle, XCircle } from "lucide-react";

type TermCardProps = {
  title: string;
  accept: boolean;
};

export function TermCard({ title, accept }: TermCardProps) {
  return (
    <Card className="p-3">
      <CardContent className="flex items-center justify-between px-2">
        <p className="text-sm font-medium">{title}</p>
        {accept ? (
          <CheckCircle className="h-5 w-5 text-green-500" />
        ) : (
          <XCircle className="h-5 w-5 text-red-500" />
        )}
      </CardContent>
    </Card>
  );
}
