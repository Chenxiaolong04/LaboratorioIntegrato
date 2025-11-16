import {
  FaFacebook,
  FaInstagram,
  FaXTwitter,
  FaWhatsapp,
  FaPhone,
  FaEnvelope,
  FaMapLocationDot,
} from "react-icons/fa6";
import logo from "../assets/img/logo.svg";

export default function Footer() {
  return (
    <footer className="footer" role="contentinfo" id="contatti">
      <div className="footer-container">
        <div className="brand">
          <div className="footer-logo">
            <img
              src={logo}
              alt="Logo Laboratorio Integrato"
              width="150"
              height="auto"
            />
          </div>
          <div className="socials" aria-label="Seguici sui social">
            <ul>
              <li>
                <a
                  href="https://facebook.com/"
                  aria-label="Visita la nostra pagina Facebook"
                  target="_blank"
                  rel="noopener noreferrer"
                >
                  <FaFacebook size={28} />
                </a>
              </li>
              <li>
                <a
                  href="https://instagram.com/"
                  aria-label="Visita il nostro profilo Instagram"
                  target="_blank"
                  rel="noopener noreferrer"
                >
                  <FaInstagram size={28} />
                </a>
              </li>
              <li>
                <a
                  href="https://twitter.com/"
                  aria-label="Visita il nostro profilo su X (Twitter)"
                  target="_blank"
                  rel="noopener noreferrer"
                >
                  <FaXTwitter size={28} />
                </a>
              </li>
              <li>
                <a
                  href="https://wa.me/3900000000"
                  aria-label="Contattaci su WhatsApp"
                  target="_blank"
                  rel="noopener noreferrer"
                >
                  <FaWhatsapp size={28} />
                </a>
              </li>
            </ul>
          </div>
        </div>
        <nav className="resources" aria-label="Link rapidi">
          <h3>Risorse</h3>
          <ul>
            <li>
              <a href="/">Home</a>
            </li>
            <li>
              <a href="/chi-siamo">Chi siamo</a>
            </li>
            <li>
              <a href="/contatti">Contatti</a>
            </li>
          </ul>
        </nav>
        <nav className="legal" aria-label="Documenti legali">
          <h3>Legal</h3>
          <ul>
            <li>
              <a href="/privacy-policy">Privacy Policy</a>
            </li>
            <li>
              <a href="/termini-condizioni">Condizioni d'uso</a>
            </li>
          </ul>
        </nav>
        <div className="contacts" aria-label="Contatti aziendali">
          <h3>Contatti</h3>
          <ul>
            <li>
              <FaPhone size={28} />
              <a href="tel:+3900000000" aria-label="Chiama +39 00000000">
                +39 00000000
              </a>
            </li>
            <li>
              <FaEnvelope size={28} />
              <a
                href="mailto:wneognwi@gmail.com"
                aria-label="Invia un'email a wneognwi@gmail.com"
              >
                wneognwi@gmail.com
              </a>
            </li>
            <li>
              <FaMapLocationDot size={28} />
              <a
                href="https://www.google.com/maps?q=Via+pippi+bro+12,+Milano"
                target="_blank"
                rel="noopener noreferrer"
                aria-label="Apri la posizione su Google Maps"
              >
                Via Jacopo Durandi 12, Milano
              </a>
            </li>
            <li>
              <FaMapLocationDot size={28} />
              <a
                href="https://www.google.com/maps?q=Via+pippi+bro+12,+Milano"
                target="_blank"
                rel="noopener noreferrer"
                aria-label="Apri la posizione su Google Maps"
              >
                Via Jacopo Durandi 12, Torino
              </a>
            </li>
          </ul>
        </div>
      </div>

      {/* Copyright */}
      <p className="copyright">
        © {new Date().getFullYear()} Immobiliaris - Made by SixWave
      </p>
    </footer>
  );
}
