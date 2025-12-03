import { FaLocationDot } from "react-icons/fa6";

type Property = {
  id: number;
  type: string;
  name: string;
  address: string;
  state: string;
  price: number;
  surface: number;
  plus: string[];
  description: string;
  image: string;
};

export default function PropertyModal({
  property,
  onClose,
}: {
  property: Property | null;
  onClose: () => void;
}) {
  if (!property) return null;

  return (
    <div className="modal-overlay-property" onClick={onClose}>
      <div
        className="modal-content-property"
        onClick={(e) => e.stopPropagation()}
      >
        <button className="close-btn" onClick={onClose}>
          ✕
        </button>

        <div className="modal-header">
          <img src={property.image} alt={property.type} />
          <div className="modal-title">
            <h2>{property.type}</h2>
            <h3>{property.name}</h3>
            <p>
              <FaLocationDot /> {property.address}
            </p>
          </div>
        </div>

        <div className="modal-body">
          <p>
            <strong>Stato:</strong> {property.state}
          </p>
          <p>
            <strong>Prezzo:</strong> {property.price.toLocaleString()}€
          </p>
          <p>
            <strong>Superficie:</strong> {property.surface} m²
          </p>
          <p>
            <strong>Plus:</strong> {property.plus.join(", ")}
          </p>
          <p>
            <strong>Descrizione:</strong> {property.description}
          </p>
        </div>
      </div>
    </div>
  );
}
