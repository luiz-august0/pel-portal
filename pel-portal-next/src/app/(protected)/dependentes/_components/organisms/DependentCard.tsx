"use client";

import { Button } from "@/components/ui/button";
import { Card, CardContent } from "@/components/ui/card";
import { DataSection } from "@/components/customized/DataSection";
import { applyCpfMask, applyPhoneMask } from "@/helpers/masks";
import dayjs from "dayjs";
import { DependentDTO } from "@/types/domains/dependent";
import { useRouter } from "next/navigation";
import { useMemo } from "react";
import { Badge } from "@/components/ui/badge";

interface DependentCardProps {
  dependent: DependentDTO;
  onRecognize: (id: string, recognize: boolean) => void;
  onEdit?: (dependent: DependentDTO) => void;
  isPending?: boolean;
}

export function DependentCard({
  dependent,
  onRecognize,
  onEdit,
  isPending,
}: DependentCardProps) {
  const router = useRouter();
  const initials = dependent.dependent.name
    .split(" ")
    .map((name) => name.charAt(0))
    .join("")
    .toUpperCase()
    .slice(0, 2);

  const badgeStatus = useMemo(() => {
    if (isPending) {
      return <Badge variant="destructive">Pendente</Badge>;
    }
    if (!isPending && dependent.dependent.reviewed) {
      return <Badge variant="success">Ativo</Badge>;
    }
    if (!isPending && !dependent.dependent.reviewed) {
      return <Badge variant="warning">Em análise</Badge>;
    }
    return <Badge variant="destructive">Pendente</Badge>;
  }, [isPending, dependent.dependent.reviewed]);

  return (
    <Card className="mb-4">
      <CardContent className="px-4">
        <div className="flex items-center gap-3 mb-4">
          <div className="w-10 h-10 bg-gray-300 rounded-full flex items-center justify-center text-sm font-medium text-gray-700">
            {initials}
          </div>
          <h3 className="text-lg font-medium text-gray-900 flex-1">
            {dependent.dependent.name}
          </h3>
          {badgeStatus}
        </div>

        <DataSection
          values={[
            { label: "CPF", value: applyCpfMask(dependent.dependent.cpf) },
            {
              label: "Celular",
              value: dependent.dependent.userDetails?.phone
                ? applyPhoneMask(dependent.dependent.userDetails?.phone)
                : "",
            },
            {
              label: "Data de nascimento",
              value: dayjs(dependent.dependent.userDetails?.birthDate).format(
                "DD/MM/YYYY"
              ),
            },
          ]}
        />

        {onEdit && !dependent.fromLink && (
          <div className="flex mt-3 gap-2 w-full">
            <Button
              onClick={() => onEdit(dependent)}
              className="text-blue-600 flex-1"
              variant="outline"
            >
              Editar perfil
            </Button>
            {!isPending && (
              <Button
                onClick={() => router.push(`/dependentes/${dependent.dependent.cpf}/inscricao`)}
                className="text-blue-600 flex-1"
                variant="outline"
              >
                Ver cursos
              </Button>
            )}
          </div>
        )}

        {dependent.fromLink && isPending && (
          <div className="mt-4 p-3 bg-yellow-50 rounded-lg border border-yellow-200">
            <p className="text-sm text-gray-700 mb-3">
              <strong>Você reconhece esta pessoa?</strong>
            </p>
            <p className="text-xs text-gray-600 mb-3">
              Dependente adicionado via link do aluno menor de idade.
            </p>
            <div className="flex gap-2">
              <Button
                variant="destructive"
                size="sm"
                className="cursor-pointer"
                onClick={() => onRecognize(dependent.id, false)}
              >
                Não
              </Button>
              <Button
                variant="default"
                size="sm"
                className="cursor-pointer bg-green-600 hover:bg-green-700"
                onClick={() => onRecognize(dependent.id, true)}
              >
                Sim, reconheço
              </Button>
            </div>
          </div>
        )}
      </CardContent>
    </Card>
  );
}
