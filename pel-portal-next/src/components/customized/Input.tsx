import { forwardRef, ReactNode } from 'react';

import { Input as CNInput, InputProps as CNInputProps } from '@/components/ui/input';
import { cn } from '@/helpers/cn';

export interface InputProps extends CNInputProps {
  label?: string;
  error?: boolean;
  errorMessage?: string;
  left?: ReactNode;
  right?: ReactNode;
}

const Input = forwardRef<HTMLInputElement, InputProps>(
  ({ className, label, error, errorMessage, left, right, required, ...props }, ref) => {
    return (
      <div className={cn('relative', className)}>
        {label && (
          <label htmlFor={props.id} className="mb-2 block text-sm font-medium text-foreground">
            {`${label}${required ? ' *' : ''}`}
          </label>
        )}
        <div className="relative">
          {left}
          <CNInput className={cn(error || errorMessage ? 'border-destructive' : '', className)} ref={ref} {...props} />
          {right}
        </div>
        {errorMessage && <span className="text-destructive text-sm">{errorMessage}</span>}
      </div>
    );
  },
);

export default Input;
