import { useState, forwardRef, useImperativeHandle } from "react";
import { useFormContext } from "../../context/FormContext";
import InputGroup from "../InputGroup";
import { FaChevronDown } from "react-icons/fa6";

export type StepGeneralRef = {
  validate: () => boolean;
};

interface StepGeneralProps {
  error: string;
  setError: React.Dispatch<React.SetStateAction<string>>;
}

const StepGeneral = forwardRef<StepGeneralRef, StepGeneralProps>(
  ({ error, setError }, ref) => {
    const { formData, setFormData } = useFormContext();

    const [surface, setSurface] = useState(formData.surface?.toString() || "");
    const [floor, setFloor] = useState(formData.floor?.toString() || "");
    const [rooms, setRooms] = useState(formData.rooms?.toString() || "");
    const [bathrooms, setBathrooms] = useState(
      formData.bathrooms?.toString() || ""
    );
    const [heating, setHeating] = useState(formData.heating || "");

    const handleSurface = (e: React.ChangeEvent<HTMLInputElement>) => {
      setSurface(e.target.value);
      setFormData({ ...formData, surface: e.target.value });
    };

    const handleFloor = (e: React.ChangeEvent<HTMLInputElement>) => {
      setFloor(e.target.value);
      setFormData({ ...formData, floor: e.target.value });
    };

    const handleRooms = (e: React.ChangeEvent<HTMLInputElement>) => {
      setRooms(e.target.value);
      setFormData({ ...formData, rooms: e.target.value });
    };

    const handleBathrooms = (e: React.ChangeEvent<HTMLInputElement>) => {
      setBathrooms(e.target.value);
      setFormData({ ...formData, bathrooms: e.target.value });
    };

    const handleHeating = (e: React.ChangeEvent<HTMLSelectElement>) => {
      setHeating(e.target.value);
      setFormData({ ...formData, heating: e.target.value });
    };

    useImperativeHandle(ref, () => ({
      validate() {
        if (parseInt(surface) < 10 || parseInt(surface) > 1000) {
          setError("La superficie deve essere tra 10 e 1000 m².");
          return false;
        }

        if (parseInt(floor) < -2 || parseInt(floor) > 100) {
          setError("Il piano deve essere tra -2 e 100.");
          return false;
        }

        if (parseInt(rooms) < 1 || parseInt(rooms) > 50) {
          setError("I locali devono essere tra 1 e 50.");
          return false;
        }

        if (parseInt(bathrooms) < 1 || parseInt(bathrooms) > 20) {
          setError("I bagni devono essere tra 1 e 20.");
          return false;
        }

        if (!heating) {
          setError("Seleziona il tipo di riscaldamento.");
          return false;
        }

        setError("");
        return true;
      },
    }));

    return (
      <div className="step">
        <h2>Caratteristiche principali</h2>

        <InputGroup
          name="superficie"
          label="Superficie totale immobile m²"
          type="number"
          placeholder="Es: 90"
          autoComplete="off"
          value={surface}
          required
          min={10}
          max={1000}
          onChange={handleSurface}
        />

        <InputGroup
          name="piano"
          label="A quale piano si trova"
          type="number"
          placeholder="Piano terra corrisponde a 0"
          autoComplete="off"
          value={floor}
          required
          min={-2}
          max={100}
          onChange={handleFloor}
        />

        <InputGroup
          name="locali"
          label="Numero di locali (esclusi bagni e cucina)"
          type="number"
          placeholder="Es: 5"
          autoComplete="off"
          value={rooms}
          required
          min={1}
          max={50}
          onChange={handleRooms}
        />

        <InputGroup
          name="bagni"
          label="Numero di bagni"
          type="number"
          placeholder="Es: 2"
          autoComplete="off"
          value={bathrooms}
          required
          min={1}
          max={20}
          onChange={handleBathrooms}
        />

        <div className="input-group">
          <label htmlFor="heating">Tipo di riscaldamento *</label>
          <select
            id="heating"
            value={heating}
            onChange={handleHeating}
            required
            className="input-form"
          >
            <option value=""></option>
            <option value="autonomo">Autonomo</option>
            <option value="centralizzato">Centralizzato</option>
            <option value="pompa di calore">Pompa di calore</option>
            <option value="teleriscaldamento">Teleriscaldamento</option>
            <option value="nessuno">Nessuno</option>
          </select>
        </div>

        {error && <p className="error">{error}</p>}
      </div>
    );
  }
);

export default StepGeneral;
