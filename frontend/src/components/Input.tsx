import type { ChangeEvent, FC } from "react";

interface InputProps {
  id?: string;
  name: string;
  type?: string;
  placeholder?: string;
  autoComplete?: string;
  value?: string | number;
  className?: string;
  required?: boolean;
  min?: number;
  max?: number;
  onChange: (e: ChangeEvent<HTMLInputElement>) => void;
}

const Input: FC<InputProps> = ({
  id,
  name,
  type = "text",
  placeholder = "",
  autoComplete,
  value = "",
  className = "input-form",
  required = false,
  min = 0,
  max = 0,
  onChange,
}) => {
  return (
    <input
      id={id || name}
      name={name}
      type={type}
      placeholder={placeholder}
      autoComplete={autoComplete}
      value={value}
      className={className}
      required={required}
      min={min}
      max={max}
      onChange={onChange}
    />
  );
};

export default Input;
