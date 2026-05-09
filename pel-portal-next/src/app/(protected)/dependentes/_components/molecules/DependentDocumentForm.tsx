"use client";

import { useEffect, useState } from "react";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { Button } from "@/components/ui/button";
import UploadFile from "@/components/customized/UploadFile";
import { cn } from "@/helpers/cn";
import { Document, DocumentType, MultipartBean } from "@/types/domains/document";
import { DependentDTO } from "@/types/domains/dependent";
import { openBase64File, openFileFromBlob } from "@/helpers/general";
import { handlerHttpError } from "@/helpers/toast";
import { downloadDependentDocument, getDependentDocument } from "@/core/services/dependent/getDependentInfoService";
import { uploadDependentDocument } from "@/core/services/dependent/createUpdateDependentService";

interface DependentDocumentFormProps {
  dependent?: DependentDTO;
  onBack?: () => void;
  onFinish: () => void;
}

export function DependentDocumentForm({
  dependent,
  onBack,
  onFinish,
}: DependentDocumentFormProps) {
  const [document, setDocument] = useState<Document>();
  const [medicalDocument, setMedicalDocument] = useState<Document>();
  const queryClient = useQueryClient();

  const { isLoading: isLoadingDocuments } = useQuery({
    queryKey: ["dependent-documents", dependent?.dependent.id],
    queryFn: async () => {
      const documentData = await getDependentDocument(
        dependent!.dependent.id,
        DocumentType.DOCUMENT_WITH_PHOTO
      );
      if (documentData) {
        setDocument({
          ...documentData,
          name: documentData.originalFilename,
          size: documentData.size,
        } as Document);
      }
      const medicalDocumentData = await getDependentDocument(
        dependent!.dependent.id,
        DocumentType.MEDICAL_REPORT
      );
      if (medicalDocumentData) {
        setMedicalDocument({
          ...medicalDocumentData,
          name: medicalDocumentData.originalFilename,
          size: medicalDocumentData.size,
        } as Document);
      }
      return {
        document,
        medicalDocument,
      };
    },
    enabled: !!dependent?.dependent.id,
  });

  useEffect(() => {
    queryClient.invalidateQueries({
      queryKey: ["dependent-documents", dependent?.dependent.id],
    });
  }, [dependent?.dependent.id, queryClient]);

  const removeDocument = async () => {
    setDocument(undefined);
  };

  const removeMedicalDocument = async () => {
    setMedicalDocument(undefined);
  };

  const downloadDocument = async () => {
    if (document?.id && dependent?.dependent.id) {
      const blob = await downloadDependentDocument(
        dependent.dependent.id,
        DocumentType.DOCUMENT_WITH_PHOTO
      );
      await openFileFromBlob(blob, document.name);
    } else {
      await openBase64File(
        document?.data ?? "",
        document?.name ?? ""
      );
    }
  };

  const downloadMedicalDocument = async () => {
    if (medicalDocument?.id && dependent?.dependent.id) {
      const blob = await downloadDependentDocument(
        dependent.dependent.id,
        DocumentType.MEDICAL_REPORT
      );
      await openFileFromBlob(blob, medicalDocument.name);
    } else {
      await openBase64File(
        medicalDocument?.data ?? "",
        medicalDocument?.name ?? ""
      );
    }
  };

  const isValid = dependent?.dependent.userDetails?.specialNeeds
    ? document && medicalDocument
    : document;

  const documentUploadMutation = useMutation({
    mutationFn: async (file: Document) => {
      if (!dependent?.dependent.id)
        throw new Error("ID do dependente não encontrado");
      const multipartFile = { file: file.data, filename: file.name };
      return await uploadDependentDocument(
        dependent.dependent.id,
        DocumentType.DOCUMENT_WITH_PHOTO,
        multipartFile as MultipartBean
      );
    },
  });

  const medicalUploadMutation = useMutation({
    mutationFn: async (file: Document) => {
      if (!dependent?.dependent.id)
        throw new Error("ID do dependente não encontrado");
      const multipartFile = { file: file.data, filename: file.name };
      return await uploadDependentDocument(
        dependent.dependent.id,
        DocumentType.MEDICAL_REPORT,
        multipartFile as MultipartBean
      );
    },
  });

  const handleSubmit = () => {
    if (!isValid) {
      throw new Error("Deve ser anexado todos os documentos");
    }
    const uploadPromises = [];
    if (document?.data) {
      uploadPromises.push(documentUploadMutation.mutateAsync(document));
    }
    if (medicalDocument?.data) {
      uploadPromises.push(medicalUploadMutation.mutateAsync(medicalDocument));
    }
    Promise.all(uploadPromises)
      .then(() => {
        onFinish();
      })
      .catch((err) => {
        handlerHttpError(err);
      });
  };

  if (isLoadingDocuments) {
    return (
      <div className={cn("flex flex-col items-center justify-center p-15")}>
        <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-primary mx-auto"></div>
        <p className="text-gray-600 mt-2">Carregando...</p>
      </div>
    );
  }

  return (
    <div className="space-y-4">
      <div className="space-y-2">
        <h3 className="text-base font-medium">
          Documento com foto (frente e verso)
        </h3>
      </div>

      <UploadFile
        value={document}
        setValue={setDocument}
        onRemove={removeDocument}
        accept={{
          "application/pdf": [".pdf"],
          "image/jpeg": [".jpeg", ".jpg"],
          "image/png": [".png"],
        }}
        onDownload={downloadDocument}
      />

      {dependent?.dependent.userDetails?.specialNeeds && (
        <>
          <div className="space-y-2">
            <h3 className="text-base font-medium">Laudo médico</h3>
          </div>
          <UploadFile
            value={medicalDocument}
            setValue={setMedicalDocument}
            onRemove={removeMedicalDocument}
            onDownload={downloadMedicalDocument}
            accept={{
              "application/pdf": [".pdf"],
              "image/jpeg": [".jpeg", ".jpg"],
              "image/png": [".png"],
            }}
          />
        </>
      )}

      {/* Botões de navegação */}
      <div className="flex justify-between pt-6">
        {onBack && (
          <Button
            type="button"
            variant="outline"
            onClick={() => {
              onBack();
            }}
            disabled={
              documentUploadMutation.isPending ||
              medicalUploadMutation.isPending
            }
          >
            Voltar
          </Button>
        )}
        <Button
          type="button"
          onClick={handleSubmit}
          disabled={
            !isValid ||
            documentUploadMutation.isPending ||
            medicalUploadMutation.isPending
          }
          className="ml-auto"
        >
          {documentUploadMutation.isPending || medicalUploadMutation.isPending
            ? "Enviando..."
            : "Finalizar"}
        </Button>
      </div>
    </div>
  );
}
