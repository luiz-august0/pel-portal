"use client";

import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { PersonalDataType, personalDataSchema } from "@/types/domains/register";
import { Button } from "@/components/ui/button";
import Input from "@/components/customized/Input";
import { DatePicker } from "@/components/customized/DatePicker";
import { applyCpfMask, applyPhoneMask } from "@/helpers/masks";

interface PersonalDataFormProps {
  onNext: (data: PersonalDataType) => void;
  initialData?: Partial<PersonalDataType>;
}

export function PersonalDataForm({
  onNext,
  initialData,
}: PersonalDataFormProps) {
  const {
    register,
    handleSubmit,
    formState: { errors, isValid },
    setValue,
    watch,
    trigger,
  } = useForm<PersonalDataType>({
    resolver: zodResolver(personalDataSchema),
    defaultValues: initialData,
    mode: "onChange",
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

  const onSubmit = (data: PersonalDataType) => {
    onNext(data);
  };

  return (
    <div className="space-y-6">
      <div className="text-center">
        <h1 className="text-xl font-semibold text-gray-800 mb-6">
          Informe seus dados
        </h1>
      </div>

      <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
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
          placeholder="Ex: email@gmail.com"
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
          placeholder="Selecione sua data de nascimento"
          value={watch("birthDate")}
          onChange={(date) => {
            setValue("birthDate", date || "");
            trigger("birthDate");
          }}
          error={!!errors.birthDate}
          errorMessage={errors.birthDate?.message}
          required
        />

        <div className="pt-4">
          <Button
            type="submit"
            disabled={!isValid}
            className="w-full bg-primary hover:bg-primary/90 text-white font-medium py-3 px-6 rounded-lg transition-colors"
          >
            Próximo
          </Button>
        </div>
      </form>
    </div>
  );
}
