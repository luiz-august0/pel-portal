"use client";

import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { Button } from "@/components/ui/button";
import RadioButton from "@/components/customized/RadioButton";
import { CourseSelector } from "@/components/customized/CourseSelector";
import {
  LevelingSchedulingStep,
  LevelingSchedulingStepSchema,
} from "@/types/domains/leveling";
import { useQuery } from "@tanstack/react-query";
import { getAvailableHours } from "@/core/services/leveling/levelingService";
import { Course } from "@/types/domains/course";
import { formatDateTime } from "../levelingUtils";

type LevelingSchedulingStepProps = {
  onNext: (data: LevelingSchedulingStep) => void;
  onBack: () => void;
  initialData?: LevelingSchedulingStep;
  courses: Course[];
  isLoadingCourses: boolean;
};

export function LevelingSchedulingStepComponent({
  onNext,
  onBack,
  initialData,
  courses,
  isLoadingCourses,
}: LevelingSchedulingStepProps) {
  const {
    setValue,
    handleSubmit,
    watch,
    clearErrors,
    formState: { errors, isValid },
  } = useForm<LevelingSchedulingStep>({
    resolver: zodResolver(LevelingSchedulingStepSchema),
    defaultValues: initialData,
    mode: "onChange",
  });

  const onChangeValue = (field: "courseId" | "selectedTime", value: any) => {
    setValue(field, value, { shouldValidate: true });
    clearErrors(field);
  };

  const courseId = watch("courseId");
  const selectedTime = watch("selectedTime");

  // Buscar horários disponíveis
  const { data: availableHours = [], isLoading: hoursLoading } = useQuery({
    queryKey: ["available-hours", courseId],
    queryFn: () => getAvailableHours(courseId!),
    enabled: !!courseId,
  });

  const handleCourseSelect = (selectedCourseId: number) => {
    onChangeValue("courseId", selectedCourseId);
    onChangeValue("selectedTime", "");
  };

  const handleTimeSelect = (time: string) => {
    onChangeValue("selectedTime", time);
  };

  const onSubmit = (data: LevelingSchedulingStep) => {
    onNext(data);
  };

  const isTimeDisabled = !courseId;

  return (
    <form onSubmit={handleSubmit(onSubmit)} className="space-y-6">
      {/* Seleção de curso */}
      <div className="max-sm:max-w-[calc(100vw-70px)]">
        <CourseSelector
          courses={courses}
          selectedCourseId={courseId}
          onCourseSelect={handleCourseSelect}
          isLoading={isLoadingCourses}
        />
        {errors.courseId && (
          <p className="text-sm text-red-500 mt-1">{errors.courseId.message}</p>
        )}
      </div>

      {/* Seleção de horário */}
      <div className="flex flex-col gap-2">
        <label className="text-sm font-medium text-foreground">
          Horários disponíveis
        </label>

        {isTimeDisabled ? (
          <p className="text-sm text-gray-500">Selecione um curso primeiro</p>
        ) : hoursLoading ? (
          <div className="text-center py-4">
            <div className="animate-spin rounded-full h-6 w-6 border-b-2 border-primary mx-auto"></div>
            <p className="text-sm text-gray-600 mt-2">Carregando horários...</p>
          </div>
        ) : availableHours.length === 0 ? (
          <p className="text-sm text-gray-500">
            Nenhum horário disponível para este curso.
          </p>
        ) : (
          <div className="space-y-2">
            {availableHours.map((hour) => (
              <RadioButton
                key={hour}
                name="selectedTime"
                value={hour}
                checked={selectedTime === hour}
                onChange={handleTimeSelect}
              >
                {formatDateTime(hour)}
              </RadioButton>
            ))}
          </div>
        )}
      </div>

      {/* Botões */}
      <div className="flex gap-3">
        <Button
          type="button"
          variant="outline"
          onClick={onBack}
          className="flex-1"
        >
          Voltar
        </Button>
        <Button type="submit" className="flex-1" disabled={!isValid}>
          Próximo
        </Button>
      </div>
    </form>
  );
}
