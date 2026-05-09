"use client";

import { useQuery } from "@tanstack/react-query";
import RadioButton from "@/components/customized/RadioButton";
import { getPaymentForms } from "@/core/services/inscription/inscriptionService";
import { formatMoney } from "@/helpers/formatters";

type PaymentFormSelectorProps = {
  selectedPaymentForm?: string;
  onPaymentFormChange: (paymentForm: string) => void;
  showOnlySelected?: boolean;
};

export function PaymentFormSelector({
  selectedPaymentForm,
  onPaymentFormChange,
  showOnlySelected = false,
}: PaymentFormSelectorProps) {
  // Query para buscar formas de pagamento
  const { data: paymentForms, isLoading: isLoadingPaymentForms } = useQuery({
    queryKey: ["payment-forms"],
    queryFn: getPaymentForms,
  });

  if (isLoadingPaymentForms) {
    return (
      <div className="flex flex-col items-center justify-center py-8">
        <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-primary mx-auto"></div>
        <p className="text-gray-600 mt-2">Carregando formas de pagamento...</p>
      </div>
    );
  }

  // Filtrar formas de pagamento baseado no showOnlySelected
  const filteredPaymentForms = showOnlySelected && selectedPaymentForm
    ? paymentForms?.filter(form => form.paymentForm === selectedPaymentForm)
    : paymentForms;

  return (
    <div className="space-y-4">      
      {filteredPaymentForms?.map((paymentForm) => (
        <RadioButton
          key={paymentForm.paymentForm}
          name="paymentForm"
          value={paymentForm.paymentForm}
          checked={selectedPaymentForm === paymentForm.paymentForm}
          onChange={onPaymentFormChange}
        >
          <div className="flex-1">
            <div className="font-medium">
              {`${paymentForm.name}${paymentForm.installments === 1 ? ` - ${formatMoney(paymentForm.total)}` : ""}`}
            </div>
            <div className="text-sm text-gray-600 mt-1 whitespace-pre-line">
              {paymentForm.description}
            </div>
            {paymentForm.installments > 1 && (
              <div className="text-sm text-gray-600 mt-1">
                {paymentForm.installments}x de{" "}
                {formatMoney(paymentForm.installmentsValue)}
              </div>
            )}
          </div>
        </RadioButton>
      ))}
    </div>
  );
}
