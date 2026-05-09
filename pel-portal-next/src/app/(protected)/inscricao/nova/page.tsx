"use client";

import { useEffect, useState } from "react";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { ArrowLeft } from "lucide-react";
import { useRouter } from "next/navigation";

import { Button } from "@/components/ui/button";
import { Progress } from "@/components/ui/progress";
import {
  InscriptionStep1Schema,
  type InscriptionStep1,
  type AvailableClass,
  type ActualLevel,
  InscriptionStep2,
  InscriptionStep2Schema,
  InscriptionStep3,
  InscriptionStep3Schema,
  InscriptionStep4,
  InscriptionStep4Schema,
} from "@/types/domains/inscription";
import { InscriptionClassSelectionStep } from "../_components/organisms/InscriptionClassSelectionStep";
import { InscriptionPaymentStep } from "../_components/organisms/InscriptionPaymentStep";
import { InscriptionConsentStep } from "../_components/organisms/InscriptionConsentStep";
import { InscriptionReviewStep } from "../_components/organisms/InscriptionReviewStep";
import { HeaderPage } from "@/components/customized/HeaderPage";
import { useSearchParams } from "next/navigation";
import { getInscriptionDetails } from "@/core/services/inscription/inscriptionService";

export default function NewInscriptionPage() {
  const router = useRouter();
  const params = useSearchParams();
  const [currentStep, setCurrentStep] = useState(1);
  const [inscriptionId, setInscriptionId] = useState<number>();
  const [selectedClass, setSelectedClass] = useState<AvailableClass>();
  const [actualLevel, setActualLevel] = useState<ActualLevel>();
  const [contract, setContract] = useState<string>("");
  const totalSteps = 4;

  const form = useForm<InscriptionStep1>({
    resolver: zodResolver(InscriptionStep1Schema),
    defaultValues: {
      courseId: 0,
      classId: 0,
    },
  });

  const formPayment = useForm<InscriptionStep2>({
    resolver: zodResolver(InscriptionStep2Schema),
  });

  const formConsent = useForm<InscriptionStep3>({
    resolver: zodResolver(InscriptionStep3Schema),
    defaultValues: {
      acceptContract: false,
      acceptImageTerm: true,
    },
  });

  const formReview = useForm<InscriptionStep4>({
    resolver: zodResolver(InscriptionStep4Schema),
    defaultValues: {
      acceptContract: false,
      acceptImageTerm: false,
    },
  });

  const handleBack = () => {
    if (currentStep > 2) {
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

  const handleContinueInscription = async(inscriptionId: number) => {
    const inscription = await getInscriptionDetails(inscriptionId);
    if (inscription) {
      setCurrentStep(2);
      setInscriptionId(inscriptionId);
      setSelectedClass(inscription.clazz);
      setActualLevel(inscription.clazz.level as ActualLevel);
      setContract(inscription.contractText)
      form.setValue("courseId", inscription.clazz.course.id);
      form.setValue("classId", inscription.clazz.id);
    }
  };

  useEffect(() => {
    const inscriptionIdParam = params.get("inscriptionId");
    if (inscriptionIdParam) {
      handleContinueInscription(Number(inscriptionIdParam));
    }
  }, [params]);

  const progressPercentage = (currentStep / totalSteps) * 100;

  const getStepTitle = () => {
    switch (currentStep) {
      case 1:
        return "Turma";
      case 2:
        return "Forma de pagamento";
      case 3:
        return "Consentimento";
      case 4:
        return "Revisão";
      default:
        return "Turma";
    }
  };

  return (
    <div className="min-h-screen bg-gray-50 p-4">
      <div className="mx-auto max-w-md">
        {/* Header */}
        <HeaderPage title="Inscrição" onBack={handleBack} />

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
            <InscriptionClassSelectionStep
              form={form}
              onNext={handleNext}
              setInscriptionId={setInscriptionId}
              setSelectedClass={setSelectedClass}
              setActualLevel={setActualLevel}
            />
          )}

          {currentStep === 2 &&
            inscriptionId &&
            selectedClass &&
            actualLevel && (
              <InscriptionPaymentStep
                formPayment={formPayment}
                inscriptionId={inscriptionId}
                selectedClass={selectedClass}
                actualLevel={actualLevel}
                onNext={handleNext}
                onContractReceived={setContract}
              />
            )}

          {currentStep === 3 && inscriptionId && contract && (
            <InscriptionConsentStep
              formConsent={formConsent}
              inscriptionId={inscriptionId}
              contract={contract}
              onNext={handleNext}
            />
          )}

          {currentStep === 4 &&
            inscriptionId &&
            selectedClass &&
            actualLevel &&
            formPayment.watch("paymentForm") && (
              <InscriptionReviewStep
                formReview={formReview}
                paymentForm={formPayment}
                inscriptionId={inscriptionId}
                selectedClass={selectedClass}
                actualLevel={actualLevel}
                acceptContract={formConsent.watch("acceptContract")}
                acceptImageTerm={formConsent.watch("acceptImageTerm")}
              />
            )}
        </div>
      </div>
    </div>
  );
}
