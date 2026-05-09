'use client';

import { SidebarProvider } from '@/components/ui/sidebar';
import { AuthProvider, useAuth } from '@/providers/AuthProvider';
import { ReactNode } from 'react';
import { LayoutProvider } from '@/providers/LayoutProvider';
import LoadingScreen from '@/components/customized/LoadingScreen';

interface Props {
  children: ReactNode;
}

function RenderLayout({ children }: { children: ReactNode }) {
  const { loadingSession } = useAuth();

  if (loadingSession) {
    return <LoadingScreen />;
  }

  return (
    <div className="flex w-full">
      <main className="flex-1 bg-background">{children}</main>
    </div>
  );
}

export default function Layout({ children }: Props) {
  return (
    <LayoutProvider>
      <AuthProvider>
        <SidebarProvider>
          <RenderLayout>{children}</RenderLayout>
        </SidebarProvider>
      </AuthProvider>
    </LayoutProvider>
  );
}
