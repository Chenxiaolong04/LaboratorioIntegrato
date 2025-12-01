import type { ButtonHTMLAttributes, ReactNode } from "react";

/**
 * Props for the Button component.
 *
 * @interface ButtonProps
 * @extends ButtonHTMLAttributes<HTMLButtonElement>
 * @property {string} [className] - Additional CSS classes to customize the button style.
 * @property {ReactNode} [children] - Content to be displayed inside the button.
 */
interface ButtonProps extends ButtonHTMLAttributes<HTMLButtonElement> {
  className?: string;
  children?: ReactNode;
}

/**
 * Reusable Button component.
 *
 * @function Button
 * @param {ButtonProps} props - Component props, including all native `<button>` attributes.
 * @returns {JSX.Element} A `<button>` element with the provided classes and content.
 */
export default function Button({
  className = "",
  children,
  ...props
}: ButtonProps) {
  return (
    <button
      {...props}
      className={`btn ${className}`}
    >
      {children}
    </button>
  );
}
