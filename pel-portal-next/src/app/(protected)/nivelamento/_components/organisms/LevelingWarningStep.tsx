"use client";

import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { Button } from "@/components/ui/button";
import {
  LevelingWarningStep,
  LevelingWarningStepSchema,
} from "@/types/domains/leveling";
import { useState } from "react";
import Select from "@/components/customized/Select";

type LevelingWarningStepProps = {
  onNext: (data: LevelingWarningStep) => void;
  initialData?: LevelingWarningStep;
  studyLanguageTime?: LevelingWarningStep;
  setStudyLanguageTime: (value: LevelingWarningStep) => void;
};

export function LevelingWarningStepComponent({
  onNext,
  studyLanguageTime,
  setStudyLanguageTime,
}: LevelingWarningStepProps) {
  const [showNeverMessage, setShowNeverMessage] = useState(false);

  const handleStudyTimeChange = (value: string) => {
    setStudyLanguageTime(value as LevelingWarningStep);
    setShowNeverMessage(value === "NEVER");
  };

  const onSubmit = () => {
    if (studyLanguageTime) {
      onNext(studyLanguageTime);
    }
  };

  const studyTimeOptions = [
    { value: "NEVER", label: "Nunca" },
    { value: "ONE_TO_TWO_YEARS", label: "1 a 2 anos" },
    { value: "TWO_TO_THREE_YEARS", label: "2 a 3 anos" },
    { value: "THREE_PLUS_YEARS", label: "+3 anos" },
  ];

  return (
    <form onSubmit={onSubmit} className="space-y-6">
      {/* Aviso inicial */}
      <div className="bg-teal-50 border border-teal-200 rounded-lg p-4">
        <div className="flex items-start gap-3">
          <span className="text-2xl">💡</span>
          <div className="space-y-2">
            <p className="text-sm font-medium text-gray-900">
              Se esta é a sua primeira vez estudando um idioma, você não precisa
              fazer a prova.{" "}
              <span className="text-teal-600">
                Basta aguardar a abertura das inscrições.
              </span>
            </p>
            <p className="text-sm text-gray-900">
              Esta prova é apenas para alunos que já{" "}
              <span className="font-medium">estudaram um idioma</span> e desejam
              continuar de onde pararam.
            </p>
          </div>
        </div>
      </div>

      {/* Campo de seleção */}
      <Select
        id="studyLanguageTime"
        label="Por quanto tempo você já estudou o idioma?"
        placeholder="Selecione uma opção"
        required
        className="w-full"
        options={studyTimeOptions.map((option) => ({
          key: option.value,
          value: option.value,
          label: option.label,
        }))}
        value={studyLanguageTime}
        onValueChange={(value) => {
          handleStudyTimeChange(value);
        }}
      />

      {/* Mensagem para "Nunca" */}
      {showNeverMessage && (
        <div className="bg-yellow-50 border border-yellow-200 rounded-lg p-4">
          <p className="text-sm text-gray-900">
            Se é a sua primeira vez estudando este idioma, não é necessário
            fazer a prova. Você pode voltar e se inscrever diretamente quando as
            turmas abrirem.
          </p>
        </div>
      )}

      {/* Botão */}
      <Button
        type="submit"
        className="w-full"
        disabled={showNeverMessage || !studyLanguageTime}
      >
        Próximo
      </Button>
    </form>
  );
}
