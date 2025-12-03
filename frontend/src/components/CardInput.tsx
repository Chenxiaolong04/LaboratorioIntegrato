import type { MouseEventHandler } from "react";

interface CardInputProps {
  /**
   * Text displayed inside the card.
   */
  text: string;

  /**
   * Source URL for the card image.
   */
  img: string;

  /**
   * Indicates whether the card is currently active/selected.
   */
  isActive?: boolean;

  /**
   * Callback function executed when the card is clicked.
   */
  onClick?: MouseEventHandler<HTMLButtonElement>;
}

/**
 * CardInput component used to display a selectable card
 * with an image and text. Useful for option selection UI.
 *
 * @component
 * @param {CardInputProps} props - Component properties.
 * @param {string} props.text - Text label displayed on the card.
 * @param {string} props.img - Path or URL of the image shown in the card.
 * @param {boolean} [props.isActive] - Highlights the card when true.
 * @param {MouseEventHandler<HTMLButtonElement>} [props.onClick] - Event triggered on click.
 * @returns {JSX.Element} A clickable card with text and image.
 */
export default function CardInput({
  text,
  img,
  isActive,
  onClick,
}: CardInputProps) {
  return (
    <button
      type="button"
      className={`card-input ${isActive ? "active" : ""}`}
      onClick={onClick}
    >
      <img src={img} alt={`Image for ${text}`} />
      <p>{text}</p>
    </button>
  );
}
