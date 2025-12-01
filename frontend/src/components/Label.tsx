import type { FC } from "react";

/**
 * Props for the Label component.
 *
 * @interface LabelProps
 * @property {string} text - Text content displayed inside the label.
 * @property {string} [htmlFor] - Optional ID of the input element the label is associated with.
 * @property {string} [className] - Optional CSS class for styling the label.
 */
interface LabelProps {
  text: string;
  htmlFor?: string;
  className?: string;
}

/**
 * Reusable label component for form fields.
 *
 * Renders a `<label>` element with optional `htmlFor` attribute and
 * custom styling class.
 *
 * @function Label
 * @param {LabelProps} props - Properties passed to the label component.
 * @returns {JSX.Element} A styled label element.
 */
const Label: FC<LabelProps> = ({ text, htmlFor, className = "" }) => {
  return (
    <label htmlFor={htmlFor} className={className}>
      {text}
    </label>
  );
};

export default Label;
