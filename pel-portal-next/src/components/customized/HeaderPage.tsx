import { BackButton } from './BackButton';

type HeaderPageProps = {
  title: string;
  onBack: () => void;
};

export function HeaderPage({
  title,
  onBack,
}: HeaderPageProps) {
  return (
    <div className="flex items-center mb-6 w-full">
      <BackButton onClick={onBack} />
      <h1 className="text-xl font-semibold text-gray-900">{title}</h1>
    </div>
  )
}
