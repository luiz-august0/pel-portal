import { portalClient } from '@/core/http/httpClient';
import { PasswordChangeType, RegenerateResponsibleLinkResponse, UserDataType, UserStatus } from '@/types/domains/user';

export async function regenerateResponsibleLink(): Promise<RegenerateResponsibleLinkResponse> {
  const { data } = await portalClient.post<RegenerateResponsibleLinkResponse>('/user/regenerate-responsible-link');
  return data;
}

export async function updateUser(userData: UserDataType): Promise<void> {
  await portalClient.put('/user/update', userData);
}

export async function getUserStatus(): Promise<UserStatus[]> {
  const { data } = await portalClient.get<UserStatus[]>('/user/status');
  return data;
}

export async function changePassword(req: Omit<PasswordChangeType, 'confirmPassword'>): Promise<void> {
  await portalClient.post('/user/change-password', req);
}
