import { validateBr } from "js-brasil";
import { z } from "zod";
import { REGEX_PASSWORD_VALIDATION } from "../general";
import dayjs from "dayjs";

export interface RegenerateResponsibleLinkResponse {
  token: string;
  generatedAt: string;
  expires: string;
}

export interface UserStatus {
  description: string;
  checked: boolean;
  optional: boolean;
}

// Schema para dados do usuário que podem ser editados
export const userDataSchema = z.object({
  cpf: z
    .string()
    .min(1, "CPF é obrigatório")
    .refine((value) => validateBr.cpf(value), "CPF inválido"),
  name: z
    .string()
    .min(1, "Nome é obrigatório")
    .max(150, "Nome deve ter no máximo 150 caracteres"),
  email: z.email("E-mail inválido").min(1, "E-mail é obrigatório"),
  phone: z
    .string()
    .min(1, "Telefone é obrigatório")
    .refine((value) => {
      const cleanPhone = value.replace(/\D/g, "");
      return cleanPhone.length === 11;
    }, "Telefone inválido"),
  birthDate: z
    .string()
    .min(1, "Data de nascimento é obrigatória")
    .refine((value) => dayjs(value).isValid(), "Data de nascimento inválida")
    .refine((value) => {
      const date = new Date(value);
      const today = new Date();
      return date <= today;
    }, "Data de nascimento não pode ser futura"),
});

export const userDataWithPasswordSchema = userDataSchema.merge(
  z.object({
    password: z
      .string()
      .min(1, "Senha é obrigatória")
      .refine(
        (value) => value.match(REGEX_PASSWORD_VALIDATION),
        "Senha inválida"
      ),
  })
);

// Schema para alteração de senha
export const passwordChangeSchema = z
  .object({
    password: z
      .string()
      .refine(
        (value) => value.match(REGEX_PASSWORD_VALIDATION),
        "Senha deve conter pelo menos: 1 letra minúscula, 1 maiúscula, 1 número e 1 caractere especial"
      ),
    confirmPassword: z.string(),
  })
  .refine((data) => data.password === data.confirmPassword, {
    message: "Senhas não coincidem",
    path: ["confirmPassword"],
  });

export type PasswordChangeType = z.infer<typeof passwordChangeSchema>;
export type UserDataType = z.infer<typeof userDataSchema>;
export type UserDataTypeWithPassword = z.infer<
  typeof userDataWithPasswordSchema
>;
