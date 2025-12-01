import { useState, useEffect } from "react";
import { Link } from "react-router-dom";

/**
 * Navbar component for public users (non-authenticated).
 *
 * Displays the brand logo, navigation links, a CTA button, and a responsive
 * mobile menu. Toggles body scroll when the mobile menu is open.
 *
 * @function NavbarPublic
 * @returns {JSX.Element} A responsive navigation bar for public pages.
 */
export default function NavbarPublic() {
  const [menuOpen, setMenuOpen] = useState(false);

  // Toggle body scroll when the mobile menu is open
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
      <div className="navbar-container container">
        <div className="brand">
          <Link to={"/"}>
            <img src='./logo.svg' alt="Logo Immobiliaris" title="Logo Immobiliaris" />
          </Link>
          <h2>Immobiliaris</h2>
        </div>
        <div className="links-desktop-navbar">
          <ul>
            <li>
              <a href="/" title="Go to homepage">
                Home
              </a>
            </li>
            <li>
              <a href="#chi-siamo" title="Learn about us">
                Chi siamo
              </a>
            </li>
            <li>
              <a href="#contatti" title="Contact us">
                Contatti
              </a>
            </li>
            <li>
              <Link to={"/form"} className="btn btn-cta-navbar lightblu">
                Valuta la tua casa
              </Link>
            </li>
          </ul>
        </div>
        <button
          className={`menu-btn ${menuOpen ? "open" : ""}`}
          onClick={() => setMenuOpen(!menuOpen)}
        >
          <span className="span-hamburger"></span>
          <span className="span-hamburger"></span>
          <span className="span-hamburger"></span>
        </button>
        <div className={`menu-overlay ${menuOpen ? "active" : ""}`}>
          <ul>
            <li>
              <a href="/" title="Go to homepage" onClick={handleLinkClick}>
                Home
              </a>
            </li>
            <li>
              <a href="#chi-siamo" title="Learn about us" onClick={handleLinkClick}>
                Chi siamo
              </a>
            </li>
            <li>
              <a href="#contatti" title="Contact us" onClick={handleLinkClick}>
                Contatti
              </a>
            </li>
            <li>
              <Link to={"/form"}
                className="btn btn-cta-navbar lightblu"
                onClick={handleLinkClick}
              >
                Valuta la tua casa
              </Link>
            </li>
          </ul>
        </div>
      </div>
    </nav>
  );
}
