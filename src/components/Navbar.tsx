import { useState, useEffect } from "react";
import Button from "./Button";

export default function Navbar() {
  const [menuOpen, setMenuOpen] = useState(false);

  useEffect(() => {
    // Aggiunge o rimuove la classe "no-scroll" sul body
    document.body.classList.toggle("no-scroll", menuOpen);

    // Pulizia quando il componente viene smontato
    return () => {
      document.body.classList.remove("no-scroll");
    };
  }, [menuOpen]);

  // funzione provvisoria tanto userò React Router
  const handleLinkClick = () => {
    setMenuOpen(false); // Chiude il menu quando clicchi un link
  };

  return (
    <nav className="navbar">
      <h2 className="logo">Immobiliaris</h2>

      {/* Links desktop */}
      <div className="links-desktop-navbar">
        <ul>
          <li><a href="#">Home</a></li>
          <li><a href="#">Chi siamo</a></li>
          <li><a href="#">Contatti</a></li>
          <li><Button className="btn-cta-navbar lightblu">Valuta la tua casa</Button></li>
        </ul>
      </div>

      {/* Hamburger button mobile */}
      <button
        className={`menu-btn ${menuOpen ? "open" : ""}`}
        onClick={() => setMenuOpen(!menuOpen)}
      >
        <span className="span-hamburger"></span>
        <span className="span-hamburger"></span>
        <span className="span-hamburger"></span>
      </button>

      {/* Overlay menu mobile */}
      <div className={`menu-overlay ${menuOpen ? "active" : ""}`}>
        <ul>
          <li><a href="#" onClick={handleLinkClick}>Home</a></li>
          <li><a href="#" onClick={handleLinkClick}>Chi siamo</a></li>
          <li><a href="#" onClick={handleLinkClick}>Contatti</a></li>
          <li>
            <Button className="btn-cta-navbar lightblu" onClick={handleLinkClick}>
              Valuta la tua casa
            </Button>
          </li>
        </ul>
      </div>
    </nav>
  );
}
