import { Badge } from '@/components/ui/badge';
import { Button } from '@/components/ui/button';
import { Card, CardContent } from '@/components/ui/card';
import { ExternalLink, ImageIcon, X } from 'lucide-react';
import { useState } from 'react';

interface ImageModalProps {
  src: string;
  alt: string;
  isOpen: boolean;
  onClose: () => void;
}

interface ImageGalleryProps {
  images: string[];
  title: string;
}

const ImageModal: React.FC<ImageModalProps> = ({ src, alt, isOpen, onClose }) => {
  if (!isOpen) return null;

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center overflow-auto bg-black/80" onClick={onClose}>
      <div className="relative max-w-[90vw] max-h-[90vh]" onClick={(e) => e.stopPropagation()}>
        <Button
          variant="outline"
          size="icon"
          className="absolute -top-12 right-0 bg-white/10 border-white/20 text-white hover:bg-white/20"
          onClick={onClose}
        >
          <X className="h-4 w-4" />
        </Button>
        <img
          src={src || '/placeholder.svg'}
          alt={alt}
          className="max-w-full max-h-full object-contain rounded-lg"
          onError={(e) => {
            const target = e.target as HTMLImageElement;
            target.style.display = 'none';
          }}
        />
      </div>
    </div>
  );
};

export const ImageGallery: React.FC<ImageGalleryProps> = ({ images, title }) => {
  const [selectedImage, setSelectedImage] = useState<string | null>(null);
  const [loadedImages, setLoadedImages] = useState<Set<string>>(new Set());
  const [failedImages, setFailedImages] = useState<Set<string>>(new Set());

  const handleImageLoad = (src: string) => {
    setLoadedImages((prev) => new Set([...prev, src]));
  };

  const handleImageError = (src: string) => {
    setFailedImages((prev) => new Set([...prev, src]));
  };

  const validImages = images.filter((img) => !failedImages.has(img));

  if (validImages.length === 0) {
    return (
      <div className="text-center py-8">
        <ImageIcon className="h-8 w-8 text-muted-foreground mx-auto mb-2" />
        <p className="text-muted-foreground">Nenhuma imagem válida encontrada</p>
      </div>
    );
  }

  return (
    <div className="space-y-4">
      <div className="flex items-center justify-between">
        <h4 className="text-sm font-medium">
          {title} ({validImages.length})
        </h4>
        <Badge variant="outline">{validImages.length} imagens</Badge>
      </div>

      <div className="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-4">
        {validImages.map((src, index) => (
          <Card key={index} className="overflow-hidden group cursor-pointer hover:shadow-lg transition-shadow">
            <div className="relative aspect-square bg-muted">
              {!loadedImages.has(src) && !failedImages.has(src) && (
                <div className="absolute inset-0 flex items-center justify-center">
                  <div className="animate-spin rounded-full h-6 w-6 border-b-2 border-blue-600"></div>
                </div>
              )}

              <img
                src={src || '/placeholder.svg'}
                alt={`${title} ${index + 1}`}
                className="w-full h-full object-cover transition-transform group-hover:scale-105"
                onLoad={() => handleImageLoad(src)}
                onError={() => handleImageError(src)}
                onClick={() => setSelectedImage(src)}
              />

              <div className="absolute top-2 right-2 opacity-0 group-hover:opacity-100 transition-opacity">
                <Button
                  variant="outline"
                  size="icon"
                  className="h-8 w-8 bg-white/10 border-white/20 text-white hover:bg-white/20"
                  onClick={(e) => {
                    e.stopPropagation();
                    window.open(src, '_blank');
                  }}
                >
                  <ExternalLink className="h-3 w-3" />
                </Button>
              </div>
            </div>

            <CardContent className="p-2">
              <div className="flex items-center justify-between">
                <span className="text-xs text-muted-foreground">Imagem {index + 1}</span>
              </div>
            </CardContent>
          </Card>
        ))}
      </div>

      <ImageModal
        src={selectedImage || ''}
        alt={`${title} ampliada`}
        isOpen={!!selectedImage}
        onClose={() => setSelectedImage(null)}
      />
    </div>
  );
};
