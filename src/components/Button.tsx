import React from "react";

interface ButtonProps {
    className?: string;
    onClick?: () => void;
    children?: React.ReactNode; // contenuto dentro il bottone
}

export default function Button({
    className,
    onClick,
    children,
}: ButtonProps) {
  return (
    <button type="button" className={`btn ${className || ''}`} onClick={onClick}>
      {children}
    </button>
  );
}
