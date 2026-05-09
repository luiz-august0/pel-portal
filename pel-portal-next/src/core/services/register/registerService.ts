import { portalAuthClient } from "@/core/http/httpClient";
import { RegisterType } from "@/types/domains/register";

export async function register(req: RegisterType): Promise<void> {
  await portalAuthClient.post('/auth/register', req);
}
