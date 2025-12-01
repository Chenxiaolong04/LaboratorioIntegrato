import {
  FaCheckCircle,
} from "react-icons/fa";
import ContractImage from "../../assets/img/form-img/contract_sign.webp";

const SuccessPage = () => {
  return (
    <section className="success-page">
      <div className="success-content">
        <FaCheckCircle size={150} color="#348AA7" />

        <h2>Richiesta avvenuta con successo!</h2>

        <h4>Riceverai a breve un'email sui prossimi step</h4>
        <img src={ContractImage} alt="contratto firmato" />
      </div>
    </section>
  );
};

export default SuccessPage;
