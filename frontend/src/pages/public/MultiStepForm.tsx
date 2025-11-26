import { useRef, useState } from "react";
import { Link } from "react-router-dom";
import StepLocation, {
  type StepLocationRef,
} from "../../components/formSteps/StepLocation";
import StepContacts from "../../components/formSteps/StepContacts";
import StepSuccess from "../../components/formSteps/StepSuccess";
import Button from "../../components/Button";
import { useFormContext } from "../../context/FormContext";
import StepType, {
  type StepTypeRef,
} from "../../components/formSteps/StepType";
import StepStatus, {
  type StepStatusRef,
} from "../../components/formSteps/StepStatus";
import type { StepGeneralRef } from "../../components/formSteps/StepGenerals";
import StepGeneral from "../../components/formSteps/StepGenerals";
import StepPlus, {
  type StepPlusRef,
} from "../../components/formSteps/StepPlus";
import StepProgress from "../../components/StepProgress";
import { saveImmobile, type SaveImmobileBody } from "../../services/api";

export default function MultiStepForm() {
  const [error, setError] = useState("");
  const [errorType, setErrorType] = useState("");
  const [errorStatus, setErrorStatus] = useState("");
  const [errorGeneral, setErrorGeneral] = useState("");
  const [step, setStep] = useState(0);

  const { formData } = useFormContext();

  const stepLocationRef = useRef<StepLocationRef>(null);
  const stepTypeRef = useRef<StepTypeRef>(null);
  const stepStatusRef = useRef<StepStatusRef>(null);
  const stepGeneralRef = useRef<StepGeneralRef>(null);
  const stepPlusRef = useRef<StepPlusRef>(null);
  const totalSteps = 7;

  const handleNext = () => {
    if (step === 0) {
      const valid = stepLocationRef.current?.validate();
      if (!valid) return;
    }

    if (step === 1) {
      const valid = stepTypeRef.current?.validate();
      if (!valid) return;
    }

    if (step === 2) {
      const valid = stepStatusRef.current?.validate();
      if (!valid) return;
    }

    if (step === 3) {
      const valid = stepGeneralRef.current?.validate();
      if (!valid) return;
    }

    if (step === 4) {
      const valid = stepPlusRef.current?.validate();
      if (!valid) return;
    }

    if (step < totalSteps - 1) setStep(step + 1);
  };

  const handlePrev = () => {
    setError("");
    setErrorType("");
    if (step > 0) setStep(step - 1);
  };

  const handleFinalSubmit = async () => {
    console.log("formData al submit:", formData);
    try {
      const featuresBool = {
        ascensore: formData.features.includes("Ascensore"),
        garage: formData.features.includes("Box garage"),
        giardino: formData.features.includes("Giardino privato"),
        balcone: formData.features.includes("Balcone"),
        terrazzo: formData.features.includes("Terrazzo"),
        cantina: formData.features.includes("Cantina"),
      };

      const body: SaveImmobileBody = {
        via: formData.address,
        citta: formData.city,
        cap: formData.cap,
        tipologia: formData.type,
        metratura: Number(formData.surface),
        condizioni: formData.status,
        stanze: Number(formData.rooms),
        bagni: Number(formData.bathrooms),
        riscaldamento: formData.heating,
        piano: Number(formData.floor),

        ...featuresBool,

        nomeProprietario: formData.name,
        cognomeProprietario: formData.surname,
        emailProprietario: formData.email,
        telefonoProprietario: formData.phone,
      };

      const res = await saveImmobile(body);
      console.log("Immobile salvato:", res);

      setStep(6);
    } catch (err: unknown) {
      console.error(err);
      alert("Errore durante il salvataggio dell'immobile");
    }
  };

  return (
    <section className="form-container">
      <div className="container">
        <div className="brand-steps-container">
          <div className="brand">
            <div className="brand-logo">
              <Link to="/">
                <img
                  className="logo"
                  src="./logo.svg"
                  alt="Logo Immobiliaris"
                />
              </Link>
              <h2>Immobiliaris</h2>
            </div>
            <Link to="/" className="btn lightblu">
              Torna alla home
            </Link>
          </div>
          {step < totalSteps - 1 && <StepProgress step={step + 1} />}
        </div>
        <form
          onSubmit={(e) => {
            e.preventDefault();
            if (step === totalSteps - 2) {
              handleFinalSubmit();
            } else {
              handleNext();
            }
          }}
        >
          {step === 0 && (
            <StepLocation
              ref={stepLocationRef}
              error={error}
              setError={setError}
            />
          )}
          {step === 1 && (
            <StepType
              ref={stepTypeRef}
              error={errorType}
              setError={setErrorType}
            />
          )}
          {step === 2 && (
            <StepStatus
              ref={stepStatusRef}
              error={errorStatus}
              setError={setErrorStatus}
            />
          )}
          {step === 3 && (
            <StepGeneral
              ref={stepGeneralRef}
              error={errorGeneral}
              setError={setErrorGeneral}
            />
          )}
          {step === 4 && <StepPlus ref={stepPlusRef} />}
          {step === 5 && <StepContacts />}
          {step === 6 && <StepSuccess />}
          {step < totalSteps - 1 && (
            <div className="step-control">
              {step > 0 && (
                <Button type="button" onClick={handlePrev}>
                  Torna indietro
                </Button>
              )}
              <Button type="submit" className="btn-next lightblu">
                {step === totalSteps - 2 ? "Invia" : "Avanti"}
              </Button>
            </div>
          )}
        </form>
      </div>
    </section>
  );
}
