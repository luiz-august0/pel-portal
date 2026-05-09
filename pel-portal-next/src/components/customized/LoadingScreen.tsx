'use client';

export default function LoadingScreen() {
  return (
    <div className="fixed inset-0 w-screen h-screen pel-gradient flex items-center justify-center">
      {/* Background overlay com blur */}
      <div className="absolute inset-0 bg-black/10 backdrop-blur-sm" />
      
      {/* Container principal */}
      <div className="relative z-10 flex flex-col items-center justify-center space-y-8 p-8">
        
        {/* Logo ou ícone principal com animação */}
        <div className="relative">
          
          {/* Ícone central */}
          <div className="absolute inset-0 flex items-center justify-center">
            <div className="w-8 h-8 bg-primary rounded-full animate-pulse" />
          </div>
        </div>

        {/* Texto principal */}
        <div className="text-center space-y-3">
          <p className="font-medium text-sm max-w-xs">
            Preparando sua experiência no PEL Portal
          </p>
        </div>

        {/* Indicadores de status */}
        <div className="flex space-x-2">
          {[0, 1, 2].map((index) => (
            <div
              key={index}
              className={`w-2 h-2 rounded-full transition-all duration-300 ${
                'bg-primary animate-pulse'
              }`}
              style={{
                animationDelay: `${index * 0.2}s`
              }}
            />
          ))}
        </div>
      </div>

      {/* Elementos decorativos de fundo */}
      <div className="absolute top-1/4 left-1/4 w-32 h-32 bg-primary/5 rounded-full blur-xl animate-float" />
      <div className="absolute bottom-1/4 right-1/4 w-24 h-24 bg-accent/5 rounded-full blur-xl animate-float-delayed" />
    </div>
  );
}
    