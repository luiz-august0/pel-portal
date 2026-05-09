export function formatMoney(value?: number) {
  return new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(value || 0);
}

export const formatMoneyNotCifra = (value?: number): string => {
  const formatter = new Intl.NumberFormat('pt-BR', {
    style: 'currency',
    currency: 'BRL',
  });

  return formatter
    .format(value || 0)
    .replace('R$', '')
    .trim();
};

export function unmaskInputMoney(value: string): number {
  if (!value) {
    return 0;
  }
  return typeof value === 'number' ? value : Number(value.replace(/\D/g, '')) / 100;
}

export function formatTime(timeString: string) {
  const [hours, minutes] = timeString.split(':');
  return `${hours}:${minutes}`;
}
