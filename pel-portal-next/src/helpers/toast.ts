"use client";

import { HttpStandardError } from "@/types/api";
import { HttpError } from "@/core/http/httpError";
import { toast } from "sonner";

export const handlerHttpError = (error: any) => {
  httpErrorToast(error as HttpStandardError & HttpError);
};

const httpErrorToast = (error: HttpStandardError & HttpError) => {
  const message = (error.response?.data as any)?.message ?? (error.message ?? "");

  if (error.response?.status == 401 || error.status == 401) {
    toast.warning(message);
  } else {
    toast.error(message);
  }
};
