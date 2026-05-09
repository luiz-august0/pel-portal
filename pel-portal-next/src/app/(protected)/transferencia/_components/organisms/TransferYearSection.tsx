import { useState } from "react";
import { ChevronDown, ChevronUp } from "lucide-react";

import { TransfersByYear } from "@/types/domains/transfer";
import { TransferCard } from "../atoms/TransferCard";
import dayjs from "dayjs";

type TransferYearSectionProps = {
  yearData: TransfersByYear;
  onTransferClick: (transferId: number) => void;
};

export function TransferYearSection({
  yearData,
  onTransferClick,
}: TransferYearSectionProps) {
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

      {/* Transfers Cards */}
      {isExpanded && (
        <div className="border-t border-gray-100 p-2">
          {yearData.transfers.length > 0 ? (
            <div className="space-y-3">
              {yearData.transfers.map((transfer, index) => (
                <div key={transfer.id}>
                  <TransferCard
                    transfer={transfer}
                    onClick={() => onTransferClick(transfer.id)}
                  />
                  {index < yearData.transfers.length - 1 && (
                    <div className="border-t border-gray-100" />
                  )}
                </div>
              ))}
            </div>
          ) : (
            <div className="p-4 text-center text-gray-500">
              Nenhuma transferência encontrada para este ano
            </div>
          )}
        </div>
      )}
    </div>
  );
}
