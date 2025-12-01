import { Link } from "react-router-dom";

/**
 * Header component for the homepage.
 *
 * Displays the main hero section with a title, subtitles, and
 * a call-to-action button that leads to the evaluation form.
 *
 * @function Header
 * @returns {JSX.Element} The header section with promotional text and a CTA link.
 */
export default function Header() {
  return (
    <header>
      <div className="container">
        <h1>Vendi casa in modo semplice e veloce</h1>
        <h2>Scopri quanto vale la tua casa in pochi clic</h2>
        <h2>Immobiliaris ti offre una valutazione gratuita in 72 ore.</h2>
        <Link to="/form" className="btn lightblu">
          Inizia la valutazione
        </Link>
      </div>
    </header>
  );
}
