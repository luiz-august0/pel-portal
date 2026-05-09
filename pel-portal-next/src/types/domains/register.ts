import { validateBr } from "js-brasil";
import { z } from "zod";
import { REGEX_PASSWORD_VALIDATION } from "../general";
import dayjs from "dayjs";

export enum ProgramKnowledgeSource {
  FACEBOOK = "FACEBOOK",
  INSTAGRAM = "INSTAGRAM",
  GOOGLE = "GOOGLE",
  WHATSAPP = "WHATSAPP",
  EMAIL = "EMAIL",
  OUTRO = "OUTRO",
}

// Schema para Dados Pessoais
export const personalDataSchema = z.object({
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

// Schema para Necessidades Especiais
export const specialNeedsSchema = z.object({
  specialNeeds: z
    .boolean()
    .describe("Informação sobre necessidades especiais é obrigatória"),
});

// Schema para Origem de Conhecimento do Programa
export const programKnowledgeSchema = z
  .object({
    programKnowledgeSource: z
      .enum(ProgramKnowledgeSource)
      .describe("Origem de conhecimento do programa é obrigatória"),
    programKnowledgeSourceOther: z.string().optional(),
  })
  .refine(
    (data) => {
      // Se selecionou "OUTRO", deve preencher o campo de especificação
      if (data.programKnowledgeSource === ProgramKnowledgeSource.OUTRO) {
        return (
          data.programKnowledgeSourceOther &&
          data.programKnowledgeSourceOther.trim().length > 0
        );
      }
      return true;
    },
    {
      message:
        "Quando origem de conhecimento for 'Outro', o campo de especificação é obrigatório",
      path: ["programKnowledgeSourceOther"],
    }
  );

// Schema para Senha
export const passwordSchema = z
  .object({
    password: z
      .string()
      .min(1, "Senha é obrigatória")
      .refine(
        (value) => value.match(REGEX_PASSWORD_VALIDATION),
        "Senha inválida"
      ),
    confirmPassword: z.string().min(1, "Confirmação de senha é obrigatória"),
  })
  .refine((data) => data.password === data.confirmPassword, {
    message: "As senhas não coincidem",
    path: ["confirmPassword"],
  });

// Schema completo do registro
export const registerSchema = personalDataSchema
  .merge(specialNeedsSchema)
  .merge(programKnowledgeSchema)
  .merge(passwordSchema);

export type PersonalDataType = z.infer<typeof personalDataSchema>;
export type SpecialNeedsType = z.infer<typeof specialNeedsSchema>;
export type ProgramKnowledgeType = z.infer<typeof programKnowledgeSchema>;
export type PasswordType = z.infer<typeof passwordSchema>;

export type RegisterType = {
  name: string;
  email: string;
  password: string;
  birthDate: string;
  phone?: string;
  cpf: string;
  specialNeeds: boolean;
  programKnowledgeSource: ProgramKnowledgeSource;
  programKnowledgeSourceOther?: string;
  authorizedToken?: string;
};
