import {
  FaCheckCircle,
  FaEnvelope,
  FaArrowDown,
  FaPhone,
  FaFileContract,
} from "react-icons/fa";
import TestImage from "../../assets/img/test-image.jpg";
import { useFormContext } from "../../context/FormContext";

const SuccessPage = () => {
  const {formData} = useFormContext()
  return (
    <section className="success-page">
      <pre>{JSON.stringify(formData, null, 2)}</pre>
      <div className="success-content">
        <FaCheckCircle size={150} color="blue" />

        <h2>Richiesta avvenuta con successo!</h2>
        <h3 className="subtitle">Ecco i tuoi prossimi step:</h3>

        <FaEnvelope size={100} />

        <h4>Riceverai a breve un'email riepilogo contenente:</h4>
        <ul className="steps-list">
          <li>I dati da te inseriti;</li>
          <li>Una stima della fascia di prezzo;</li>
          <li>Ulteriori considerazioni o problemi;</li>
          <li>Un contatto con un nostro agente immobiliare a tuo servizio;</li>
        </ul>

        <FaArrowDown size={32} />

        <FaPhone size={75} />
        <h4>
          Avrai la possibilità di analizzare e fare decisioni riguardo
          all'autovalutazione insieme all'aiuto di un agente immobiliare
          qualificato in grado di rispondere a tutti i tuoi quesiti e portarti
          alla proposta di vendità esclusiva da noi.
        </h4>

        <FaArrowDown size={32} />

        <FaFileContract size={75} />
        <h4>
          Una volta soddisfatto bisognerà firmare il contratto ricevuto via
          email dopo la conferma con l'agente immobiliare
        </h4>

        <FaArrowDown size={32} />
        <img src={TestImage} alt="" />
      </div>
    </section>
  );
};

export default SuccessPage;
