import React, { useState, useImperativeHandle, forwardRef } from "react";
import InputGroup from "../InputGroup";

const indirizzoRegex =
  /^(via|viale|corso|piazza|largo)\s+[a-zàèéìòù'\s]+[\s,]*\d+[a-zA-Z]?$/i;

export interface StepLocationRef {
  validate: () => boolean;
}

interface StepLocationProps {
  error: string;
  setError: React.Dispatch<React.SetStateAction<string>>;
  setIsValid: React.Dispatch<React.SetStateAction<boolean>>;
}

const StepLocation = forwardRef<StepLocationRef, StepLocationProps>(
  ({ error, setError, setIsValid }, ref) => {
    const [address, setAddress] = useState("");

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
      setAddress(e.target.value);
    };

    // 👇 Esponiamo la funzione validate() al genitore tramite ref
    useImperativeHandle(ref, () => ({
      validate: () => {
        if (!indirizzoRegex.test(address)) {
          setError("Inserisci un indirizzo valido (es: Via Roma 10)");
          setIsValid(false);
          return false;
        }
        setError("");
        setIsValid(true);
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

        {/* Mostra l'errore solo dopo che l'utente ha cliccato Avanti */}
        {error && <p className="error-message">{error}</p>}
      </div>
    );
  }
);

export default StepLocation;
