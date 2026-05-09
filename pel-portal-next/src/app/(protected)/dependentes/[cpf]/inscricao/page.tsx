"use client";

import { useRouter } from "next/navigation";
import { useQuery } from "@tanstack/react-query";
import { InscriptionListPage } from "@/app/(protected)/inscricao/_components/pages/InscriptionListPage";
import { useParams } from "next/navigation";
import { getInscriptionsGroupedByYearByDependentCpf } from "@/core/services/inscription/inscriptionService";

export default function DependentInscriptionsPage() {
  const router = useRouter();
  const params = useParams();
  const dependentCpf = String(params.cpf);

  const { data: inscriptionData, isLoading } = useQuery({
    queryKey: ["dependent-inscriptions-grouped-by-year", dependentCpf],
    queryFn: () => getInscriptionsGroupedByYearByDependentCpf(dependentCpf),
    enabled: !!params.cpf,
  });

  const handleGoBack = () => {
    router.push("/dependentes");
  };

  return (
    <InscriptionListPage
      isLoading={isLoading}
      inscriptionData={inscriptionData}
      handleGoBack={handleGoBack}
    />
  );
}
