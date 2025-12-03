import React, { createContext, useContext, useState } from "react";
import type { ReactNode } from "react";

interface FormData {
  address: string;
  city: string;
  cap: string;
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

interface FormContextType {
  formData: FormData;
  setFormData: React.Dispatch<React.SetStateAction<FormData>>;
}

const FormContext = createContext<FormContextType | undefined>(undefined);

export const FormProvider = ({ children }: { children: ReactNode }) => {
  const [formData, setFormData] = useState<FormData>({
    address: "",
    city: "",
    cap: "",
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

export const useFormContext = (): FormContextType => {
  const context = useContext(FormContext);
  if (!context) {
    throw new Error("useFormContext must be used inside FormProvider");
  }
  return context;
};
