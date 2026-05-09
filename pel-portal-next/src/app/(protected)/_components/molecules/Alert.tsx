import { ForwardRefExoticComponent, ReactNode, RefAttributes } from 'react';
import { AlertTriangle, Info, CheckCircle, XCircle, ArrowRight, ChevronRight, LucideProps } from 'lucide-react';
import { cn } from '@/helpers/cn';

type AlertVariant = 'warning' | 'info' | 'success' | 'error';

interface AlertProps {
  variant?: AlertVariant;
  title: string;
  description?: string;
  children?: ReactNode;
  className?: string;
  onClick?: () => void;
  icon?: ForwardRefExoticComponent<Omit<LucideProps, "ref"> & RefAttributes<SVGSVGElement>>
}

const alertStyles = {
  warning: 'bg-yellow-50 border-yellow-200 text-yellow-800',
  info: 'bg-blue-50 border-blue-200 text-blue-800',
  success: 'bg-green-50 border-green-200 text-green-800',
  error: 'bg-red-50 border-red-200 text-red-800'
};

const alertIcons = {
  warning: AlertTriangle,
  info: Info,
  success: CheckCircle,
  error: XCircle
};

const iconColors = {
  warning: 'text-yellow-600',
  info: 'text-blue-600',
  success: 'text-green-600',
  error: 'text-red-600'
};

export function Alert({ 
  variant = 'info', 
  title, 
  description, 
  children, 
  className = '',
  onClick,
  icon
}: AlertProps) {
  const Icon = icon ?? alertIcons[variant];
  
  return (
    <div onClick={onClick} className={cn(`border rounded-xl p-4 flex items-center justify-between ${alertStyles[variant]} ${className}`,
      onClick && 'cursor-pointer'
    )}>
      <div className="flex items-start gap-3">
        <Icon className={`w-5 h-5 mt-0.5 flex-shrink-0 ${iconColors[variant]}`} />
        <div>
          <h3 className="font-semibold text-base mb-1">{title}</h3>
          {description && (
            <p className="text-sm text-gray-700">{description}</p>
          )}
          {children}
        </div>
      </div>
      {onClick && (
        <ChevronRight className="w-5 h-5 text-gray-400 ml-4" />
      )}
    </div>
  );
}
