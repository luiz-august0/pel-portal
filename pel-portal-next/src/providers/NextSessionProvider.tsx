'use client';

import { ReactNode } from "react";
import { SessionProvider } from "next-auth/react"

interface SessionProps {
  children: ReactNode;
}

export default function NextSessionProvider({children}: SessionProps) {
  return <SessionProvider>{children}</SessionProvider>
}