import { ArrowLeft } from "lucide-react";

interface BackButtonProps {
  onClick: () => void;
  className?: string;
  size?: "sm" | "md" | "lg";
  variant?: "default" | "ghost";
}

export function BackButton({ 
  onClick, 
  className = "", 
  size = "md",
  variant = "default" 
}: BackButtonProps) {
  const sizeClasses = {
    sm: "p-1",
    md: "p-2", 
    lg: "p-3"
  };

  const iconSizes = {
    sm: "w-4 h-4",
    md: "w-5 h-5",
    lg: "w-6 h-6"
  };

  const variantClasses = {
    default: "hover:bg-gray-100 rounded-full cursor-pointer",
    ghost: "hover:bg-transparent cursor-pointer"
  };

  return (
    <button
      onClick={onClick}
      className={`${sizeClasses[size]} ${variantClasses[variant]} ${className}`}
    >
      <ArrowLeft className={`${iconSizes[size]} text-gray-600`} />
    </button>
  );
}
