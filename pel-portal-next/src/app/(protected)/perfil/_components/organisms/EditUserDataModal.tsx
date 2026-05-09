"use client";

import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { UserDataType, userDataSchema } from "@/types/domains/user";
import { updateUser } from "@/core/services/user/userService";
import { Button } from "@/components/ui/button";
import {
  Dialog,
  DialogClose,
  DialogContent,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog";
import Input from "@/components/customized/Input";
import { DatePicker } from "@/components/customized/DatePicker";
import { applyCpfMask, applyPhoneMask } from "@/helpers/masks";
import { XIcon } from "lucide-react";
import { signOut, useSession } from "next-auth/react";
import { toast } from "sonner";
import dayjs from "dayjs";
import { useMutation } from "@tanstack/react-query";
import { ScrollArea } from "@/components/ui/scroll-area";
import { useRouter } from "next/navigation";

interface EditUserDataModalProps {
  open: boolean;
  onOpenChange: (open: boolean) => void;
}

export function EditUserDataModal({
  open,
  onOpenChange,
}: EditUserDataModalProps) {
  const { data: session, update } = useSession();
  const router = useRouter();
  const {
    register,
    handleSubmit,
    formState: { errors, isValid },
    setValue,
    watch,
    trigger,
    reset,
  } = useForm<UserDataType>({
    resolver: zodResolver(userDataSchema),
    defaultValues: {
      name: session?.user?.name || "",
      email: session?.user?.email || "",
      cpf: applyCpfMask(session?.user?.cpf || ""),
      phone: session?.user?.userDetails?.phone
        ? applyPhoneMask(session?.user?.userDetails?.phone)
        : "",
      birthDate: session?.user?.userDetails?.birthDate
        ? dayjs(session?.user?.userDetails?.birthDate).format("YYYY-MM-DD")
        : undefined,
    },
    mode: "onChange",
  });

  // Funções para aplicar máscaras
  const handleCpfChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const maskedValue = applyCpfMask(e.target.value);
    setValue("cpf", maskedValue);
    trigger("cpf");
  };

  const handlePhoneChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const maskedValue = applyPhoneMask(e.target.value);
    setValue("phone", maskedValue);
    trigger("phone");
  };

  const updateMutation = useMutation({
    mutationFn: async (data: UserDataType) => {
      const cleanData = {
        ...data,
        phone: data.phone ? data.phone.replace(/\D/g, "") : "",
        cpf: data.cpf.replace(/\D/g, ""),
        birthDate: data.birthDate
          ? dayjs(data.birthDate).format("YYYY-MM-DD")
          : "",
      };
      await updateUser(cleanData);
      // Atualiza a sessão do usuário com os novos dados
      if (cleanData.cpf !== session?.user?.cpf) {
        toast.success(
          "Dados atualizados com sucesso! Deve ser feito o login novamente após alterar o CPF."
        );
        localStorage.setItem("cpf", cleanData.cpf);
        await signOut({ redirect: false });
        router.replace("/onboarding");
        return;
      }

      await update({
        ...session,
        user: {
          ...session?.user,
          name: data.name,
          email: data.email,
          cpf: cleanData.cpf,
          userDetails: {
            ...session?.user?.userDetails,
            phone: cleanData.phone,
            birthDate: cleanData.birthDate,
          },
          reviewed:
            session.user?.cpf == cleanData.cpf &&
            session.user.name == cleanData.name &&
            dayjs(session.user.userDetails.birthDate).format("YYYY-MM-DD") ==
              dayjs(cleanData.birthDate).format("YYYY-MM-DD") &&
            session.user.reviewed,
        },
      });
      toast.success("Dados atualizados com sucesso!");
      onOpenChange(false);
    },
    onError: () => {
      toast.error("Erro ao atualizar dados. Tente novamente.");
    },
  });

  // Reset form quando o modal abrir com dados atualizados da sessão
  const handleOpenChange = (newOpen: boolean) => {
    if (newOpen) {
      reset({
        name: session?.user?.name || "",
        email: session?.user?.email || "",
        cpf: session?.user?.cpf || "",
        phone: session?.user?.userDetails?.phone || "",
        birthDate: session?.user?.userDetails?.birthDate
          ? dayjs(session?.user?.userDetails?.birthDate).format("YYYY-MM-DD")
          : undefined,
      });
    }
    onOpenChange(newOpen);
  };

  return (
    <Dialog open={open} onOpenChange={handleOpenChange}>
      <DialogContent className="max-sm:min-h-[80vh] max-h-[80vh] max-sm:min-w-[100vw] max-sm:px-0">
        <DialogHeader className="text-center">
          <DialogClose className="absolute right-4 top-4 cursor-pointer">
            <XIcon className="w-4 h-4" />
          </DialogClose>
          <DialogTitle className="text-lg font-semibold">
            Meus dados
          </DialogTitle>
        </DialogHeader>

        <div className="max-h-[calc(80vh-10rem)] overflow-auto">
          <form
            onSubmit={handleSubmit((data) => updateMutation.mutate(data))}
            className="max-sm:px-6 space-y-4"
          >
            <Input
              id="cpf"
              label="CPF"
              placeholder="000.000.000-00"
              value={watch("cpf") || ""}
              onChange={handleCpfChange}
              error={!!errors.cpf}
              errorMessage={errors.cpf?.message}
              required
            />

            <Input
              id="name"
              label="Nome"
              type="text"
              placeholder="Digite seu nome"
              error={!!errors.name}
              errorMessage={errors.name?.message}
              required
              {...register("name")}
            />

            <Input
              id="email"
              label="E-mail"
              type="email"
              placeholder="Ex: email@gmail.com"
              error={!!errors.email}
              errorMessage={errors.email?.message}
              required
              {...register("email")}
            />

            <Input
              id="phone"
              label="Celular"
              placeholder="(00) 00000-0000"
              value={watch("phone") || ""}
              onChange={handlePhoneChange}
              error={!!errors.phone}
              errorMessage={errors.phone?.message}
              required
            />

            <DatePicker
              id="birthDate"
              label="Data de nascimento"
              placeholder="Selecione sua data de nascimento"
              value={watch("birthDate")}
              onChange={(date) => {
                setValue("birthDate", date || "");
                trigger("birthDate");
              }}
              error={!!errors.birthDate}
              errorMessage={errors.birthDate?.message}
              required
            />

            <div className="flex gap-3 pt-4">
              <Button
                type="submit"
                className="flex-1"
                disabled={!isValid || updateMutation.isPending}
              >
                {updateMutation.isPending ? "Salvando..." : "Salvar"}
              </Button>
            </div>
          </form>
        </div>
      </DialogContent>
    </Dialog>
  );
}
