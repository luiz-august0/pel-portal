import { z } from "zod";
import { CourseSchema } from "@/types/domains/course";

// Enum para tempo de estudo do idioma
export const StudyLanguageTimeEnum = z.enum([
  "ONE_TO_TWO_YEARS",
  "TWO_TO_THREE_YEARS", 
  "THREE_PLUS_YEARS"
]);

// Schema para nível (sem nextLevel primeiro)
const BaseLevelSchema = z.object({
  id: z.number(),
  levelName: z.string(),
  levelCode: z.number(),
  levelDescription: z.string(),
  allowsEntry: z.boolean(),
  allowsCompetition: z.boolean(),
  isGraduating: z.boolean(),
  isPostCompletion: z.boolean(),
  course: CourseSchema.optional(),
});

// Schema para nível com nextLevel
export const LevelSchema: z.ZodType<{
  id: number;
  levelName: string;
  levelCode: number;
  levelDescription: string;
  allowsEntry: boolean;
  allowsCompetition: boolean;
  isGraduating: boolean;
  isPostCompletion: boolean;
  course?: {
    id: number;
    courseName: string;
    certificateCourseName: string;
  };
  nextLevel?: {
    id: number;
    levelName: string;
    levelCode: number;
    levelDescription: string;
    allowsEntry: boolean;
    allowsCompetition: boolean;
    isGraduating: boolean;
    isPostCompletion: boolean;
  } | null;
}> = BaseLevelSchema.extend({
  nextLevel: z.lazy(() => BaseLevelSchema.nullable()).optional(),
});

// Schema para avaliador
export const EvaluatorSchema = z.object({
  id: z.number(),
  name: z.string(),
});

// Schema para agendamento de nivelamento
export const LevelingScheduleSchema = z.object({
  id: z.number(),
  course: CourseSchema,
  levelingDate: z.string(),
  availableSlots: z.number(),
});

// Schema para registro de nivelamento
export const LevelingRegistrationSchema = z.object({
  id: z.number(),
  levelingSchedule: LevelingScheduleSchema,
  course: CourseSchema,
  approvedLevel: LevelSchema.nullable(),
  evaluator: EvaluatorSchema.nullable(),
  studyLanguageTime: StudyLanguageTimeEnum,
});

// Schema para nivelamento agrupado por ano
export const LevelingByYearSchema = z.object({
  year: z.string(),
  levelingRegistrations: z.array(LevelingRegistrationSchema),
});

// Schema para resposta da API
export const LevelingGroupedByYearResponseSchema = z.array(LevelingByYearSchema);

// Schema para agendamento de nivelamento
export const LevelingRegisterSchema = z.object({
  courseId: z.number(),
  levelingDate: z.string(), // formato: yyyy-MM-ddTHH:mm
  studyLanguageTime: StudyLanguageTimeEnum,
});

// Schema para horários disponíveis
export const AvailableHoursResponseSchema = z.array(z.string());

// Schema para datas disponíveis
export const AvailableDatesResponseSchema = z.array(z.string());

// Schema para dados do step 1 (aviso)
export const LevelingWarningStepSchema = z.enum(["NEVER", "ONE_TO_TWO_YEARS", "TWO_TO_THREE_YEARS", "THREE_PLUS_YEARS"]);

// Schema para dados do step 2 (agendamento)
export const LevelingSchedulingStepSchema = z.object({
  courseId: z.number().min(1, "Selecione um curso"),
  selectedTime: z.string().min(1, "Selecione um horário"),
});

// Schema para dados completos do formulário
export const LevelingFormDataSchema = z.object({
  warningStep: LevelingWarningStepSchema.optional(),
  schedulingStep: LevelingSchedulingStepSchema.optional(),
});

// Tipos TypeScript derivados dos schemas
export type StudyLanguageTime = z.infer<typeof StudyLanguageTimeEnum>;
export type Level = z.infer<typeof LevelSchema>;
export type Evaluator = z.infer<typeof EvaluatorSchema>;
export type LevelingSchedule = z.infer<typeof LevelingScheduleSchema>;
export type LevelingRegistration = z.infer<typeof LevelingRegistrationSchema>;
export type LevelingByYear = z.infer<typeof LevelingByYearSchema>;
export type LevelingGroupedByYearResponse = z.infer<typeof LevelingGroupedByYearResponseSchema>;
export type LevelingRegister = z.infer<typeof LevelingRegisterSchema>;
export type AvailableHoursResponse = z.infer<typeof AvailableHoursResponseSchema>;
export type AvailableDatesResponse = z.infer<typeof AvailableDatesResponseSchema>;
export type LevelingWarningStep = z.infer<typeof LevelingWarningStepSchema>;
export type LevelingSchedulingStep = z.infer<typeof LevelingSchedulingStepSchema>;
export type LevelingFormData = z.infer<typeof LevelingFormDataSchema>;
