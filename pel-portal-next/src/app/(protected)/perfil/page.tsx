"use client";

import { useRouter } from "next/navigation";
import { HeaderPage } from "@/components/customized/HeaderPage";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { CheckCircle, FileIcon, Loader2Icon } from "lucide-react";
import { DataSection } from "@/components/customized/DataSection";
import { applyCpfMask, applyPhoneMask } from "@/helpers/masks";
import dayjs from "dayjs";
import { useSession } from "next-auth/react";
import { EditUserDataModal } from "./_components/organisms/EditUserDataModal";
import { EditUserAddressModal } from "./_components/organisms/EditUserAddressModal";
import { useState } from "react";
import { DocumentUploadModal } from "./_components/organisms/DocumentUploadModal";
import { ProofOfInternalRelationshipModal } from "./_components/organisms/ProofOfInternalRelationshipModal";
import { useQuery } from "@tanstack/react-query";
import { cn } from "@/helpers/cn";
import { useMemo } from "react";
import { ProgramKnowledgeSource } from "@/types/domains/register";
import {
  Document,
  DocumentType,
  RelationshipType,
  relationshipTypeLabels,
} from "@/types/domains/document";

import { formatFileSize } from "@/helpers/general";
import { Skeleton } from "@/components/ui/skeleton";
import { getUserStatus } from "@/core/services/user/userService";
import { getDocument } from "@/core/services/document/documentService";

function DocumentCard({ document }: { document: Document }) {
  return (
    <div className="space-y-3">
      <div className="flex border-1 border-gray-200 items-center justify-between p-3 bg-gray-50 rounded-lg">
        <div className="flex items-center space-x-3">
          <FileIcon className="w-5 h-5 text-gray-500" />
          <div>
            <p className="text-sm font-medium truncate max-w-48">
              {document?.name}
            </p>
            <p className="text-xs text-muted-foreground">
              {formatFileSize(document?.size)}
            </p>
          </div>
        </div>
      </div>
    </div>
  );
}

export default function ProfilePage() {
  const router = useRouter();
  const { data: session } = useSession();
  const [isEditModalOpen, setIsEditModalOpen] = useState(false);
  const [isAddressModalOpen, setIsAddressModalOpen] = useState(false);
  const [documentModalOpen, setDocumentModalOpen] = useState(false);
  const [document, setDocument] = useState<Document>();
  const [medicalDocument, setMedicalDocument] = useState<Document>();
  const [proofDocument, setProofDocument] = useState<Document>();
  const [
    proofOfInternalRelationshipModalOpen,
    setProofOfInternalRelationshipModalOpen,
  ] = useState(false);

  const { data: userStatus, isLoading: userStatusLoading } = useQuery({
    queryKey: ["user-status", session?.user?.id],
    queryFn: async () => await getUserStatus(),
    enabled: !!session?.user?.id,
  });

  const { isLoading: isLoadingDocuments } = useQuery({
    queryKey: ["documents"],
    queryFn: async () => {
      const documentResponse = await getDocument(
        DocumentType.DOCUMENT_WITH_PHOTO
      );
      const medicalDocumentResponse = await getDocument(
        DocumentType.MEDICAL_REPORT
      );
      if (documentResponse) {
        setDocument({
          ...documentResponse,
          name: documentResponse.originalFilename,
          size: documentResponse.size,
        } as Document);
      }
      if (medicalDocumentResponse) {
        setMedicalDocument({
          ...medicalDocumentResponse,
          name: medicalDocumentResponse.originalFilename,
          size: medicalDocumentResponse.size,
        } as Document);
      }
      return {
        document,
        medicalDocument,
      };
    },
  });

  const userStatusIsOk = useMemo(() => {
    return userStatus
      ?.filter((status) => !status.optional)
      .every((status) => status.checked);
  }, [userStatus]);

  const handleGoBack = () => {
    router.push("/");
  };

  const initials = session?.user?.name
    .split(" ")
    .map((name: string) => name.charAt(0))
    .join("")
    .toUpperCase()
    .slice(0, 2);

  const badgeStatus = useMemo(() => {
    if (userStatusIsOk && session?.user?.reviewed) {
      return <Badge variant="success">Ativo</Badge>;
    }
    if (!userStatusIsOk && session?.user?.reviewed) {
      return <Badge variant="destructive">Pendente</Badge>;
    }
    if (userStatusIsOk && !session?.user?.reviewed) {
      return <Badge variant="warning">Em análise</Badge>;
    }
    return <Badge variant="destructive">Pendente</Badge>;
  }, [userStatusIsOk, session?.user?.reviewed]);

  return (
    <>
      <div className="min-h-screen bg-gray-50">
        <div className="max-w-md mx-auto p-4">
          {/* Header */}
          <HeaderPage title="Perfil" onBack={handleGoBack} />

          {/* Avatar Section */}
          <div className="bg-white rounded-lg p-4 mb-4 flex items-center justify-between shadow-sm">
            <div className="w-12 h-12 bg-gray-200 rounded-full flex items-center justify-center">
              <span className="text-gray-600 font-medium text-lg">
                {initials}
              </span>
            </div>
            {/* <Button
              variant="ghost"
              className="text-blue-500 hover:text-blue-600 cursor-pointer"
            >
              Alterar foto
            </Button> */}
          </div>

          {/* Status Section */}
          <div className="bg-white rounded-lg p-4 mb-4 shadow-sm">
            <div className="flex items-center justify-between mb-4">
              <h2 className="text-lg font-semibold text-gray-900">Status</h2>
              {!userStatusLoading && badgeStatus}
            </div>
            <div
              className={cn("text-center py-8", !userStatusLoading && "hidden")}
            >
              <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-primary mx-auto"></div>
            </div>
            <div className={cn("space-y-3", userStatusLoading && "hidden")}>
              {userStatus?.map((item, index) => (
                <div key={index} className="flex items-start gap-3">
                  {item.checked ? (
                    <CheckCircle className="w-5 h-5 text-green-500 mt-0.5 flex-shrink-0" />
                  ) : (
                    <div className="w-2 h-2 bg-gray-400 rounded-full mt-2 flex-shrink-0" />
                  )}
                  <span
                    className={`text-sm ${
                      item.checked ? "text-gray-900" : "text-gray-600"
                    }`}
                  >
                    {item.description}
                  </span>
                </div>
              ))}
            </div>
          </div>

          {/* Meus Dados Section */}
          <div className="bg-white rounded-lg p-4 mb-4 shadow-sm">
            <div className="flex items-center justify-between mb-4">
              <h2 className="text-lg font-semibold text-gray-900">
                Meus dados
              </h2>
              <Button
                variant="ghost"
                className="text-blue-500 hover:text-blue-600 cursor-pointer"
                onClick={() => setIsEditModalOpen(true)}
                disabled={session?.user?.userDetails?.minor}
              >
                Editar
              </Button>
            </div>
            <DataSection
              values={[
                { label: "Nome", value: session?.user?.name },
                { label: "E-mail", value: session?.user?.email },
                { label: "CPF", value: applyCpfMask(session?.user?.cpf || "") },
                {
                  label: "Celular",
                  value: session?.user?.userDetails?.phone
                    ? applyPhoneMask(session?.user?.userDetails?.phone)
                    : "Não informado",
                },
                {
                  label: "Data de nascimento",
                  value: session?.user?.userDetails?.birthDate
                    ? dayjs(session?.user?.userDetails?.birthDate).format(
                        "DD/MM/YYYY"
                      )
                    : "Não informado",
                },
              ]}
            />
          </div>

          {/* Endereço Section */}
          <div className="bg-white rounded-lg p-4 mb-4 shadow-sm">
            <div className="flex items-center justify-between">
              <h2 className="text-lg font-semibold text-gray-900">Endereço</h2>
              <Button
                variant="ghost"
                className="text-blue-500 hover:text-blue-600 cursor-pointer"
                onClick={() => setIsAddressModalOpen(true)}
                disabled={session?.user?.userDetails?.minor}
              >
                {session?.user?.address ? "Editar" : "Cadastrar"}
              </Button>
            </div>
            {session?.user?.address && (
              <div className="mt-4">
                <DataSection
                  values={[
                    {
                      label: "CEP",
                      value: session?.user?.address?.cep || "Não informado",
                    },
                    {
                      label: "Rua",
                      value: session?.user?.address?.street || "Não informado",
                    },
                    {
                      label: "Número",
                      value: session?.user?.address?.number || "Não informado",
                    },
                    {
                      label: "Complemento",
                      value:
                        session?.user?.address?.complement || "Não informado",
                    },
                    {
                      label: "Bairro",
                      value:
                        session?.user?.address?.neighborhood || "Não informado",
                    },
                    {
                      label: "Cidade",
                      value: session?.user?.address?.city || "Não informado",
                    },
                    {
                      label: "Estado",
                      value: session?.user?.address?.state || "Não informado",
                    },
                  ]}
                />
              </div>
            )}
          </div>

          {/* Documentos Section */}
          <div className="bg-white rounded-lg p-4 mb-4 shadow-sm">
            <div className="flex items-center justify-between">
              <h2 className="text-lg font-semibold text-gray-900">
                Documentos
              </h2>
              <Button
                variant="ghost"
                className="text-blue-500 hover:text-blue-600 cursor-pointer"
                onClick={() => setDocumentModalOpen(true)}
                disabled={session?.user?.userDetails?.minor}
              >
                Upload
              </Button>
            </div>
            <div
              className={cn(
                "flex flex-col gap-2",
                !!document || !!medicalDocument ? "mt-4" : ""
              )}
            >
              {document && <DocumentCard document={document} />}
              {medicalDocument && <DocumentCard document={medicalDocument} />}
            </div>
          </div>

          {/* Comprovante de Vínculo Interno Section */}
          <div className="bg-white rounded-lg p-4 mb-4 shadow-sm">
            <div className="flex items-center justify-between">
              <h2 className="text-lg font-semibold text-gray-900">
                Comprovante de Vínculo Interno
              </h2>
              <Button
                variant="ghost"
                className="text-blue-500 hover:text-blue-600 cursor-pointer"
                onClick={() => setProofOfInternalRelationshipModalOpen(true)}
                disabled={session?.user?.userDetails?.minor}
              >
                Upload
              </Button>
            </div>
            <div
              className={cn(
                "flex flex-col gap-2",
                !!proofDocument ? "mt-4" : ""
              )}
            >
              {proofDocument && <DocumentCard document={proofDocument} />}
            </div>
          </div>
        </div>
      </div>
      {/* Modal para editar dados do usuário */}
      <EditUserDataModal
        open={isEditModalOpen}
        onOpenChange={setIsEditModalOpen}
      />

      {/* Modal para editar endereço do usuário */}
      <EditUserAddressModal
        open={isAddressModalOpen}
        onOpenChange={setIsAddressModalOpen}
      />

      {/* Modal para upload de documentos */}
      <DocumentUploadModal
        open={documentModalOpen}
        onOpenChange={setDocumentModalOpen}
        document={document}
        medicalDocument={medicalDocument}
        setDocument={setDocument}
        setMedicalDocument={setMedicalDocument}
        isLoadingDocuments={isLoadingDocuments}
      />

      {/* Modal para upload de comprovante de vínculo interno */}
      <ProofOfInternalRelationshipModal
        open={proofOfInternalRelationshipModalOpen}
        onOpenChange={setProofOfInternalRelationshipModalOpen}
        proofDocument={proofDocument}
        setProofDocument={setProofDocument}
      />
    </>
  );
}
