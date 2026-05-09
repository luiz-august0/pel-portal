import { gestaoClient } from "@/core/http/httpClient";
import {
  LevelingGroupedByYearResponse,
  LevelingRegister,
  AvailableHoursResponse,
  AvailableDatesResponse,
} from "@/types/domains/leveling";

export async function getLevelingGroupedByYear(): Promise<LevelingGroupedByYearResponse> {
  const { data } = await gestaoClient.get<LevelingGroupedByYearResponse>("/leveling/grouped-by-year");
  return data;
}

export async function cancelLeveling(levelingId: number): Promise<void> {
  await gestaoClient.delete(`/leveling/${levelingId}/cancel`);
}

export async function getAvailableHours(courseId: number): Promise<AvailableHoursResponse> {
  const { data } = await gestaoClient.get<AvailableHoursResponse>(`/leveling/hours-available`, {
    params: { courseId }
  });
  return data;
}

export async function registerLeveling(data: LevelingRegister): Promise<void> {
  await gestaoClient.post("/leveling/register", data);
}


