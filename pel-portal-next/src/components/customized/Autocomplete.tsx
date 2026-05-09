import { Check, ChevronsUpDown, Loader2 } from 'lucide-react';
import type * as React from 'react';
import { useEffect, useRef, useState } from 'react';

import { Button } from '@/components/ui/button';
import { Command, CommandEmpty, CommandGroup, CommandInput, CommandItem, CommandList } from '@/components/ui/command';
import { Popover, PopoverContent, PopoverTrigger } from '@/components/ui/popover';
import { cn } from '@/helpers/cn';

type AutocompleteProps = {
  options: any[];
  value?: any;
  onChange: (value: any) => void;
  getMore?: () => void;
  loading?: boolean;
  label: string;
  search?: (value: string) => void;
  required?: boolean;
  error?: boolean;
  errorMessage?: string;
  placeholder?: string;
  disabled?: boolean;
  multiple?: boolean;
  optionEqualToValue?: (option: any, value: any) => boolean;
  getOptionLabel?: (option: any) => string;
  getOptionKey?: (option: any) => number;
  isEmpty?: (value: any) => boolean;
};

export default function Autocomplete({
  options,
  value,
  onChange,
  getMore,
  loading = false,
  label,
  search,
  required = false,
  error = false,
  errorMessage,
  placeholder = 'Selecione uma opção',
  disabled = false,
  multiple = false,
  getOptionLabel = (option: any) => option?.name,
  getOptionKey = (option: any) => option?.id,
  optionEqualToValue = (option: any, value: any) => value?.id == option?.id,
  isEmpty = (value: any) => !value?.id,
}: AutocompleteProps) {
  const [open, setOpen] = useState(false);
  const [searchValue, setSearchValue] = useState<string>('');
  const listRef = useRef<HTMLDivElement>(null);
  const buttonRef = useRef<HTMLButtonElement>(null);

  const handleScroll = (e: React.UIEvent<HTMLDivElement>) => {
    const target = e.target as HTMLDivElement;
    if (target.scrollHeight - target.scrollTop === target.clientHeight && getMore) {
      getMore();
    }
  };

  useEffect(() => {
    search?.(searchValue);
  }, [searchValue, search]);

  return (
    <div className="relative">
      {label && (
        <label className="mb-2 block text-sm font-medium text-foreground">{`${label}${required ? ' *' : ''}`}</label>
      )}
      <div className="relative">
        <Popover open={open} onOpenChange={setOpen} modal>
          <PopoverTrigger asChild>
            <Button
              ref={buttonRef}
              variant="outline"
              role="combobox"
              aria-expanded={open}
              className={cn(
                'w-full justify-between font-normal hover:bg-inherit',
                (error || errorMessage) && 'border-destructive',
                isEmpty(value) && 'text-muted-foreground',
              )}
              disabled={disabled}
            >
              {!isEmpty(value) ? getOptionLabel(value) : placeholder}
              {loading ? (
                <Loader2 className="ml-2 h-4 w-4 shrink-0 animate-spin" />
              ) : (
                <ChevronsUpDown className="ml-2 h-4 w-4 shrink-0 opacity-50" />
              )}
            </Button>
          </PopoverTrigger>
          <PopoverContent
            className={cn('p-0', !buttonRef?.current?.clientWidth && 'w-full')}
            style={buttonRef?.current?.clientWidth ? { width: `${buttonRef.current.clientWidth}px` } : undefined}
          >
            <Command shouldFilter={false}>
              {search && (
                <CommandInput
                  placeholder="Pesquisar..."
                  value={searchValue}
                  onValueChange={(value) => {
                    setSearchValue(value);
                  }}
                />
              )}
              <CommandList ref={listRef} onScroll={handleScroll} className="max-h-[300px] overflow-auto">
                {loading && options.length === 0 ? (
                  <div className="flex items-center justify-center py-6">
                    <Loader2 className="h-6 w-6 animate-spin text-primary" />
                  </div>
                ) : (
                  <>
                    <CommandEmpty>Nenhum resultado encontrado</CommandEmpty>
                    <CommandGroup>
                      {options.map((option) => (
                        <CommandItem
                          key={getOptionKey(option)}
                          value={option}
                          onSelect={() => {
                            onChange(optionEqualToValue(option, value) && !multiple ? undefined : option);
                            if (!multiple) {
                              setOpen(false);
                            }
                          }}
                          className="cursor-pointer justify-between items-center w-full"
                        >
                          {getOptionLabel(option)}
                          <Check
                            className={cn(
                              'mr-2 h-4 w-4',
                              optionEqualToValue(option, value) ? 'opacity-100' : 'opacity-0',
                            )}
                          />
                        </CommandItem>
                      ))}
                      {loading && options.length > 0 && (
                        <div className="flex items-center justify-center py-2">
                          <Loader2 className="h-4 w-4 animate-spin text-primary" />
                        </div>
                      )}
                    </CommandGroup>
                  </>
                )}
              </CommandList>
            </Command>
          </PopoverContent>
        </Popover>
      </div>
      {errorMessage && <span className="text-destructive text-sm">{errorMessage}</span>}
    </div>
  );
}
