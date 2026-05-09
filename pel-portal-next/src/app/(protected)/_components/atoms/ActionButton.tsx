import { ReactNode } from 'react';
import { ChevronRight } from 'lucide-react';
import { badgeVariants } from '@/components/ui/badge';
import { Badge } from '@/components/ui/badge';
import { VariantProps } from 'class-variance-authority';

export interface ActionButtonProps {
  children: ReactNode;
  onClick?: () => void;
  badge?: {
    text: string;
    variant?: VariantProps<typeof badgeVariants>['variant'];
  };
  className?: string;
  disabled?: boolean;
}

export function ActionButton({ 
  children, 
  onClick, 
  badge, 
  className = '', 
  disabled = false 
}: ActionButtonProps) {
  return (
    <button
      className={`w-full flex items-center justify-between p-4 bg-gray-100 border border-gray-200 rounded-xl cursor-pointer hover:bg-gray-200 transition-colors disabled:opacity-50 disabled:cursor-not-allowed ${className}`}
      onClick={onClick}
      disabled={disabled}
    >
      <span className="text-base">{children}</span>
      <div className="flex items-center gap-2">
        {badge && (
          <Badge variant={badge.variant}>
            {badge.text}
          </Badge>
        )}
        <ChevronRight className="w-5 h-5 text-gray-400" />
      </div>
    </button>
  );
}
