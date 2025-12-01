import React, { createContext, useContext, useState } from "react"; 
import type { ReactNode } from "react";

/**
 * Interface for the form data.
 */
interface FormData {
  address: string;
  name: string;
  surname: string;
  email: string;
  phone: string;
  type: string;
  status: string;
  surface: string;
  floor: string;
  rooms: string;
  bathrooms: string;
  heating: string;
  features: string[];
}

/**
 * Interface for the form context.
 */
interface FormContextType {
  /** Current form data */
  formData: FormData;
  /** Setter function to update form data */
  setFormData: React.Dispatch<React.SetStateAction<FormData>>;
}

/** Context for form data */
const FormContext = createContext<FormContextType | undefined>(undefined);

/**
 * Provider component for the FormContext.
 *
 * @param {Object} props
 * @param {ReactNode} props.children - Child components that will have access to the form context.
 * @returns {JSX.Element} FormContext provider wrapping its children.
 */
export const FormProvider = ({ children }: { children: ReactNode }) => {
  const [formData, setFormData] = useState<FormData>({
    address: "",
    name: "",
    surname: "",
    email: "",
    phone: "",
    type: "",
    status: "",
    surface: "",
    floor: "",
    rooms: "",
    bathrooms: "",
    heating: "",
    features: [],
  });

  return (
    <FormContext.Provider value={{ formData, setFormData }}>
      {children}
    </FormContext.Provider>
  );
};

/**
 * Hook to access the FormContext.
 *
 * @throws {Error} If used outside of FormProvider.
 * @returns {FormContextType} The current form data and setter function.
 */
export const useFormContext = (): FormContextType => {
  const context = useContext(FormContext);
  if (!context) {
    throw new Error("useFormContext must be used inside FormProvider");
  }
  return context;
};
