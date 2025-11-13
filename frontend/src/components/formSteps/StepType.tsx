import React, { useState, useImperativeHandle, forwardRef } from "react";
import { useFormContext } from "../../context/FormContext";
import CardInput from "../CardInput";
import TestImage from "../../assets/img/test-image.jpg";

export interface StepTypeRef {
  validate: () => boolean;
}

interface StepTypeProps {
  error: string;
  setError: React.Dispatch<React.SetStateAction<string>>;
}

const StepType = forwardRef<StepTypeRef, StepTypeProps>(
  ({ error, setError }, ref) => {
    const { formData, setFormData } = useFormContext();
    const [selectedType, setSelectedType] = useState(formData.type || "");

    const handleSelect = (value: string) => {
      setSelectedType(value);
      setFormData((prev) => ({ ...prev, type: value }));
      setError("");
    };

    useImperativeHandle(ref, () => ({
      validate: () => {
        if (!selectedType) {
          setError("Seleziona una tipologia di immobile");
          return false;
        }
        setError("");
        return true;
      },
    }));

    return (
      <div className="step">
        <h2>Tipologia dell'immobile</h2>

        <div className="card-group-input">
          {["Appartamento", "Villa", "Monolocale", "Attico"].map((text) => (
            <CardInput
              key={text}
              text={text}
              img={TestImage}
              isActive={selectedType === text}
              onClick={() => handleSelect(text)}
            />
          ))}
        </div>

        {error && <p className="error-message">{error}</p>}
      </div>
    );
  }
);

export default StepType;
