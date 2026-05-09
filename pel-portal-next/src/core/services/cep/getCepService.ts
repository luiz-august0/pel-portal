import { HttpClientImpl } from "@/core/http/httpClient";
import { CEPResponse } from "@/types/domains/cep";

const cepClient = new HttpClientImpl("https://viacep.com.br");

export async function getCEP(cep: string): Promise<CEPResponse> {
  const { data } = await cepClient.get<CEPResponse>(`/ws/${cep}/json/`);
  return data;
}
