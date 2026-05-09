"use client";

import * as React from "react";
import { cn } from "@/helpers/cn";
import { Input } from "@/components/ui/input";

interface DatePickerProps {
  id?: string;
  label?: string;
  placeholder?: string;
  value?: string;
  onChange?: (date: string | undefined) => void;
  error?: boolean;
  errorMessage?: string;
  required?: boolean;
  disabled?: boolean;
  className?: string;
}

export function DatePicker({
  id,
  label,
  placeholder = "dd/mm/aaaa",
  value,
  onChange,
  error = false,
  errorMessage,
  required = false,
  disabled = false,
  className,
}: DatePickerProps) {
  return (
    <div className={cn("space-y-2", className)}>
      {label && (
        <label
          htmlFor={id}
          className="mb-2 block text-sm font-medium text-foreground"
        >
          {`${label}${required ? " *" : ""}`}
        </label>
      )}

      <Input
        id={id}
        type="date"
        placeholder={placeholder}
        value={value}
        onChange={(e) => {
          onChange?.(e.target.value)
        }}
        disabled={disabled}
        className={cn(
          error && "border-red-500 focus:border-red-500 focus:ring-red-500",
          className
        )}
      />

      {error && errorMessage && (
        <p className="text-sm text-red-600 mt-1">{errorMessage}</p>
      )}
    </div>
  );
}
