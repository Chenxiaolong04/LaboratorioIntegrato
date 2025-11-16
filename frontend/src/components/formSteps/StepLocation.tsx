import React, { useState, useImperativeHandle, forwardRef } from "react";
import InputGroup from "../InputGroup";
import FormMap from "../../assets/img/form-img/form-map.webp";
import { useFormContext } from "../../context/FormContext";

const indirizzoRegex =
  /^(via|viale|corso|piazza|largo)\s+[a-zàèéìòù'\s]+[\s,]*\d+[a-zA-Z]?$/i;

export interface StepLocationRef {
  validate: () => boolean;
}

interface StepLocationProps {
  error: string;
  setError: React.Dispatch<React.SetStateAction<string>>;
}

const StepLocation = forwardRef<StepLocationRef, StepLocationProps>(
  ({ error, setError }, ref) => {
    const { formData, setFormData } = useFormContext();
    const [address, setAddress] = useState(formData.address || "");

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
      const value = e.target.value;
      setAddress(value);
      setFormData((prev) => ({ ...prev, address: value }));
    };

    useImperativeHandle(ref, () => ({
      validate: () => {
        if (!indirizzoRegex.test(address)) {
          setError("Inserisci un indirizzo valido (es: Via Roma 10)");
          return false;
        }
        setError("");
        return true;
      },
    }));

    return (
      <div className="step">
        <h2>Dove si trova l'immobile da valutare?</h2>

        <InputGroup
          name="address"
          label="Indirizzo dell'immobile"
          type="text"
          placeholder="Es: Via Roma 10"
          autoComplete="street-address"
          value={address}
          required
          onChange={handleChange}
        />

        {error && <p className="error-message">{error}</p>}

        <img
          className="map"
          src={FormMap}
          alt="mappa posizione immobile"
        />
      </div>
    );
  }
);

export default StepLocation;
