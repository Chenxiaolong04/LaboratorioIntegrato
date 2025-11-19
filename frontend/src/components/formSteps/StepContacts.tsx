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
    <div className="step step-contact">
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

      <h3>Teniamo molto alla tua privacy e odiamo lo spam. Custodiremo con cura il tuo numero di telefono e non lo condivideremo con nessuno. Garantito.</h3>
      <h3>Con l'invio dei miei dati autorizzo il loro trattamento secondo la normativa GDPR e accetto la privacy policy e le condizioni di servizio</h3>
    </div>
  );
};

export default StepContacts;
