import { useState, useEffect } from "react";
import logo from '../assets/img/logo.svg'
import Button from "./Button";
import { Link } from "react-router-dom";

export default function NavbarPublic() {
  const [menuOpen, setMenuOpen] = useState(false);

  useEffect(() => {
    document.body.classList.toggle("no-scroll", menuOpen);

    return () => {
      document.body.classList.remove("no-scroll");
    };
  }, [menuOpen]);

  const handleLinkClick = () => {
    setMenuOpen(false);
  };

  return (
    <nav className="navbar">
      <Link to={'/'}><img src={logo} alt="Logo Immobiliaris" title="Logo Immobiliaris" /></Link>

      {/* Links desktop */}
      <div className="links-desktop-navbar">
        <ul>
          <li>
            <a href="#" title="Vai alla homepage" >Home</a>
          </li>
          <li>
            <a href="#" title="Scopri chi siamo" >Chi siamo</a>
          </li>
          <li>
            <a href="#" title="Contattaci" >Contatti</a>
          </li>
          <li>
            <Button className="btn-cta-navbar lightblu">
              Valuta la tua casa
            </Button>
          </li>
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
          <li>
            <a href="#" title="Vai alla homepage" onClick={handleLinkClick}>
              Home
            </a>
          </li>
          <li>
            <a href="#" title="Scopri chi siamo" onClick={handleLinkClick}>
              Chi siamo
            </a>
          </li>
          <li>
            <a href="#" title="Contattaci" onClick={handleLinkClick}>
              Contatti
            </a>
          </li>
          <li>
            <Button
              className="btn-cta-navbar lightblu"
              onClick={handleLinkClick}
            >
              Valuta la tua casa
            </Button>
          </li>
        </ul>
      </div>
    </nav>
  );
}
