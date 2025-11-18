import { BsFastForwardCircleFill } from "react-icons/bs";
import { FaMapLocationDot } from "react-icons/fa6";
import { GrServices } from "react-icons/gr";
import { FaBrain } from "react-icons/fa";
import { FaLongArrowAltRight } from "react-icons/fa";
import { Link } from "react-router-dom";

export default function Reasons() {
  return (
    <section className="reasons" id="chi-siamo">
      <div className="container">
        <h2>Perché sceglierci</h2>
        <div className="reasons-container">
          <div className="cards-container">
            <div className="card">
              <BsFastForwardCircleFill className="icon" />
              <h4>Valutazioni rapide e trasparenti</h4>
            </div>
            <div className="card">
              <FaMapLocationDot className="icon" />
              <h4>Team locale esperto</h4>
            </div>
            <div className="card">
              <GrServices className="icon" />
              <h4>Servizio esclusivo e personalizzato</h4>
            </div>
            <div className="card">
              <FaBrain className="icon" />
              <h4>Zero stress nella vendita</h4>
            </div>
          </div>
          <h3>
            Immobiliaris è un'agenzia immobiliare radicata nel territorio
            piemontese e parte del gruppo Indomus. Da anni aiutiamo i
            proprietari a vendere casa in modo rapido e sicuro. Uniamo
            l'esperienza locale alla tecnologia digitale per offrire un servizio
            moderno, personalizzato e vicino alle persone.
          </h3>
          <div className="steps-button">
            <Link to={'/form'} className="btn lightblu">
              Valuta ora <FaLongArrowAltRight />
            </Link>
          </div>
        </div>
      </div>
    </section>
  );
}
