"use client";

import { useQuery } from "@tanstack/react-query";
import {
  ResponsiveDialog,
  ResponsiveDialogContent,
  ResponsiveDialogFooter,
  ResponsiveDialogHeader,
  ResponsiveDialogTitle,
} from "@/components/customized/ResponsiveDialog";
import { Button } from "@/components/ui/button";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import { getInscriptionAttendance } from "@/core/services/inscription/inscriptionService";
import { type Attendance } from "@/types/domains/inscription";
import dayjs from "dayjs";
import "dayjs/locale/pt-br";
import { ScrollArea } from "@/components/ui/scroll-area";
import { cn } from "@/helpers/cn";

dayjs.locale("pt-br");

type InscriptionAttendanceModalProps = {
  inscriptionId: number;
  open: boolean;
  onOpenChange: (open: boolean) => void;
};

export const InscriptionAttendanceModal = ({
  inscriptionId,
  open,
  onOpenChange,
}: InscriptionAttendanceModalProps) => {
  const { data: attendances, isLoading } = useQuery({
    queryKey: ["inscription-attendance", inscriptionId],
    queryFn: () => getInscriptionAttendance(inscriptionId),
    enabled: open && !!inscriptionId,
  });

  const getAttendanceStatus = (attendance: Attendance) => {
    if (attendance.isPresent === "ABSENT") {
      return "FALTA";
    }
    if (attendance.isPresent === "PRESENT") {
      return "PRESENTE";
    }
    return "NÃO INFORMADO";
  };

  return (
    <ResponsiveDialog open={open} onOpenChange={onOpenChange}>
      <ResponsiveDialogContent className="max-w-md mx-auto">
        <ResponsiveDialogHeader>
          <ResponsiveDialogTitle>Faltas</ResponsiveDialogTitle>
        </ResponsiveDialogHeader>

        <ScrollArea className={cn("overflow-y-auto max-md:p-4")}>
          <div className="space-y-4">
            {/* Loading state */}
            {isLoading && (
              <div className="flex justify-center py-8">
                <div className="text-gray-500">Carregando faltas...</div>
              </div>
            )}

            {/* Tabela de Faltas */}
            {attendances && attendances.length > 0 && (
              <div className="overflow-auto max-h-[300px]">
                <Table>
                  <TableHeader>
                    <TableRow>
                      <TableHead className="font-medium">Data</TableHead>
                      <TableHead className="font-medium text-center">
                        Horário 1
                      </TableHead>
                      <TableHead className="font-medium text-center">
                        Horário 2
                      </TableHead>
                    </TableRow>
                  </TableHeader>
                  <TableBody>
                    {attendances.map((attendance, idx) => {
                      return (
                        <TableRow key={idx}>
                          <TableCell>
                            {dayjs(attendance.lessonPlan.completedDate).format(
                              "DD/MM/YYYY"
                            )}
                          </TableCell>
                          <TableCell className="text-center">
                            {getAttendanceStatus(attendance)}
                          </TableCell>
                          <TableCell className="text-center">
                            {getAttendanceStatus(attendance)}
                          </TableCell>
                        </TableRow>
                      );
                    })}
                  </TableBody>
                </Table>
              </div>
            )}

            {/* Estado vazio */}
            {attendances && attendances.length === 0 && (
              <div className="flex justify-center py-8">
                <div className="text-gray-500">
                  Nenhum resultado encontrado
                </div>
              </div>
            )}
          </div>
        </ScrollArea>

        <ResponsiveDialogFooter>
          <Button onClick={() => onOpenChange(false)} className="w-full">
            OK
          </Button>
        </ResponsiveDialogFooter>
      </ResponsiveDialogContent>
    </ResponsiveDialog>
  );
};
