import type * as React from 'react';
import { X } from 'lucide-react';

import { Button } from '@/components/ui/button';
import { Sheet, SheetContent, SheetFooter, SheetHeader, SheetTitle } from '@/components/ui/sheet';

type FilterDrawerProps = {
  openFilter: boolean;
  setOpenFilter: React.Dispatch<React.SetStateAction<boolean>>;
  disableButtons?: boolean;
  onCleanFilters?: () => void;
  onFilter?: () => void;
  children: React.ReactNode;
};

export default function FilterDrawer({
  openFilter,
  setOpenFilter,
  disableButtons = false,
  onCleanFilters,
  onFilter,
  children,
}: FilterDrawerProps) {
  return (
    <Sheet open={openFilter} onOpenChange={setOpenFilter}>
      <SheetContent side="right" className="w-[320px] sm:w-[400px]">
        <SheetHeader className="flex flex-row items-center justify-between p-6">
          <SheetTitle className="text-2xl">Filtros</SheetTitle>
          <Button variant="ghost" size="icon" onClick={() => setOpenFilter(false)} className="h-8 w-8">
            <X className="h-4 w-4" />
          </Button>
        </SheetHeader>
        <div className="flex flex-col gap-4 p-6">{children}</div>
        {!disableButtons && (
          <SheetFooter className="flex-row justify-end gap-2 p-6">
            <Button variant="outline" onClick={onCleanFilters}>
              Limpar
            </Button>
            <Button onClick={onFilter}>Filtrar</Button>
          </SheetFooter>
        )}
      </SheetContent>
    </Sheet>
  );
}
