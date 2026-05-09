import { gestaoClient } from "@/core/http/httpClient";
import { AvailableClassesResponse } from "@/types/domains/inscription";
import {
  Transfer,
  TransfersGroupedByYearResponse,
  TransfersGroupedByYearResponseSchema,
} from "@/types/domains/transfer";

export async function requestTransfer(
  inscriptionId: number,
  classId: number
): Promise<number> {
  const { data } = await gestaoClient.post<number>(
    `/transfer`,
    undefined,
    { params: { inscriptionId, classId } }
  );
  return data;
}

export async function getAvailableClassesForTransfer(
  inscriptionId: number
): Promise<AvailableClassesResponse> {
  const { data } = await gestaoClient.get<AvailableClassesResponse>(
    `/class/available-for-transfer`,
    { params: { inscriptionId } }
  );
  return data;
}

export async function getTransfersGroupedByYear(): Promise<TransfersGroupedByYearResponse> {
  const { data } = await gestaoClient.get<TransfersGroupedByYearResponse>("/transfer/grouped-by-year");
  return data;
}

export async function getTransferDetails(
  transferId: number
): Promise<Transfer> {
  const { data } = await gestaoClient.get<Transfer>(`/transfer/${transferId}/details`);
  return data;
}
