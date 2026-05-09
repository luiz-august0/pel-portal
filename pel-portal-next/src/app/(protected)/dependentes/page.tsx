"use client";

import { HeaderPage } from "@/components/customized/HeaderPage";
import { Button } from "@/components/ui/button";
import { recognizeDependent } from "@/core/services/dependent/createUpdateDependentService";
import { listDependents } from "@/core/services/dependent/getDependentInfoService";
import { getUserStatus } from "@/core/services/user/userService";
import { cn } from "@/helpers/cn";
import { DependentDTO } from "@/types/domains/dependent";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { useSession } from "next-auth/react";
import { useRouter } from "next/navigation";
import { useMemo, useState } from "react";
import { toast } from "sonner";
import { Alert } from "../_components/molecules/Alert";
import { DependentCard } from "./_components/organisms/DependentCard";
import { DependentModal } from "./_components/organisms/DependentModal";

export default function DependentsPage() {
  const router = useRouter();
  const [modalOpen, setModalOpen] = useState(false);
  const [selectedDependent, setSelectedDependent] = useState<
    DependentDTO | undefined
  >();
  const queryClient = useQueryClient();
  const { data: session } = useSession();

  const { data: userStatus, isLoading: userStatusLoading } = useQuery({
    queryKey: ["user-status", session?.user?.id],
    queryFn: async () => await getUserStatus(),
    enabled: !!session?.user?.id,
  });

  const userStatusIsOk = useMemo(() => {
    return userStatus
      ?.filter((status) => !status.optional)
      .every((status) => status.checked);
  }, [userStatus]);

  const { data: dependents, isLoading } = useQuery({
    queryKey: ["dependents", session?.user?.id],
    queryFn: listDependents,
    enabled: !!session?.user?.id,
  });

  const recognizeMutation = useMutation({
    mutationFn: recognizeDependent,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["dependents"] });
      toast.success("Ação realizada com sucesso!");
    },
    onError: () => {
      toast.error("Erro ao processar a ação. Tente novamente.");
    },
  });

  const handleRecognize = (id: string, recognize: boolean) => {
    if (recognize) {
      // Se reconhecer, abrir modal para cadastro completo
      const dependent = dependents?.pending.find((d) => d.id === id);
      if (dependent) {
        setSelectedDependent(dependent);
        setModalOpen(true);
      }
    } else {
      // Se não reconhecer, apenas chamar o endpoint
      recognizeMutation.mutate({ id, recognize });
    }
  };

  const handleEditDependent = (dependent: DependentDTO) => {
    setSelectedDependent(dependent);
    setModalOpen(true);
  };

  const handleGoBack = () => {
    router.push("/");
  };

  return (
    <div className="min-h-screen bg-gray-50">
      <div className="max-w-md mx-auto p-4">
        {/* Header */}
        <HeaderPage title="Dependentes" onBack={handleGoBack} />

        {!userStatusLoading && !userStatusIsOk && (
          <div className="pb-8 space-y-3">
            <Alert
              variant="warning"
              title="Finalize seu cadastro!"
              description="Complete seus dados para cadastrar dependentes."
              onClick={() => router.push("/perfil")}
            />
          </div>
        )}

        <div className={cn("text-center py-8", !isLoading && "hidden")}>
          <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-primary mx-auto"></div>
          <p className="text-gray-600 mt-2">Carregando...</p>
        </div>

        <div className={cn("pb-4 space-y-3", !userStatusIsOk && "hidden")}>
          {dependents?.pending.length === 0 &&
            dependents?.active.length === 0 && (
              <div className="text-center py-6 text-gray-500">
                Nenhum dependente encontrado
              </div>
            )}
          {dependents?.pending.map((dependent) => (
            <DependentCard
              key={dependent.id}
              dependent={dependent}
              onRecognize={handleRecognize}
              onEdit={handleEditDependent}
              isPending
            />
          ))}
          {dependents?.active.map((dependent) => (
            <DependentCard
              key={dependent.id}
              dependent={dependent}
              onRecognize={handleRecognize}
              onEdit={handleEditDependent}
            />
          ))}
        </div>

        {/* Novo Dependente Button */}
        <div className="bottom-4 left-4 right-4 max-w-md mx-auto">
          <Button
            className="w-full"
            onClick={() => {
              setSelectedDependent(undefined);
              setModalOpen(true);
            }}
            disabled={!userStatusIsOk}
          >
            Novo dependente
          </Button>
        </div>

        {/* Modal de Dependente */}
        {modalOpen && (
          <DependentModal
            open={modalOpen}
            onOpenChange={(open) => {
              setModalOpen(open);
              if (!open) {
                setSelectedDependent(undefined);
                queryClient.invalidateQueries({ queryKey: ["dependents"] });
              }
            }}
            dependentIdRef={selectedDependent?.dependent?.id}
            isNewDependent={!selectedDependent}
          />
        )}
      </div>
    </div>
  );
}
