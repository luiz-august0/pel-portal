"use client";

import { useState } from "react";
import dayjs from "dayjs";
import ptBR from "dayjs/locale/pt-br";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";

import { LevelingRegistration } from "@/types/domains/leveling";
import { cancelLeveling } from "@/core/services/leveling/levelingService";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import { toast } from "sonner";
import { ConfirmationModal } from "@/components/customized/ConfirmationModal";
import { LevelingInfoCard } from "../atoms/LevelingInfoCard";
import { formatDateTime } from "../levelingUtils";

dayjs.locale(ptBR);

type LevelingCardProps = {
  registration: LevelingRegistration;
};

export function LevelingCard({ registration }: LevelingCardProps) {
  const [isLoading, setIsLoading] = useState(false);
  const [showConfirmModal, setShowConfirmModal] = useState(false);
  const queryClient = useQueryClient();

  const cancelMutation = useMutation({
    mutationFn: (levelingId: number) => cancelLeveling(levelingId),
    onSuccess: () => {
      toast.success("Nivelamento cancelado com sucesso!");
      queryClient.invalidateQueries({ queryKey: ["leveling-grouped-by-year"] });
    },
    onError: () => {
      toast.error("Erro ao cancelar nivelamento. Tente novamente.");
    },
    onSettled: () => {
      setIsLoading(false);
    },
  });

  const handleCancelClick = () => {
    setShowConfirmModal(true);
  };

  const handleConfirmCancel = () => {
    setIsLoading(true);
    setShowConfirmModal(false);
    cancelMutation.mutate(registration.id);
  };

  // Verificar se deve mostrar botão de cancelar
  const shouldShowCancelButton = 
    !registration.approvedLevel && 
    dayjs(registration.levelingSchedule.levelingDate).isAfter(dayjs());

  // Determinar status
  const getStatusBadge = () => {
    if (registration.approvedLevel) {
      return <Badge variant="success">Aprovado</Badge>;
    }
    
    if (dayjs(registration.levelingSchedule.levelingDate).isAfter(dayjs())) {
      return <Badge className="bg-blue-500">Agendado</Badge>;
    }
    
    return <Badge variant="secondary">Realizado</Badge>;
  };

  return (
    <>
      <LevelingInfoCard
        courseName={registration.course.courseName}
        badge={getStatusBadge()}
        dataValues={[
          {
            label: "Horário",
            value: formatDateTime(registration.levelingSchedule.levelingDate),
          },
          {
            label: "Resultado",
            value: registration.approvedLevel 
              ? registration.approvedLevel.levelName 
              : "-",
          },
        ]}
      >
        {/* Cancel Button */}
        {shouldShowCancelButton && (
          <div className="mt-4">
            <Button
              variant="ghost"
              className="w-full text-red-500 hover:text-red-600 hover:bg-red-50 cursor-pointer"
              onClick={handleCancelClick}
              disabled={isLoading}
            >
              {isLoading ? "Cancelando..." : "Cancelar agenda"}
            </Button>
          </div>
        )}
      </LevelingInfoCard>

      {/* Modal de confirmação */}
      <ConfirmationModal
        open={showConfirmModal}
        onOpenChange={setShowConfirmModal}
        title="Cancelar agendamento"
        description="Tem certeza de que deseja cancelar seu agendamento para a prova de nivelamento? Essa ação não poderá ser desfeita."
        confirmText="Confirmar cancelamento"
        cancelText="Manter agendamento"
        onConfirm={handleConfirmCancel}
        isLoading={isLoading}
        variant="destructive"
      />
    </>
  );
}
