"use client";

import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { ProgramKnowledgeType, programKnowledgeSchema, ProgramKnowledgeSource } from "@/types/domains/register";
import { Button } from "@/components/ui/button";
import Input from "@/components/customized/Input";
import RadioButton from "@/components/customized/RadioButton";
import { useState } from "react";

interface ProgramKnowledgeFormProps {
  onNext: (data: ProgramKnowledgeType) => void;
  onBack: () => void;
  initialData?: Partial<ProgramKnowledgeType>;
}

export function ProgramKnowledgeForm({ onNext, onBack, initialData }: ProgramKnowledgeFormProps) {
  const {
    register,
    handleSubmit,
    formState: { errors, isValid },
    watch,
    setValue
  } = useForm<ProgramKnowledgeType>({
    resolver: zodResolver(programKnowledgeSchema),
    defaultValues: initialData
  });

  const selectedSource = watch("programKnowledgeSource");
  const [showOtherInput, setShowOtherInput] = useState(
    selectedSource === ProgramKnowledgeSource.OUTRO
  );

  const handleSourceChange = (source: ProgramKnowledgeSource) => {
    setValue("programKnowledgeSource", source);
    if (source === ProgramKnowledgeSource.OUTRO) {
      setShowOtherInput(true);
    } else {
      setShowOtherInput(false);
      setValue("programKnowledgeSourceOther", "");
    }
  };

  const onSubmit = (data: ProgramKnowledgeType) => {
    onNext(data);
  };

  const sourceOptions = [
    { value: ProgramKnowledgeSource.FACEBOOK, label: "Facebook" },
    { value: ProgramKnowledgeSource.INSTAGRAM, label: "Instagram" },
    { value: ProgramKnowledgeSource.GOOGLE, label: "Google" },
    { value: ProgramKnowledgeSource.WHATSAPP, label: "WhatsApp" },
    { value: ProgramKnowledgeSource.EMAIL, label: "E-mail" },
    { value: ProgramKnowledgeSource.OUTRO, label: "Outro" },
  ];

  return (
    <div className="space-y-6">
      <div className="text-center">
        <h1 className="text-lg font-semibold text-gray-800 mb-8">
          Como ficou sabendo do Programa de Ensino de Línguas?
        </h1>
      </div>

      <form onSubmit={handleSubmit(onSubmit)} className="space-y-3">
        <div className="space-y-3">
          {sourceOptions.map((option) => (
            <RadioButton
              key={option.value}
              name="programKnowledgeSource"
              value={option.value}
              checked={selectedSource === option.value}
              onChange={() => handleSourceChange(option.value)}
            >
              {option.label}
            </RadioButton>
          ))}
        </div>

        {errors.programKnowledgeSource && (
          <p className="text-sm text-red-600 mt-2">{errors.programKnowledgeSource.message}</p>
        )}

        {showOtherInput && (
          <div className="mt-4">
            <Input
              id="other"
              label="Qual?"
              type="text"
              placeholder="Ex: Jornal, rádio, meu filho..."
              error={!!errors.programKnowledgeSourceOther}
              errorMessage={errors.programKnowledgeSourceOther?.message}
              {...register("programKnowledgeSourceOther")}
            />
          </div>
        )}

        <div className="flex gap-4 pt-4">
          <Button
            type="button"
            variant="outline"
            onClick={onBack}
            className="flex-1 py-3 px-6 rounded-lg transition-colors"
          >
            Voltar
          </Button>
          <Button
            type="submit"
            disabled={!selectedSource}
            className="flex-1 bg-primary hover:bg-primary/90 text-white font-medium py-3 px-6 rounded-lg transition-colors"
          >
            Próximo
          </Button>
        </div>
      </form>
    </div>
  );
}
