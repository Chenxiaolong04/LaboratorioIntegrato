import { Link } from "react-router-dom";

export default function FormCheck() {
  const steps = [
    {
      number: "1",
      title: "Indirizzo immobile",
    },
    {
      number: "2",
      title: "Tipologia",
      desc: "Indica se è appartamento, villa, loft o attico.",
    },
    {
      number: "3",
      title: "Stato dell’immobile",
    },
    {
      number: "4",
      title: "Informazioni generali",
    },
    {
      number: "5",
      title: "Plus dell’immobile",
    },
    {
      number: "6",
      title: "Contatti",
    },
  ];

  return (
    <section className="form-check">
      <div className="logo-container">
        <img src="./logo.svg" alt="logo Immobiliaris" />
        <h3>Immobiliaris</h3>
      </div>

      <div className="info-text">
        <p>
          Attraverso pochi semplici step potrai avere una valutazione sul tuo
          immobile entro 72h!
        </p>
        <p>
          Avrai poi a disposizione un agente immobiliare per fare qualsiasi
          domanda e arrivare alla proposta di contratto con noi.
        </p>
        <p className="violet">
          *Servizio disponibile solo per immobili a Torino, Cuneo, Asti e
          Alessandria.
        </p>
      </div>

      <div className="action-btns">
        <Link to="/form" className="btn lightblu">
          Inzia ora
        </Link>
        <span>|</span>
        <p>oppure</p>
        <span>|</span>
        <Link to="/" className="btn lightblu">
          Torna alla home
        </Link>
      </div>

      <div className="steps-info">
        <h4>Cosa ti verrà chiesto</h4>
        <div className="steps-container">
          {steps.map((step) => (
            <div key={step.number} className="step-card">
              <div className="number">{step.number}</div>
              <h5>{step.title}</h5>
            </div>
          ))}
        </div>
      </div>
    </section>
  );
}
