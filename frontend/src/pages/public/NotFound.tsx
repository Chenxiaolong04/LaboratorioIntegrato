import { Link } from "react-router-dom";

export default function NotFound() {
  return (
    <div className="notfound-container">
      <img src="./logo.svg" alt="logo immobiliaris" />
      <h2>Immobiliaris</h2>
      <h1>404</h1>
      <p>Ops! La pagina che stai cercando non esiste.</p>

      <Link to="/" className="back-home">
        Torna alla Home
      </Link>
    </div>
  );
}
