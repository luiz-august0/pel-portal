'use client';

import StandardForm, { FormButton } from "@/components/customized/StandardForm";
import { buttonVariants } from '@/components/ui/button';
import { VariantProps } from 'class-variance-authority';
import { createContext, Dispatch, SetStateAction, useState } from 'react';

export type ConfirmationProps = {
  title: string;
  subtitle?: string;
  cancelTitle?: string;
  confirmTitle?: string;
  cancelButtonProps?: React.ComponentProps<'button'> &
    VariantProps<typeof buttonVariants> & {
      asChild?: boolean;
    };
  confirmButtonProps?: React.ComponentProps<'button'> &
    VariantProps<typeof buttonVariants> & {
      asChild?: boolean;
    };
  open: boolean;
  loading: boolean;
};

type Props = {
  confirmationProps: ConfirmationProps;
  setConfirmationProps: Dispatch<SetStateAction<ConfirmationProps>>;
};

export const LayoutContext = createContext<Props>({} as Props);

export const LayoutProvider = ({ children }: { children: React.ReactNode }) => {
  const [confirmationProps, setConfirmationProps] = useState<ConfirmationProps>({} as ConfirmationProps);

  const handleCloseConfirmation = () => {
    setConfirmationProps({} as ConfirmationProps);
  };

  const confirmationButtons: FormButton[] = [
    {
      id: 'cancel',
      title: confirmationProps?.cancelTitle ?? 'Cancelar',
      onClick: handleCloseConfirmation,
      variant: 'outline',
      ...confirmationProps?.cancelButtonProps,
    },
    {
      id: 'submit',
      title: confirmationProps?.confirmTitle ?? 'Confirmar',
      disabled: !!confirmationProps?.loading,
      ...confirmationProps?.confirmButtonProps,
    },
  ];

  return (
    <LayoutContext.Provider
      value={{
        confirmationProps,
        setConfirmationProps,
      }}
    >
      {children}
      {confirmationProps?.open && (
        <StandardForm
          formButtons={confirmationButtons}
          formTitle={confirmationProps?.title ?? ''}
          handleClose={handleCloseConfirmation}
          open={confirmationProps?.open}
        >
          {confirmationProps?.subtitle && <p className='text-muted-foreground'>{confirmationProps?.subtitle}</p>}
        </StandardForm>
      )}
    </LayoutContext.Provider>
  );
};
