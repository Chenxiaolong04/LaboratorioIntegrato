import { Link } from "react-router-dom";

/**
 * CTA (Call To Action) section component.
 * Displays a final message inviting the user to start the property evaluation process,
 * along with a button that redirects to the evaluation form page.
 *
 * @component
 * @returns {JSX.Element} A CTA section with a heading and a navigation button.
 */
export default function CTA() {
  return (
    <section className="cta-section">
      <div className="container">
        <h2>Ti abbiamo convinto?</h2>
        <Link to={"/form-check"} className="btn lightblu">
          Inizia ora la valutazione gratuita
        </Link>
      </div>
    </section>
  );
}
