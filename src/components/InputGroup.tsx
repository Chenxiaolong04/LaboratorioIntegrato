import Label from "./Label";
import Input from "./Input";
import type { ChangeEvent } from "react";

interface InputGroupProps {
  label: string;
  name: string;
  type?: string;
  placeholder?: string;
  autoComplete?: string;
  value?: string;
  className?: string;
  required: boolean;
  onChange: (e: ChangeEvent<HTMLInputElement>) => void;
}

export default function InputGroup({
  label,
  name,
  type = "text",
  placeholder = "",
  autoComplete,
  value,
  className = "",
  required = false,
  onChange,
}: InputGroupProps) {
  const id = name; // usiamo il name anche come id per collegare la label

  return (
    <div className={`input-group ${className}`}>
      <Label text={label} htmlFor={id} />
      <Input
        id={id}
        name={name}
        type={type}
        placeholder={placeholder}
        autoComplete={autoComplete}
        value={value}
        required={required}
        onChange={onChange}
      />
    </div>
  );
}
