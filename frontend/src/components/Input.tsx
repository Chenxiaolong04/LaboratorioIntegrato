import type { ChangeEvent, FC } from "react";

/**
 * Props for the reusable Input component.
 *
 * @interface InputProps
 * @property {string} [id] - Optional ID for the input element. Defaults to the value of `name` if not provided.
 * @property {string} name - Name attribute of the input, required for form handling.
 * @property {string} [type="text"] - Input type (text, number, email, etc.).
 * @property {string} [placeholder] - Placeholder text displayed inside the input.
 * @property {string} [autoComplete] - Autocomplete behavior for the input.
 * @property {string | number} [value] - Controlled input value.
 * @property {string} [className="input-form"] - CSS class applied to the input.
 * @property {boolean} [required=false] - Whether the input is required in the form.
 * @property {number} [min] - Min value for numeric input types.
 * @property {number} [max] - Max value for numeric input types.
 * @property {(e: ChangeEvent<HTMLInputElement>) => void} onChange - Callback triggered when value changes.
 */
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

/**
 * Reusable controlled input component used throughout the form system.
 * Allows customization of type, placeholder, validation rules, styles and more.
 *
 * @component
 * @param {InputProps} props - Component properties.
 * @returns {JSX.Element} A customizable input element.
 */
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
