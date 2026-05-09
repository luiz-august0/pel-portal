'use client';

import { ReactNode, useEffect } from 'react';
import LoadingScreen from '@/components/customized/LoadingScreen';
import { useSession } from 'next-auth/react';
import { redirect } from 'next/navigation';

interface Props {
  children: ReactNode;
}

export default function Layout({ children }: Props) {
  const { status } = useSession();

  useEffect(() => {
    if (status == 'authenticated') {
      redirect("/");
    }
  }, [status]);

  if (status == 'loading') {
    return <LoadingScreen />;
  }

  return (
    <div className="pel-gradient">
      {children}
    </div>
  );
}
