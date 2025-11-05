import type { ButtonHTMLAttributes, ReactNode } from "react";

interface ButtonProps extends ButtonHTMLAttributes<HTMLButtonElement> {
  className?: string;
  children?: ReactNode;
}

export default function Button({
  className = "",
  children,
  ...props
}: ButtonProps) {
  return (
    <button
      {...props}
      className={`btn ${className}`}
    >
      {children}
    </button>
  );
}
