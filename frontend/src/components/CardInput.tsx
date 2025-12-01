import type { MouseEventHandler } from "react";

/**
 * Props for the CardInput component.
 *
 * @interface CardInputProps
 * @property {string} text - The label or text displayed inside the card.
 * @property {string} img - The image source URL displayed at the top of the card.
 * @property {boolean} [isActive] - Indicates whether the card is currently active or selected.
 * @property {MouseEventHandler<HTMLButtonElement>} [onClick] - Handler for the click event on the card.
 */
interface CardInputProps {
  text: string;
  img: string;
  isActive?: boolean;
  onClick?: MouseEventHandler<HTMLButtonElement>;
}

/**
 * Interactive card-like input component.
 *
 * @function CardInput
 * @param {CardInputProps} props - The properties passed to the component.
 * @returns {JSX.Element} A button styled as a selectable card containing an image and text.
 */
export default function CardInput({ text, img, isActive, onClick }: CardInputProps) {
  return (
    <button
      type="button"
      className={`card-input ${isActive ? "active" : ""}`}
      onClick={onClick}
    >
      <img src={img} alt={`Immagine per ${text}`} />
      <p>{text}</p>
    </button>
  );
}
