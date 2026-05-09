import { portalClient } from "@/core/http/httpClient";
import { UserType } from "@/types/domains/auth";

export async function getCurrentUser(accessToken?: string) {
  const { data } = await portalClient.get<UserType>('/user/current', {
    headers: {
      ...(accessToken && { Authorization: `Bearer ${accessToken}` }),
    }
  });

  return data;
}
