import { portalClient } from '@/core/http/httpClient';
import {
  DependentDTO,
  DependentListResponse
} from '@/types/domains/dependent';

export async function listDependents(): Promise<DependentListResponse> {
  const { data } = await portalClient.get<DependentListResponse>('/dependent/list');
  return data;
}

export async function getDependentInfo(id: string): Promise<DependentDTO> {
  const { data } = await portalClient.get<DependentDTO>(`/dependent/${id}/info`);
  return data;
}

export async function getDependentDocument(id: string, documentType: string): Promise<any> {
  const { data } = await portalClient.get<any>(`/dependent/${id}/document`, {
    params: { documentType }
  });
  return data;
}

export async function downloadDependentDocument(id: string, documentType: string): Promise<Blob> {
  const { data } = await portalClient.get<Blob>(`/dependent/${id}/document/download`, {
    params: { documentType }
  });
  return data;
}

export async function getResponsible(): Promise<DependentDTO> {
  const { data } = await portalClient.get<DependentDTO>('/dependent/responsible');
  return data;
}
