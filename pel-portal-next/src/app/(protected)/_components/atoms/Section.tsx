import { ReactNode } from 'react';

interface SectionProps {
  title: string;
  children: ReactNode;
  className?: string;
}

export function Section({ title, children, className = '' }: SectionProps) {
  return (
    <section className={`mb-8 ${className}`}>
      <h2 className="text-lg font-semibold mb-4">{title}</h2>
      {children}
    </section>
  );
}
