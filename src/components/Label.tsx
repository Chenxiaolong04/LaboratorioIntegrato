import type { FC } from "react";

interface LabelProps {
  text: string;
  htmlFor?: string;
  className?: string;
}

const Label: FC<LabelProps> = ({ text, htmlFor, className = "" }) => {
  return (
    <label htmlFor={htmlFor} className={className}>
      {text}
    </label>
  );
};

export default Label;
