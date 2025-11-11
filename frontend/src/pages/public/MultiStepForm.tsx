import { useRef, useState } from "react";
import { Link } from "react-router-dom";
import TestImage from "../../assets/img/test-image.jpg";
import StepLocation, {
  type StepLocationRef,
} from "../../components/formSteps/StepLocation";
import StepContacts from "../../components/formSteps/StepContacts";
import StepSuccess from "../../components/formSteps/StepSuccess";
import Button from "../../components/Button";
import { FormProvider } from "../../context/FormContext";

export default function MultiStepForm() {
  const [error, setError] = useState("");
  const [step, setStep] = useState(0);

  const stepLocationRef = useRef<StepLocationRef>(null);
  const totalSteps = 3;

  const handleNext = () => {
    if (step === 0) {
      const valid = stepLocationRef.current?.validate();
      if (!valid) return;
    }

    if (step < totalSteps - 1) setStep(step + 1);
  };

  const handlePrev = () => {
    if (step > 0) setStep(step - 1);
  };

  const handleSubmit = () => {
    console.log("Invio dati al backend...");
    setStep(2);
  };

  return (
    <FormProvider>
      <section className="form-container">
        <div className="brand">
          <Link to="/">
            <img className="logo" src={TestImage} alt="" />
          </Link>
        </div>

        {step < totalSteps - 1 && (
          <div className="progress">
            <div
              className="progress-bar"
              style={{ width: `${((step + 1) / (totalSteps - 1)) * 100}%` }}
            ></div>
          </div>
        )}

        <form
          onSubmit={(e) => {
            e.preventDefault();
            if (step === totalSteps - 2) handleSubmit();
            else handleNext();
          }}
        >
          {step === 0 && (
            <StepLocation
              ref={stepLocationRef}
              error={error}
              setError={setError}
            />
          )}

          {step === 1 && <StepContacts />}

          {step === 2 && <StepSuccess />}

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
      </section>
    </FormProvider>
  );
}
