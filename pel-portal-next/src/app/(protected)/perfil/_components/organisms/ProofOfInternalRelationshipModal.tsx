"use client";

import {
  downloadDocument,
  getDocument,
  uploadDocument,
} from "@/core/services/document/documentService";
import { updateInternalRelationshipType } from "@/core/services/user/updateInternalRelationshipService";
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
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { useEffect, useState } from "react";
import { ScrollArea } from "@/components/ui/scroll-area";
import {
  Document,
  DocumentType,
  RelationshipType,
  relationshipTypeLabels,
} from "@/types/domains/document";
import UploadFile from "@/components/customized/UploadFile";
import RadioButton from "@/components/customized/RadioButton";
import { cn } from "@/helpers/cn";
import { openBase64File, openFileFromBlob } from "@/helpers/general";
import { useSession } from "next-auth/react";
import { handlerHttpError } from "@/helpers/toast";
import { Dispatch, SetStateAction } from "react";

interface ProofOfInternalRelationshipModalProps {
  open: boolean;
  onOpenChange: (open: boolean) => void;
  proofDocument?: Document;
  setProofDocument: Dispatch<SetStateAction<Document | undefined>>;
}

export function ProofOfInternalRelationshipModal({
  open,
  onOpenChange,
  proofDocument,
  setProofDocument,
}: ProofOfInternalRelationshipModalProps) {
  const { data: session, update } = useSession();
  const [selectedRelationshipType, setSelectedRelationshipType] =
    useState<RelationshipType>(
      session?.user?.userDetails?.internalRelationshipType as RelationshipType
    );
  const [showRelationshipSelection, setShowRelationshipSelection] = useState(
    !session?.user?.userDetails?.internalRelationshipType
  );
  const queryClient = useQueryClient();

  useEffect(() => {
    queryClient.invalidateQueries({
      queryKey: ["proof-of-internal-relationship"],
    });
  }, [open]);

  const removeProofDocument = async () => {
    setProofDocument(undefined);
  };

  const downloadProofDocument = async () => {
    if (proofDocument?.id) {
      const blob = await downloadDocument(
        DocumentType.PROOF_OF_INTERNAL_RELATIONSHIP
      );
      await openFileFromBlob(blob, proofDocument.name);
    } else {
      await openBase64File(
        proofDocument?.data ?? "",
        proofDocument?.name ?? ""
      );
    }
  };

  const { isLoading: isLoadingDocument } = useQuery({
    queryKey: ["proof-of-internal-relationship"],
    queryFn: async () => {
      const proofDocumentResponse = await getDocument(
        DocumentType.PROOF_OF_INTERNAL_RELATIONSHIP
      );
      if (proofDocumentResponse) {
        setProofDocument({
          ...proofDocumentResponse,
          name: proofDocumentResponse.originalFilename,
          size: proofDocumentResponse.size,
        } as Document);
      }
      return proofDocumentResponse;
    },
  });

  const isValid = proofDocument && selectedRelationshipType;

  const saveMutation = useMutation({
    mutationFn: async () => {
      if (!isValid) {
        throw new Error(
          "Deve ser selecionado o tipo de vínculo e anexado o documento comprovante"
        );
      }
      // Primeiro atualiza o tipo de vínculo
      if (selectedRelationshipType) {
        await updateInternalRelationshipType(selectedRelationshipType);
      }
      // Depois faz upload do documento se necessário
      if (proofDocument?.data) {
        await uploadDocument({
          documentType: DocumentType.PROOF_OF_INTERNAL_RELATIONSHIP,
          file: {
            file: proofDocument.data,
            filename: proofDocument.name,
          },
        });
      }
      update({
        ...session,
        user: {
          ...session?.user,
          userDetails: {
            ...session?.user?.userDetails,
            internalRelationshipType: selectedRelationshipType,
          },
        },
      });
      await queryClient.invalidateQueries({
        queryKey: ["user-status", session?.user?.id],
      });
    },
    onSuccess: () => {
      toast.success("Comprovante de vínculo interno salvo com sucesso!");
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
            Comprovante de vínculo interno
          </ResponsiveDialogTitle>
          <p className="text-sm text-muted-foreground mt-2">
            Comprove seu vínculo interno para garantir o valor especial de
            inscrição.
          </p>
        </ResponsiveDialogHeader>
        <ScrollArea
          className={cn(
            "overflow-y-auto max-md:p-4",
            isLoadingDocument && "hidden"
          )}
        >
          <form
            onSubmit={(e) => {
              e.preventDefault();
              saveMutation.mutate();
            }}
            className="space-y-6"
          >
            <div className="space-y-4">
              <div className="flex items-center justify-between">
                <h3 className="text-base font-medium">Tipo de vínculo</h3>
                {selectedRelationshipType && !showRelationshipSelection && (
                  <Button
                    type="button"
                    variant="ghost"
                    className="text-blue-600 hover:text-blue-700 p-0 h-auto cursor-pointer"
                    onClick={() => setShowRelationshipSelection(true)}
                  >
                    Alterar
                  </Button>
                )}
              </div>

              {showRelationshipSelection ? (
                <div className="space-y-3">
                  {Object.entries(relationshipTypeLabels).map(
                    ([value, label]) => (
                      <RadioButton
                        key={value}
                        name="relationshipType"
                        value={value}
                        checked={selectedRelationshipType === value}
                        onChange={() => {
                          setSelectedRelationshipType(
                            value as RelationshipType
                          );
                          setShowRelationshipSelection(false);
                        }}
                      >
                        {label}
                      </RadioButton>
                    )
                  )}
                </div>
              ) : selectedRelationshipType ? (
                <RadioButton
                  key={selectedRelationshipType}
                  name="relationshipType"
                  value={selectedRelationshipType}
                  checked={true}
                >
                  {relationshipTypeLabels[selectedRelationshipType]}
                </RadioButton>
              ) : null}
            </div>

            <div className="space-y-4">
              <div className="space-y-2">
                <h3 className="text-base font-medium">
                  Documento comprovante de vínculo
                </h3>
                <p className="text-sm text-muted-foreground">
                  Anexar contracheque ou declaração atualizada.
                </p>
              </div>
              <UploadFile
                value={proofDocument}
                setValue={setProofDocument}
                onRemove={removeProofDocument}
                accept={{
                  "application/pdf": [".pdf"],
                  "image/jpeg": [".jpeg", ".jpg"],
                  "image/png": [".png"],
                }}
                onDownload={downloadProofDocument}
              />
            </div>

            <div className="flex gap-3 pt-4">
              <Button
                type="submit"
                className="flex-1 cursor-pointer"
                disabled={saveMutation.isPending || !isValid}
              >
                {saveMutation.isPending ? "Salvando..." : "Concluir"}
              </Button>
            </div>
          </form>
        </ScrollArea>
        <div
          className={cn(
            "flex flex-col items-center justify-center py-4",
            !isLoadingDocument && "hidden"
          )}
        >
          <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-primary mx-auto"></div>
          <p className="text-gray-600 mt-2">Carregando...</p>
        </div>
      </ResponsiveDialogContent>
    </ResponsiveDialog>
  );
}
