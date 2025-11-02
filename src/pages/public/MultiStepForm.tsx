import { useState } from "react";
import StepLocation from "../../components/formSteps/StepLocation";
import Button from "../../components/Button";
import { Link } from "react-router-dom";

export default function MultiStepForm() {

  const [step, setStep] = useState(1);
  const totalSteps = 4;

  const handleNext = () => {
    if (step < totalSteps) setStep(step + 1);
  };

  return (
    <section className="form-container">
      <h1 className="brand"><Link to={'/'}>Immobiliaris</Link></h1>

      {/* Progress bar */}
      <div className="progress">
        <div
          className="progress-bar"
          style={{ width: `${(step / totalSteps) * 100}%` }}
        ></div>
      </div>

      <form>
        {/* Step 1 - Posizione immobile */}
        {step === 1 && <StepLocation />}
        <div className="step-control">
          {step > 1 && <Button onClick={() => setStep(step - 1)}>Torna indietro</Button>}
          <Button className="btn-next lightblu" onClick={handleNext}>
            Avanti
          </Button>
        </div>
      </form>
    </section>
  );
}
