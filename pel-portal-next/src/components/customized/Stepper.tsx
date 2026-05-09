import { cn } from '@/helpers/cn';
import { StepType } from "./StandardForm";

export function Stepper({ steps, activeStep }: { steps: StepType[]; activeStep: number }) {
  return (
    <div className="flex w-full items-center">
      {steps.map((step, index) => (
        <div key={index} className="flex flex-1 items-center">
          <div
            className={cn(
              'flex h-8 w-8 items-center justify-center rounded-full border text-xs font-medium',
              index <= activeStep ? 'border-primary bg-primary text-primary-foreground' : 'border-input bg-background',
            )}
          >
            {index + 1}
          </div>
          <div
            className={cn(
              'h-1 flex-1',
              index < steps.length - 1 ? (index < activeStep ? 'bg-primary' : 'bg-input') : 'hidden',
            )}
          />
          <div className="ml-2 text-sm font-medium">{step.label}</div>
        </div>
      ))}
    </div>
  );
}
