"use client";

import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { useMutation } from "@tanstack/react-query";
import { toast } from "sonner";
import {
  DependentRelationshipType,
  dependentRelationshipSchema,
  DependentRelationship,
  dependentRelationshipLabels,
  DependentDTO,
} from "@/types/domains/dependent";
import { updateDependentRelationshipAndSpecialNeeds } from "@/core/services/dependent/createUpdateDependentService";
import Select from "@/components/customized/Select";
import { Label } from "@/components/ui/label";
import { Button } from "@/components/ui/button";
import RadioButton from "@/components/customized/RadioButton";
import { useEffect } from "react";
import { handlerHttpError } from "@/helpers/toast";

interface DependentRelationshipFormProps {
  dependent?: DependentDTO;
  onNext: () => void;
  onBack?: () => void;
  relationship?: DependentRelationshipType;
  setRelationship: (
    relationship: DependentRelationshipType | undefined
  ) => void;
}

export function DependentRelationshipForm({
  dependent,
  onNext,
  onBack,
  relationship,
  setRelationship,
}: DependentRelationshipFormProps) {
  const {
    formState: { errors, isValid },
    setValue,
    watch,
    reset,
    clearErrors,
    getValues,
    trigger,
  } = useForm<DependentRelationshipType>({
    resolver: zodResolver(dependentRelationshipSchema),
    mode: "onChange",
    defaultValues: {
      dependentRelationship: relationship?.dependentRelationship,
      specialNeeds: relationship?.specialNeeds,
    },
  });

  // Mutation para atualizar relacionamento e necessidades especiais
  const updateMutation = useMutation({
    mutationFn: async (data: DependentRelationshipType) => {
      if (!dependent?.dependent.id)
        throw new Error("ID do dependente não encontrado");

      await updateDependentRelationshipAndSpecialNeeds(dependent.dependent.id, {
        dependentRelationship: data.dependentRelationship,
        specialNeeds: data.specialNeeds,
      });
    },
    onSuccess: () => {
      setRelationship(getValues());
      onNext();
    },
    onError: (error) => {
      handlerHttpError(error);
    },
  });

  // Reset form quando relationship mudar
  useEffect(() => {
    if (relationship) {
      reset(relationship);
      trigger();
    } else {
      reset();
    }
  }, [relationship, reset, trigger]);

  const handleSubmit = (data: DependentRelationshipType) => {
    updateMutation.mutate(data);
  };

  const handleSpecialNeedsChange = (value: boolean) => {
    setValue("specialNeeds", value);
    trigger("specialNeeds");
  };

  return (
    <form
      onSubmit={(e) => {
        e.preventDefault();
        handleSubmit(watch());
      }}
      className="space-y-4"
    >
      <div className="space-y-3">
        <Label className="text-base font-medium">Parentesco</Label>
        <Select
          className="w-full"
          options={Object.keys(DependentRelationship).map((relationship) => ({
            key: relationship,
            label:
              dependentRelationshipLabels[
                relationship as DependentRelationship
              ],
            value: relationship,
          }))}
          value={watch("dependentRelationship")}
          onValueChange={(value) => {
            setValue("dependentRelationship", value as DependentRelationship);
            clearErrors("dependentRelationship");
          }}
          errorMessage={errors.dependentRelationship?.message}
        />
      </div>

      <div className="space-y-3">
        <Label className="text-base font-medium">
          O dependente possui alguma necessidade especial?
        </Label>
        <div className="space-y-2">
          <RadioButton
            name="specialNeeds"
            value="false"
            checked={watch("specialNeeds") === false}
            onChange={() => handleSpecialNeedsChange(false)}
          >
            Não
          </RadioButton>

          <RadioButton
            name="specialNeeds"
            value="true"
            checked={watch("specialNeeds") === true}
            onChange={() => handleSpecialNeedsChange(true)}
          >
            Sim
          </RadioButton>
        </div>
        {errors.specialNeeds && (
          <p className="text-sm text-red-500">{errors.specialNeeds.message}</p>
        )}
      </div>

      {/* Botões de navegação */}
      <div className="flex justify-between pt-6">
        {onBack && (
          <Button
            type="button"
            variant="outline"
            onClick={() => {
              onBack();
              setRelationship(getValues());
            }}
            disabled={updateMutation.isPending}
          >
            Voltar
          </Button>
        )}
        <Button
          type="submit"
          disabled={!isValid || updateMutation.isPending}
          className="ml-auto"
        >
          {updateMutation.isPending ? "Salvando..." : "Próximo"}
        </Button>
      </div>
    </form>
  );
}
