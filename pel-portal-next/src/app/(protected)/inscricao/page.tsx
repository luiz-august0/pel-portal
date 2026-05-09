"use client";

import { useRouter } from "next/navigation";
import { useQuery } from "@tanstack/react-query";
import { useSession } from "next-auth/react";
import { InscriptionListPage } from "./_components/pages/InscriptionListPage";
import { getInscriptionsGroupedByYear } from "@/core/services/inscription/inscriptionService";

export default function InscriptionPage() {
  const router = useRouter();
  const { data: session } = useSession();

  const { data: inscriptionData, isLoading } = useQuery({
    queryKey: ["inscriptions-grouped-by-year", session?.user?.id],
    queryFn: getInscriptionsGroupedByYear,
    enabled: !!session?.user?.id,
  });

  const handleGoBack = () => {
    router.push("/");
  };

  return (
    <InscriptionListPage
      isLoading={isLoading}
      inscriptionData={inscriptionData}
      handleGoBack={handleGoBack}
      showNewInscriptionButton={true}
    />
  );
}
