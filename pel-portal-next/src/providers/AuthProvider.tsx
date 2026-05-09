'use client';

import { getCurrentUser } from "@/core/services/auth/getCurrentUserService";
import { handlerHttpError } from '@/helpers/toast';
import { signOut } from 'next-auth/react';
import { useSession } from 'next-auth/react';
import { useRouter } from 'next/navigation';
import { createContext, useContext, useEffect, useRef, useState } from 'react';
import { toast } from 'sonner';

type Props = {
  loadingSession: boolean;
  refreshUserData: () => Promise<void>;
};

export const AuthContext = createContext<Partial<Props>>({});

export const AuthProvider = ({ children }: { children: React.ReactNode }) => {
  const router = useRouter();
  const { status, data: session, update } = useSession();
  const [loadingSession, setLoadingSession] = useState<boolean>(true);
  const hasInitialized = useRef<boolean>(false);

  // Função consolidada para gerenciar sessão e atualização
  const handleSessionAndUpdate = async () => {
    try {
      // Busca dados atualizados do usuário
      const userData = await getCurrentUser();

      if (userData) {
        // Atualiza a sessão com os dados mais recentes
        await update({
          ...session,
          user: {
            ...session?.user,
            ...userData,
          },
        });
      }

      setLoadingSession(false);
    } catch (error) {
      toast.warning('Sessão expirada, realize o login novamente');
      await signOut({ redirect: false });
      router.replace("/onboarding");
    }
  };

  useEffect(() => {
    // Evita execução múltipla durante inicialização
    if (hasInitialized.current) return;

    const initializeAuth = async () => {
      setLoadingSession(true);
      
      // Aguarda um tempo para garantir que a sessão foi carregada
      setTimeout(async () => {
        if (status === 'authenticated') {
          await handleSessionAndUpdate();
          hasInitialized.current = true;
        } else if (status === 'unauthenticated') {
          router.replace("/onboarding");
        }
      }, 1000);
    };

    initializeAuth();
  }, [status]);

  return (
    <AuthContext.Provider
      value={{
        loadingSession,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
};

// Hook personalizado para usar o AuthContext
export const useAuth = () => {
  const context = useContext(AuthContext);
  
  if (!context) {
    throw new Error('useAuth deve ser usado dentro de um AuthProvider');
  }
  
  return context;
};
