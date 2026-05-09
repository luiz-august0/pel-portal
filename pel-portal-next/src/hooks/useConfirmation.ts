import { ConfirmationProps, LayoutContext } from '@/providers/LayoutProvider';
import { useContext } from 'react';

export const useConfirmation = (
  confirmation: Omit<ConfirmationProps, 'open' | 'loading'>,
  confirmFunction: (value?: any) => Promise<void>,
) => {
  const { setConfirmationProps } = useContext(LayoutContext);

  const confirm = (value?: any) => {
    setConfirmationProps({
      ...confirmation,
      confirmButtonProps: {
        onClick: async () => {
          setConfirmationProps((old) => ({ ...old, loading: true }));

          await confirmFunction(value);

          setConfirmationProps({} as ConfirmationProps);
        },
        ...confirmation?.confirmButtonProps,
      },
      loading: false,
      open: true,
    });
  };

  return { confirm };
};
