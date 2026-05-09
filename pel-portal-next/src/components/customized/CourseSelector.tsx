"use client";

import { Course } from "@/types/domains/course";
import { CountryFlag } from "./CountryFlag";
import { cn } from "@/helpers/cn";

type CourseSelectorProps = {
  courses: Course[];
  selectedCourseId?: number;
  onCourseSelect: (courseId: number) => void;
  disabled?: boolean;
  isLoading?: boolean;
};

export function CourseSelector({
  courses,
  selectedCourseId,
  onCourseSelect,
  disabled = false,
  isLoading = false,
}: CourseSelectorProps) {
  return (
    <div className="flex flex-col gap-2">
      <label className="text-lg font-medium text-foreground">
        Selecione o curso
      </label>
      {isLoading ? (
        <p className="text-sm text-gray-600">Carregando cursos...</p>
      ) : courses.length === 0 ? (
        <p className="text-sm text-gray-600">Nenhum curso encontrado</p>
      ) : (
        <div className="flex gap-3 overflow-auto items-start">
          {courses.map((course) => (
            <button
              key={course.id}
              type="button"
              onClick={() => !disabled && onCourseSelect(course.id)}
              disabled={disabled}
              className={cn(
                "flex flex-col min-w-[100px] items-center p-3 border-2 rounded-lg transition-all cursor-pointer",
                "hover:bg-gray-50",
                selectedCourseId === course.id
                  ? "border-primary bg-primary/5"
                  : "border-gray-200",
                disabled && "opacity-50 cursor-not-allowed"
              )}
            >
              <CountryFlag courseName={course.courseName} />
              <span className="text-xs font-medium text-gray-900 mt-2 text-center leading-tight break-words w-full">
                {course.courseName}
              </span>
            </button>
          ))}
        </div>
      )}
    </div>
  );
}
