import { X } from "lucide-react";
import type { ReactNode } from "react";

import { Button } from "@/components/ui/button";
import {
  Dialog,
  DialogContent,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog";
import { cn } from "@/helpers/cn";
import * as Icon from "lucide-react";

import { buttonVariants } from "@/components/ui/button";
import { VariantProps } from "class-variance-authority";
import { Stepper } from "./Stepper";

export type FormButton = React.ComponentProps<"button"> &
  VariantProps<typeof buttonVariants> & {
    asChild?: boolean;
  } & {
    title: string;
    loading?: boolean;
    isSubmit?: boolean;
  };

export interface StepType {
  label: string;
  jsx: JSX.Element;
}

export type CustomizedButtonProps = React.ComponentProps<"button"> &
  VariantProps<typeof buttonVariants> & {
    asChild?: boolean;
  } & {
    label: JSX.Element | string;
    startIcon?: keyof typeof Icon;
  };

type Props = {
  open: boolean;
  formTitle: string;
  formSubtitle?: string;
  handleClose: () => void;
  formButtons: FormButton[];
  steps?: StepType[];
  activeStep?: number;
  headerButtons?: CustomizedButtonProps[];
  children: ReactNode;
  className?: string;
};

export default function StandardForm({
  open,
  formTitle,
  formSubtitle,
  handleClose,
  formButtons,
  steps,
  activeStep = 0,
  headerButtons,
  children,
  className,
  ...rest
}: Props) {
  const isMobile =
    typeof window !== "undefined" ? window.innerWidth <= 768 : false;

  const renderHeaderButtons = () => {
    return (
      <>
        {headerButtons?.map((button, index) => (
          <Button
            key={index}
            {...button}
            className={cn("h-10", button.className)}
          >
            {button.label}
          </Button>
        ))}
      </>
    );
  };

  return (
    <Dialog
      open={open}
      onOpenChange={(open) => !open && handleClose()}
      {...rest}
    >
      <DialogContent
        className={cn("sm:max-w-[500px] md:max-w-[600px]", className)}
      >
        <DialogHeader>
          <div className="flex items-center justify-between">
            <div className="">
              <DialogTitle className="text-2xl">{formTitle}</DialogTitle>
              {formSubtitle && (
                <p className="text-muted-foreground">{formSubtitle}</p>
              )}
            </div>
            <div className="flex items-center gap-4">
              {!isMobile && renderHeaderButtons()}
              <Button
                variant="ghost"
                size="icon"
                onClick={handleClose}
                className="h-8 w-8"
              >
                <X className="h-4 w-4" />
              </Button>
            </div>
          </div>
          <div className="mt-1 flex flex-row flex-wrap gap-4">
            {isMobile && renderHeaderButtons()}
          </div>
          {steps && steps.length > 0 && (
            <div className="mt-4 w-full">
              <Stepper steps={steps} activeStep={activeStep} />
            </div>
          )}
        </DialogHeader>
        {children}
        <DialogFooter className="flex flex-row gap-2 justify-end pt-4">
          {formButtons.map(
            ({ id, onClick, variant, loading, isSubmit, title, ...props }) => (
              <Button
                key={id}
                onClick={onClick}
                variant={variant || "default"}
                disabled={loading && isSubmit}
                {...props}
              >
                {title}
              </Button>
            ),
          )}
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
}
