import Image from "next/image";

type CountryFlagProps = {
  courseName: string;
};

export function CountryFlag({ courseName }: CountryFlagProps) {
  // Mapear nome do curso para imagem da bandeira
  const getFlagImage = (courseName: string): string => {
    const lowerCourseName = courseName.toLowerCase();
    
    if (lowerCourseName.includes("inglês") || lowerCourseName.includes("english")) {
      return "/images/english.jpg";
    }
    
    if (lowerCourseName.includes("espanhol") || lowerCourseName.includes("spanish")) {
      return "/images/spanish.jpg";
    }
    
    if (lowerCourseName.includes("francês") || lowerCourseName.includes("french")) {
      return "/images/french.jpg";
    }
    
    if (lowerCourseName.includes("alemão") || lowerCourseName.includes("german")) {
      return "/images/german.jpg";
    }
    
    if (lowerCourseName.includes("italiano") || lowerCourseName.includes("italian")) {
      return "/images/italian.jpg";
    }
    
    if (lowerCourseName.includes("português") || lowerCourseName.includes("portuguese") || lowerCourseName.includes("portugues")) {
      return "/images/portuguese.jpg";
    }
    
    if (lowerCourseName.includes("japonês") || lowerCourseName.includes("japanese") || lowerCourseName.includes("japones")) {
      return "/images/japonese.jpg";
    }
    
    if (lowerCourseName.includes("grego") || lowerCourseName.includes("greek")) {
      return "/images/greek.jpg";
    }
    
    // Imagem padrão se não encontrar correspondência (inglês como padrão)
    return "/images/english.jpg";
  };

  const flagImage = getFlagImage(courseName);

  return (
    <div className="w-8 h-8 rounded-full overflow-hidden flex items-center justify-center bg-gray-100 flex-shrink-0">
      <Image
        src={flagImage}
        alt={`Bandeira do idioma ${courseName}`}
        width={32}
        height={32}
        className="w-full h-full object-cover"
        priority={false}
        loading="lazy"
      />
    </div>
  );
}
