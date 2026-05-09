"use client";

import { CheckCircle, Copy, Info, XIcon } from "lucide-react";
import { Button } from "@/components/ui/button";
import {
  Dialog,
  DialogClose,
  DialogContent,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog";
import Input from "@/components/customized/Input";
import { toast } from "sonner";
import { useSession } from "next-auth/react";
import { Badge } from "@/components/ui/badge";
import { useState } from "react";
import { useQuery } from "@tanstack/react-query";
import dayjs from "dayjs";
import relativeTime from "dayjs/plugin/relativeTime";
import { cn } from "@/helpers/cn";
import { responsibleRelationshipLabels } from "@/types/domains/dependent";
import ptBR from "dayjs/locale/pt-br";
import { getResponsible } from "@/core/services/dependent/getDependentInfoService";
import { regenerateResponsibleLink } from "@/core/services/user/userService";

dayjs.extend(relativeTime);
dayjs.locale(ptBR);

interface ResponsibleLinkModalProps {
  open: boolean;
  onOpenChange: (open: boolean) => void;
}

export function ResponsibleLinkModal({
  open,
  onOpenChange,
}: ResponsibleLinkModalProps) {
  const { data: session, update } = useSession();
  const [isGenerating, setIsGenerating] = useState(false);
  const responsibleToken = session?.user?.responsibleToken;
  const generatedLink = `${window.location.origin}/onboarding?token=${responsibleToken}`;
  const isLinkExpired =
    session?.user?.responsibleTokenExpiresAt &&
    new Date(session.user.responsibleTokenExpiresAt) < new Date();

  const { data: responsible } = useQuery({
    queryKey: ["responsible", session?.user?.id],
    queryFn: async () => await getResponsible(),
    enabled: !!session?.user?.authorized && session?.user?.userDetails?.minor,
  });

  const copyLinkToClipboard = async () => {
    if (responsibleToken) {
      try {
        await navigator.clipboard.writeText(generatedLink);
        toast.success("Link copiado para a área de transferência!");
      } catch (error) {
        // Fallback para navegadores mais antigos
        const textArea = document.createElement("textarea");
        textArea.value = generatedLink;
        document.body.appendChild(textArea);
        textArea.select();
        document.execCommand("copy");
        document.body.removeChild(textArea);
        toast.success("Link copiado para a área de transferência!");
      }
    }
  };

  const handleGenerateLink = async () => {
    setIsGenerating(true);
    try {
      // Chama o endpoint para gerar o link
      const response = await regenerateResponsibleLink();

      // Atualiza a sessão do usuário com os novos dados
      await update({
        ...session,
        user: {
          ...session?.user,
          responsibleToken: response.token,
          responsibleTokenGeneratedAt: response.generatedAt,
          responsibleTokenExpiresAt: response.expires,
        },
      });

      // Copia automaticamente o link para o clipboard
      const newGeneratedLink = `${window.location.origin}/onboarding?token=${response.token}`;
      try {
        await navigator.clipboard.writeText(newGeneratedLink);
        toast.success("Link gerado e copiado para a área de transferência!");
      } catch (error) {
        // Fallback para navegadores mais antigos
        const textArea = document.createElement("textarea");
        textArea.value = newGeneratedLink;
        document.body.appendChild(textArea);
        textArea.select();
        document.execCommand("copy");
        document.body.removeChild(textArea);
        toast.success("Link gerado e copiado para a área de transferência!");
      }
    } catch (error) {
      console.error("Erro ao gerar link do responsável:", error);
      toast.error("Erro ao gerar link. Tente novamente.");
    } finally {
      setIsGenerating(false);
    }
  };

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="sm:max-w-md">
        <DialogHeader className="text-center">
          <DialogClose className="absolute right-4 top-4 cursor-pointer">
            <XIcon className="w-4 h-4" />
          </DialogClose>
          <DialogTitle className="text-lg font-semibold">
            Responsável
          </DialogTitle>
          <p className="text-sm text-gray-600">
            Compartilhe o link com o seu responsável.
          </p>
        </DialogHeader>

        <div className={cn("space-y-4", session?.user?.authorized && "hidden")}>
          <div className="relative">
            {responsibleToken ? (
              <div className="flex flex-row items-center p-3 justify-between rounded-xl border-2 border-gray-200 bg-gray-50">
                <span className="text-sm">Link do Responsável</span>
                <Badge className="bg-yellow-300 text-black">{`Expira ${dayjs(
                  session?.user?.responsibleTokenExpiresAt
                ).fromNow()}`}</Badge>
              </div>
            ) : (
              <div className="flex flex-row items-center p-3 justify-between rounded-xl border-2 border-gray-200 bg-gray-50">
                <span className="text-sm">Link do Responsável</span>
                <Badge className="bg-destructive">Pendente</Badge>
              </div>
            )}
          </div>

          {isLinkExpired && (
            <div className="flex items-center gap-2">
              <div className="w-4 h-4 bg-red-600 rounded-full flex items-center justify-center">
                <span className="text-white text-xs font-bold">!</span>
              </div>
              <span className="text-sm">Link expirado</span>
            </div>
          )}

          {isLinkExpired ||
            (!responsibleToken && (
              <div className="flex flex-row items-center gap-2 p-3 bg-gray-100 rounded-xl border border-gray-200">
                <Info className="w-4 h-4" color="blue" />
                <span className="text-sm">{`Gere ${
                  isLinkExpired ? "um novo" : "o link"
                } clicando no botão abaixo`}</span>
              </div>
            ))}

          <div className="flex gap-3">
            {isLinkExpired || !responsibleToken ? (
              <Button
                className="flex-1"
                onClick={handleGenerateLink}
                disabled={isGenerating}
              >
                {isGenerating ? "Gerando..." : "Gerar link"}
              </Button>
            ) : (
              <Button
                className="flex-1"
                onClick={copyLinkToClipboard}
                disabled={!responsibleToken}
              >
                <Copy className="w-4 h-4 mr-2" />
                Copiar link
              </Button>
            )}
          </div>
        </div>

        <div
          className={cn("space-y-4", !session?.user?.authorized && "hidden")}
        >
          {/* Link do Responsável - Seção Ativa */}
          <div className="flex items-center justify-between p-3 bg-gray-50 rounded-xl border border-gray-200">
            <span className="text-sm font-medium">Link do Responsável</span>
            <Badge variant="success">Ativo</Badge>
          </div>

          {/* Informações do Responsável */}
          {responsible && (
            <div className="space-y-3">
              {/* Nome do responsável com iniciais */}
              <div className="flex items-center justify-between p-3 bg-gray-50 rounded-xl border border-gray-200">
                <div className="flex items-center gap-3">
                  <div className="w-8 h-8 bg-gray-300 rounded-full flex items-center justify-center">
                    <span className="text-gray-600 font-medium text-sm">
                      {responsible.dependent.name
                        .split(" ")
                        .map((name) => name.charAt(0))
                        .slice(0, 2)
                        .join("")
                        .toUpperCase()}
                    </span>
                  </div>
                  <span className="font-medium">
                    {responsible.dependent.name}
                  </span>
                </div>
                <CheckCircle className="w-6 h-6 text-green-500" />
              </div>

              {/* Parentesco */}
              {responsible.dependentRelationship && (
                <div className="flex items-center justify-between p-3 bg-gray-50 rounded-xl border border-gray-200">
                  <span className="text-sm text-gray-600">Parentesco</span>
                  <span className="font-medium">
                    {
                      responsibleRelationshipLabels[
                        responsible.dependentRelationship
                      ]
                    }
                  </span>
                </div>
              )}
            </div>
          )}

          <Button className="flex-1 w-full" onClick={() => onOpenChange(false)}>
            Concluir
          </Button>
        </div>
      </DialogContent>
    </Dialog>
  );
}
