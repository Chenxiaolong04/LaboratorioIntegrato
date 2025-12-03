import type { FC } from "react";

interface LabelProps {
  /**
   * The text to display inside the label.
   */
  text: string;

  /**
   * The id of the input this label is associated with.
   */
  htmlFor?: string;

  /**
   * Additional CSS classes to apply to the label.
   */
  className?: string;
}

/**
 * Reusable Label component for form inputs.
 * Associates text with an input via the `htmlFor` attribute.
 *
 * @component
 * @param {LabelProps} props - Properties passed to the Label component.
 * @param {string} props.text - The label text.
 * @param {string} [props.htmlFor] - The id of the input element this label is for.
 * @param {string} [props.className] - Optional CSS class names.
 * @returns {JSX.Element} A label element.
 */
const Label: FC<LabelProps> = ({ text, htmlFor, className = "" }) => {
  return (
    <label htmlFor={htmlFor} className={className}>
      {text}
    </label>
  );
};

export default Label;
