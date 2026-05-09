"use client";

import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { SpecialNeedsType, specialNeedsSchema } from "@/types/domains/register";
import { Button } from "@/components/ui/button";
import RadioButton from "@/components/customized/RadioButton";

interface SpecialNeedsFormProps {
  onNext: (data: SpecialNeedsType) => void;
  onBack: () => void;
  initialData?: Partial<SpecialNeedsType>;
}

export function SpecialNeedsForm({ onNext, onBack, initialData }: SpecialNeedsFormProps) {
  const {
    handleSubmit,
    setValue,
    formState: { errors, isValid },
    watch
  } = useForm<SpecialNeedsType>({
    resolver: zodResolver(specialNeedsSchema),
    defaultValues: initialData
  });

  const specialNeeds = watch("specialNeeds");

  const onSubmit = (data: SpecialNeedsType) => {
    onNext(data);
  };

  return (
    <div className="space-y-8">
      <div className="text-center">
        <h1 className="text-xl font-semibold text-gray-800 mb-8">
          Você possui necessidades especiais?
        </h1>
      </div>

      <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
        <div className="space-y-4">
          <RadioButton
            name="specialNeeds"
            value="true"
            checked={specialNeeds === true}
            onChange={() => setValue("specialNeeds", true)}
          >
            Sim
          </RadioButton>

          <RadioButton
            name="specialNeeds"
            value="false"
            checked={specialNeeds === false}
            onChange={() => setValue("specialNeeds", false)}
          >
            Não
          </RadioButton>
        </div>

        {errors.specialNeeds && (
          <p className="text-sm text-red-600 mt-2">{errors.specialNeeds.message}</p>
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
            disabled={specialNeeds === undefined}
            className="flex-1 bg-primary hover:bg-primary/90 text-white font-medium py-3 px-6 rounded-lg transition-colors"
          >
            Próximo
          </Button>
        </div>
      </form>
    </div>
  );
}
