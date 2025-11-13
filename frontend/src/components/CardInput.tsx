import type { MouseEventHandler } from "react";

interface CardInputProps {
  text: string;
  img: string;
  isActive?: boolean;
  onClick?: MouseEventHandler<HTMLButtonElement>;
}

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
