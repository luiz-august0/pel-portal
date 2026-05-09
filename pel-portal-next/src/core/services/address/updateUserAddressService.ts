import { portalClient } from "@/core/http/httpClient";
import { AddressType } from "@/types/domains/address";


export async function updateUserAddress(req: AddressType): Promise<string> {
  const { data } = await portalClient.post<string>("/user/address", req);
  return data;
}
