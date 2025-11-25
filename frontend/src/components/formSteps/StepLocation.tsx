import React, { useState, useImperativeHandle, forwardRef } from "react";
import InputGroup from "../InputGroup";
import FormMap from "../../assets/img/form-img/form-map.webp";
import { useFormContext } from "../../context/FormContext";

const indirizzoRegex =
  /^(via|viale|corso|piazza|largo)\s+[a-zàèéìòù'\s]+[\s,]*\d+[a-zA-Z]?$/i;

const cityRegex = /^[a-zA-Zàèéìòù\s'-]+$/i;

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
    const [city, setCity] = useState(formData.city || "");

    const handleAddressChange = (e: React.ChangeEvent<HTMLInputElement>) => {
      const value = e.target.value;
      setAddress(value);
      setFormData((prev) => ({ ...prev, address: value }));
    };

    const handleCityChange = (e: React.ChangeEvent<HTMLInputElement>) => {
      const value = e.target.value;
      setCity(value);
      setFormData((prev) => ({ ...prev, city: value }));
    };

    useImperativeHandle(ref, () => ({
      validate: () => {
        if (!indirizzoRegex.test(address)) {
          setError("Inserisci un indirizzo valido (es: Via Roma 10)");
          return false;
        }
        if (!cityRegex.test(city)) {
          setError("Inserisci una città valida (es: Torino)");
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
          onChange={handleAddressChange}
        />

        <InputGroup
          name="city"
          label="Città"
          type="text"
          placeholder="Es: Torino"
          autoComplete="address-level2"
          value={city}
          required
          onChange={handleCityChange}
        />

        {error && <p className="error-message">{error}</p>}

        <img className="map" src={FormMap} alt="mappa posizione immobile" />
      </div>
    );
  }
);

export default StepLocation;
