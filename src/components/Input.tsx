import type { ChangeEvent, FC } from "react";

interface InputProps {
  id?: string;
  name: string;
  type?: string;
  placeholder?: string;
  autoComplete?: string;
  value?: string;
  className?: string;
  required?: boolean;
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
      onChange={onChange}
    />
  );
};

export default Input;
