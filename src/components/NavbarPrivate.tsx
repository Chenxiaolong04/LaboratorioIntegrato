import { useEffect, useState } from "react";
import { FaBell, FaUser } from "react-icons/fa"; // icone moderne
import Button from "./Button";

export default function NavbarPrivate() {
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
      {/* Links desktop */}
      <div className="links-desktop-navbar">
        <ul>
          <li>
            <a href="#">Dashboard</a>
          </li>
          <li>
            <a href="#">Valutazioni AI</a>
          </li>
          <li>
            <a href="#">Incarichi</a>
          </li>
          <li>
            <a href="#">Vendite</a>
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
            <a href="#" onClick={handleLinkClick}>
              Dashboard
            </a>
          </li>
          <li>
            <a href="#" onClick={handleLinkClick}>
              Valutazioni AI
            </a>
          </li>
          <li>
            <a href="#" onClick={handleLinkClick}>
              Incarichi
            </a>
          </li>
          <li>
            <a href="#" onClick={handleLinkClick}>
              Vendite
            </a>
          </li>
        </ul>
      </div>
      <div className="button-links">
        <a href="#" className="icon-btn">
          <FaBell size={20} />
          <span className="badge">3</span>
        </a>

        <a href="#" className="icon-btn">
          <FaUser size={20} />
        </a>
      </div>
    </nav>
  );
}
