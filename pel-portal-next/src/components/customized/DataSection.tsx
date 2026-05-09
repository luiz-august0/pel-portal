interface DataSectionProps {
  values: {
    label: string;
    value?: string | React.ReactNode;
  }[];
}

export function DataSection({ values }: DataSectionProps) {
  return (
    <div className="space-y-3 text-sm">
      {values.filter(value => value.value && value.value !== '').map((value, index) => (
        <div className="flex items-center justify-between" key={index}>
          <span className="text-gray-600 font-medium">{value.label}</span>
          {typeof value.value === 'string' ? (
            <div className="text-gray-900 font-medium">
              {value.value}
            </div>
          ) : (
            value.value
          )}
        </div>
      ))}
    </div>
  );
}
