import { useEffect, useRef, useState } from "react";
import { FaBell } from "react-icons/fa";
import { Link } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

/**
 * Notification object interface.
 *
 * @interface Notification
 * @property {number} id - Unique identifier for the notification.
 * @property {string} message - Notification message text.
 * @property {string} time - Time string describing when the notification occurred.
 */
interface Notification {
  id: number;
  message: string;
  time: string;
}

/**
 * Navbar component for authenticated users.
 *
 * Displays role-based navigation links (Admin or Agent),
 * a responsive menu for mobile, and a notifications dropdown.
 *
 * @function NavbarPrivate
 * @returns {JSX.Element} A navigation bar tailored to the logged-in user's role with notifications.
 */
export default function NavbarPrivate() {
  const { user } = useAuth();
  const [menuOpen, setMenuOpen] = useState(false);
  const [notificationsOpen, setNotificationsOpen] = useState(false);
  const notificationsRef = useRef<HTMLDivElement>(null);

  const notifications: Notification[] = [
    { id: 1, message: "Nuova vendita completata", time: "1h fa" },
    { id: 2, message: "Nuovo incarico assegnato", time: "2h fa" },
    { id: 3, message: "Valutazione AI completata", time: "5h fa" },
  ];

  // Toggle body scroll when menu is open
  useEffect(() => {
    document.body.classList.toggle("no-scroll", menuOpen);
    return () => document.body.classList.remove("no-scroll");
  }, [menuOpen]);

  const handleLinkClick = () => setMenuOpen(false);

  // Close notifications dropdown when clicking outside
  useEffect(() => {
    function handleClickOutside(event: MouseEvent) {
      if (notificationsRef.current && !notificationsRef.current.contains(event.target as Node)) {
        setNotificationsOpen(false);
      }
    }
    document.addEventListener("mousedown", handleClickOutside);
    return () => document.removeEventListener("mousedown", handleClickOutside);
  }, []);

  // Determine role-based links
  const role = user?.role?.toLowerCase();
  const isAdmin = role === "admin";
  const isAgente = role === "agente";

  return (
    <nav className="navbar">
      <div className="navbar-container container">
        <div className="links-desktop-navbar">
          <ul>
            {isAdmin && (
              <>
                <li><Link to={"/admin"}>Dashboard</Link></li>
                <li><Link to={"/admin/evaluationsAI"}>Valutazioni AI</Link></li>
                <li><Link to={"/admin/assignments"}>Incarichi</Link></li>
                <li><Link to={"/"}>Immobili</Link></li>
                <li><Link to={"/admin/contracts"}>Vendite</Link></li>
                <li><Link to={"/admin/users"}>Gestione Utenti</Link></li>
                <li><Link to={"/admin/create-user"}>Registra Utenti</Link></li>
              </>
            )}
            {isAgente && (
              <>
                <li><Link to={"/agente"}>Dashboard</Link></li>
                <li><Link to={"/agente/contracts"}>I miei contratti</Link></li>
                <li><Link to={"/agente/assignments"}>I miei incarichi</Link></li>
                <li><Link to={"/agente/evaluationsAI"}>Le mie valutazioni AI</Link></li>
              </>
            )}
          </ul>
        </div>

        <button className={`menu-btn ${menuOpen ? "open" : ""}`} onClick={() => setMenuOpen(!menuOpen)}>
          <span className="span-hamburger"></span>
          <span className="span-hamburger"></span>
          <span className="span-hamburger"></span>
        </button>

        <div className={`menu-overlay ${menuOpen ? "active" : ""}`}>
          <ul>
            {isAdmin && (
              <>
                <li><Link to={"/admin"} onClick={handleLinkClick}>Dashboard</Link></li>
                <li><Link to={"/admin/evaluationsAI"} onClick={handleLinkClick}>Valutazioni AI</Link></li>
                <li><Link to={"/admin/assignments"} onClick={handleLinkClick}>Incarichi</Link></li>
                <li><Link to={"*"} onClick={handleLinkClick} >Immobili</Link></li>
                <li><Link to={"/admin/contracts"} onClick={handleLinkClick}>Vendite</Link></li>
                <li><Link to={"/admin/users"} onClick={handleLinkClick}>Gestione Utenti</Link></li>
                <li><Link to={"/admin/create-user"} onClick={handleLinkClick}>Registra Utenti</Link></li>
              </>
            )}
            {isAgente && (
              <>
                <li><Link to={"/agente"} onClick={handleLinkClick}>Dashboard</Link></li>
                <li><Link to={"/agente/contracts"} onClick={handleLinkClick}>I miei contratti</Link></li>
                <li><Link to={"/agente/assignments"} onClick={handleLinkClick}>I miei incarichi</Link></li>
                <li><Link to={"/agente/evaluationsAI"} onClick={handleLinkClick}>Le mie valutazioni AI</Link></li>
              </>
            )}
          </ul>
        </div>

        <div className="button-links">
          <div ref={notificationsRef} className="notification-wrapper">
            <button className="icon-btn" onClick={() => setNotificationsOpen(!notificationsOpen)}>
              <FaBell size={20} color="white" />
              {notifications.length > 0 && <span className="badge">{notifications.length}</span>}
            </button>
            {notificationsOpen && (
              <div className="notifications-dropdown">
                <ul>
                  {notifications.map((n) => (
                    <li key={n.id}>
                      <p>{n.message}</p>
                      <span className="time">{n.time}</span>
                    </li>
                  ))}
                </ul>
              </div>
            )}
          </div>
          <Link to={"/"}><img src="./logo.svg" alt="logo immobiliaris" /></Link>
        </div>
      </div>
    </nav>
  );
}
