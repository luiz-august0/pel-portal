import z from "zod";

// Schema para curso
export const CourseSchema = z.object({
  id: z.number(),
  courseName: z.string(),
  certificateCourseName: z.string(),
});

export type Course = z.infer<typeof CourseSchema>;
