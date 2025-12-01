import { useRef, useState } from "react";
import { Link } from "react-router-dom";
import StepLocation, {
  type StepLocationRef,
} from "../../components/formSteps/StepLocation";
import StepContacts from "../../components/formSteps/StepContacts";
import StepSuccess from "../../components/formSteps/StepSuccess";
import Button from "../../components/Button";
import { FormProvider } from "../../context/FormContext";
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

/**
 * MultiStepForm component.
 * Manages the state and navigation for a multi-step property evaluation form.
 * It uses refs to call validation methods on child step components.
 * @returns {JSX.Element} The Multi-Step Form UI.
 */
export default function MultiStepForm() {
  /**
   * State for general form error messages (mainly used for StepLocation).
   * @type {[string, function(string): void]}
   */
  const [error, setError] = useState("");

  /**
   * State for errors specific to the StepType component.
   * @type {[string, function(string): void]}
   */
  const [errorType, setErrorType] = useState("");

  /**
   * State for errors specific to the StepStatus component.
   * @type {[string, function(string): void]}
   */
  const [errorStatus, setErrorStatus] = useState("");

  /**
   * State for errors specific to the StepGeneral component.
   * @type {[string, function(string): void]}
   */
  const [errorGeneral, setErrorGeneral] = useState("");

  /**
   * State tracking the current step index of the form.
   * Steps are 0-indexed.
   * @type {[number, function(number): void]}
   */
  const [step, setStep] = useState(0);

  /**
   * Ref for accessing methods (like validate) on the StepLocation component.
   * @type {React.RefObject<StepLocationRef>}
   */
  const stepLocationRef = useRef<StepLocationRef>(null);

  /**
   * Ref for accessing methods (like validate) on the StepType component.
   * @type {React.RefObject<StepTypeRef>}
   */
  const stepTypeRef = useRef<StepTypeRef>(null);

  /**
   * Ref for accessing methods (like validate) on the StepStatus component.
   * @type {React.RefObject<StepStatusRef>}
   */
  const stepStatusRef = useRef<StepStatusRef>(null);

  /**
   * Ref for accessing methods (like validate) on the StepGeneral component.
   * @type {React.RefObject<StepGeneralRef>}
   */
  const stepGeneralRef = useRef<StepGeneralRef>(null);

  /**
   * Ref for accessing methods (like validate) on the StepPlus component.
   * @type {React.RefObject<StepPlusRef>}
   */
  const stepPlusRef = useRef<StepPlusRef>(null);

  /**
   * Total number of steps in the form (7 steps: 0 through 6).
   * @type {number}
   */
  const totalSteps = 7;

  /**
   * Handles moving to the next step.
   * Triggers validation for the current step before proceeding.
   * If validation fails, navigation is stopped.
   */
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

    // Only proceed if not on the final step (StepSuccess is totalSteps - 1)
    if (step < totalSteps - 1) setStep(step + 1);
  };

  /**
   * Handles moving to the previous step.
   * Clears relevant error states upon moving back.
   */
  const handlePrev = () => {
    // Clear errors when moving back, though specific step errors are also managed locally
    setError("");
    setErrorType("");
    if (step > 0) setStep(step - 1);
  };

  /**
   * Handles the final submission logic (currently just moves to the success step).
   */
  const handleSubmit = () => {
    // Logic for final API submission would go here (omitted for brevity)
    setStep(6); // Move to StepSuccess (step index 6)
  };

  return (
    <FormProvider>
      <section className="form-container">
        <div className="container">
          <div className="brand">
            <Link to="/">
              <img className="logo" src="./logo.svg" alt="Logo Immobiliaris" />
            </Link>
            <h2>Immobiliaris</h2>
          </div>
          {/* Progress bar displayed only for input steps (not the final success step) */}
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
              // If currently on the last input step (StepContacts, totalSteps - 2 = 5), call handleSubmit.
              // Otherwise, proceed to the next step and trigger validation.
              if (step === totalSteps - 2) handleSubmit();
              else handleNext();
            }}
          >
            {/* Conditional rendering of form steps based on the current step state */}
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
            {step === 4 && (
              <StepPlus
                ref={stepPlusRef}
              />
            )}
            {step === 5 && <StepContacts />}
            {step === 6 && <StepSuccess />}
            
            {/* Navigation controls displayed only for input steps */}
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
    </FormProvider>
  );
}