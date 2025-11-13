import React, { useState, useImperativeHandle, forwardRef } from "react";
import { useFormContext } from "../../context/FormContext";
import CardInput from "../CardInput";
import TestImage from "../../assets/img/test-image.jpg";

export interface StepStatusRef {
  validate: () => boolean;
}

interface StepStatusProps {
  error: string;
  setError: React.Dispatch<React.SetStateAction<string>>;
}

const StepStatus = forwardRef<StepStatusRef, StepStatusProps>(
  ({ error, setError }, ref) => {
    const { formData, setFormData } = useFormContext();
    const [selectedStatus, setSelectedStatus] = useState(formData.status || "");

    const handleSelect = (value: string) => {
      setSelectedStatus(value);
      setFormData((prev) => ({ ...prev, status: value }));
      setError("");
    };

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

    return (
      <div className="step">
        <h2>Stato dell'immobile</h2>

        <div className="card-group-input">
          {["Nuovo", "Ottimo stato", "Buono", "Da ristrutturare"].map((text) => (
            <CardInput
              key={text}
              text={text}
              img={TestImage}
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
