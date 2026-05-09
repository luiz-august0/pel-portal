"use client";

import { useState, useEffect } from "react";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { toast } from "sonner";
import {
  Dialog,
  DialogClose,
  DialogContent,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog";
import { ScrollArea } from "@/components/ui/scroll-area";
import { XIcon } from "lucide-react";
import { DependentPersonalDataForm } from "../molecules/DependentPersonalDataForm";
import { DependentAddressForm } from "../molecules/DependentAddressForm";
import { DependentRelationshipForm } from "../molecules/DependentRelationshipForm";
import { DependentDocumentForm } from "../molecules/DependentDocumentForm";
import { cn } from "@/helpers/cn";
import { UserDataType } from "@/types/domains/user";
import { applyCepMask, applyCpfMask, applyPhoneMask } from "@/helpers/masks";
import {
  DependentAddressType,
  DependentRelationship,
  DependentRelationshipType,
} from "@/types/domains/dependent";
import { useSession } from "next-auth/react";
import dayjs from "dayjs";
import { getDependentInfo } from "@/core/services/dependent/getDependentInfoService";
import { recognizeDependent } from "@/core/services/dependent/createUpdateDependentService";

interface DependentModalProps {
  open: boolean;
  onOpenChange: (open: boolean) => void;
  dependentIdRef?: string;
  isNewDependent?: boolean;
}

export function DependentModal({
  open,
  onOpenChange,
  dependentIdRef,
  isNewDependent = false,
}: DependentModalProps) {
  const { data: session } = useSession();
  const queryClient = useQueryClient();
  const [currentStep, setCurrentStep] = useState(1);
  const [personalData, setPersonalData] = useState<UserDataType | undefined>();
  const [address, setAddress] = useState<DependentAddressType | undefined>();
  const [relationship, setRelationship] = useState<
    DependentRelationshipType | undefined
  >();
  const [dependentId, setDependentId] = useState<string | undefined>();

  // Reset quando o modal abrir/fechar
  useEffect(() => {
    if (open) setCurrentStep(1);
  }, [open]);

  useEffect(() => {
    queryClient.invalidateQueries({ queryKey: ["dependent", dependentIdRef] });
    setDependentId(dependentIdRef);
  }, [dependentIdRef]);

  const { data: dependent, isLoading } = useQuery({
    queryKey: ["dependent", dependentId],
    queryFn: async () => await getDependentInfo(dependentId ?? ""),
    enabled: !!dependentId,
  });

  useEffect(() => {
    if (dependent?.dependent) {
      setPersonalData({
        name: dependent?.dependent.name || "",
        email: dependent?.dependent.email || "",
        cpf: dependent?.dependent.cpf
          ? applyCpfMask(dependent.dependent.cpf)
          : "",
        phone: dependent?.dependent.userDetails?.phone
          ? applyPhoneMask(dependent.dependent.userDetails.phone)
          : "",
        birthDate: dependent?.dependent.userDetails?.birthDate
          ? dayjs(dependent.dependent.userDetails.birthDate).format(
              "YYYY-MM-DD"
            )
          : "",
      });
    }

    if (dependent?.dependent.address) {
      setAddress({
        cep: dependent?.dependent.address?.cep
          ? applyCepMask(dependent.dependent.address.cep)
          : "",
        street: dependent?.dependent.address?.street || "",
        number: dependent?.dependent.address?.number || "",
        complement: dependent?.dependent.address?.complement || "",
        neighborhood: dependent?.dependent.address?.neighborhood || "",
        city: dependent?.dependent.address?.city || "",
        state: dependent?.dependent.address?.state || "",
        sameAddress:
          dependent?.dependent.address?.id == session?.user?.address?.id,
      });
    }

    if (dependent?.dependent) {
      setRelationship({
        dependentRelationship:
          dependent?.dependentRelationship || DependentRelationship.SON,
        specialNeeds: !!dependent?.dependent?.userDetails?.specialNeeds,
      });
    }
  }, [dependent]);

  // Mutation para finalizar (reconhecer dependente)
  const finalizeMutation = useMutation({
    mutationFn: async () => {
      if (!dependentId) throw new Error("ID do dependente não encontrado");

      await recognizeDependent({ id: dependentId, recognize: true });
    },
    onSuccess: () => {
      toast.success("Dependente salvo com sucesso!");
      queryClient.invalidateQueries({ queryKey: ["dependents"] });
      onOpenChange(false);
    },
    onError: () => {
      toast.error("Erro ao salvar dependente. Tente novamente.");
    },
  });

  const getStepTitle = () => {
    switch (currentStep) {
      case 1:
        return "1/4 - Dados básicos";
      case 2:
        return "2/4 - Endereço";
      case 3:
        return "3/4 - Detalhes";
      case 4:
        return "4/4 - Documentos";
      default:
        return "";
    }
  };

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="max-sm:min-h-[80vh] max-h-[80vh] max-sm:min-w-[100vw] max-sm:px-0">
        <DialogHeader className="text-center max-sm:px-6">
          <DialogClose className="absolute right-4 top-4 cursor-pointer">
            <XIcon className="w-4 h-4" />
          </DialogClose>
          <DialogTitle className="text-lg font-semibold">
            {isNewDependent ? "Novo dependente" : "Editar dependente"}
          </DialogTitle>
          <p className="text-sm text-muted-foreground mt-1">{getStepTitle()}</p>

          {/* Progress bar */}
          <div className="flex gap-1 mt-3">
            {[1, 2, 3, 4].map((step) => (
              <div
                key={step}
                className={`h-1 flex-1 rounded ${
                  step <= currentStep ? "bg-primary" : "bg-gray-200"
                }`}
              />
            ))}
          </div>
        </DialogHeader>

        <div
          className={cn(
            "fixed top-0 left-0 right-0 bottom-0 z-50 flex flex-col items-center justify-center h-full p-5",
            !isLoading && "hidden"
          )}
        >
          <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-primary mx-auto"></div>
          <p className="text-gray-600 mt-2">Carregando...</p>
        </div>

        <div className="max-h-[calc(80vh-10rem)] overflow-auto">
          <div className="max-sm:px-6 space-y-6">
            {currentStep === 1 && (
              <DependentPersonalDataForm
                dependent={dependent}
                personalData={personalData}
                setPersonalData={setPersonalData}
                onNext={() => setCurrentStep(2)}
                setDependentId={setDependentId}
              />
            )}

            {currentStep === 2 && (
              <DependentAddressForm
                dependent={dependent}
                onNext={() => setCurrentStep(3)}
                onBack={() => setCurrentStep(1)}
                address={address}
                setAddress={setAddress}
              />
            )}

            {currentStep === 3 && (
              <DependentRelationshipForm
                dependent={dependent}
                onNext={() => setCurrentStep(4)}
                onBack={() => setCurrentStep(2)}
                relationship={relationship}
                setRelationship={setRelationship}
              />
            )}

            {currentStep === 4 && dependent?.id && (
              <DependentDocumentForm
                dependent={{
                  ...dependent,
                  dependent: {
                    ...dependent?.dependent,
                    userDetails: {
                      ...dependent?.dependent.userDetails,
                      specialNeeds: !!relationship?.specialNeeds,
                    },
                  },
                }}
                onBack={() => setCurrentStep(3)}
                onFinish={finalizeMutation.mutate}
              />
            )}
          </div>
        </div>
      </DialogContent>
    </Dialog>
  );
}
