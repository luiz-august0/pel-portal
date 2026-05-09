import { ChangeEvent, Dispatch } from "react";
import { formatMoney, unmaskInputMoney } from "./formatters";
import { saveAs } from "file-saver";

type onChangeMoneyInputProps = {
  fieldName?: string;
  setValue?: any;
  clearErrors?: any;
  setInputValue: Dispatch<React.SetStateAction<string>>;
  target?: ChangeEvent<HTMLInputElement | HTMLTextAreaElement>;
};

export function onChangeMoneyInput({
  fieldName,
  setValue,
  clearErrors,
  setInputValue,
  target,
}: onChangeMoneyInputProps) {
  const val = unmaskInputMoney(target?.target.value ?? formatMoney(0));

  clearErrors?.(fieldName);

  if (val < 0) {
    setValue?.(fieldName, 0);
    setInputValue?.(formatMoney(0));
    return;
  }

  setValue?.(fieldName, val);
  setInputValue?.(formatMoney(val));
  clearErrors?.(fieldName);
}

export const getDigits = (value?: string): string =>
  value?.replace(/\D/g, "") ?? "";

export const extractFileData = async (
  file: File,
  callback?: (result: string) => void
) => {
  const reader = new FileReader();
  reader.readAsDataURL(file);
  reader.onloadend = function () {
    callback?.(reader.result as string);
  };
};

export const getFilenameFromUrl = (url: string) => {
  return url.substring(url.lastIndexOf("/") + 1, url.length);
};

// Função principal de download com suporte mobile
export const openFileFromBlob = async (data: Blob, fileName: string) => {
  try {
    // Converter blob para array de bytes
    const arrayBuffer = await data.arrayBuffer();
    const byteArray = Array.from(new Uint8Array(arrayBuffer));
    const mimeType = getMimeType(fileName);
    const blob = new Blob([new Uint8Array(byteArray)], { type: mimeType });

    // Tentar usar navigator.share (funciona melhor em mobile)
    if (
      navigator.share &&
      /Android|iPhone|iPad|iPod/i.test(navigator.userAgent)
    ) {
      const file = new File([blob], fileName, { type: mimeType });
      navigator
        .share({
          files: [file],
          title: fileName,
        })
        .catch(() => {
          // Se share falhar, usar método tradicional
          fallbackDownload(blob, fileName);
        });
      return;
    }

    // Fallback para método tradicional
    fallbackDownload(blob, fileName);
  } catch (error) {
    console.error("Erro ao baixar arquivo:", error);
  }
};

// Função de fallback para download tradicional
const fallbackDownload = (blob: Blob, fileName: string) => {
  const url = window.URL.createObjectURL(blob);
  const link = document.createElement("a");
  link.href = url;
  link.download = fileName;
  link.style.display = "none";

  document.body.appendChild(link);

  // Usar setTimeout para garantir que funcione no iOS
  setTimeout(() => {
    link.click();
    document.body.removeChild(link);
    window.URL.revokeObjectURL(url);
  }, 0);
};

// Função para obter MIME type baseado na extensão do arquivo
const getMimeType = (fileName: string): string => {
  const extension = fileName.split(".").pop()?.toLowerCase();

  const mimeTypes: Record<string, string> = {
    // Documentos
    pdf: "application/pdf",
    doc: "application/msword",
    docx: "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
    xls: "application/vnd.ms-excel",
    xlsx: "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
    ppt: "application/vnd.ms-powerpoint",
    pptx: "application/vnd.openxmlformats-officedocument.presentationml.presentation",
    txt: "text/plain",
    csv: "text/csv",
    rtf: "application/rtf",

    // Imagens
    jpg: "image/jpeg",
    jpeg: "image/jpeg",
    png: "image/png",
    gif: "image/gif",
    bmp: "image/bmp",
    webp: "image/webp",
    svg: "image/svg+xml",
    ico: "image/x-icon",

    // Áudio
    mp3: "audio/mpeg",
    wav: "audio/wav",
    ogg: "audio/ogg",
    m4a: "audio/mp4",

    // Vídeo
    mp4: "video/mp4",
    avi: "video/x-msvideo",
    mov: "video/quicktime",
    wmv: "video/x-ms-wmv",
    webm: "video/webm",

    // Compactados
    zip: "application/zip",
    rar: "application/x-rar-compressed",
    "7z": "application/x-7z-compressed",
    tar: "application/x-tar",
    gz: "application/gzip",

    // Outros
    json: "application/json",
    xml: "application/xml",
    html: "text/html",
    css: "text/css",
    js: "application/javascript",
  };

  return mimeTypes[extension || ""] || "application/octet-stream";
};

// Função para detectar o MIME type a partir do base64
const getMimeTypeFromBase64 = (base64String: string): string => {
  // Se o base64 já contém o MIME type no cabeçalho
  if (base64String.startsWith("data:")) {
    const mimeMatch = base64String.match(/data:([^;]+)/);
    if (mimeMatch) {
      return mimeMatch[1];
    }
  }

  // Detecta pelo cabeçalho do arquivo (magic numbers)
  const base64Data = base64String.includes(",")
    ? base64String.split(",")[1]
    : base64String;

  try {
    const binaryString = atob(base64Data.substring(0, 20)); // Primeiros bytes
    const bytes = new Uint8Array(binaryString.length);
    for (let i = 0; i < binaryString.length; i++) {
      bytes[i] = binaryString.charCodeAt(i);
    }

    // Verifica assinaturas de arquivo comuns
    const signature = Array.from(bytes.slice(0, 4))
      .map((b) => b.toString(16).padStart(2, "0"))
      .join("");

    switch (signature.substring(0, 8)) {
      case "25504446": // PDF
        return "application/pdf";
      case "89504e47": // PNG
        return "image/png";
      case "ffd8ffe0": // JPEG
      case "ffd8ffe1":
      case "ffd8ffe2":
        return "image/jpeg";
      case "47494638": // GIF
        return "image/gif";
      case "504b0304": // ZIP/DOCX/XLSX
        return "application/zip";
      default:
        // Fallback para PDF se não conseguir detectar
        return "application/pdf";
    }
  } catch (error) {
    // Se houver erro na detecção, assume PDF
    return "application/pdf";
  }
};

export const base64ToBlob = (base64String: string, mimeType?: string): Blob => {
  const base64Data = base64String.includes(",")
    ? base64String.split(",")[1]
    : base64String;
  const byteCharacters = atob(base64Data);
  const byteNumbers = new Array(byteCharacters.length);
  for (let i = 0; i < byteCharacters.length; i++) {
    byteNumbers[i] = byteCharacters.charCodeAt(i);
  }
  const byteArray = new Uint8Array(byteNumbers);

  // Se não foi fornecido mimeType, detecta automaticamente
  const finalMimeType = mimeType || getMimeTypeFromBase64(base64String);

  return new Blob([byteArray], { type: finalMimeType });
};

export const openBase64File = async (base64String: string, fileName: string) => {
  const blob = base64ToBlob(base64String);
  await openFileFromBlob(blob, fileName);
};

export const formatFileSize = (bytes: number) => {
  if (bytes === 0) return "0 Bytes";
  const k = 1024;
  const sizes = ["Bytes", "KB", "MB", "GB"];
  const i = Math.floor(Math.log(bytes) / Math.log(k));
  return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + " " + sizes[i];
};

export const calculateAge = (birthday: string) => {
  const today = new Date();
  const birthDate = new Date(birthday);

  let age = today.getFullYear() - birthDate.getFullYear();
  const monthDifference = today.getMonth() - birthDate.getMonth();

  if (
    monthDifference < 0 ||
    (monthDifference === 0 && today.getDate() < birthDate.getDate())
  ) {
    age--;
  }

  return age;
};
