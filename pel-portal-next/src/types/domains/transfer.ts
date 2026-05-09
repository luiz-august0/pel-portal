import { z } from "zod";
import { ClassSchema, InscriptionDetailsSchema } from "@/types/domains/inscription";

// Schema para transferência
export const TransferSchema = z.object({
  id: z.number(),
  requestDate: z.string(),
  sourceInscription: InscriptionDetailsSchema,
  destinationClass: ClassSchema,
  approvalDate: z.string().optional(),
  status: z.string(),
  observation: z.string(),
});

// Schema para transferências agrupadas por ano
export const TransfersByYearSchema = z.object({
  year: z.string(),
  transfers: z.array(TransferSchema),
});

export const TransfersGroupedByYearResponseSchema = z.array(TransfersByYearSchema);

// Tipos TypeScript
export type Transfer = z.infer<typeof TransferSchema>;
export type TransfersByYear = z.infer<typeof TransfersByYearSchema>;
export type TransfersGroupedByYearResponse = z.infer<typeof TransfersGroupedByYearResponseSchema>;
