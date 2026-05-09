"use client";

import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import Input from "@/components/customized/Input";
import { DatePicker } from "@/components/customized/DatePicker";
import { applyCpfMask, applyPhoneMask } from "@/helpers/masks";
import dayjs from "dayjs";
import {
  userDataSchema,
  UserDataType,
  UserDataTypeWithPassword,
  userDataWithPasswordSchema,
} from "@/types/domains/user";
import { Button } from "@/components/ui/button";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import { DependentDTO } from "@/types/domains/dependent";
import { useEffect } from "react";
import { handlerHttpError } from "@/helpers/toast";
import PasswordInput from "@/components/customized/PasswordInput";
import { createDependent, updateDependentPersonalData } from "@/core/services/dependent/createUpdateDependentService";

interface DependentPersonalDataFormProps {
  dependent?: DependentDTO;
  personalData?: UserDataType;
  setPersonalData: (data: UserDataType | undefined) => void;
  onNext: () => void;
  setDependentId: (id: string) => void;
}

export function DependentPersonalDataForm({
  dependent,
  personalData,
  setPersonalData,
  onNext,
  setDependentId,
}: DependentPersonalDataFormProps) {
  const queryClient = useQueryClient();
  const {
    register,
    formState: { errors, isValid },
    setValue,
    getValues,
    watch,
    trigger,
    reset,
  } = useForm<UserDataTypeWithPassword | UserDataType>({
    resolver: zodResolver(
      !dependent ? userDataWithPasswordSchema : userDataSchema
    ),
    mode: "onChange",
  });

  useEffect(() => {
    if (personalData) {
      reset(personalData);
      trigger();
    } else {
      reset();
    }
  }, [personalData, reset, trigger]);

  // Mutation para atualizar dados pessoais
  const updateMutation = useMutation({
    mutationFn: async (data: UserDataType | UserDataTypeWithPassword) => {
      const cleanData = {
        name: data.name,
        email: data.email,
        cpf: data.cpf.replace(/\D/g, ""),
        phone: data.phone ? data.phone.replace(/\D/g, "") : "",
        birthDate: dayjs(data.birthDate).format("YYYY-MM-DD"),
      };

      if (!dependent) {
        const dependentId = await createDependent({
          ...cleanData,
          password: (data as UserDataTypeWithPassword).password,
        });
        return dependentId;
      } else {
        await updateDependentPersonalData(
          dependent?.dependent?.id ?? "",
          cleanData
        );
        return dependent?.dependent?.id ?? "";
      }
    },
    onSuccess: (dependentId) => {
      if (!dependent) {
        queryClient.invalidateQueries({ queryKey: ["dependents"] });
        queryClient.invalidateQueries({ queryKey: ["dependent", dependentId] });
      }
      setDependentId(dependentId);
      setPersonalData(getValues());
      onNext();
    },
    onError: (error) => {
      handlerHttpError(error);
    },
  });

  // Funções para aplicar máscaras
  const handleCpfChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const maskedValue = applyCpfMask(e.target.value);
    setValue("cpf", maskedValue);
    trigger("cpf");
  };

  const handlePhoneChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const maskedValue = applyPhoneMask(e.target.value);
    setValue("phone", maskedValue);
    trigger("phone");
  };

  const handleSubmit = (data: UserDataType) => {
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
      <Input
        id="cpf"
        label="CPF"
        placeholder="000.000.000-00"
        value={watch("cpf") || ""}
        onChange={handleCpfChange}
        error={!!errors.cpf}
        errorMessage={errors.cpf?.message}
        required
      />

      <Input
        id="name"
        label="Nome"
        type="text"
        placeholder="Digite seu nome"
        error={!!errors.name}
        errorMessage={errors.name?.message}
        required
        {...register("name")}
      />

      <Input
        id="email"
        label="E-mail"
        type="email"
        placeholder="Ex: email@mail.com"
        error={!!errors.email}
        errorMessage={errors.email?.message}
        required
        {...register("email")}
      />

      <Input
        id="phone"
        label="Celular"
        placeholder="(00) 00000-0000"
        value={watch("phone") || ""}
        onChange={handlePhoneChange}
        error={!!errors.phone}
        errorMessage={errors.phone?.message}
        required
      />

      <DatePicker
        id="birthDate"
        label="Data de nascimento"
        value={watch("birthDate")}
        onChange={(date) => {
          setValue("birthDate", date || "");
          trigger("birthDate");
        }}
        error={!!errors.birthDate}
        errorMessage={errors.birthDate?.message}
        required
      />

      {!dependent && (
        <PasswordInput
          label="Senha"
          required
          id="password"
          value={watch("password")}
          showValidation
          {...register("password")}
        />
      )}

      <div className="flex gap-3 pt-4">
        <Button
          type="submit"
          disabled={!isValid || updateMutation.isPending}
          className="flex-1 cursor-pointer"
        >
          {updateMutation.isPending ? "Salvando..." : "Próximo"}
        </Button>
      </div>
    </form>
  );
}
