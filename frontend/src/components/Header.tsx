import { Link } from "react-router-dom";

export default function Header() {
  return (
    <header>
      <div className="container">
        <h1>Vendi casa in modo semplice e veloce</h1>
        <h2>
          Scopri quanto vale la tua casa a <strong>Torino</strong>, <strong>Cuneo</strong>, <strong>Asti</strong> o <strong>Alessandria</strong>
        </h2>
        <h2>Immobiliaris ti offre una valutazione gratuita in 72 ore.</h2>
        <Link to="/form-check" className="btn lightblu">
          Inizia la valutazione
        </Link>
      </div>
    </header>
  );
}
