import { z } from "zod";

// Schema para professor
export const ProfessorSchema = z.object({
  id: z.number(),
  cpf: z.string(),
  name: z.string(),
  birthDate: z.string(),
  email: z.string(),
  phone: z.string(),
  cellphone: z.string(),
  facebookLink: z.string(),
  bondType: z.string(),
  preRegistration: z.boolean(),
  active: z.boolean(),
});

// Schema para o nível atual do usuário
export const ActualLevelSchema = z.object({
  id: z.number(),
  course: z.object({
    id: z.number(),
    courseName: z.string(),
    certificateCourseName: z.string(),
  }),
  levelName: z.string(),
  levelCode: z.number(),
  levelDescription: z.string(),
  nextLevel: z.string(),
  allowsEntry: z.boolean(),
  allowsCompetition: z.boolean(),
  isGraduating: z.boolean(),
  isPostCompletion: z.boolean(),
});

// Schema para turma disponível
export const AvailableClassSchema = z.object({
  id: z.number(),
  professorName: z.string().optional(),
  professor: ProfessorSchema,
  className: z.string(),
  dayOfWeek: z.string(),
  roomName: z.string(),
  subscribers: z.number(),
  startTime: z.string(),
  endTime: z.string(),
  plannedStartDate: z.string(),
  plannedEndDate: z.string(),
  availableSlots: z.number(),
});

// Schema para lista de turmas disponíveis
export const AvailableClassesResponseSchema = z.array(AvailableClassSchema);

// Schema para o primeiro step da inscrição
export const InscriptionStep1Schema = z.object({
  courseId: z.number().min(1, "Selecione um curso"),
  classId: z.number().min(1, "Selecione uma turma"),
});

// Schema para resposta de inscrição
export const InscriptionResponseSchema = z.object({
  success: z.boolean(),
  message: z.string().optional(),
});

// Schema para forma de pagamento
export const PaymentFormSchema = z.object({
  name: z.string(),
  description: z.string(),
  paymentForm: z.enum(["B1x", "B2x", "B3x"]),
  total: z.number(),
  installments: z.number(),
  installmentsValue: z.number(),
});

// Schema para lista de formas de pagamento
export const PaymentFormsResponseSchema = z.array(PaymentFormSchema);

// Schema para o segundo step da inscrição (forma de pagamento)
export const InscriptionStep2Schema = z.object({
  paymentForm: z.enum(["B1x", "B2x", "B3x"], {
    message: "Selecione uma forma de pagamento",
  }),
});

// Schema para o terceiro step da inscrição (consentimento)
export const InscriptionStep3Schema = z.object({
  acceptContract: z.boolean().refine((val) => val === true, {
    message: "Você deve aceitar o contrato",
  }),
  acceptImageTerm: z.boolean()
});

// Schema para o quarto step da inscrição (revisão e finalização)
export const InscriptionStep4Schema = z.object({
  acceptContract: z.boolean(),
  acceptImageTerm: z.boolean(),
});

// Schema para estudante
export const StudentSchema = z.object({
  id: z.number(),
  cpf: z.string(),
  name: z.string(),
  birthDate: z.string(),
  email: z.string(),
  phone: z.string(),
  cellphone: z.string(),
  facebookLink: z.string(),
  bondType: z.string(),
  preRegistration: z.boolean(),
  active: z.boolean(),
});

// Schema para curso
export const CourseSchema = z.object({
  id: z.number(),
  courseName: z.string(),
  certificateCourseName: z.string(),
});

// Schema para nível
export const LevelSchema = z.object({
  id: z.number(),
  course: CourseSchema,
  levelName: z.string(),
  levelCode: z.number(),
  levelDescription: z.string(),
  allowsEntry: z.boolean(),
  allowsCompetition: z.boolean(),
  isGraduating: z.boolean(),
  isPostCompletion: z.boolean(),
});

// Schema para turma completa
export const ClassSchema = z.object({
  id: z.number(),
  course: CourseSchema,
  level: LevelSchema,
  professor: ProfessorSchema,
  dayOfWeek: z.string(),
  availableSlots: z.number(),
  startTime: z.string(),
  endTime: z.string(),
  className: z.string(),
  plannedStartDate: z.string(),
  inactive: z.boolean(),
  roomName: z.string(),
  closed: z.boolean(),
  plannedEndDate: z.string(),
  subscribers: z.number(),
});

// Schema para detalhes da inscrição
export const InscriptionDetailsSchema = z.object({
  id: z.number(),
  student: StudentSchema,
  clazz: ClassSchema,
  status: z.enum(["A", "D", "T", "P", "C"]),
  observation: z.string(),
  lastUpdateUser: z.string(),
  lastUpdateDate: z.string(),
  attendancePercentage: z.number(),
  finalGrade: z.number(),
  result: z.string(),
  certificateCode: z.string(),
  inscriptionDate: z.string(),
  exitDate: z.string().nullable(),
  contractText: z.string(),
  closed: z.boolean(),
  imageTermDescription: z.string(),
  inscriptionType: z.string(),
  inscriptionFinalized: z.boolean().optional(),
  fileContractName: z.string().optional(),
  fileCertificateName: z.string().optional(),
});

// Schema para duplicata
export const DuplicateSchema = z.object({
  id: z.number(),
  issueDate: z.string(),
  dueDate: z.string(),
  operationDate: z.string().nullable(),
  status: z.enum(["A", "L", "C"]),
  installmentNumber: z.number(),
  totalInstallments: z.number(),
  duplicateAmount: z.number(),
  discountAmount: z.number(),
  interestAmount: z.number(),
  operationAmount: z.number(),
  observation: z.string(),
});

// Schema para boleto
export const DuplicateReceivableSchema = z.object({
  id: z.number(),
  duplicate: DuplicateSchema,
  lateInterestPercentage: z.number(),
  finePercentage: z.number(),
  bankSlipNumber: z.string(),
  reissue: z.boolean(),
  collection: z.boolean(),
});

// Schema para lista de boletos
export const DuplicateReceivablesResponseSchema = z.array(DuplicateReceivableSchema);

// Schema para inscrições agrupadas por ano
export const InscriptionsByYearSchema = z.object({
  year: z.string(),
  inscriptions: z.array(InscriptionDetailsSchema),
});

// Schema para lista de inscrições agrupadas por ano
export const InscriptionsGroupedByYearResponseSchema = z.array(InscriptionsByYearSchema);

// Schema para avaliação do curso
export const CourseEvaluationSchema = z.object({
  id: z.number(),
  courseEvaluationName: z.string(),
  groupNumber: z.number(),
  weight: z.number(),
  isOral: z.boolean(),
});

// Schema para nota individual
export const GradeSchema = z.object({
  id: z.number(),
  courseEvaluation: CourseEvaluationSchema,
  gradeValue: z.number(),
});

// Schema para trimestre
export const QuarterGradeSchema = z.object({
  quarter: z.number(),
  grade: z.number(),
  grades: z.array(GradeSchema),
});

// Schema para lista de notas da inscrição
export const InscriptionGradesResponseSchema = z.array(QuarterGradeSchema);

// Schema para plano de aula
export const LessonPlanSchema = z.object({
  id: z.number(),
  activityName: z.string(),
  activityDescription: z.string(),
  completedDate: z.string(),
  isExtraClassMakeup: z.boolean(),
  unitName: z.string(),
});

// Schema para presença
export const AttendanceSchema = z.object({
  id: z.number(),
  lessonPlan: LessonPlanSchema,
  isPresent: z.enum(["NONE", "PRESENT", "ABSENT"]),
  isPresent2: z.enum(["NONE", "PRESENT", "ABSENT"]),
});

// Schema para lista de presenças da inscrição
export const InscriptionAttendanceResponseSchema = z.array(AttendanceSchema);

// Tipos TypeScript
export type ActualLevel = z.infer<typeof ActualLevelSchema>;
export type AvailableClass = z.infer<typeof AvailableClassSchema>;
export type AvailableClassesResponse = z.infer<typeof AvailableClassesResponseSchema>;
export type PaymentForm = z.infer<typeof PaymentFormSchema>;
export type PaymentFormsResponse = z.infer<typeof PaymentFormsResponseSchema>;
export type InscriptionStep1 = z.infer<typeof InscriptionStep1Schema>;
export type InscriptionStep2 = z.infer<typeof InscriptionStep2Schema>;
export type InscriptionStep3 = z.infer<typeof InscriptionStep3Schema>;
export type InscriptionStep4 = z.infer<typeof InscriptionStep4Schema>;
export type InscriptionResponse = z.infer<typeof InscriptionResponseSchema>;
export type Student = z.infer<typeof StudentSchema>;
export type Course = z.infer<typeof CourseSchema>;
export type Level = z.infer<typeof LevelSchema>;
export type Professor = z.infer<typeof ProfessorSchema>;
export type Class = z.infer<typeof ClassSchema>;
export type InscriptionDetails = z.infer<typeof InscriptionDetailsSchema>;
export type Duplicate = z.infer<typeof DuplicateSchema>;
export type DuplicateReceivable = z.infer<typeof DuplicateReceivableSchema>;
export type DuplicateReceivablesResponse = z.infer<typeof DuplicateReceivablesResponseSchema>;
export type InscriptionsByYear = z.infer<typeof InscriptionsByYearSchema>;
export type InscriptionsGroupedByYearResponse = z.infer<typeof InscriptionsGroupedByYearResponseSchema>;
export type CourseEvaluation = z.infer<typeof CourseEvaluationSchema>;
export type Grade = z.infer<typeof GradeSchema>;
export type QuarterGrade = z.infer<typeof QuarterGradeSchema>;
export type InscriptionGradesResponse = z.infer<typeof InscriptionGradesResponseSchema>;
export type LessonPlan = z.infer<typeof LessonPlanSchema>;
export type Attendance = z.infer<typeof AttendanceSchema>;
export type InscriptionAttendanceResponse = z.infer<typeof InscriptionAttendanceResponseSchema>;
