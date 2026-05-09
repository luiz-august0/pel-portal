"use client";

import { useParams, useRouter } from "next/navigation";
import { useQuery } from "@tanstack/react-query";
import { useState, useEffect, useRef, useMemo } from "react";
import { ArrowLeft, Download, FileText } from "lucide-react";
import dayjs from "dayjs";
import "dayjs/locale/pt-br";
import relativeTime from "dayjs/plugin/relativeTime";

import { Button } from "@/components/ui/button";
import { Card, CardContent } from "@/components/ui/card";
import { Alert } from "@/app/(protected)/_components/molecules/Alert";
import { Badge } from "@/components/ui/badge";
import { Separator } from "@/components/ui/separator";
import {
  getInscriptionDetails,
  getDuplicateReceivables,
  downloadDuplicateReceivable,
  downloadContract,
  downloadDeclaration,
  downloadCertificate,
} from "@/core/services/inscription/inscriptionService";
import { openFileFromBlob } from "@/helpers/general";
import { formatMoney } from "@/helpers/formatters";
import { toast } from "sonner";
import { HeaderPage } from "@/components/customized/HeaderPage";
import { DuplicateReceivable } from "@/types/domains/inscription";
import { cn } from "@/helpers/cn";
import { InscriptionCourseCard } from "../../_components/molecules/InscriptionCourseCard";

dayjs.locale("pt-br");
dayjs.extend(relativeTime);

export default function InscriptionDetailsPage() {
  const params = useParams();
  const router = useRouter();
  const inscriptionId = Number(params.id);
  const finalizeInscriptionRef = useRef(false);

  const [isDownloading, setIsDownloading] = useState<{
    [key: number]: boolean;
  }>({});
  const [isDownloadingContract, setIsDownloadingContract] = useState(false);
  const [isDownloadingDeclaration, setIsDownloadingDeclaration] =
    useState(false);
  const [isDownloadingCertificate, setIsDownloadingCertificate] =
    useState(false);
  const [shouldRefetch, setShouldRefetch] = useState(true);

  // Query para buscar detalhes da inscrição
  const { data: inscription, isLoading: isLoadingInscription } = useQuery({
    queryKey: ["inscription-details", inscriptionId],
    queryFn: () => getInscriptionDetails(inscriptionId),
    enabled: !!inscriptionId,
  });

  // Query para buscar boletos com interval de 1 segundo
  const { data: duplicateReceivables, isLoading: isLoadingReceivables } =
    useQuery({
      queryKey: ["duplicate-receivables", inscriptionId],
      queryFn: () => getDuplicateReceivables(inscriptionId),
      enabled: !!inscriptionId && !!inscription?.inscriptionDate,
      refetchInterval: shouldRefetch ? 1000 : false,
    });

  // Effect para parar o interval quando tiver boletos
  useEffect(() => {
    if (duplicateReceivables && duplicateReceivables.length > 0) {
      setShouldRefetch(false);
    }
  }, [duplicateReceivables]);

  useEffect(() => {
    if (
      inscription &&
      !inscription.inscriptionFinalized &&
      !finalizeInscriptionRef.current
    ) {
      toast.info(
        "Inscrição não finalizada. Retornando para a página de inscrição"
      );
      router.push(`/inscricao/nova?inscriptionId=${inscription.id}`);
      finalizeInscriptionRef.current = true;
    }
  }, [inscription]);

  const handleDownloadReceivable = async (
    duplicateReceivableId: number,
    installmentNumber: number
  ) => {
    try {
      setIsDownloading((prev) => ({ ...prev, [duplicateReceivableId]: true }));

      const blob = await downloadDuplicateReceivable(
        inscriptionId,
        duplicateReceivableId
      );
      const fileName = `boleto_${installmentNumber}_de_${
        duplicateReceivables?.length || 1
      }.pdf`;

      await openFileFromBlob(blob, fileName);

      toast.success("Boleto baixado com sucesso!");
    } catch (error) {
      toast.error("Não foi possível baixar o boleto. Tente novamente.");
    } finally {
      setIsDownloading((prev) => ({ ...prev, [duplicateReceivableId]: false }));
    }
  };

  const handleDownloadContract = async () => {
    try {
      setIsDownloadingContract(true);

      const blob = await downloadContract(inscriptionId);
      const fileName = `contrato_inscricao_${inscriptionId}.pdf`;

      await openFileFromBlob(blob, fileName);

      toast.success("Contrato baixado com sucesso!");
    } catch (error) {
      toast.error("Não foi possível baixar o contrato. Tente novamente.");
    } finally {
      setIsDownloadingContract(false);
    }
  };

  const handleDownloadDeclaration = async () => {
    try {
      setIsDownloadingDeclaration(true);

      const blob = await downloadDeclaration(inscriptionId);
      const fileName = `declaracao_inscricao_${inscriptionId}.pdf`;

      await openFileFromBlob(blob, fileName);

      toast.success("Declaração baixada com sucesso!");
    } catch (error) {
      toast.error("Não foi possível baixar a declaração. Tente novamente.");
    } finally {
      setIsDownloadingDeclaration(false);
    }
  };

  const handleDownloadCertificate = async () => {
    try {
      setIsDownloadingCertificate(true);

      const blob = await downloadCertificate(inscriptionId);
      const fileName = `certificado_inscricao_${inscriptionId}.pdf`;

      await openFileFromBlob(blob, fileName);

      toast.success("Certificado baixado com sucesso!");
    } catch (error) {
      toast.error("Não foi possível baixar o certificado. Tente novamente.");
    } finally {
      setIsDownloadingCertificate(false);
    }
  };

  const getReceivableStatusBadge = (receivable: DuplicateReceivable) => {
    const statusMap: {
      [key: string]: {
        label: string;
        variant:
          | "default"
          | "secondary"
          | "destructive"
          | "outline"
          | "success";
      };
    } = {
      A: {
        label: getTimeUntilDue(
          receivable.duplicate.dueDate,
          receivable.duplicate.installmentNumber > 1
        ),
        variant: "destructive",
      },
      L: { label: "Pago", variant: "success" },
      C: { label: "Cancelado", variant: "destructive" },
    };
    const statusInfo = statusMap[receivable.duplicate.status] || {
      label: "Em aberto",
      variant: "destructive",
    };
    return (
      <Badge
        className={cn(
          receivable.duplicate.installmentNumber > 1 &&
            receivable.duplicate.status === "A" &&
            "bg-blue-600 text-white"
        )}
        variant={statusInfo.variant}
      >
        {statusInfo.label}
      </Badge>
    );
  };

  const isFirstReceivableUnpaid = useMemo(() => {
    const firstUnpaid = duplicateReceivables?.find(
      (receivable) => receivable.duplicate.status === "A"
    );
    return firstUnpaid?.duplicate.installmentNumber === 1;
  }, [duplicateReceivables]);

  const classAlreadyStarted = useMemo(() => {
    return (
      inscription?.status === "A" &&
      dayjs(inscription?.clazz?.plannedStartDate).isBefore(dayjs())
    );
  }, [inscription]);

  const getTimeUntilDue = (dueDate: string, viewDate: boolean = false) => {
    const due = dayjs(dueDate + "T23:59:59");
    const formattedDate = dayjs(dueDate).format("DD/MM/YYYY");
    const now = dayjs();

    if (due.isBefore(now)) {
      return `Venceu ${viewDate ? `em ${formattedDate}` : due.fromNow()}`;
    }

    if (viewDate) {
      return `Vence em ${formattedDate}`;
    }

    const diffHours = due.diff(now, "hours");
    if (diffHours <= 24) {
      return `Vence em ${diffHours}h!`;
    }

    return `Vence ${due.fromNow()}`;
  };

  if (isLoadingInscription) {
    return (
      <div className="container mx-auto max-w-md p-4">
        <div className="animate-pulse space-y-4">
          <div className="h-8 w-48 bg-gray-200 rounded"></div>
          <div className="h-64 bg-gray-200 rounded"></div>
        </div>
      </div>
    );
  }

  if (!inscription) {
    return (
      <div className="container mx-auto max-w-4xl p-4">
        <div className="text-center">
          <h1 className="text-2xl font-bold text-gray-900">
            Inscrição não encontrada
          </h1>
          <Button onClick={() => router.push("/")} className="mt-4">
            Voltar
          </Button>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gray-50 p-4">
      <div className="flex flex-col gap-4 mx-auto max-w-md">
        {/* Header */}
        <HeaderPage title="Detalhes do curso" onBack={() => router.push("/")} />

        {/* Alert de aviso para inscrições pendentes */}
        {inscription.status === "P" && isFirstReceivableUnpaid && (
          <Alert
            variant="warning"
            title="Atenção!"
            description="Dentro de 24h, realize o pagamento do boleto para garantir sua inscrição."
          />
        )}

        {/* Card do Curso */}
        <InscriptionCourseCard inscription={inscription} />

        {/* Botões de Declaração e Certificado */}
        <div className="flex justify-center gap-4">
          <Button
            variant="outline"
            disabled={
              isFirstReceivableUnpaid ||
              isDownloadingDeclaration ||
              !classAlreadyStarted
            }
            onClick={handleDownloadDeclaration}
            className="flex items-center gap-2"
          >
            <FileText className="h-4 w-4" />
            {isDownloadingDeclaration ? "Baixando..." : "Declaração"}
          </Button>
          <Button
            variant="outline"
            disabled={
              isFirstReceivableUnpaid ||
              isDownloadingCertificate ||
              !inscription.fileCertificateName
            }
            onClick={handleDownloadCertificate}
            className="flex items-center gap-2"
          >
            <FileText className="h-4 w-4" />
            {isDownloadingCertificate ? "Baixando..." : "Certificado"}
          </Button>
        </div>

        {/* Seção Financeira */}
        {!!inscription?.inscriptionDate && (
          <div className="space-y-4">
            <h2 className="text-sm font-semibold">Financeiro</h2>

            {isLoadingReceivables ||
            !duplicateReceivables ||
            duplicateReceivables.length <= 0 ? (
              <div className="text-center py-8">
                <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-primary mx-auto"></div>
                <p className="text-gray-500 mt-2">Carregando boletos...</p>
              </div>
            ) : (
              <div className="space-y-4">
                {duplicateReceivables.map((receivable, idx) => (
                  <Card key={receivable.id} className="bg-gray-50 py-3">
                    <CardContent className="px-3">
                      <div className="flex items-center justify-between">
                        <div className="space-y-1">
                          <div className="flex items-center gap-2">
                            <span className="text-gray-600 text-xs">
                              Boleto {receivable.duplicate.installmentNumber} de{" "}
                              {receivable.duplicate.totalInstallments}
                            </span>
                          </div>
                          <div className="text-sm font-semibold">
                            {formatMoney(receivable.duplicate.duplicateAmount)}
                          </div>
                          {getReceivableStatusBadge(receivable)}
                        </div>
                        <Button
                          variant="outline"
                          size="sm"
                          onClick={() =>
                            handleDownloadReceivable(
                              receivable.id,
                              receivable.duplicate.installmentNumber
                            )
                          }
                          disabled={isDownloading[receivable.id]}
                          className="flex items-center gap-2"
                        >
                          <Download className="h-4 w-4" />
                          {isDownloading[receivable.id]
                            ? "Baixando..."
                            : "Download"}
                        </Button>
                      </div>
                    </CardContent>
                  </Card>
                ))}
              </div>
            )}
          </div>
        )}

        <Separator />

        {/* Links úteis */}
        <div className="space-y-4">
          <h2 className="text-sm font-medium">Links úteis</h2>

          <div className="flex flex-col gap-2">
            <Button
              variant="link"
              onClick={handleDownloadContract}
              disabled={isDownloadingContract}
              className="h-auto p-0 text-blue-600 justify-start"
            >
              {isDownloadingContract
                ? "Baixando contrato..."
                : "Download do contrato"}
            </Button>

            <Button
              variant="link"
              disabled={isFirstReceivableUnpaid || !classAlreadyStarted}
              className="h-auto p-0 text-blue-400 justify-start"
              onClick={() =>
                router.push(
                  `/transferencia/nova?inscriptionId=${inscription.id}`
                )
              }
            >
              Solicitar transferência de turma
            </Button>
          </div>
        </div>
      </div>
    </div>
  );
}
