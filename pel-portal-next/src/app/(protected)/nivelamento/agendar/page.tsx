"use client";

import { useState } from "react";
import { useRouter } from "next/navigation";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { toast } from "sonner";
import { HeaderPage } from "@/components/customized/HeaderPage";
import { LevelingWarningStepComponent } from "../_components/organisms/LevelingWarningStep";
import { LevelingSchedulingStepComponent } from "../_components/organisms/LevelingSchedulingStep";
import { LevelingReviewStepComponent } from "../_components/organisms/LevelingReviewStep";
import {
  LevelingFormData,
  LevelingWarningStep,
  LevelingSchedulingStep,
  LevelingRegister,
} from "@/types/domains/leveling";
import { registerLeveling } from "@/core/services/leveling/levelingService";
import dayjs from "dayjs";
import ptBR from "dayjs/locale/pt-br";
import { handlerHttpError } from "@/helpers/toast";
import { getAvailableCourses } from "@/core/services/course/getAvailableCoursesService";
import { formatDateTime } from "../_components/levelingUtils";

dayjs.locale(ptBR);

export default function LevelingSchedulePage() {
  const router = useRouter();
  const [currentStep, setCurrentStep] = useState(1);
  const [formData, setFormData] = useState<LevelingFormData>({});
  const [studyLanguageTime, setStudyLanguageTime] =
    useState<LevelingWarningStep>();
  const queryClient = useQueryClient();

  // Buscar cursos disponíveis
  const { data: courses = [], isLoading: isLoadingCourses } = useQuery({
    queryKey: ["available-courses"],
    queryFn: getAvailableCourses,
  });

  // Mutation para registrar nivelamento
  const registerMutation = useMutation({
    mutationFn: async (data: LevelingRegister) => {
      await registerLeveling(data);
    },
    onSuccess: () => {
      // Formatar data/hora para o toast
      const schedulingData = formData.schedulingStep;
      if (schedulingData) {
        toast.success("Nivelamento agendado", {
          description: formatDateTime(schedulingData.selectedTime),
        });

        queryClient.invalidateQueries({
          queryKey: ["leveling-grouped-by-year"],
        });

        router.push("/nivelamento");
      }
    },
    onError: (error) => {
      handlerHttpError(error);
    },
  });

  const handleGoBack = () => {
    router.push("/nivelamento");
  };

  const getStepTitle = () => {
    switch (currentStep) {
      case 1:
        return "1/3 - Aviso";
      case 2:
        return "2/3 - Agendamento";
      case 3:
        return "3/3 - Revisão";
      default:
        return "";
    }
  };

  const handleWarningStepNext = (data: LevelingWarningStep) => {
    setFormData((prev) => ({
      ...prev,
      warningStep: data,
    }));
    setCurrentStep(2);
  };

  const handleSchedulingStepNext = (data: LevelingSchedulingStep) => {
    setFormData((prev) => ({
      ...prev,
      schedulingStep: data,
    }));
    setCurrentStep(3);
  };

  const handleSchedulingStepBack = () => {
    setCurrentStep(1);
  };

  const handleReviewStepBack = () => {
    setCurrentStep(2);
  };

  const handleFinish = () => {
    if (!formData.warningStep || !formData.schedulingStep) {
      toast.error("Dados incompletos. Tente novamente.");
      return;
    }

    const registerData: LevelingRegister = {
      courseId: formData.schedulingStep.courseId,
      levelingDate: dayjs(formData.schedulingStep.selectedTime).format("YYYY-MM-DDHH:mm:ss"),
      studyLanguageTime: formData.warningStep as any,
    };

    registerMutation.mutate(registerData);
  };

  return (
    <div className="min-h-screen bg-gray-50">
      <div className="max-w-md mx-auto p-4">
        {/* Header */}
        <HeaderPage title="Prova de Nivelamento" onBack={handleGoBack} />

        {/* Step indicator */}
        <div className="mb-6">
          <p className="text-sm text-gray-600 mb-3">{getStepTitle()}</p>

          {/* Progress bar */}
          <div className="flex gap-1">
            {[1, 2, 3].map((step) => (
              <div
                key={step}
                className={`h-1 flex-1 rounded ${
                  step <= currentStep ? "bg-primary" : "bg-gray-200"
                }`}
              />
            ))}
          </div>
        </div>

        {/* Steps content */}
        <div className="bg-white rounded-lg p-4 shadow-sm">
          {currentStep === 1 && (
            <LevelingWarningStepComponent
              onNext={handleWarningStepNext}
              initialData={formData.warningStep}
              studyLanguageTime={studyLanguageTime}
              setStudyLanguageTime={setStudyLanguageTime}
            />
          )}

          {currentStep === 2 && (
            <LevelingSchedulingStepComponent
              onNext={handleSchedulingStepNext}
              onBack={handleSchedulingStepBack}
              initialData={formData.schedulingStep}
              courses={courses}
              isLoadingCourses={isLoadingCourses}
            />
          )}

          {currentStep === 3 && (
            <LevelingReviewStepComponent
              formData={formData}
              courses={courses}
              onBack={handleReviewStepBack}
              onFinish={handleFinish}
              isSubmitting={registerMutation.isPending}
            />
          )}
        </div>
      </div>
    </div>
  );
}
