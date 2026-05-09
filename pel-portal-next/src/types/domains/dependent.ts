import { z } from "zod";
import { validateBr } from "js-brasil";
import { UserType } from "@/types/domains/auth";
import { addressSchema } from "@/types/domains/address";

// Enum para parentesco do dependente
export enum DependentRelationship {
  SON = "SON",
  NEPHEW = "NEPHEW",
  STEPSON = "STEPSON",
  GRANDCHILD = "GRANDCHILD",
}

// Labels para exibição do parentesco
export const dependentRelationshipLabels = {
  [DependentRelationship.SON]: "Filho(a)",
  [DependentRelationship.NEPHEW]: "Sobrinho(a)",
  [DependentRelationship.STEPSON]: "Enteado(a)",
  [DependentRelationship.GRANDCHILD]: "Neto(a)",
};

export const responsibleRelationshipLabels = {
  [DependentRelationship.SON]: "Pai/Mãe",
  [DependentRelationship.NEPHEW]: "Tio/Tia",
  [DependentRelationship.STEPSON]: "Avô/Avó",
  [DependentRelationship.GRANDCHILD]: "Padrasto/Madrasta",
};

// Schema para endereço do dependente (Step 2)
export const dependentAddressSchema = z
  .object({
    sameAddress: z.boolean(),
    cep: z.string().optional(),
    street: z.string().optional(),
    number: z.string().optional(),
    complement: z.string().optional(),
    neighborhood: z.string().optional(),
    city: z.string().optional(),
    state: z.string().optional(),
  })
  .refine(
    (data) => {
      // Validação específica do CEP quando sameAddress for false
      if (data.sameAddress || !data.cep) {
        return true;
      }
      return validateBr.cep(data.cep);
    },
    {
      message: "CEP inválido",
      path: ["cep"],
    }
  )
  .refine(
    (data) => {
      // Se sameAddress for true, não valida os outros campos
      if (data.sameAddress) {
        return true;
      }

      // Se sameAddress for false, valida todos os campos obrigatórios
      const requiredFields = [
        "cep",
        "street",
        "number",
        "neighborhood",
        "city",
        "state",
      ] as const;
      return requiredFields.every((field) => {
        const value = data[field];
        return value && typeof value === "string" && value.trim() !== "";
      });
    },
    {
      message:
        "Todos os campos de endereço são obrigatórios quando não usar o mesmo endereço",
      path: ["cep"], // Define onde o erro será exibido
    }
  )
  .refine(
    (data) => {
      // Validação específica do estado quando sameAddress for false
      if (data.sameAddress || !data.state) {
        return true;
      }
      return data.state.length === 2;
    },
    {
      message: "Estado inválido",
      path: ["state"],
    }
  );

// Schema para parentesco e necessidades especiais (Step 3)
export const dependentRelationshipSchema = z.object({
  dependentRelationship: z
    .nativeEnum(DependentRelationship)
    .refine((val) => val, {
      message: "Parentesco é obrigatório",
    }),
  specialNeeds: z.boolean(),
});

// Tipos TypeScript derivados dos schemas
export type DependentAddressType = z.infer<typeof dependentAddressSchema>;
export type DependentRelationshipType = z.infer<typeof dependentRelationshipSchema>;

export interface DependentDTO {
  id: string;
  dependent: UserType;
  dependentRelationship?: DependentRelationship;
  fromLink: boolean;
}

export interface DependentListResponse {
  pending: DependentDTO[];
  active: DependentDTO[];
}

export interface RecognizeDependentRequest {
  id: string;
  recognize: boolean;
}

// Request types para os endpoints
export interface UpdateDependentRequest {
  name: string;
  email: string;
  birthDate: string;
  phone?: string;
  cpf: string;
}

export interface DependentAddressRequest {
  cep: string;
  street: string;
  number: string;
  complement?: string;
  neighborhood: string;
  city: string;
  state: string;
  sameAddress: boolean;
}

export interface UpdateDependentRelationshipRequest {
  dependentRelationship: DependentRelationship;
  specialNeeds: boolean;
}
