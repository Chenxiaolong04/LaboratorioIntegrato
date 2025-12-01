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

/**
 * StepLocation Component
 *
 * @description
 * This component represents the location step in a multi-step form.
 * It allows the user to input the address of the property, validates the format,
 * and updates the shared form state through `FormContext`.
 * A `validate()` method is exposed via `forwardRef` to allow parent components
 * to trigger validation externally.
 *
 * @component
 * @param {StepLocationProps} props - Contains the error message and the setter for updating it.
 * @param {React.Ref<StepLocationRef>} ref - A forwarded ref exposing the `validate` function.
 * @returns {JSX.Element} The JSX layout for the property location form step.
 */
const StepLocation = forwardRef<StepLocationRef, StepLocationProps>(
  ({ error, setError }, ref) => {
    const { formData, setFormData } = useFormContext();
    const [address, setAddress] = useState(formData.address || "");

    /**
     * Handles changes in the address input field and updates both local and global form state.
     *
     * @param {React.ChangeEvent<HTMLInputElement>} e - The input change event.
     * @returns {void}
     */
    const handleChange = (e: React.ChangeEvent<HTMLInputElement>): void => {
      const value = e.target.value;
      setAddress(value);
      setFormData((prev) => ({ ...prev, address: value }));
    };

    /**
     * Exposes validation logic to the parent component.
     *
     * @function validate
     * @description
     * Validates the address using a predefined regex pattern. If invalid, an error
     * message is set and `false` is returned.
     *
     * @returns {boolean} Whether the field is valid.
     */
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
