import NextSessionProvider from "@/providers/NextSessionProvider";
import QueryClientProviderWrapper from "@/providers/QueryClientProvider";
import type { Metadata } from "next";
import { Inter as FontInter } from "next/font/google";

import { Toaster } from "sonner";
import "./globals.css";

const fontInter = FontInter({
  subsets: ["latin"],
  variable: "--font-sans",
});

export const metadata: Metadata = {
  title: "PEL Portal",
  description: "PEL Portal",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="en" suppressHydrationWarning>
      <body className={`${fontInter.variable} antialiased`}>
        <QueryClientProviderWrapper>
          <NextSessionProvider>
            <Toaster richColors position="top-center" />
            {children}
          </NextSessionProvider>
        </QueryClientProviderWrapper>
      </body>
    </html>
  );
}
