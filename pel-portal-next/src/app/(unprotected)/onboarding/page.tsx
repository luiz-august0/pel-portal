"use client";

import { useRouter, useSearchParams } from "next/navigation";
import dayjs from "dayjs";
import { Button } from "@/components/ui/button";

export default function OnboardingPage() {
  const router = useRouter();
  const params = useSearchParams();

  const handleRegistration = () => {
    router.push(`/registro?${params.toString()}`);
  };

  const handleLogin = () => {
    router.push(`/login?${params.toString()}`);
  };

  return (
    <div className="min-h-screen flex items-center justify-center p-4">
      <div className="bg-white rounded-lg shadow-lg max-w-sm w-full p-8 text-center">
        {/* Logo */}
        <div className="mb-8">
          <h1 className="text-xl font-semibold text-gray-800">PEL</h1>
          <p className="text-sm text-gray-600">Programa de Ensino de Línguas</p>
        </div>

        {/* Action Buttons */}
        <div className="space-y-4 mb-8">
          <Button
            onClick={handleRegistration}
            className="w-full text-white font-medium py-5 px-6 rounded-lg transition-colors"
          >
            Quero ser aluno PEL
          </Button>
          <Button
            onClick={handleLogin}
            variant="outline"
            className="w-full text-primary font-medium py-5 px-6 border-1 border-primary hover:bg-primary/10 hover:text-primary"
          >
            Já sou aluno PEL
          </Button>
        </div>

        {/* Footer */}
        <div className="text-xs text-gray-500">
          <p>{`© ${dayjs().year()} PEL - Programa de Ensino de Línguas.`}</p>
          <p>Todos os direitos reservados.</p>
        </div>
      </div>
    </div>
  );
}
