"use client";

import { XIcon } from "lucide-react";
import { Button } from "@/components/ui/button";
import {
  ResponsiveDialog,
  ResponsiveDialogClose,
  ResponsiveDialogContent,
  ResponsiveDialogHeader,
  ResponsiveDialogTitle,
} from "@/components/customized/ResponsiveDialog";
import PasswordInput from "@/components/customized/PasswordInput";
import { toast } from "sonner";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { useMutation } from "@tanstack/react-query";
import { PasswordChangeType, passwordChangeSchema } from "@/types/domains/user";
import { ScrollArea } from "@radix-ui/react-scroll-area";
import { cn } from "@/helpers/cn";
import { changePassword } from "@/core/services/user/userService";

interface ChangePasswordModalProps {
  open: boolean;
  onOpenChange: (open: boolean) => void;
}

export function ChangePasswordModal({
  open,
  onOpenChange,
}: ChangePasswordModalProps) {
  const {
    register,
    handleSubmit,
    formState: { errors, isValid },
    watch,
    reset,
  } = useForm<PasswordChangeType>({
    resolver: zodResolver(passwordChangeSchema),
    defaultValues: {
      password: "",
      confirmPassword: "",
    },
    mode: "onChange",
  });

  const changePasswordMutation = useMutation({
    mutationFn: changePassword,
    onSuccess: () => {
      toast.success("Senha alterada com sucesso!");
      reset();
      onOpenChange(false);
    },
    onError: (error: Error) => {
      toast.error(error.message || "Erro ao alterar senha");
    },
  });

  const onSubmit = (data: PasswordChangeType) => {
    changePasswordMutation.mutate({
      password: data.password,
    });
  };

  const handleClose = () => {
    reset();
    onOpenChange(false);
  };

  const password = watch("password");
  const confirmPassword = watch("confirmPassword");

  return (
    <ResponsiveDialog open={open} onOpenChange={handleClose}>
      <ResponsiveDialogContent className="sm:max-w-md">
        <ResponsiveDialogHeader className="text-center">
          <ResponsiveDialogClose className="absolute right-4 top-4 cursor-pointer">
            <XIcon className="w-4 h-4" />
          </ResponsiveDialogClose>
          <ResponsiveDialogTitle className="text-lg font-semibold">
            Alterar Senha
          </ResponsiveDialogTitle>
          <p className="text-sm text-gray-600">
            Digite sua nova senha nos campos abaixo.
          </p>
        </ResponsiveDialogHeader>

        <ScrollArea className={cn("overflow-y-auto max-md:p-4")}>
          <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
            {/* Nova Senha */}
            <PasswordInput
              id="password"
              label="Nova senha"
              showValidation={true}
              value={password}
              {...register("password")}
            />

            {/* Confirmação de senha */}
            <PasswordInput
              id="confirmPassword"
              label="Confirmação de senha"
              value={confirmPassword}
              error={
                !!errors.confirmPassword ||
                (!!confirmPassword && password !== confirmPassword)
              }
              errorMessage={
                errors.confirmPassword?.message ||
                (confirmPassword &&
                password !== confirmPassword &&
                !errors.confirmPassword
                  ? "As senhas não coincidem"
                  : undefined)
              }
              {...register("confirmPassword")}
            />

            {/* Botões */}
            <div className="flex pt-2">
              <Button
                type="submit"
                disabled={changePasswordMutation.isPending || !isValid}
                className="flex-1"
              >
                {changePasswordMutation.isPending
                  ? "Alterando..."
                  : "Alterar senha"}
              </Button>
            </div>
          </form>
        </ScrollArea>
      </ResponsiveDialogContent>
    </ResponsiveDialog>
  );
}
