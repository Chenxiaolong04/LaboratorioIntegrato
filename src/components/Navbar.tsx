import { useState } from "react";

export default function Navbar() {
  const [menuOpen, setMenuOpen] = useState(false);

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
          <li><button className="btn-cta-navbar">Valuta la tua casa</button></li>
        </ul>
      </div>

      {/* Hamburger button mobile */}
      <button
        className={`menu-btn ${menuOpen ? "open" : ""}`}
        onClick={() => setMenuOpen(!menuOpen)}
        aria-label="Menu"
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
            <button className="btn-cta-navbar" onClick={handleLinkClick}>
              Valuta la tua casa
            </button>
          </li>
        </ul>
      </div>
    </nav>
  );
}
