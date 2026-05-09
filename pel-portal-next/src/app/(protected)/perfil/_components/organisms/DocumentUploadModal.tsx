"use client";

import {
  downloadDocument as downloadDocumentService,
  getDocument,
  uploadDocument,
} from "@/core/services/document/documentService";
import { Button } from "@/components/ui/button";
import {
  ResponsiveDialog,
  ResponsiveDialogClose,
  ResponsiveDialogContent,
  ResponsiveDialogHeader,
  ResponsiveDialogTitle,
} from "@/components/customized/ResponsiveDialog";
import { XIcon } from "lucide-react";
import { toast } from "sonner";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import { Dispatch, SetStateAction, useEffect, useMemo } from "react";
import { ScrollArea } from "@/components/ui/scroll-area";
import { Document, DocumentType } from "@/types/domains/document";
import UploadFile from "@/components/customized/UploadFile";
import { cn } from "@/helpers/cn";
import { openBase64File, openFileFromBlob } from "@/helpers/general";
import { useSession } from "next-auth/react";
import { handlerHttpError } from "@/helpers/toast";

interface DocumentUploadModalProps {
  open: boolean;
  onOpenChange: (open: boolean) => void;
  document?: Document;
  medicalDocument?: Document;
  setDocument: Dispatch<SetStateAction<Document | undefined>>;
  setMedicalDocument: Dispatch<SetStateAction<Document | undefined>>;
  isLoadingDocuments: boolean;
}

export function DocumentUploadModal({
  open,
  onOpenChange,
  document,
  medicalDocument,
  setDocument,
  setMedicalDocument,
  isLoadingDocuments,
}: DocumentUploadModalProps) {
  const { data: session, update } = useSession();
  const queryClient = useQueryClient();

  useEffect(() => {
    queryClient.invalidateQueries({ queryKey: ["documents"] });
  }, [open]);

  const removeDocument = async () => {
    setDocument(undefined);
  };

  const removeMedicalDocument = async () => {
    setMedicalDocument(undefined);
  };

  const downloadDocument = async () => {
    if (document?.id) {
      const blob = await downloadDocumentService(
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
    if (medicalDocument?.id) {
      const blob = await downloadDocumentService(DocumentType.MEDICAL_REPORT);
      await openFileFromBlob(blob, medicalDocument.name);
    } else {
      await openBase64File(
        medicalDocument?.data ?? "",
        medicalDocument?.name ?? ""
      );
    }
  };

  const isValid = useMemo(() => {
    return (
      document &&
      (session?.user?.userDetails?.specialNeeds ? medicalDocument : true)
    );
  }, [document, medicalDocument]);

  const updateSessionForReview = async () => {
    await update({
      ...session,
      user: {
        ...session?.user,
        reviewed: false,
      },
    });
  };

  const uploadMutation = useMutation({
    mutationFn: async () => {
      if (!isValid) {
        throw new Error("Deve ser anexado todos os documentos");
      }
      if (document?.data) {
        await uploadDocument({
          documentType: DocumentType.DOCUMENT_WITH_PHOTO,
          file: {
            file: document?.data,
            filename: document?.name,
          },
        });
        await updateSessionForReview();
      }
      if (medicalDocument?.data) {
        await uploadDocument({
          documentType: DocumentType.MEDICAL_REPORT,
          file: {
            file: medicalDocument?.data,
            filename: medicalDocument?.name,
          },
        });
        await updateSessionForReview();
      }
      await queryClient.invalidateQueries({
        queryKey: ["user-status", session?.user?.id],
      });
    },
    onSuccess: () => {
      toast.success(`Documentos enviados com sucesso!`);
      onOpenChange(false);
    },
    onError: (err) => {
      handlerHttpError(err);
    },
  });

  return (
    <ResponsiveDialog open={open} onOpenChange={onOpenChange}>
      <ResponsiveDialogContent>
        <ResponsiveDialogHeader className="text-center">
          <ResponsiveDialogClose className="absolute right-4 top-4 cursor-pointer">
            <XIcon className="w-4 h-4" />
          </ResponsiveDialogClose>
          <ResponsiveDialogTitle className="text-lg font-semibold">
            Documentos
          </ResponsiveDialogTitle>
        </ResponsiveDialogHeader>
        <ScrollArea
          className={cn(
            "overflow-y-auto max-md:p-4",
            isLoadingDocuments && "hidden"
          )}
        >
          <form
            onSubmit={(e) => {
              e.preventDefault();
              uploadMutation.mutate();
            }}
            className="space-y-4"
          >
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
            {session?.user?.userDetails?.specialNeeds && (
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
            <div className="flex gap-3 pt-4">
              <Button
                type="submit"
                className="flex-1"
                disabled={uploadMutation.isPending || !isValid}
              >
                {uploadMutation.isPending ? "Enviando..." : "Concluir"}
              </Button>
            </div>
          </form>
        </ScrollArea>
        <div
          className={cn(
            "flex flex-col items-center justify-center p-15",
            !isLoadingDocuments && "hidden"
          )}
        >
          <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-primary mx-auto"></div>
          <p className="text-gray-600 mt-2">Carregando...</p>
        </div>
      </ResponsiveDialogContent>
    </ResponsiveDialog>
  );
}
