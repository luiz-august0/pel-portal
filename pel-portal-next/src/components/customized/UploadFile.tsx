import { DownloadIcon, FileIcon, Loader2Icon, TrashIcon, UploadIcon, XIcon } from "lucide-react";
import { useCallback, useState } from "react";
import { extractFileData, formatFileSize } from "@/helpers/general";
import { FileWithData } from "@/types/domains/document";
import { Accept, useDropzone } from "react-dropzone";
import { Button } from "@/components/ui/button";
import { toast } from "sonner";

interface UploadFileProps {
  value?: FileWithData;
  setValue: (value: FileWithData | any) => void;
  onRemove?: () => void;
  onDownload: () => Promise<void>;
  accept?: Accept;
  maxSize?: number;
}

export default function UploadFile({
  value,
  setValue,
  onRemove,
  onDownload,
  accept,
  maxSize,
}: UploadFileProps) {
  const [loadingDownload, setLoadingDownload] = useState(false);

  const handleDownload = async() => {
    setLoadingDownload(true);
    try {
      await onDownload();
    } catch (error) {
      toast.error("Erro ao baixar arquivo.");
    } finally {
      setLoadingDownload(false);
    }
  };

  const onDrop = useCallback(
    (acceptedFiles: File[]) => {
      if (acceptedFiles.length > 0) {
        extractFileData(acceptedFiles[0], (result) => {
          setValue({
            name: acceptedFiles[0].name,
            size: acceptedFiles[0].size,
            type: acceptedFiles[0].type,
            data: result,
          } as FileWithData);
        });
      }
    },
    [value, setValue]
  );

  const { getRootProps, getInputProps, isDragActive } = useDropzone({
    onDrop,
    accept: accept || {
      "image/*": [".png", ".jpg", ".jpeg", ".gif", ".bmp", ".webp"],
      "application/pdf": [".pdf"],
      "application/*": [".doc", ".docx", ".txt"],
    },
    maxSize: maxSize ?? 10 * 1024 * 1024,
    onDropRejected: (fileRejections) => {
      fileRejections.forEach((rejection) => {
        if (rejection.errors.some((error) => error.code === "file-too-large")) {
          toast.error(`Arquivo muito grande. Máximo ${maxSize}MB por arquivo.`);
        } else if (
          rejection.errors.some((error) => error.code === "file-invalid-type")
        ) {
          toast.error("Tipo de arquivo não permitido.");
        }
      });
    },
  });

  if (value) {
    return (
      <div className="space-y-3">
        <div className="flex border-1 border-gray-200 items-center justify-between p-3 bg-gray-50 rounded-lg">
          <div className="flex items-center space-x-3">
            <FileIcon className="w-5 h-5 text-gray-500" />
            <div>
              <p className="text-sm font-medium truncate max-w-48">
                {value.name}
              </p>
              <p className="text-xs text-muted-foreground">
                {formatFileSize(value.size)}
              </p>
            </div>
          </div>
          <div className="flex items-center">
          <Button
            type="button"
            variant="ghost"
            size="sm"
            onClick={() => handleDownload()}
            disabled={loadingDownload}
          >
            {loadingDownload ? <Loader2Icon className="w-4 h-4 animate-spin" /> : <DownloadIcon className="w-4 h-4" />}
          </Button>
          <Button
            type="button"
            variant="ghost"
            size="sm"
            onClick={() => onRemove?.()}
          >
            <XIcon className="w-4 h-4" />
          </Button>
          </div>

        </div>
      </div>
    )
  }

  return (
    <div className="space-y-3">
      <div
        {...getRootProps()}
        className={`border-2 border-dashed rounded-lg p-3 text-center cursor-pointer transition-colors ${
          isDragActive
            ? "border-primary bg-primary/5"
            : "border-gray-300 hover:border-primary hover:bg-gray-50"
        }`}
      >
        <input {...getInputProps()} />
        <p className="text-primary font-medium">Selecionar arquivo</p>
      </div>
    </div>
  );
}
