import React from "react";
import InputGroup from "../InputGroup";
import { useFormContext } from "../../context/FormContext";

/**
 * StepContacts Component
 *
 * @description
 * This component renders the contact information step of a multi-step form.
 * It uses the `FormContext` to access and update the shared form state.
 * The component includes input fields for first name, last name, email, and phone number.
 *
 * @component
 * @returns {JSX.Element} The JSX structure for the contact information form step.
 */
const StepContacts: React.FC = () => {
  const { formData, setFormData } = useFormContext();

  /**
   * Handles input change events and updates the form state accordingly.
   *
   * @param {React.ChangeEvent<HTMLInputElement>} e - The change event triggered by an input field.
   * @returns {void}
   */
  const handleChange = (e: React.ChangeEvent<HTMLInputElement>): void => {
    setFormData((prev) => ({
      ...prev,
      [e.target.name]: e.target.value,
    }));
  };

  return (
    <div className="step step-contact">
      <h2>Contact Information</h2>

      <InputGroup
        label="First Name"
        name="name"
        placeholder="e.g., Mario"
        autoComplete="given-name"
        value={formData.name || ""}
        required
        onChange={handleChange}
      />

      <InputGroup
        label="Last Name"
        name="surname"
        placeholder="e.g., Rossi"
        autoComplete="family-name"
        value={formData.surname || ""}
        required
        onChange={handleChange}
      />

      <InputGroup
        label="Email"
        name="email"
        type="email"
        placeholder="e.g., mario.rossi@email.it"
        autoComplete="email"
        value={formData.email || ""}
        required
        onChange={handleChange}
      />

      <InputGroup
        label="Phone"
        name="phone"
        type="tel"
        placeholder="e.g., 3331234567"
        autoComplete="tel"
        value={formData.phone || ""}
        required
        onChange={handleChange}
      />

      <h3>
        We care deeply about your privacy and hate spam. We will protect your phone number and never share it with anyone. Guaranteed.
      </h3>
      <h3>
        By submitting my data, I authorize its processing according to GDPR regulations and accept the privacy policy and terms of service.
      </h3>
    </div>
  );
};

export default StepContacts;
