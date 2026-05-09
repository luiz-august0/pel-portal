"use client";

import { Label } from "@/components/ui/label";
import { forwardRef } from "react";

interface RadioButtonProps {
  id?: string;
  name?: string;
  value?: string;
  checked?: boolean;
  onChange?: (value: any) => void;
  children: React.ReactNode;
  className?: string;
  disabled?: boolean;
}

const RadioButton = forwardRef<HTMLInputElement, RadioButtonProps>(
  (
    {
      id,
      name,
      value,
      checked,
      onChange,
      children,
      className = "",
      disabled = false,
      ...props
    },
    ref
  ) => {
    const handleChange = () => {
      if (!disabled && onChange) {
        onChange(value);
      }
    };

    return (
      <Label
        className={`flex items-center p-4 border border-gray-300 rounded-lg cursor-pointer hover:bg-gray-50 ${
          disabled ? "opacity-50 cursor-not-allowed" : ""
        } ${className}`}
      >
        <input
          ref={ref}
          type="radio"
          id={id}
          name={name}
          value={value}
          checked={checked}
          onChange={handleChange}
          className="w-5 h-5 text-primary focus:ring-primary"
          disabled={disabled}
          {...props}
        />
        <span className={`ml-3 ${disabled ? "text-gray-400" : "text-gray-700"}`}>
          {children}
        </span>
      </Label>
    );
  }
);

RadioButton.displayName = "RadioButton";

export default RadioButton;
