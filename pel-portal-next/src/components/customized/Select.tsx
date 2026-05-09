import { Select as CNSelect, SelectContent, SelectItem, SelectTrigger, SelectValue } from '@/components/ui/select';
import { cn } from '@/helpers/cn';

export type SelectProps = React.ComponentProps<typeof CNSelect> &
  React.ComponentProps<typeof SelectTrigger> & {
    label?: string;
    error?: boolean;
    errorMessage?: string;
    placeholder?: string;
    options: { key: any; value: any; label: string }[];
  };

export default function Select({
  label,
  error,
  errorMessage,
  placeholder,
  options,
  className,
  required,
  ...props
}: SelectProps) {
  return (
    <div className="relative">
      {label && (
        <label htmlFor={props.id} className="mb-2 block text-sm font-medium text-foreground">
          {`${label}${required ? ' *' : ''}`}
        </label>
      )}
      <div className="relative">
        <CNSelect {...props}>
          <SelectTrigger className={cn(error || errorMessage ? 'border-destructive' : '', className)} {...props}>
            <SelectValue placeholder={placeholder} />
          </SelectTrigger>
          <SelectContent>
            {options.map((option) => (
              <SelectItem key={option.key} value={option.value}>
                {option.label}
              </SelectItem>
            ))}
          </SelectContent>
        </CNSelect>
      </div>
      {errorMessage && <span className="text-destructive text-sm">{errorMessage}</span>}
    </div>
  );
}
