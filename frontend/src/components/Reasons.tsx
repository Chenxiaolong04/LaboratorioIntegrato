import { BsFastForwardCircleFill } from "react-icons/bs";
import { FaMapLocationDot } from "react-icons/fa6";
import { GrServices } from "react-icons/gr";
import { FaBrain } from "react-icons/fa";
import { FaLongArrowAltRight } from "react-icons/fa";
import Button from "./Button";

export default function Reasons() {
  return (
    <section className="reasons">
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
        <div className="steps-button">
          <Button className="lightblu">
            Valuta ora <FaLongArrowAltRight />
          </Button>
        </div>
      </div>
    </section>
  );
}
