"use client"

import * as React from "react"
import { useIsMobile } from "@/hooks/useMobile"
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from "@/components/ui/dialog"
import {
  Drawer,
  DrawerClose,
  DrawerContent,
  DrawerDescription,
  DrawerFooter,
  DrawerHeader,
  DrawerTitle,
  DrawerTrigger,
} from "@/components/ui/drawer"
import { cn } from "@/helpers/cn"

interface ResponsiveDialogProps {
  children: React.ReactNode
  open?: boolean
  onOpenChange?: (open: boolean) => void
}

interface ResponsiveDialogTriggerProps {
  children: React.ReactNode
  asChild?: boolean
}

interface ResponsiveDialogContentProps {
  children: React.ReactNode
  className?: string
}

interface ResponsiveDialogHeaderProps {
  children: React.ReactNode
  className?: string
}

interface ResponsiveDialogTitleProps {
  children: React.ReactNode
  className?: string
}

interface ResponsiveDialogDescriptionProps {
  children: React.ReactNode
  className?: string
}

interface ResponsiveDialogFooterProps {
  children: React.ReactNode
  className?: string
}

interface ResponsiveDialogCloseProps {
  children: React.ReactNode
  asChild?: boolean
  className?: string
}

const ResponsiveDialog = ({ children, ...props }: ResponsiveDialogProps) => {
  const isMobile = useIsMobile()

  if (isMobile) {
    return <Drawer {...props}>{children}</Drawer>
  }

  return <Dialog {...props}>{children}</Dialog>
}

const ResponsiveDialogTrigger = ({ children, ...props }: ResponsiveDialogTriggerProps) => {
  const isMobile = useIsMobile()

  if (isMobile) {
    return <DrawerTrigger {...props}>{children}</DrawerTrigger>
  }

  return <DialogTrigger {...props}>{children}</DialogTrigger>
}

const ResponsiveDialogContent = ({ children, className, ...props }: ResponsiveDialogContentProps) => {
  const isMobile = useIsMobile()

  if (isMobile) {
    return (
      <DrawerContent className={className} {...props}>
        {children}
      </DrawerContent>
    )
  }

  return (
    <DialogContent className={className} {...props}>
      {children}
    </DialogContent>
  )
}

const ResponsiveDialogHeader = ({ children, className, ...props }: ResponsiveDialogHeaderProps) => {
  const isMobile = useIsMobile()

  if (isMobile) {
    return (
      <DrawerHeader className={className} {...props}>
        {children}
      </DrawerHeader>
    )
  }

  return (
    <DialogHeader className={className} {...props}>
      {children}
    </DialogHeader>
  )
}

const ResponsiveDialogTitle = ({ children, className, ...props }: ResponsiveDialogTitleProps) => {
  const isMobile = useIsMobile()

  if (isMobile) {
    return (
      <DrawerTitle className={className} {...props}>
        {children}
      </DrawerTitle>
    )
  }

  return (
    <DialogTitle className={className} {...props}>
      {children}
    </DialogTitle>
  )
}

const ResponsiveDialogDescription = ({ children, className, ...props }: ResponsiveDialogDescriptionProps) => {
  const isMobile = useIsMobile()

  if (isMobile) {
    return (
      <DrawerDescription className={className} {...props}>
        {children}
      </DrawerDescription>
    )
  }

  return (
    <DialogDescription className={className} {...props}>
      {children}
    </DialogDescription>
  )
}

const ResponsiveDialogFooter = ({ children, className, ...props }: ResponsiveDialogFooterProps) => {
  const isMobile = useIsMobile()

  if (isMobile) {
    return (
      <DrawerFooter className={className} {...props}>
        {children}
      </DrawerFooter>
    )
  }

  // Para desktop, retornamos apenas uma div com as classes apropriadas
  return (
    <div className={`flex flex-col-reverse sm:flex-row sm:justify-end sm:space-x-2 ${className || ""}`} {...props}>
      {children}
    </div>
  )
}

const ResponsiveDialogClose = ({ children, className, ...props }: ResponsiveDialogCloseProps) => {
  const isMobile = useIsMobile()

  if (isMobile) {
    return null;
  }

  // Para desktop, retornamos uma div com a classe para manter a funcionalidade
  return <div className={className} {...props}>{children}</div>
}

export {
  ResponsiveDialog,
  ResponsiveDialogTrigger,
  ResponsiveDialogContent,
  ResponsiveDialogHeader,
  ResponsiveDialogTitle,
  ResponsiveDialogDescription,
  ResponsiveDialogFooter,
  ResponsiveDialogClose,
}
