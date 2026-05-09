"use client";

import Input from "@/components/customized/Input";
import RadioButton from "@/components/customized/RadioButton";
import Select from "@/components/customized/Select";
import { Button } from "@/components/ui/button";
import { Label } from "@/components/ui/label";
import { getCEP } from "@/core/services/cep/getCepService";
import { updateDependentAddress } from "@/core/services/dependent/createUpdateDependentService";
import { applyCepMask } from "@/helpers/masks";
import { handlerHttpError } from "@/helpers/toast";
import {
  dependentAddressSchema,
  DependentAddressType,
  DependentDTO,
} from "@/types/domains/dependent";
import { zodResolver } from "@hookform/resolvers/zod";
import { useMutation, useQuery } from "@tanstack/react-query";
import { utilsBr } from "js-brasil";
import { Loader2Icon } from "lucide-react";
import { useEffect } from "react";
import { useForm } from "react-hook-form";

interface DependentAddressFormProps {
  dependent?: DependentDTO;
  onNext: () => void;
  onBack?: () => void;
  address?: DependentAddressType;
  setAddress: (address: DependentAddressType | undefined) => void;
}

export function DependentAddressForm({
  dependent,
  onNext,
  onBack,
  address,
  setAddress,
}: DependentAddressFormProps) {
  const {
    register,
    formState: { errors, isValid },
    setValue,
    watch,
    trigger,
    reset,
    clearErrors,
    getValues,
  } = useForm<DependentAddressType>({
    resolver: zodResolver(dependentAddressSchema),
    mode: "onChange",
  });

  const { data: cepData, isLoading: isLoadingCep } = useQuery({
    queryKey: ["cep", watch("cep")],
    queryFn: async () => {
      const cep = watch("cep");
      return cep ? await getCEP(cep.replace(/\D/g, "")) : null;
    },
    enabled:
      !!watch("cep") &&
      (watch("cep")?.replace(/\D/g, "").length === 8 || false),
  });

  // Mutation para atualizar endereço
  const updateMutation = useMutation({
    mutationFn: async (data: DependentAddressType) => {
      if (!dependent?.dependent.id)
        throw new Error("ID do dependente não encontrado");

      const cleanData = {
        cep: data.cep?.replace(/\D/g, "") || "",
        street: data.street || "",
        number: data.number || "",
        complement: data.complement || "",
        neighborhood: data.neighborhood || "",
        city: data.city || "",
        state: data.state?.toUpperCase() || "",
        sameAddress: data.sameAddress,
      };

      await updateDependentAddress(dependent.dependent.id, cleanData);
    },
    onSuccess: () => {
      setAddress(getValues());
      onNext();
    },
    onError: (error) => {
      handlerHttpError(error);
    },
  });

  // Reset form quando address mudar
  useEffect(() => {
    if (address) {
      reset(address);
      trigger();
    } else {
      reset({
        sameAddress: false,
      });
    }
  }, [address, reset, trigger]);

  // Auto-preenchimento com dados do CEP
  useEffect(() => {
    if (cepData) {
      cepData?.logradouro && setValue("street", cepData?.logradouro);
      cepData?.bairro && setValue("neighborhood", cepData?.bairro);
      cepData?.localidade && setValue("city", cepData?.localidade);
      cepData?.uf && setValue("state", cepData?.uf);
      clearErrors();
    }
  }, [cepData, setValue, clearErrors]);

  const handleCepChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const maskedValue = applyCepMask(e.target.value);
    setValue("cep", maskedValue, { shouldValidate: true });
  };

  const handleSameAddressChange = (value: boolean) => {
    setValue("sameAddress", value);
    trigger("sameAddress");

    // Se escolher "mesmo endereço", limpar os campos
    if (value) {
      setValue("cep", "");
      setValue("street", "");
      setValue("number", "");
      setValue("complement", "");
      setValue("neighborhood", "");
      setValue("city", "");
      setValue("state", "");
    }
  };

  const handleSubmit = (data: DependentAddressType) => {
    updateMutation.mutate(data);
  };

  return (
    <form
      onSubmit={(e) => {
        e.preventDefault();
        handleSubmit(watch());
      }}
      className="space-y-4"
    >
      {
        <div className="space-y-3">
          <Label className="text-base font-medium">
            O dependente reside com você?
          </Label>
          <div className="space-y-2">
            <RadioButton
              name="sameAddress"
              value="true"
              checked={watch("sameAddress") === true}
              onChange={() => handleSameAddressChange(true)}
            >
              Sim
            </RadioButton>
            <RadioButton
              name="sameAddress"
              value="false"
              checked={watch("sameAddress") === false}
              onChange={() => handleSameAddressChange(false)}
            >
              Não
            </RadioButton>
          </div>
        </div>
      }

      {!watch("sameAddress") && (
        <>
          <Input
            id="cep"
            label="CEP"
            placeholder="00000-000"
            error={!!errors.cep}
            errorMessage={errors.cep?.message}
            right={
              isLoadingCep ? (
                <div className="absolute right-0 top-0 h-full px-3 py-2 hover:bg-transparent">
                  <Loader2Icon className="h-4 w-4 animate-spin" />
                </div>
              ) : null
            }
            required
            {...register("cep", {
              onChange: handleCepChange,
            })}
          />

          <Input
            id="street"
            label="Rua"
            type="text"
            placeholder="Rua"
            error={!!errors.street}
            errorMessage={errors.street?.message}
            required
            {...register("street")}
          />

          <Input
            id="number"
            label="Número"
            type="text"
            placeholder="Número"
            error={!!errors.number}
            errorMessage={errors.number?.message}
            required
            {...register("number")}
          />

          <Input
            id="complement"
            label="Complemento"
            type="text"
            placeholder="Complemento"
            error={!!errors.complement}
            errorMessage={errors.complement?.message}
            {...register("complement")}
          />

          <Input
            id="neighborhood"
            label="Bairro"
            type="text"
            placeholder="Bairro"
            error={!!errors.neighborhood}
            errorMessage={errors.neighborhood?.message}
            required
            {...register("neighborhood")}
          />

          <Input
            id="city"
            label="Cidade"
            type="text"
            placeholder="Cidade"
            error={!!errors.city}
            errorMessage={errors.city?.message}
            required
            {...register("city")}
          />

          <Select
            id="state"
            label="Estado"
            required
            className="w-full"
            options={utilsBr?.ESTADOS.map((state: any) => ({
              key: state?.shortname,
              label: state?.name,
              value: state?.shortname,
            }))}
            value={watch("state")}
            onValueChange={(value) => {
              setValue("state", value);
              clearErrors("state");
            }}
            errorMessage={errors.state?.message}
          />
        </>
      )}
      {/* Botões de navegação */}
      <div className="flex justify-between pt-6">
        {onBack && (
          <Button
            type="button"
            variant="outline"
            onClick={() => {
              setAddress(getValues());
              onBack();
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
