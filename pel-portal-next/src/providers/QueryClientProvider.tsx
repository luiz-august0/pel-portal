'use client';

import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { useState } from 'react';

interface QueryClientProviderWrapperProps {
  children: React.ReactNode;
}

export default function QueryClientProviderWrapper({ children }: QueryClientProviderWrapperProps) {
  // Cria uma nova instância do QueryClient para cada componente
  // Isso evita problemas de hidratação entre servidor e cliente
  const [queryClient] = useState(
    () =>
      new QueryClient({
        defaultOptions: {
          queries: {
            // Tempo de cache padrão de 5 minutos
            staleTime: 1000 * 60 * 5,
            // Revalidar dados quando a janela recebe foco
            refetchOnWindowFocus: false,
            // Tentar novamente em caso de erro
            retry: 1,
          },
        },
      })
  );

  return (
    <QueryClientProvider client={queryClient}>
      {children}
    </QueryClientProvider>
  );
}
