import React, { useState, useImperativeHandle, forwardRef } from "react";
import { useFormContext } from "../../context/FormContext";
import CardInput from "../CardInput";
import NuovoImg from "../../assets/img/form-img/nuovo_immobile.webp";
import OttimoImg from "../../assets/img/form-img/ottimo_immobile.webp";
import BuonoImg from "../../assets/img/form-img/buono_immobile.webp";
import RistrutturareImg from "../../assets/img/form-img/ristrutturare_immobile.webp";

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
