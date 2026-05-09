/**
 * Aplica máscara de CPF no formato XXX.XXX.XXX-XX
 */
export const applyCpfMask = (value: string): string => {
  // Remove todos os caracteres não numéricos
  const numericValue = value.replace(/\D/g, '');
  
  // Aplica a máscara
  return numericValue
    .replace(/(\d{3})(\d)/, '$1.$2')
    .replace(/(\d{3})(\d)/, '$1.$2')
    .replace(/(\d{3})(\d{1,2})/, '$1-$2')
    .replace(/(-\d{2})\d+?$/, '$1');
};

/**
 * Aplica máscara de telefone no formato (XX) XXXXX-XXXX
 */
export const applyPhoneMask = (value: string): string => {
  // Remove todos os caracteres não numéricos
  const numericValue = value.replace(/\D/g, '');
  
  // Aplica a máscara
  return numericValue
    .replace(/(\d{2})(\d)/, '($1) $2')
    .replace(/(\d{5})(\d)/, '$1-$2')
    .replace(/(-\d{4})\d+?$/, '$1');
};

/**
 * Remove máscara de CPF, retornando apenas números
 */
export const removeCpfMask = (value: string): string => {
  return value.replace(/\D/g, '');
};

/**
 * Remove máscara de telefone, retornando apenas números
 */
export const removePhoneMask = (value: string): string => {
  return value.replace(/\D/g, '');
};

export const applyCepMask = (value?: string): string => {
  const numericValue = value?.replace(/\D/g, '');
  return numericValue?.replace(/(\d{5})(\d{3})/, '$1-$2') ?? '';
}
