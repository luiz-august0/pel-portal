import { validateBr } from "js-brasil";
import { z } from "zod";
import { RelationshipType } from "@/types/domains/document";
import { ProgramKnowledgeSource } from "@/types/domains/register";
import { REGEX_PASSWORD_VALIDATION } from "../general";

export const loginSchema = z.object({
  cpf: z.string()
    .min(1, "CPF é obrigatório")
    .refine((value) => validateBr.cpf(value), "CPF inválido"),
  password: z.string()
    .min(1, "Senha é obrigatória"),
  authorizedToken: z.string().optional()
});

export type LoginType = z.infer<typeof loginSchema>;

export type LoginResponseType = { accessToken: string }

// Schema para solicitação de recuperação de senha
export const passwordRecoveryRequestSchema = z.object({
  cpf: z.string()
    .min(1, "CPF é obrigatório")
    .refine((value) => validateBr.cpf(value), "CPF inválido")
});

export type PasswordRecoveryRequestType = z.infer<typeof passwordRecoveryRequestSchema>;

// Schema para redefinição de senha
export const passwordResetSchema = z.object({
  token: z.string().min(1, "Token é obrigatório"),
  password: z.string()
    .refine((value) => value.match(REGEX_PASSWORD_VALIDATION), "Senha deve conter pelo menos: 1 letra minúscula, 1 maiúscula, 1 número e 1 caractere especial"),
  confirmPassword: z.string()
}).refine((data) => data.password === data.confirmPassword, {
  message: "Senhas não coincidem",
  path: ["confirmPassword"]
});

export type PasswordResetType = z.infer<typeof passwordResetSchema>;

export type UserType = {
	id: string,
	name: string,
	email: string,
	cpf: string,
	password: string,
	active: boolean,
	authorized: boolean,
	reviewed: boolean,
	userDetails: {
		birthDate: string,
		specialNeeds: boolean,
		programKnowledgeSource: ProgramKnowledgeSource,
		internalRelationshipType: RelationshipType,
		minor: boolean,
		phone?: string
	},
	address?: {
		id?: string,
		cep: string,
		street: string,
		number: string,
		complement?: string,
		neighborhood: string,
		city: string,
		state: string
	},
  responsibleToken?: string,
  responsibleTokenGeneratedAt?: string,
  responsibleTokenExpiresAt?: string
}