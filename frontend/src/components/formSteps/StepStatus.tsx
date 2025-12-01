import React, { useState, useImperativeHandle, forwardRef } from "react";
import { useFormContext } from "../../context/FormContext";
import CardInput from "../CardInput";
import NuovoImg from "../../assets/img/form-img/nuovo_immobile.webp";
import OttimoImg from "../../assets/img/form-img/ottimo_immobile.webp";
import BuonoImg from "../../assets/img/form-img/buono_immobile.webp";
import RistrutturareImg from "../../assets/img/form-img/ristrutturare_immobile.webp";

/**
 * Reference interface exposed by StepStatus.
 * Allows the parent component to trigger validation.
 */
export interface StepStatusRef {
  validate: () => boolean;
}

interface StepStatusProps {
  /** Error message displayed if validation fails */
  error: string;

  /** Function used to update the error message */
  setError: React.Dispatch<React.SetStateAction<string>>;
}

/**
 * Step component used to select the condition/status of the property.
 *
 * This component:
 * - Stores the selected status locally
 * - Syncs the selected value with the global form context
 * - Exposes a validate() method to the parent via forwardRef
 */
const StepStatus = forwardRef<StepStatusRef, StepStatusProps>(
  ({ error, setError }, ref) => {
    const { formData, setFormData } = useFormContext();

    /** Local state storing the selected property status */
    const [selectedStatus, setSelectedStatus] = useState(formData.status || "");

    /**
     * Selects a status and updates both local and global form data.
     * Also clears any existing validation error.
     *
     * @param value - The property status selected by the user
     */
    const handleSelect = (value: string) => {
      setSelectedStatus(value);
      setFormData((prev) => ({ ...prev, status: value }));
      setError("");
    };

    /**
     * Exposes validation logic to the parent component.
     * Ensures the user selects a status before proceeding.
     */
    useImperativeHandle(ref, () => ({
      validate: () => {
        if (!selectedStatus) {
          setError("Seleziona uno stato dell'immobile");
          return false;
        }
        setError("");
        return true;
      },
    }));

    /** Available property statuses rendered as selectable cards */
    const statuses = [
      { text: "Nuovo", img: NuovoImg },
      { text: "Ottimo stato", img: OttimoImg },
      { text: "Buono", img: BuonoImg },
      { text: "Da ristrutturare", img: RistrutturareImg },
    ];

    return (
      <div className="step">
        <h2>Stato dell'immobile</h2>

        <div className="card-group-input">
          {statuses.map(({ text, img }) => (
            <CardInput
              key={text}
              text={text}
              img={img}
              isActive={selectedStatus === text}
              onClick={() => handleSelect(text)}
            />
          ))}
        </div>

        {error && <p className="error-message">{error}</p>}
      </div>
    );
  }
);

export default StepStatus;
