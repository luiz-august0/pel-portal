"use client";

import { Dispatch, SetStateAction, useState } from "react";
import { UseFormReturn } from "react-hook-form";
import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";
import { Info, ChevronRight } from "lucide-react";
import dayjs from "dayjs";
import "dayjs/locale/pt-br";

import { Button } from "@/components/ui/button";
import { Card, CardContent } from "@/components/ui/card";
import { CourseSelector } from "@/components/customized/CourseSelector";
import {
  getActualLevel,
  getAvailableClasses,
  registerInscription,
} from "@/core/services/inscription/inscriptionService";
import { getAvailableCourses } from "@/core/services/course/getAvailableCoursesService";
import { type Course } from "@/types/domains/course";
import {
  type InscriptionStep1,
  type AvailableClass,
  type ActualLevel,
} from "@/types/domains/inscription";
import { LevelInfoModal } from "../molecules/LevelInfoModal";
import { ClassDetailsModal } from "../molecules/ClassDetailsModal";
import { formatDayOfWeek } from "../inscriptionUtils";
import { formatTime } from "@/helpers/formatters";
import { handlerHttpError } from "@/helpers/toast";
import { ClassesSelection } from "../atoms/ClassesSelection";

dayjs.locale("pt-br");

type InscriptionClassSelectionStepProps = {
  form: UseFormReturn<InscriptionStep1>;
  onNext: () => void;
  setInscriptionId: Dispatch<SetStateAction<number | undefined>>;
  setSelectedClass: Dispatch<SetStateAction<AvailableClass | undefined>>;
  setActualLevel: Dispatch<SetStateAction<ActualLevel | undefined>>;
};

export function InscriptionClassSelectionStep({
  form,
  onNext,
  setInscriptionId,
  setSelectedClass: setSelectedClassProp,
  setActualLevel: setActualLevelProp,
}: InscriptionClassSelectionStepProps) {
  const queryClient = useQueryClient();
  const [selectedCourseId, setSelectedCourseId] = useState<number>(0);
  const [selectedClass, setSelectedClass] = useState<AvailableClass | null>(
    null
  );
  const [isLevelModalOpen, setIsLevelModalOpen] = useState(false);
  const [isClassModalOpen, setIsClassModalOpen] = useState(false);

  const { setValue } = form;

  // Query para buscar cursos disponíveis
  const { data: courses = [], isLoading: isLoadingCourses } = useQuery<Course[]>({
    queryKey: ["courses"],
    queryFn: getAvailableCourses,
  });

  // Query para buscar nível atual do usuário
  const { data: actualLevel } = useQuery({
    queryKey: ["actualLevel", selectedCourseId],
    queryFn: () => getActualLevel(selectedCourseId),
    enabled: selectedCourseId > 0,
  });

  // Query para buscar turmas disponíveis
  const { data: availableClasses = [], isLoading: isLoadingClasses } = useQuery(
    {
      queryKey: ["availableClasses", selectedCourseId, actualLevel?.id],
      queryFn: () => getAvailableClasses(selectedCourseId, actualLevel!.id),
      enabled: selectedCourseId > 0 && !!actualLevel,
      gcTime: 0,
      staleTime: 0,
    }
  );

  // Mutation para inscrição
  const inscriptionMutation = useMutation({
    mutationFn: registerInscription,
    onSuccess: (id: number) => {
      setInscriptionId(id);
      if (selectedClass) {
        setSelectedClassProp(selectedClass);
      }
      if (actualLevel) {
        setActualLevelProp(actualLevel);
      }
      setIsClassModalOpen(false);
      onNext(); // Avança para o próximo step
      queryClient.invalidateQueries({
        queryKey: ["inscriptions-grouped-by-year"],
      });
      queryClient.invalidateQueries({
        queryKey: ["last-inscription"],
      });
    },
    onError: (error) => {
      handlerHttpError(error);
    },
  });

  const handleCourseSelect = (courseId: number) => {
    setSelectedCourseId(courseId);
    setSelectedClass(null);
    setValue("courseId", courseId, { shouldValidate: true });
    setValue("classId", 0, { shouldValidate: true });
  };

  const handleClassSelect = (classData: AvailableClass) => {
    setSelectedClass(classData);
    setValue("classId", classData.id, { shouldValidate: true });
    setIsClassModalOpen(true);
  };

  const handleLevelInfoClick = () => {
    setIsLevelModalOpen(true);
  };

  // Agrupar turmas por dia da semana
  const classesByDay = availableClasses.reduce((acc, classItem) => {
    const day = classItem.dayOfWeek;
    if (!acc[day]) {
      acc[day] = [];
    }
    acc[day].push(classItem);
    return acc;
  }, {} as Record<string, AvailableClass[]>);

  return (
    <div className="space-y-6">
      {/* Seleção do Curso */}
      <div className="max-sm:max-w-[calc(100vw-35px)]">
        <CourseSelector
          courses={courses}
          selectedCourseId={selectedCourseId}
          onCourseSelect={handleCourseSelect}
          isLoading={isLoadingCourses}
        />
      </div>

      {/* Nível Atual */}
      {actualLevel && (
        <Card className="bg-gray-100 p-2">
          <CardContent className="px-2">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm text-gray-600">Nível atual</p>
              </div>
              <div className="flex items-center gap-1 text-sm font-medium">
                <p>{actualLevel.levelName}</p>
                <Button
                  variant="ghost"
                  size="icon"
                  onClick={handleLevelInfoClick}
                  className="h-8 w-8"
                >
                  <Info className="h-4 w-4 text-blue-500" />
                </Button>
              </div>
            </div>
          </CardContent>
        </Card>
      )}

      {/* Turmas Disponíveis */}
      <ClassesSelection
        isLoadingClasses={isLoadingClasses}
        classesByDay={classesByDay}
        selectedCourseId={selectedCourseId}
        handleClassSelect={handleClassSelect}
      />

      {/* Modals */}
      {actualLevel && (
        <LevelInfoModal
          isOpen={isLevelModalOpen}
          onClose={() => setIsLevelModalOpen(false)}
          actualLevel={actualLevel}
        />
      )}

      {selectedClass && (
        <ClassDetailsModal
          isOpen={isClassModalOpen}
          onClose={() => setIsClassModalOpen(false)}
          classData={selectedClass}
          actualLevel={actualLevel}
          onInscribe={() => inscriptionMutation.mutate(selectedClass.id)}
          isLoading={inscriptionMutation.isPending}
        />
      )}
    </div>
  );
}
