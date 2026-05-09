"use client";

import { useQuery, useQueryClient } from "@tanstack/react-query";
import dayjs from "dayjs";
import "dayjs/locale/pt-br";
import { Dispatch, SetStateAction, useEffect, useMemo, useState } from "react";

import { ClassesSelection } from "@/app/(protected)/inscricao/_components/atoms/ClassesSelection";
import { ClassDetailsModal } from "@/app/(protected)/inscricao/_components/molecules/ClassDetailsModal";
import { formatDayOfWeek } from "@/app/(protected)/inscricao/_components/inscriptionUtils";
import { CourseSelector } from "@/components/customized/CourseSelector";
import { Card, CardContent } from "@/components/ui/card";
import {
  ActualLevel,
  type AvailableClass,
  InscriptionDetails,
} from "@/types/domains/inscription";
import {
  getActiveToTransfer,
  getAvailableClasses,
} from "@/core/services/inscription/inscriptionService";
import { formatTime } from "@/helpers/formatters";
import { useSession } from "next-auth/react";
import { getAvailableClassesForTransfer } from "@/core/services/transfer/transferService";
import { useSearchParams } from "next/navigation";

dayjs.locale("pt-br");

type TransferClassSelectionStepProps = {
  onNext: () => void;
  inscription?: InscriptionDetails;
  setInscription: Dispatch<SetStateAction<InscriptionDetails | undefined>>;
  selectedClass?: AvailableClass;
  setSelectedClass: Dispatch<SetStateAction<AvailableClass | undefined>>;
};

export function TransferClassSelectionStep({
  onNext,
  setInscription,
  inscription,
  selectedClass,
  setSelectedClass,
}: TransferClassSelectionStepProps) {
  const { data: session } = useSession();
  const [isClassModalOpen, setIsClassModalOpen] = useState(false);
  const params = useSearchParams();
  const inscriptionId = params.get("inscriptionId");

  // Query para buscar matrículas
  const { data: inscriptions = [], isLoading: isLoadingInscriptions } =
    useQuery<InscriptionDetails[]>({
      queryKey: ["inscriptions", session?.user?.id],
      queryFn: getActiveToTransfer,
      enabled: !!session?.user?.id,
    });

  // Query para buscar turmas disponíveis
  const { data: availableClasses = [], isLoading: isLoadingClasses } = useQuery(
    {
      queryKey: ["availableClasses", inscription],
      queryFn: () => getAvailableClassesForTransfer(inscription?.id || 0),
      enabled: !!inscription,
      gcTime: 0,
      staleTime: 0,
    }
  );

  const handleClassSelect = (classData: AvailableClass) => {
    setSelectedClass(classData);
    setIsClassModalOpen(true);
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

  const courses = useMemo(() => {
    return (
      inscriptions?.map((inscription) => ({
        id: inscription.clazz?.course?.id || 0,
        courseName: inscription.clazz?.course?.courseName || "",
        certificateCourseName:
          inscription.clazz?.course?.certificateCourseName || "",
      })) || []
    );
  }, [inscriptions]);

  const selectedCourseId = useMemo(() => {
    return inscription?.clazz?.course?.id || 0;
  }, [inscription]);

  const handleCourseSelect = (courseId: number) => {
    setInscription(
      inscriptions.find(
        (inscription) => inscription.clazz?.course?.id === courseId
      )
    );
    setSelectedClass(undefined);
  };

  useEffect(() => {
    if (inscriptions && inscriptions.length == 1) {
      setInscription(inscriptions[0]);
    }
  }, [inscriptions]);

  useEffect(() => {
    if (inscriptionId && inscriptions && inscriptions.length > 1) {
      const inscription = inscriptions.find(
        (inscription) => inscription.id === Number(inscriptionId)
      );
      if (inscription) {
        setInscription(inscription);
      }
    }
  }, [inscriptionId, inscriptions]);

  return (
    <div className="space-y-6">
      {/* Seleção do Curso */}
      <div className="max-sm:max-w-[calc(100vw-35px)]">
        <CourseSelector
          courses={courses}
          selectedCourseId={selectedCourseId}
          onCourseSelect={handleCourseSelect}
          isLoading={isLoadingInscriptions}
        />
      </div>

      <div className="space-y-2">
        {/* Nível atual */}
        {inscription && (
          <Card className="bg-gray-100 p-2">
            <CardContent className="px-2">
              <div className="flex items-center justify-between">
                <div>
                  <p className="text-sm text-gray-600">Nível atual</p>
                </div>
                <div className="flex items-center gap-1 text-sm font-medium">
                  <p>{inscription?.clazz?.level?.levelName}</p>
                </div>
              </div>
            </CardContent>
          </Card>
        )}

        {/* Horário atual */}
        {inscription && (
          <Card className="bg-gray-100 p-2">
            <CardContent className="px-2">
              <div className="flex items-center justify-between">
                <div>
                  <p className="text-sm text-gray-600">Horário atual</p>
                </div>
                <div className="flex items-center gap-1 text-sm font-medium">
                  <p>{`${formatDayOfWeek(
                    inscription?.clazz?.dayOfWeek
                  )}, ${formatTime(
                    inscription?.clazz?.startTime
                  )} - ${formatTime(inscription?.clazz?.endTime)}`}</p>
                </div>
              </div>
            </CardContent>
          </Card>
        )}
      </div>

      {/* Turmas Disponíveis */}
      <ClassesSelection
        isLoadingClasses={isLoadingClasses}
        classesByDay={classesByDay}
        selectedCourseId={selectedCourseId}
        handleClassSelect={handleClassSelect}
      />

      {selectedClass && (
        <ClassDetailsModal
          isOpen={isClassModalOpen}
          onClose={() => setIsClassModalOpen(false)}
          classData={selectedClass}
          actualLevel={inscription?.clazz?.level as ActualLevel}
          onInscribe={onNext}
          transfer
        />
      )}
    </div>
  );
}
