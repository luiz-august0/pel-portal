import { extractFileData } from '@/helpers/general';
import { useRef, useState, type SyntheticEvent } from 'react';
import { Edit, Pencil } from 'lucide-react';

import { Avatar, AvatarFallback, AvatarImage } from '@/components/ui/avatar';
import { Button } from '@/components/ui/button';
import {
  DropdownMenu,
  DropdownMenuContent,  
  DropdownMenuItem,
  DropdownMenuTrigger,
} from '@/components/ui/dropdown-menu';
import { MultipartBean } from '@/types/domains/document';

type Props = {
  imageUrl?: string;
  onChange: (multipart: MultipartBean) => void;
  onRemove: () => void;
  width?: number;
  height?: number;
};

export default function AvatarEditor({ imageUrl, onChange, onRemove, width = 150, height = 150 }: Props) {
  const [open, setOpen] = useState(false);
  const fileInputRef = useRef<HTMLInputElement>(null);

  const handleChange = (event: SyntheticEvent) => {
    const input = event.target as HTMLInputElement;
    const file = input.files?.[0];

    if (file) {
      extractFileData(file, (result) => onChange({
        file: result,
        filename: file.name,
      }));
    }
  };

  const handleClick = () => {
    if (imageUrl) {
      setOpen(true);
    } else {
      fileInputRef.current?.click();
    }
  };

  return (
    <>
      <input
        id="avatar-input-file"
        type="file"
        accept="image/*"
        hidden
        ref={fileInputRef}
        onChange={(e) => {
          handleChange(e);
          setOpen(false);
        }}
      />
      <div className="relative inline-block">
        <Button
          variant="ghost"
          className="relative flex h-auto w-auto items-center justify-center p-0"
          onClick={handleClick}
        >
          <Avatar className="transition-opacity duration-300 group-hover:opacity-50" style={{ height, width }}>
            <AvatarImage src={imageUrl || '/placeholder.svg'} alt="Avatar" />
            <AvatarFallback>{imageUrl ? '' : <Pencil className="h-6 w-6 text-muted-foreground" />}</AvatarFallback>
          </Avatar>
          <div
            className="absolute flex h-full w-full items-center justify-center rounded-full bg-black/50 opacity-0 transition-opacity duration-300 hover:opacity-100"
            style={{ height, width }}
          >
            <Edit className="h-6 w-6 text-white" />
          </div>
        </Button>
      </div>
      <DropdownMenu open={open} onOpenChange={setOpen}>
        <DropdownMenuTrigger asChild>
          <span className="hidden">Open</span>
        </DropdownMenuTrigger>
        <DropdownMenuContent>
          <DropdownMenuItem onSelect={() => fileInputRef.current?.click()}>Alterar</DropdownMenuItem>
          <DropdownMenuItem
            onSelect={() => {
              onRemove();
              setOpen(false);
            }}
          >
            Remover
          </DropdownMenuItem>
        </DropdownMenuContent>
      </DropdownMenu>
    </>
  );
}
