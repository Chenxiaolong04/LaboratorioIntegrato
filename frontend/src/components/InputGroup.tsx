import Label from "./Label";
import Input from "./Input";
import type { ChangeEvent } from "react";

interface InputGroupProps {
  label: string;
  name: string;
  type?: string;
  placeholder?: string;
  autoComplete?: string;
  value?: string | number;
  className?: string;
  required: boolean;
  min?: number;
  max?: number;
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
  min = 0,
  max = 0,
  onChange,
}: InputGroupProps) {
  const id = name;
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
        min={min}
        max={max}
        onChange={onChange}
      />
    </div>
  );
}
