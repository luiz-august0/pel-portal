"use client";

import { useRouter } from "next/navigation";
import { Button } from "@/components/ui/button";
import { Card, CardContent } from "@/components/ui/card";
import { type ActualLevel } from "@/types/domains/inscription";
import {
  ResponsiveDialog,
  ResponsiveDialogContent,
  ResponsiveDialogHeader,
  ResponsiveDialogTitle,
} from "@/components/customized/ResponsiveDialog";
import { ScrollArea } from "@/components/ui/scroll-area";
import { cn } from "@/helpers/cn";

type LevelInfoModalProps = {
  isOpen: boolean;
  onClose: () => void;
  actualLevel: ActualLevel;
};

export function LevelInfoModal({
  isOpen,
  onClose,
  actualLevel,
}: LevelInfoModalProps) {
  const router = useRouter();

  const handleGoToHome = () => {
    router.push("/");
    onClose();
  };

  return (
    <ResponsiveDialog open={isOpen} onOpenChange={onClose}>
      <ResponsiveDialogContent className="max-w-md">
        <ResponsiveDialogHeader>
          <ResponsiveDialogTitle>Seu nível</ResponsiveDialogTitle>
        </ResponsiveDialogHeader>

        <ScrollArea className={cn("overflow-y-auto max-md:p-4")}>
          <div className="space-y-6">
            {/* Card do Nível */}
            <Card className="bg-gray-100 p-3">
              <CardContent className="text-center">
                <h3 className="text-sm font-medium">{actualLevel.levelName}</h3>
              </CardContent>
            </Card>

            {/* Descrição */}
            <div className="space-y-4 text-gray-600 text-sm">
              <p>
                Este é o nível definido a partir do seu histórico conhecido.
              </p>
              <p>
                Caso já tenha experiência no idioma, agende o nivelamento na
                página inicial para ajustarmos sua turma.
              </p>
            </div>

            {/* Botões */}
            <div className="space-y-3">
              <Button
                variant="outline"
                onClick={handleGoToHome}
                className="w-full text-primary border-primary"
              >
                Ir para página inicial
              </Button>
              <Button onClick={onClose} className="w-full">
                OK
              </Button>
            </div>
          </div>
        </ScrollArea>
      </ResponsiveDialogContent>
    </ResponsiveDialog>
  );
}
