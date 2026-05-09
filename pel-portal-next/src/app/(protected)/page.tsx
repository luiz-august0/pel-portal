"use client";

import { signOut, useSession } from "next-auth/react";
import { ResponsibleLinkModal } from "./_components/organisms/ResponsibleLinkModal";
import { ChangePasswordModal } from "./_components/organisms/ChangePasswordModal";
import { Section } from "./_components/atoms/Section";
import { Alert } from "./_components/molecules/Alert";
import { ActionButton } from "./_components/atoms/ActionButton";
import { useMemo, useState } from "react";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";
import { CheckCircle, LogOut, Megaphone } from "lucide-react";
import { useRouter } from "next/navigation";
import { useQuery } from "@tanstack/react-query";
import { getUserStatus } from "@/core/services/user/userService";
import { getLastInscription } from "@/core/services/inscription/inscriptionService";
import { ActionButtonProps } from "./_components/atoms/ActionButton";
import { MinimalInscriptionCard } from "./_components/molecules/MinimalInscriptionCard";
import Skeleton from "@/components/customized/Skeleton";
import { getAvailableCourses } from "@/core/services/course/getAvailableCoursesService";

export default function HomePage() {
  const { data: session } = useSession();
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [isChangePasswordModalOpen, setIsChangePasswordModalOpen] =
    useState(false);
  const userName = session?.user?.name || "userName";
  const router = useRouter();

  const { data: userStatus, isLoading: userStatusLoading } = useQuery({
    queryKey: ["user-status", session?.user?.id],
    queryFn: async () => await getUserStatus(),
    enabled: !!session?.user?.id,
  });

  const { data: lastInscription, isLoading: lastInscriptionLoading } = useQuery(
    {
      queryKey: ["last-inscription", session?.user?.id],
      queryFn: getLastInscription,
      enabled: !!session?.user?.id,
    }
  );

  // Buscar cursos disponíveis
  const { data: courses = [], isLoading: isLoadingCourses } = useQuery({
    queryKey: ["available-courses"],
    queryFn: getAvailableCourses,
  });

  const handleSignOut = async () => {
    await signOut({ redirect: false });
    router.push("/onboarding");
  };

  const userStatusIsOk = useMemo(() => {
    return userStatus
      ?.filter((status) => !status.optional)
      .every((status) => status.checked);
  }, [userStatus]);

  const initials = session?.user?.name
    .split(" ")
    .map((name: string) => name.charAt(0))
    .join("")
    .toUpperCase()
    .slice(0, 2);

  const badgeStatus: ActionButtonProps["badge"] = useMemo(() => {
    if (userStatusLoading) {
      return { variant: "default", text: "Carregando..." };
    }
    if (userStatusIsOk && session?.user?.reviewed) {
      return { variant: "success", text: "Ativo" };
    }
    if (!userStatusIsOk) {
      return { variant: "destructive", text: "Pendente" };
    }
    if (userStatusIsOk && !session?.user?.reviewed) {
      return { variant: "warning", text: "Em análise" };
    }
  }, [userStatusIsOk, session?.user?.reviewed, userStatusLoading]);

  return (
    <div className="min-h-screen bg-primary">
      {/* Header */}
      <header className="flex justify-between items-center p-5 pt-8 text-white">
        <h1 className="text-xl font-medium">Olá, {userName}!</h1>
        <DropdownMenu>
          <DropdownMenuTrigger asChild>
            <button className="w-12 h-12 bg-white rounded-full flex items-center justify-center text-[#4A9B9F] font-semibold text-base cursor-pointer hover:bg-gray-100 transition-colors">
              {initials}
            </button>
          </DropdownMenuTrigger>
          <DropdownMenuContent align="end" className="w-48">
            <DropdownMenuItem
              onClick={handleSignOut}
              className="cursor-pointer"
            >
              <LogOut className="mr-2 h-4 w-4" />
              Sair
            </DropdownMenuItem>
          </DropdownMenuContent>
        </DropdownMenu>
      </header>

      {/* Main Content */}
      <main className="bg-white text-gray-900 p-5 rounded-t-3xl mt-4 min-h-[calc(100vh-120px)]">
        {/* Cursos Section */}
        <Section title="Cursos">
          {!session?.user?.authorized && (
            <Alert
              variant="warning"
              title="Cadastro pendente!"
              description="Seu responsável precisa concluir o cadastro para liberar sua inscrição nos cursos."
              onClick={() => setIsModalOpen(true)}
            />
          )}
          {session?.user?.authorized &&
            !session?.user?.reviewed &&
            userStatusIsOk &&
            !userStatusLoading && (
              <Alert
                variant="warning"
                title="Cadastro em análise!"
                description="Seu cadastro está em análise. Aguarde a confirmação para se inscrever nos cursos."
              />
            )}
          {session?.user?.authorized &&
            !userStatusIsOk &&
            !userStatusLoading && (
              <Alert
                variant="warning"
                title="Finalize seu cadastro!"
                description="Complete seus dados para se inscrever nos cursos."
                onClick={() => router.push("/perfil")}
              />
            )}

          {/* Mostrar última inscrição ou alert de inscrições abertas */}
          {session?.user?.authorized &&
            session?.user?.reviewed &&
            userStatusIsOk &&
            !userStatusLoading &&
            !lastInscriptionLoading && (
              <>
                {lastInscription ? (
                  <div className="space-y-2">
                    <MinimalInscriptionCard
                      inscription={lastInscription}
                      onClick={() =>
                        router.push(`/inscricao/${lastInscription.id}/detalhes`)
                      }
                    />
                    <button
                      className="p-0 text-blue-600 cursor-pointer text-sm hover:underline"
                      onClick={() => router.push("/inscricao")}
                    >
                      Ver tudo
                    </button>
                  </div>
                ) : !isLoadingCourses && courses.length > 0 ? (
                  <Alert
                    variant="warning"
                    title="Inscrições abertas!"
                    icon={Megaphone}
                    onClick={() => router.push("/inscricao/nova")}
                  />
                ) : (
                  <Alert
                    variant="success"
                    title="Tudo pronto!"
                    description="Agora é só aguardar. Avisaremos você quando as inscrições abrirem."
                    icon={CheckCircle}
                  />
                )}
              </>
            )}
        </Section>

        {/* Conta Section */}
        <Section title="Conta">
          <div className="space-y-3">
            <ActionButton
              onClick={() => router.push("/perfil")}
              badge={badgeStatus}
            >
              Perfil
            </ActionButton>
            {session?.user?.userDetails?.minor && (
              <ActionButton
                onClick={() => setIsModalOpen(true)}
                {...(!session?.user?.authorized
                  ? { badge: { text: "Pendente", variant: "destructive" } }
                  : { badge: { text: "Ativo", variant: "success" } })}
              >
                Responsável
              </ActionButton>
            )}
            {!session?.user?.userDetails?.minor && (
              <ActionButton onClick={() => router.push("/dependentes")}>
                Dependentes
              </ActionButton>
            )}
            <ActionButton onClick={() => setIsChangePasswordModalOpen(true)}>
              Alterar senha
            </ActionButton>
          </div>
        </Section>

        {/* Solicitações Section */}
        {session?.user?.reviewed && (
          <Section title="Solicitações">
            <div className="space-y-3">
              <ActionButton onClick={() => router.push("/nivelamento")}>
                Prova de nivelamento
              </ActionButton>
              <ActionButton onClick={() => router.push("/transferencia")}>
                Transferência de turma
              </ActionButton>
            </div>
          </Section>
        )}
      </main>
      <ResponsibleLinkModal open={isModalOpen} onOpenChange={setIsModalOpen} />
      <ChangePasswordModal
        open={isChangePasswordModalOpen}
        onOpenChange={setIsChangePasswordModalOpen}
      />
    </div>
  );
}
