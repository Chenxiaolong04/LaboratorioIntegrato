import {
  FaCheckCircle,
  // FaEnvelope,
  // FaArrowDown,
  // FaPhone,
  // FaFileContract,
} from "react-icons/fa";
import ContractImage from "../../assets/img/form-img/contract_sign.webp";

/**
 * SuccessPage Component
 *
 * This component is displayed after the user successfully completes
 * their request. It shows a confirmation icon, a success message,
 * and an illustrative image related to contract signing.
 *
 * All additional elements (future steps, icons, information text) are
 * currently commented out but kept for potential future use.
 *
 * @component
 */
const SuccessPage = () => {
  return (
    <section className="success-page">
      <div className="success-content">
        {/* Success icon */}
        <FaCheckCircle size={150} color="#348AA7" />

        {/* Main success message */}
        <h2>Richiesta avvenuta con successo!</h2>

        {/* User feedback message */}
        <h4>Riceverai a breve un'email sui prossimi step</h4>

        {/* Illustration related to contract signing */}
        <img src={ContractImage} alt="contratto firmato" />
      </div>
    </section>
  );
};

export default SuccessPage;
