import type { ButtonHTMLAttributes, ReactNode } from "react";

interface ButtonProps extends ButtonHTMLAttributes<HTMLButtonElement> {
  /**
   * Additional CSS classes to apply to the button.
   * Useful for styling and customizing the component appearance.
   */
  className?: string;

  /**
   * Button inner content, usually text or icons.
   */
  children?: ReactNode;
}

/**
 * A reusable button component that extends the native HTML button element.
 * Supports all default button properties and accepts custom styling and children.
 *
 * @component
 * @param {ButtonProps} props - Properties passed to the Button component.
 * @param {string} [props.className] - Optional custom CSS class names.
 * @param {ReactNode} [props.children] - Content displayed inside the button.
 * @returns {JSX.Element} A styled button element.
 */
export default function Button({
  className = "",
  children,
  ...props
}: ButtonProps) {
  return (
    <button {...props} className={`btn ${className}`}>
      {children}
    </button>
  );
}
