"use client";

import { Button } from "@/components/ui/button";
import { LevelingFormData } from "@/types/domains/leveling";
import dayjs from "dayjs";
import ptBR from "dayjs/locale/pt-br";
import { LevelingInfoCard } from "../atoms/LevelingInfoCard";
import { Course } from "@/types/domains/course";
import { formatDateTime } from "../levelingUtils";

dayjs.locale(ptBR);

type LevelingReviewStepProps = {
  formData: LevelingFormData;
  courses: Course[];
  onBack: () => void;
  onFinish: () => void;
  isSubmitting?: boolean;
};

export function LevelingReviewStepComponent({ 
  formData, 
  courses, 
  onBack, 
  onFinish, 
  isSubmitting = false 
}: LevelingReviewStepProps) {
  const selectedCourse = courses.find(
    (course) => course.id === formData.schedulingStep?.courseId
  );

  return (
    <div className="space-y-6">
      {/* Card de revisão */}
      {selectedCourse && (
        <LevelingInfoCard
          courseName={selectedCourse.courseName}
          dataValues={[
            {
              label: "Horário",
              value: formatDateTime(formData.schedulingStep?.selectedTime || ""),
            },
          ]}
        />
      )}

      {/* Botões */}
      <div className="flex gap-3">
        <Button
          type="button"
          variant="outline"
          onClick={onBack}
          className="flex-1"
          disabled={isSubmitting}
        >
          Voltar
        </Button>
        <Button
          type="button"
          onClick={onFinish}
          className="flex-1"
          disabled={isSubmitting}
        >
          {isSubmitting ? "Processando..." : "Concluir solicitação"}
        </Button>
      </div>
    </div>
  );
}
