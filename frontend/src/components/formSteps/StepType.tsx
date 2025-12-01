import React, { useState, useImperativeHandle, forwardRef } from "react";
import { useFormContext } from "../../context/FormContext";
import CardInput from "../CardInput";
import AppartamentoImg from "../../assets/img/form-img/appartamento.webp";
import VillaImg from "../../assets/img/form-img/villa.webp";
import LoftImg from "../../assets/img/form-img/loft.webp";
import AtticoImg from "../../assets/img/form-img/attico.webp";

export interface StepTypeRef {
  /**
   * Validates the current step.
   * @returns {boolean} True if the step is valid, otherwise false.
   */
  validate: () => boolean;
}

interface StepTypeProps {
  /** Error message displayed when no type is selected */
  error: string;

  /** Function used to update the error message */
  setError: React.Dispatch<React.SetStateAction<string>>;
}

/**
 * StepType Component
 *
 * This component allows the user to select the type of property
 * they want to evaluate. It uses card-based inputs to visually
 * present each option and stores the selected value in the
 * shared form context.
 *
 * The component also exposes a `validate` method through `forwardRef`
 * to ensure that a type has been selected before proceeding.
 *
 * @component
 * @param {StepTypeProps} props - The props containing the error state and setter.
 * @param {React.Ref<StepTypeRef>} ref - Ref exposing the validate() method.
 */
const StepType = forwardRef<StepTypeRef, StepTypeProps>(
  ({ error, setError }, ref) => {
    const { formData, setFormData } = useFormContext();
    const [selectedType, setSelectedType] = useState(formData.type || "");

    /**
     * Handles selecting a property type.
     * Updates both local component state and global form context.
     *
     * @param {string} value - The selected property type.
     */
    const handleSelect = (value: string) => {
      setSelectedType(value);
      setFormData((prev) => ({ ...prev, type: value }));
      setError("");
    };

    // Expose validation logic to parent component
    useImperativeHandle(ref, () => ({
      /**
       * Ensures that the user has selected a property type.
       *
       * @returns {boolean} True if valid, false otherwise.
       */
      validate: () => {
        if (!selectedType) {
          setError("Seleziona una tipologia di immobile");
          return false;
        }
        setError("");
        return true;
      },
    }));

    // Available property types
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
