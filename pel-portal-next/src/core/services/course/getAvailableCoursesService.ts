import { gestaoClient } from "@/core/http/httpClient";
import { Course } from "@/types/domains/course";

// Buscar cursos disponíveis (assumindo que existe endpoint para isso)
export async function getAvailableCourses(): Promise<Course[]> {
  const response = await gestaoClient.get<Course[]>("/course/list-active");
  return response.data;
}