"use client";

import { Button } from "@/components/ui/button";
import {
  ResponsiveDialog,
  ResponsiveDialogContent,
  ResponsiveDialogHeader,
  ResponsiveDialogTitle,
  ResponsiveDialogDescription,
  ResponsiveDialogFooter,
} from "./ResponsiveDialog";

type ConfirmationModalProps = {
  open: boolean;
  onOpenChange: (open: boolean) => void;
  title: string;
  description: string;
  confirmText?: string;
  cancelText?: string;
  onConfirm: () => void;
  onCancel?: () => void;
  isLoading?: boolean;
  variant?: "default" | "destructive";
};

export function ConfirmationModal({
  open,
  onOpenChange,
  title,
  description,
  confirmText = "Confirmar",
  cancelText = "Cancelar",
  onConfirm,
  onCancel,
  isLoading = false,
  variant = "default",
}: ConfirmationModalProps) {
  const handleCancel = () => {
    if (onCancel) {
      onCancel();
    } else {
      onOpenChange(false);
    }
  };

  const handleConfirm = () => {
    onConfirm();
  };

  return (
    <ResponsiveDialog open={open} onOpenChange={onOpenChange}>
      <ResponsiveDialogContent className="sm:max-w-[425px]">
        <ResponsiveDialogHeader>
          <ResponsiveDialogTitle>{title}</ResponsiveDialogTitle>
          <ResponsiveDialogDescription>{description}</ResponsiveDialogDescription>
        </ResponsiveDialogHeader>

        <ResponsiveDialogFooter className="gap-2 sm:gap-0">
          <Button
            variant="outline"
            onClick={handleCancel}
            disabled={isLoading}
            className="cursor-pointer"
          >
            {cancelText}
          </Button>
          <Button
            variant={variant}
            onClick={handleConfirm}
            disabled={isLoading}
            className="cursor-pointer"
          >
            {isLoading ? "Processando..." : confirmText}
          </Button>
        </ResponsiveDialogFooter>
      </ResponsiveDialogContent>
    </ResponsiveDialog>
  );
}
