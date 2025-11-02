import React from "react";

interface InputProps {
  name: string;
  type: string;
  placeholder: string;
  autoComplete: string;
  value?: string;
  className?: string;
  onChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
}

export default function Input({
  name,
  type = "text",
  placeholder = "",
  autoComplete,
  value,
  className = 'input-form',
  onChange,
}: InputProps) {
  return (
    <input
      name={name}
      type={type}
      placeholder={placeholder}
      autoComplete={autoComplete}
      value={value}
      className={className}
      onChange={onChange}
    />
  );
}
