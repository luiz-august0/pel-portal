"use client";

import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { AddressType, addressSchema } from "@/types/domains/address";
import { updateUserAddress } from "@/core/services/address/updateUserAddressService";
import { Button } from "@/components/ui/button";
import {
  Dialog,
  DialogClose,
  DialogContent,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog";
import Input from "@/components/customized/Input";
import { Loader2Icon, XIcon } from "lucide-react";
import { useSession } from "next-auth/react";
import { toast } from "sonner";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { applyCepMask } from "@/helpers/masks";
import { utilsBr } from "js-brasil";
import Select from "@/components/customized/Select";
import { useEffect } from "react";
import { getCEP } from "@/core/services/cep/getCepService";
import { ScrollArea } from "@/components/ui/scroll-area";

interface EditUserAddressModalProps {
  open: boolean;
  onOpenChange: (open: boolean) => void;
}

export function EditUserAddressModal({
  open,
  onOpenChange,
}: EditUserAddressModalProps) {
  const { data: session, update } = useSession();
  const queryClient = useQueryClient();

  const {
    register,
    handleSubmit,
    formState: { errors, isValid },
    setValue,
    watch,
    trigger,
    reset,
    clearErrors,
  } = useForm<AddressType>({
    resolver: zodResolver(addressSchema),
    mode: "onChange",
  });

  const { data: cepData, isLoading: isLoadingCep } = useQuery({
    queryKey: ["cep", watch("cep")],
    queryFn: async () => await getCEP(watch("cep").replace(/\D/g, "")),
    enabled: !!watch("cep") && watch("cep").replace(/\D/g, "").length === 8,
  });

  useEffect(() => {
    if (open) {
      reset({
        cep: applyCepMask(session?.user?.address?.cep),
        street: session?.user?.address?.street || "",
        number: session?.user?.address?.number || "",
        complement: session?.user?.address?.complement || "",
        neighborhood: session?.user?.address?.neighborhood || "",
        city: session?.user?.address?.city || "",
        state: session?.user?.address?.state || "",
      });
    }
  }, [open]);

  useEffect(() => {
    if (cepData) {
      cepData?.logradouro && setValue("street", cepData?.logradouro);
      cepData?.bairro && setValue("neighborhood", cepData?.bairro);
      cepData?.localidade && setValue("city", cepData?.localidade);
      cepData?.uf && setValue("state", cepData?.uf);
      clearErrors();
    }
  }, [cepData]);

  const handleCepChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const maskedValue = applyCepMask(e.target.value);
    setValue("cep", maskedValue, { shouldValidate: true });
  };

  const updateMutation = useMutation({
    mutationFn: async (data: AddressType) => {
      const formatedData = {
        ...data,
        cep: data.cep.replace(/\D/g, ""),
        state: data.state.toUpperCase(),
      };
      const addressId = await updateUserAddress(formatedData);

      // Atualiza a sessão do usuário com os novos dados de endereço
      await update({
        ...session,
        user: {
          ...session?.user,
          address: {
            id: addressId,
            cep: formatedData.cep,
            street: data.street,
            number: data.number,
            complement: data.complement,
            neighborhood: data.neighborhood,
            city: data.city,
            state: formatedData.state,
          },
        },
      });
      await queryClient.invalidateQueries({
        queryKey: ["user-status", session?.user?.id],
      });
      toast.success("Endereço atualizado com sucesso!");
      onOpenChange(false);
    },
    onError: () => {
      toast.error("Erro ao atualizar endereço. Tente novamente.");
    },
  });

  const handleOpenChange = (newOpen: boolean) => {
    onOpenChange(newOpen);
  };

  return (
    <Dialog open={open} onOpenChange={handleOpenChange}>
      <DialogContent className="max-sm:min-h-[80vh] max-h-[80vh] max-sm:min-w-[100vw] max-sm:px-0">
        <DialogHeader className="text-center">
          <DialogClose className="absolute right-4 top-4 cursor-pointer">
            <XIcon className="w-4 h-4" />
          </DialogClose>
          <DialogTitle className="text-lg font-semibold">Endereço</DialogTitle>
        </DialogHeader>

        <div className="max-h-[calc(80vh-10rem)] overflow-auto">
          <form
            onSubmit={handleSubmit((data) => updateMutation.mutate(data))}
            className="max-sm:px-6 space-y-4"
          >
            <Input
              id="cep"
              label="CEP"
              placeholder="00000-000"
              error={!!errors.cep}
              errorMessage={errors.cep?.message}
              right={
                isLoadingCep ? (
                  <div className="absolute right-0 top-0 h-full px-3 py-2 hover:bg-transparent">
                    <Loader2Icon className="h-4 w-4 animate-spin" />
                  </div>
                ) : null
              }
              required
              {...register("cep", {
                onChange: handleCepChange,
              })}
            />

            <Input
              id="street"
              label="Rua"
              type="text"
              placeholder="Rua"
              error={!!errors.street}
              errorMessage={errors.street?.message}
              required
              {...register("street")}
            />

            <Input
              id="number"
              label="Número"
              type="text"
              placeholder="Número"
              error={!!errors.number}
              errorMessage={errors.number?.message}
              required
              {...register("number")}
            />

            <Input
              id="complement"
              label="Complemento"
              type="text"
              placeholder="Complemento"
              error={!!errors.complement}
              errorMessage={errors.complement?.message}
              {...register("complement")}
            />

            <Input
              id="neighborhood"
              label="Bairro"
              type="text"
              placeholder="Bairro"
              error={!!errors.neighborhood}
              errorMessage={errors.neighborhood?.message}
              required
              {...register("neighborhood")}
            />

            <Input
              id="city"
              label="Cidade"
              type="text"
              placeholder="Cidade"
              error={!!errors.city}
              errorMessage={errors.city?.message}
              required
              {...register("city")}
            />

            <Select
              id="state"
              label="Estado"
              required
              className="w-full"
              options={utilsBr?.ESTADOS.map((state: any) => ({
                key: state?.shortname,
                label: state?.name,
                value: state?.shortname,
              }))}
              value={watch("state")}
              onValueChange={(value) => {
                setValue("state", value);
                clearErrors("state");
              }}
              errorMessage={errors.state?.message}
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
