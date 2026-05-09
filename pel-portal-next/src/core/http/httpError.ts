export class HttpError extends Error {
  readonly status: number;
  readonly statusText: string;
  readonly response?: any;

  constructor(status: number, statusText: string, response?: any) {
    super((response as any)?.error || (response as any)?.message || (response as any)?.mensagem || (response as any)?.erro || "Erro");
    this.name = 'HttpError';
    this.status = status;
    this.statusText = statusText;
    this.response = response;
  }
}
