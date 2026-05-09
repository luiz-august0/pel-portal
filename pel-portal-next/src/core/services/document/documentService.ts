import { portalClient } from "@/core/http/httpClient";
import { DocumentResponse, DocumentType, DocumentUploadRequest, RelationshipType } from "@/types/domains/document";

export async function uploadDocument(document: DocumentUploadRequest): Promise<void> {
  await portalClient.post(`/document/upload`, document.file, {
    params: { documentType: document.documentType }
  });
}

export async function deleteDocument(documentType: DocumentType): Promise<void> {
  await portalClient.delete(`/document/delete`, undefined, {
    params: { documentType }
  });
}

export async function downloadDocument(documentType: DocumentType): Promise<Blob> {
  const { data } = await portalClient.get<Blob>(`/document/download`, {
    params: { documentType }
  });
  return data;
}

export async function getDocument(documentType: DocumentType): Promise<DocumentResponse> {
  const { data } = await portalClient.get<DocumentResponse>(`/document`, {
    params: { documentType }
  });
  return data;
}
