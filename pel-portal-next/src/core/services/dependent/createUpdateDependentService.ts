import { portalClient } from "@/core/http/httpClient";
import {
  DependentAddressRequest,
  RecognizeDependentRequest,
  UpdateDependentRelationshipRequest,
  UpdateDependentRequest
} from "@/types/domains/dependent";
import { MultipartBean } from "@/types/domains/document";
import { UserDataTypeWithPassword } from "@/types/domains/user";

export async function createDependent(
  req: UserDataTypeWithPassword,
): Promise<string> {
  const { data } = await portalClient.post<string>("/dependent/create", req);
  return data;
}

export async function recognizeDependent(
  req: RecognizeDependentRequest,
): Promise<void> {
  await portalClient.post(`/dependent/${req.id}/recognize`, undefined, {
    params: { recognize: req.recognize },
  });
}

export async function updateDependentPersonalData(
  id: string,
  data: UpdateDependentRequest,
): Promise<void> {
  await portalClient.put(`/dependent/${id}/update`, data);
}

export async function updateDependentAddress(
  id: string,
  data: DependentAddressRequest,
): Promise<void> {
  await portalClient.post(`/dependent/${id}/address`, data);
}

export async function updateDependentRelationshipAndSpecialNeeds(
  id: string,
  data: UpdateDependentRelationshipRequest,
): Promise<void> {
  await portalClient.put(
    `/dependent/${id}/update-relationship-special-needs`,
    data,
  );
}

export async function uploadDependentDocument(
  id: string,
  documentType: string,
  file: MultipartBean,
): Promise<void> {
  await portalClient.post(`/dependent/${id}/document/upload`, file, {
    params: { documentType },
  });
}
