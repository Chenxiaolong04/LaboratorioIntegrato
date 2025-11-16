import React, { useState, useImperativeHandle, forwardRef } from "react";
import { useFormContext } from "../../context/FormContext";
import CardInput from "../CardInput";
import AppartamentoImg from "../../assets/img/form-img/appartamento.webp";
import VillaImg from "../../assets/img/form-img/villa.webp";
import LoftImg from "../../assets/img/form-img/loft.webp";
import AtticoImg from "../../assets/img/form-img/attico.webp";

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

    const types = [
      { text: "Appartamento", img: AppartamentoImg },
      { text: "Villa", img: VillaImg },
      { text: "Loft", img: LoftImg },
      { text: "Attico", img: AtticoImg },
    ];

    return (
      <div className="step">
        <h2>Tipologia dell'immobile</h2>

        <div className="card-group-input">
          {types.map(({ text, img }) => (
            <CardInput
              key={text}
              text={text}
              img={img}
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
