"use client";

import { Card, CardContent } from "@/components/ui/card";
import { ReactNode } from "react";
import { DataSection } from "@/components/customized/DataSection";
import { CountryFlag } from "@/components/customized/CountryFlag";

type LevelingInfoCardProps = {
  courseName: string;
  dataValues: Array<{
    label: string;
    value?: string;
  }>;
  badge?: ReactNode;
  children?: ReactNode;
  className?: string;
};

export function LevelingInfoCard({
  courseName,
  dataValues,
  badge,
  children,
  className = "",
}: LevelingInfoCardProps) {
  return (
    <Card className={`mb-4 ${className}`}>
      <CardContent className="px-4">
        {/* Course Header */}
        <div className="flex items-center justify-between mb-4">
          <div className="flex items-center gap-3">
            <CountryFlag courseName={courseName} />
            <span className="text-sm font-medium text-gray-900">
              {courseName}
            </span>
          </div>
          {badge}
        </div>

        {/* Data Section */}
        <DataSection values={dataValues} />

        {/* Additional Content */}
        {children}
      </CardContent>
    </Card>
  );
}
