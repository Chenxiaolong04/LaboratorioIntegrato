import { useState, useEffect } from "react";

type Step = { number: number; title: string };

type StepProgressProps = { step: number };

const steps: Step[] = [
  { number: 1, title: "Indirizzo" },
  { number: 2, title: "Tipologia" },
  { number: 3, title: "Stato" },
  { number: 4, title: "Dati generali" },
  { number: 5, title: "Plus" },
  { number: 6, title: "Contatti" },
];

export default function StepProgress({ step }: StepProgressProps) {
  const [isMobile, setIsMobile] = useState(
    typeof window !== "undefined" ? window.innerWidth < 768 : true
  );

  useEffect(() => {
    const handleResize = () => setIsMobile(window.innerWidth < 768);
    window.addEventListener("resize", handleResize);
    return () => window.removeEventListener("resize", handleResize);
  }, []);

  let visibleSteps: Step[];
  let startIndex = 0;

  if (isMobile) {
    startIndex = Math.min(Math.max(0, step - 2), steps.length - 3);
    visibleSteps = steps.slice(startIndex, startIndex + 3);
  } else {
    visibleSteps = steps; // desktop: tutti visibili
  }

  return (
    <div className="steps-progress-slider">
      <div className="steps-container">
        {visibleSteps.map((s, index) => {
          const realIndex = isMobile ? startIndex + index : index;
          const isActive = realIndex === step - 1;
          const isCompleted = realIndex < step - 1;

          return (
            <div
              key={s.number}
              className={`step-card ${isActive ? "active" : ""} ${
                isCompleted ? "completed" : ""
              }`}
            >
              <div className="number">{s.number}</div>
              <p className={isActive ? "active" : ""}>{s.title}</p>
            </div>
          );
        })}
      </div>
    </div>
  );
}
