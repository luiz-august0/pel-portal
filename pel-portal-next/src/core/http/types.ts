export interface HttpResponse<T = unknown> {
  data: T;
  status: number;
  ok: boolean;
}

export interface HttpRequestConfig {
  headers?: Record<string, string>;
  params?: Record<string, string | number | boolean | string[]>;
  signal?: AbortSignal;
}

export interface HttpClient {
  get<T = unknown>(url: string, config?: HttpRequestConfig): Promise<HttpResponse<T>>;
  post<T = unknown>(url: string, data?: unknown, config?: HttpRequestConfig): Promise<HttpResponse<T>>;
  put<T = unknown>(url: string, data?: unknown, config?: HttpRequestConfig): Promise<HttpResponse<T>>;
  patch<T = unknown>(url: string, data?: unknown, config?: HttpRequestConfig): Promise<HttpResponse<T>>;
  delete<T = unknown>(url: string, config?: HttpRequestConfig): Promise<HttpResponse<T>>;
}
