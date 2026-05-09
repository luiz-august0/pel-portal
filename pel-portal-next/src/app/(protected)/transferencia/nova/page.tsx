"use client";

import { useRouter } from "next/navigation";
import { useState } from "react";

import { HeaderPage } from "@/components/customized/HeaderPage";
import { Progress } from "@/components/ui/progress";
import {
  InscriptionDetails,
  type AvailableClass
} from "@/types/domains/inscription";
import { TransferClassSelectionStep } from "../_components/organisms/TransferClassSelectionStep";
import { TransferReviewStep } from "../_components/organisms/TransferReviewStep";

export default function NewTransferPage() {
  const router = useRouter();
  const [currentStep, setCurrentStep] = useState(1);
  const [inscription, setInscription] = useState<InscriptionDetails>();
  const [selectedClass, setSelectedClass] = useState<AvailableClass>();
  const totalSteps = 2;

  const handleBack = () => {
    if (currentStep > 1) {
      setCurrentStep(currentStep - 1);
    } else {
      router.push("/");
    }
  };

  const handleNext = () => {
    if (currentStep < totalSteps) {
      setCurrentStep(currentStep + 1);
    }
  };

  const progressPercentage = (currentStep / totalSteps) * 100;

  const getStepTitle = () => {
    switch (currentStep) {
      case 1:
        return "Agendamento";
      case 2:
        return "Revisão";
      default:
        return "Agendamento";
    }
  };

  return (
    <div className="min-h-screen bg-gray-50 p-4">
      <div className="mx-auto max-w-md">
        {/* Header */}
        <HeaderPage title="Transferência" onBack={handleBack} />

        {/* Progress */}
        <div className="mb-8">
          <div className="mb-2 flex items-center justify-between text-sm text-gray-600">
            <span>
              {currentStep}/{totalSteps} - {getStepTitle()}
            </span>
          </div>
          <Progress value={progressPercentage} className="h-2" />
        </div>

        {/* Step Content */}
        <div className="space-y-6">
          {currentStep === 1 && (
            <TransferClassSelectionStep
              onNext={handleNext}
              inscription={inscription}
              selectedClass={selectedClass}
              setInscription={setInscription}
              setSelectedClass={setSelectedClass}
            />
          )}

          {currentStep === 2 &&
            inscription &&
            selectedClass && (
              <TransferReviewStep
                inscription={inscription}
                selectedClass={selectedClass}
              />
            )}
        </div>
      </div>
    </div>
  );
}
