
import { Check, Eye, EyeOff, X } from 'lucide-react';
import { forwardRef, useState } from 'react';

import { Button } from '@/components/ui/button';
import { cn } from '@/helpers/cn';
import Input, { InputProps } from './Input';

interface PasswordInputProps extends Omit<InputProps, 'right'> {
  showValidation?: boolean;
  value?: string;
}

interface ValidationRule {
  label: string;
  test: (password: string) => boolean;
}

const PasswordInput = forwardRef<HTMLInputElement, PasswordInputProps>(
  ({ showValidation = false, value = '', ...props }, ref) => {
    const [showPassword, setShowPassword] = useState<boolean>(false);
    const [validationRules] = useState<ValidationRule[]>([
      {
        label: 'Pelo menos 8 caracteres',
        test: (password: string) => password.length >= 8,
      },
      {
        label: 'Uma letra maiúscula',
        test: (password: string) => /[A-Z]/.test(password),
      },
      {
        label: 'Uma letra minúscula',
        test: (password: string) => /[a-z]/.test(password),
      },
      {
        label: 'Um número',
        test: (password: string) => /\d/.test(password),
      },
      {
        label: 'Um caractere especial',
        test: (password: string) => /[!@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?]/.test(password),
      },
    ]);

    return (
      <div className="space-y-2">
        <Input
          type={showPassword ? 'text' : 'password'}
          ref={ref}
          value={value}
          right={
            <Button
              type="button"
              variant="ghost"
              size="icon"
              className="absolute right-0 top-0 h-full px-3 py-2 hover:bg-transparent"
              onClick={() => setShowPassword((show) => !show)}
              onMouseDown={(e) => e.preventDefault()}
            >
              {showPassword ? (
                <EyeOff className="h-4 w-4 text-muted-foreground" />
              ) : (
                <Eye className="h-4 w-4 text-muted-foreground" />
              )}
            </Button>
          }
          {...props}
        />

        {/* Checklist de validação */}
        {showValidation && (
          <div className="space-y-1 p-3 bg-muted/30 rounded-md">
            <p className="text-sm font-medium text-muted-foreground mb-2">
              Requisitos da senha:
            </p>
            {validationRules.map((rule, index) => {
              const isValid = rule.test(value);
              return (
                <div key={index} className="flex items-center gap-2">
                  {isValid ? (
                    <Check className="h-4 w-4 text-green-600" />
                  ) : (
                    <X className="h-4 w-4 text-muted-foreground" />
                  )}
                  <span
                    className={cn(
                      'text-sm',
                      isValid ? 'text-green-600' : 'text-muted-foreground'
                    )}
                  >
                    {rule.label}
                  </span>
                </div>
              );
            })}
          </div>
        )}
      </div>
    );
  }
);

export default PasswordInput;
