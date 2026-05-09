import { z } from "zod";
import { validateBr } from "js-brasil";

export const addressSchema = z.object({
  cep: z.string().min(1, "CEP é obrigatório").refine((value) => validateBr.cep(value), "CEP inválido"),
  street: z.string().min(1, "Rua é obrigatória"),
  number: z.string().min(1, "Número é obrigatório"),
  complement: z.string().optional(),
  neighborhood: z.string().min(1, "Bairro é obrigatório"),
  city: z.string().min(1, "Cidade é obrigatória"),
  state: z.string().min(1, "Estado é obrigatório").refine((value) => value.length === 2, "Estado inválido"),
});

export type AddressType = z.infer<typeof addressSchema>;
