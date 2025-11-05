import { Link } from "react-router-dom";
import { FaCheckCircle, FaEnvelope } from "react-icons/fa";


const SuccessPage = () => {
  return (
    <section className="success-page">

      <div className="success-content">
        <FaCheckCircle size={200} color="blue"/>

        <h2>Richiesta avvenuta con successo!</h2>
        <p className="subtitle">Ecco i tuoi prossimi step:</p>

        <FaEnvelope size={100}/>

        <ul className="steps-list">
          <li>I dati da te inseriti;</li>
          <li>Una stima della fascia di prezzo;</li>
          <li>Ulteriori considerazioni o problemi da gestire;</li>
          <li>
            Un contatto con un nostro agente immobiliare a tuo servizio;
          </li>
        </ul>

        <div className="arrow-down">↓</div>
      </div>
    </section>
  );
};

export default SuccessPage;
