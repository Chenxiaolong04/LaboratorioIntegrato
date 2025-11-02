import Label from "./Label";
import Input from "./Input";

interface InputGroupProps {
  label: string;
  type: string;
  placeholder: string;
  autoComplete: string;
  value?: string;
  className?: string;
  onChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
}

export default function InputGroup({
  label,
  type = "text",
  placeholder = "",
  autoComplete,
  value,
  className,
  onChange,
}: InputGroupProps) {
  return (
    <div className={`input-group ${className || ""}`}>
      <Label text={label} />
      <Input name={label} type={type} placeholder={placeholder} autoComplete={autoComplete} value={value} className={className} onChange={onChange} />
    </div>
  );
}