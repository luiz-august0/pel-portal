"use client";

import { useState } from "react";
import { ChevronDown, ChevronUp } from "lucide-react";
import { LevelingByYear } from "@/types/domains/leveling";
import { LevelingCard } from "../molecules/LevelingCard";
import dayjs from "dayjs";

type LevelingYearSectionProps = {
  yearData: LevelingByYear;
};

export function LevelingYearSection({ yearData }: LevelingYearSectionProps) {
  const [isExpanded, setIsExpanded] = useState(yearData.year === dayjs().year().toString());

  return (
    <div className="bg-white rounded-lg shadow-sm overflow-hidden">
      {/* Year Header */}
      <button
        className="w-full p-4 flex items-center justify-between bg-white hover:bg-gray-50 transition-colors cursor-pointer"
        onClick={() => setIsExpanded(!isExpanded)}
      >
        <h2 className="text-lg font-semibold text-gray-900">{yearData.year}</h2>
        {isExpanded ? (
          <ChevronUp className="w-5 h-5 text-gray-500" />
        ) : (
          <ChevronDown className="w-5 h-5 text-gray-500" />
        )}
      </button>

      {/* Leveling Cards */}
      {isExpanded && (
        <div className="border-t border-gray-100 p-2">
          {yearData.levelingRegistrations.length > 0 ? (
            <div className="space-y-3">
              {yearData.levelingRegistrations.map((registration, index) => (
                <div key={registration.id}>
                  <LevelingCard registration={registration} />
                  {index < yearData.levelingRegistrations.length - 1 && (
                    <div className="border-t border-gray-100" />
                  )}
                </div>
              ))}
            </div>
          ) : (
            <div className="p-4 text-center text-gray-500">
              Nenhum nivelamento encontrado para este ano
            </div>
          )}
        </div>
      )}
    </div>
  );
}
