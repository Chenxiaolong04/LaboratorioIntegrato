import type { ChangeEvent, FC } from "react";

/**
 * Props for the Input component.
 *
 * @interface InputProps
 * @property {string} [id] - Optional ID for the input element. Defaults to the `name` value.
 * @property {string} name - Name attribute of the input.
 * @property {string} [type] - Input type (e.g., text, number). Defaults to "text".
 * @property {string} [placeholder] - Placeholder text displayed inside the input.
 * @property {string} [autoComplete] - Autocomplete attribute for browser autofill.
 * @property {string | number} [value] - Current value of the input.
 * @property {string} [className] - Custom CSS class for styling. Defaults to "input-form".
 * @property {boolean} [required] - Whether the input is mandatory.
 * @property {number} [min] - Minimum numeric value (for inputs of type number).
 * @property {number} [max] - Maximum numeric value (for inputs of type number).
 * @property {(e: ChangeEvent<HTMLInputElement>) => void} onChange - Change event handler.
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
 * Reusable input component for form fields.
 *
 * Supports full customization of standard input attributes and forwards
 * the `onChange` event for controlled form usage.
 *
 * @function Input
 * @param {InputProps} props - The properties passed to the input element.
 * @returns {JSX.Element} A styled input field with configurable behavior.
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
