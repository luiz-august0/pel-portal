import { HttpError } from "./httpError";
import type { HttpClient, HttpRequestConfig, HttpResponse } from "./types";

const getAuthHeader = (): Record<string, string> => {
  if (typeof window !== "undefined") {
    const accessToken = document.cookie
      .split(";")
      .find((cookie) => cookie.trim().startsWith("accessToken="))
      ?.split("=")[1]
      ?.trim();

    if (accessToken) {
      return { Authorization: `Bearer ${accessToken}` };
    }
  }
  return {};
};

export class HttpClientImpl implements HttpClient {
  private readonly baseURL: string;
  private readonly getDefaultHeaders:
    | (() => Record<string, string>)
    | undefined;

  constructor(
    baseURL: string = "/api",
    getDefaultHeaders?: () => Record<string, string>,
  ) {
    this.baseURL = baseURL;
    this.getDefaultHeaders = getDefaultHeaders;
  }

  private buildURL(
    path: string,
    params?: Record<string, string | number | boolean | string[]>,
  ): string {
    const url = `${this.baseURL}${path}`;
    if (!params || Object.keys(params).length === 0) return url;
    const searchParams = new URLSearchParams();
    Object.entries(params).forEach(([key, value]) => {
      if (Array.isArray(value)) {
        value.forEach((v) => searchParams.append(key, String(v)));
      } else {
        searchParams.append(key, String(value));
      }
    });
    return `${url}?${searchParams.toString()}`;
  }

  protected async request<T>(
    method: string,
    url: string,
    data?: unknown,
    config?: HttpRequestConfig,
  ): Promise<HttpResponse<T>> {
    const fullURL = this.buildURL(url, config?.params);
    const isFormData =
      typeof FormData !== "undefined" && data instanceof FormData;
    const headers: Record<string, string> = {
      ...(isFormData ? {} : { "Content-Type": "application/json" }),
      ...(this.getDefaultHeaders?.() || {}),
      ...config?.headers,
    };

    const options: RequestInit = {
      method,
      headers,
      signal: config?.signal,
    };

    if (data !== undefined && method !== "GET") {
      options.body = isFormData ? (data as FormData) : JSON.stringify(data);
    }

    const response = await fetch(fullURL, options);
    let responseData: T;
    const contentType = response.headers.get("content-type");

    if (contentType?.includes("application/json")) {
      responseData = await response.json();
    } else {
      responseData = (await response.text()) as unknown as T;
    }

    if (!response.ok) {
      throw new HttpError(response.status, response.statusText, responseData);
    }

    return { data: responseData, status: response.status, ok: response.ok };
  }

  get<T = unknown>(
    url: string,
    config?: HttpRequestConfig,
  ): Promise<HttpResponse<T>> {
    return this.request<T>("GET", url, undefined, config);
  }

  post<T = unknown>(
    url: string,
    data?: unknown,
    config?: HttpRequestConfig,
  ): Promise<HttpResponse<T>> {
    return this.request<T>("POST", url, data, config);
  }

  put<T = unknown>(
    url: string,
    data?: unknown,
    config?: HttpRequestConfig,
  ): Promise<HttpResponse<T>> {
    return this.request<T>("PUT", url, data, config);
  }

  patch<T = unknown>(
    url: string,
    config?: HttpRequestConfig,
    data?: unknown,
  ): Promise<HttpResponse<T>> {
    return this.request<T>("PATCH", url, data, config);
  }

  delete<T = unknown>(
    url: string,
    config?: HttpRequestConfig,
    data?: unknown,
  ): Promise<HttpResponse<T>> {
    return this.request<T>("DELETE", url, data, config);
  }
}

export const portalClient = new HttpClientImpl(
  `${process.env.NEXT_PUBLIC_API}/portal`,
  getAuthHeader,
);
export const gestaoClient = new HttpClientImpl(
  `${process.env.NEXT_PUBLIC_API}/gestao`,
  getAuthHeader,
);
export const portalAuthClient = new HttpClientImpl(
  `${process.env.NEXT_PUBLIC_API}/portal`,
);
