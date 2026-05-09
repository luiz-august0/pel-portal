import { portalClient } from "@/core/http/httpClient";
import { LoginResponseType, LoginType } from "@/types/domains/auth";


export async function login(req: LoginType): Promise<LoginResponseType> {
  const { data } = await portalClient.post<LoginResponseType>('/auth/login', req);
  return data;
}

