export enum DocumentType {
  DOCUMENT_WITH_PHOTO = "DOCUMENT_WITH_PHOTO",
  MEDICAL_REPORT = "MEDICAL_REPORT",
  PROOF_OF_INTERNAL_RELATIONSHIP = "PROOF_OF_INTERNAL_RELATIONSHIP"  
}

export enum RelationshipType {
  ACADEMIC_GRADUATION = "ACADEMIC_GRADUATION",
  ACADEMIC_POS_GRADUATION = "ACADEMIC_POS_GRADUATION", 
  UNIVERSITY_STAFF = "UNIVERSITY_STAFF",
  ACTIVE_TEACHING_STAFF = "ACTIVE_TEACHING_STAFF",
  TEMPORARY_TEACHING_STAFF = "TEMPORARY_TEACHING_STAFF",
  PARANA_STATE_SERVER = "PARANA_STATE_SERVER"
}

export const relationshipTypeLabels: Record<RelationshipType, string> = {
  [RelationshipType.ACADEMIC_GRADUATION]: "Acadêmico Graduação",
  [RelationshipType.ACADEMIC_POS_GRADUATION]: "Acadêmico Pós-Graduação",
  [RelationshipType.UNIVERSITY_STAFF]: "Funcionário Ag. Universitário",
  [RelationshipType.ACTIVE_TEACHING_STAFF]: "Funcionário Docente Efetivo",
  [RelationshipType.TEMPORARY_TEACHING_STAFF]: "Funcionário Docente Temporário",
  [RelationshipType.PARANA_STATE_SERVER]: "Servidor do Estado do Paraná"
};

export interface MultipartBean {
  file: string;
  filename: string;
}

export interface FileWithData extends File {
  data?: string;
}

export interface DocumentUploadRequest {
  documentType: DocumentType;
  file: MultipartBean;
}

export interface DocumentResponse {
  id: string;
  documentType: DocumentType;
  originalFilename: string;
  filename: string;
  size: number;
  s3File: boolean;
}

export type Document = DocumentResponse & FileWithData;