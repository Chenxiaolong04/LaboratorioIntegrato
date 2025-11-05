import React from "react";
import InputGroup from "../InputGroup";
import { useFormContext } from "../../context/FormContext";

const StepContacts: React.FC = () => {
  const { formData, setFormData } = useFormContext();

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setFormData((prev) => ({
      ...prev,
      [e.target.name]: e.target.value,
    }));
  };

  return (
    <div className="step">
      <h2>Dati di contatto</h2>

      <InputGroup
        label="Nome"
        name="name"
        placeholder="Es: Mario"
        autoComplete="given-name"
        value={formData.name || ""}
        required
        onChange={handleChange}
      />

      <InputGroup
        label="Cognome"
        name="surname"
        placeholder="Es: Rossi"
        autoComplete="family-name"
        value={formData.surname || ""}
        required
        onChange={handleChange}
      />

      <InputGroup
        label="Email"
        name="email"
        type="email"
        placeholder="Es: mario.rossi@email.it"
        autoComplete="email"
        value={formData.email || ""}
        required
        onChange={handleChange}
      />

      <InputGroup
        label="Telefono"
        name="phone"
        type="tel"
        placeholder="Es: 3331234567"
        autoComplete="tel"
        value={formData.phone || ""}
        required
        onChange={handleChange}
      />
    </div>
  );
};

export default StepContacts;
