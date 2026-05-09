import { portalAuthClient } from "@/core/http/httpClient";
import { PasswordRecoveryRequestType, PasswordResetType } from "@/types/domains/auth";

// Função para solicitar recuperação de senha
export async function requestPasswordRecovery(req: PasswordRecoveryRequestType): Promise<void> {
  await portalAuthClient.post('/auth/recovery', req);
}

// Função para redefinir senha
export async function resetPassword(req: Omit<PasswordResetType, 'confirmPassword'>): Promise<void> {
  await portalAuthClient.post('/auth/recovery/password', req);
}