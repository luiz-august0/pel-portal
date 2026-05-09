"use client";

import { Card, CardContent } from "@/components/ui/card";
import { ChevronRight } from "lucide-react";
import dayjs from "dayjs";
import "dayjs/locale/pt-br";

import { AvailableClass } from "@/types/domains/inscription";
import { formatDayOfWeek } from "../inscriptionUtils";
import { formatTime } from "@/helpers/formatters";

dayjs.locale("pt-br");

export function ClassesSelection({
  isLoadingClasses,
  classesByDay,
  selectedCourseId,
  handleClassSelect,
}: {
  isLoadingClasses: boolean;
  classesByDay: Record<string, AvailableClass[]>;
  selectedCourseId: number;
  handleClassSelect: (classItem: AvailableClass) => void;
}) {
  return (
    <div>
      <h3 className="mb-4 text-sm font-medium">Turmas disponíveis</h3>
      {isLoadingClasses ? (
        <div className="text-center text-sm text-gray-500">
          Carregando turmas...
        </div>
      ) : Object.keys(classesByDay).length === 0 ? (
        <div className="text-center text-sm text-gray-500">
          {selectedCourseId > 0
            ? "Nenhuma turma disponível"
            : "Selecione um curso para ver as turmas disponíveis"}
        </div>
      ) : (
        <div className="space-y-3">
          {Object.entries(classesByDay).map(([dayOfWeek, classes]) => (
            <div key={dayOfWeek}>
              <Card className="transition-colors hover:bg-gray-50 p-2">
                <CardContent className="px-2">
                  <div className="flex items-center justify-between">
                    <div className="flex items-center gap-3 text-sm">
                      <span className="font-medium">
                        {formatDayOfWeek(dayOfWeek)}
                      </span>
                      <span className="text-gray-500">{classes.length}</span>
                    </div>
                  </div>
                  <div className="mt-2 space-y-2">
                    {classes.map((classItem) => (
                      <Card
                        key={classItem.id}
                        className="cursor-pointer transition-colors hover:bg-gray-50 p-2"
                        onClick={() => handleClassSelect(classItem)}
                      >
                        <CardContent className="px-2">
                          <div className="flex items-center justify-between">
                            <div>
                              <p className="font-medium">
                                {formatTime(classItem.startTime)} -{" "}
                                {formatTime(classItem.endTime)}
                              </p>
                            </div>
                            <div className="flex items-center gap-2">
                              <span className="text-sm text-gray-500">
                                {classItem.availableSlots -
                                  (classItem.subscribers ?? 0)}
                                /{classItem.availableSlots}
                              </span>
                              <ChevronRight className="h-4 w-4 text-gray-400" />
                            </div>
                          </div>
                        </CardContent>
                      </Card>
                    ))}
                  </div>
                </CardContent>
              </Card>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}
