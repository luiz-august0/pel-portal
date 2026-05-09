import {
  gestaoClient,
} from "@/core/http/httpClient";
import {
  InscriptionAttendanceResponseSchema,
  InscriptionGradesResponseSchema,
  type ActualLevel,
  type AvailableClassesResponse,
  type DuplicateReceivablesResponse,
  type InscriptionAttendanceResponse,
  type InscriptionDetails,
  type InscriptionGradesResponse,
  type InscriptionsGroupedByYearResponse,
  type PaymentFormsResponse
} from "@/types/domains/inscription";

export async function getActualLevel(courseId: number): Promise<ActualLevel> {
  const { data } = await gestaoClient.get<ActualLevel>(
    `/course/${courseId}/actual-level`
  );
  return data;
}

export async function getAvailableClasses(
  courseId: number,
  levelId: number
): Promise<AvailableClassesResponse> {
  const { data } = await gestaoClient.get<AvailableClassesResponse>(
    `/class/list-available`,
    { params: { course: courseId, level: levelId } }
  );
  return data;
}

export async function registerInscription(classId: number): Promise<number> {
  const { data } = await gestaoClient.post<number>(`/inscription/register`, undefined, {
    params: { classId }
  });
  return data;
}

export async function getPaymentForms(): Promise<PaymentFormsResponse> {
  const { data } = await gestaoClient.get<PaymentFormsResponse>("/inscription/payments-form");
  return data;
}

export async function addPaymentFormToInscription(
  inscriptionId: number,
  paymentForm: string
): Promise<string> {
  const { data } = await gestaoClient.post<string>(
    `/inscription/${inscriptionId}/payment-form`,
    undefined,
    { params: { paymentForm } }
  );
  return data;
}

export async function getImageTerm(inscriptionId: number): Promise<string> {
  const { data } = await gestaoClient.get<string>(`/inscription/${inscriptionId}/image-term`);
  return data;
}

export async function finalizeInscription(
  inscriptionId: number,
  paymentForm: string,
  acceptContract: boolean,
  acceptImageAuthorization: boolean
): Promise<void> {
  await gestaoClient.post(
    `/inscription/${inscriptionId}/finalize-register`,
    undefined,
    {
      params: {
        paymentForm,
        acceptContract,
        acceptImageAuthorization
      }
    }
  );
}

export async function getInscriptionDetails(inscriptionId: number): Promise<InscriptionDetails> {
  const { data } = await gestaoClient.get<InscriptionDetails>(`/inscription/${inscriptionId}`);
  return data;
}

export async function getDuplicateReceivables(inscriptionId: number): Promise<DuplicateReceivablesResponse> {
  const { data } = await gestaoClient.get<DuplicateReceivablesResponse>(`/inscription/${inscriptionId}/duplicate-receivables`);
  return data;
}

export async function downloadDuplicateReceivable(
  inscriptionId: number,
  duplicateReceivableId: number
): Promise<Blob> {
  const { data } = await gestaoClient.get<Blob>(
    `/inscription/${inscriptionId}/duplicate-receivable/${duplicateReceivableId}/download`
  );
  return data;
}

export async function downloadContract(inscriptionId: number): Promise<Blob> {
  const { data } = await gestaoClient.get<Blob>(
    `/inscription/${inscriptionId}/contract-pdf`
  );
  return data;
}

export async function getInscriptionsGroupedByYear(): Promise<InscriptionsGroupedByYearResponse> {
  const { data } = await gestaoClient.get<InscriptionsGroupedByYearResponse>("/inscription/grouped-by-year");
  return data;
}

export async function getInscriptionsGroupedByYearByDependentCpf(dependentCpf: string): Promise<InscriptionsGroupedByYearResponse> {
  const { data } = await gestaoClient.get<InscriptionsGroupedByYearResponse>(`/inscription/dependent/grouped-by-year`, {
    params: { cpf: dependentCpf }
  });
  return data;
}

export async function getLastInscription(): Promise<InscriptionDetails | null> {
  const { data } = await gestaoClient.get<InscriptionDetails | null>("/inscription/last");
  return data;
}

export async function getInscriptionGrades(inscriptionId: number): Promise<InscriptionGradesResponse> {
  const { data } = await gestaoClient.get<InscriptionGradesResponse>(`/inscription/${inscriptionId}/grades`);
  return InscriptionGradesResponseSchema.parse(data);
}

export async function getInscriptionAttendance(inscriptionId: number): Promise<InscriptionAttendanceResponse> {
  const { data } = await gestaoClient.get<InscriptionAttendanceResponse>(`/inscription/${inscriptionId}/attendance`);
  return InscriptionAttendanceResponseSchema.parse(data);
}

export async function downloadDeclaration(inscriptionId: number): Promise<Blob> {
  const { data } = await gestaoClient.get<Blob>(
    `/inscription/${inscriptionId}/declaration-pdf`
  );
  return data;
}

export async function downloadCertificate(inscriptionId: number): Promise<Blob> {
  const { data } = await gestaoClient.get<Blob>(
    `/inscription/${inscriptionId}/certificate-pdf`
  );
  return data;
}

export async function getActiveToTransfer(): Promise<InscriptionDetails[]> {
  const { data } = await gestaoClient.get<InscriptionDetails[]>("/inscription/active-to-transfer");
  return data;
}
