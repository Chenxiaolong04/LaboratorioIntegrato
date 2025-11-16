import { useEffect, useRef, useState } from "react";
import { FaBell } from "react-icons/fa";
import { Link } from "react-router-dom";
import logo from "../assets/img/logo.svg";

interface Notification {
  id: number;
  message: string;
  time: string;
}

export default function NavbarPrivate() {
  const [menuOpen, setMenuOpen] = useState(false);
  const [notificationsOpen, setNotificationsOpen] = useState(false);
  const notificationsRef = useRef<HTMLDivElement>(null);

  const notifications: Notification[] = [
    { id: 1, message: "Nuova vendita completata", time: "1h fa" },
    { id: 2, message: "Nuovo incarico assegnato", time: "2h fa" },
    { id: 3, message: "Valutazione AI completata", time: "5h fa" },
  ];

  useEffect(() => {
    document.body.classList.toggle("no-scroll", menuOpen);

    return () => {
      document.body.classList.remove("no-scroll");
    };
  }, [menuOpen]);

  const handleLinkClick = () => {
    setMenuOpen(false);
  };

  useEffect(() => {
    function handleClickOutside(event: MouseEvent) {
      if (
        notificationsRef.current &&
        !notificationsRef.current.contains(event.target as Node)
      ) {
        setNotificationsOpen(false);
      }
    }
    document.addEventListener("mousedown", handleClickOutside);
    return () => document.removeEventListener("mousedown", handleClickOutside);
  }, []);

  return (
    <nav className="navbar">
      <div className="navbar-container">
        <div className="links-desktop-navbar">
          <ul>
            <li>
              <Link to={"/admin"}>Dashboard</Link>
            </li>
            <li>
              <Link to={"/admin/create-user"}>Crea profilo</Link>
            </li>
            <li>
              <Link to={"/admin/evaluationsAI"}>Valutazioni AI</Link>
            </li>
            <li>
              <Link to={"/admin/assignments"}>Incarichi</Link>
            </li>
            <li>
              <Link to={"/admin/contracts"}>Contratti</Link>
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
              <Link to={"/admin"} onClick={handleLinkClick}>Dashboard</Link>
            </li>
            <li>
              <Link to={"/admin/create-user"} onClick={handleLinkClick}>Crea profilo</Link>
            </li>
            <li>
              <Link to={"/admin/evaluationsAI"} onClick={handleLinkClick}>Valutazioni AI</Link>
            </li>
            <li>
              <Link to={"/admin/assignments"} onClick={handleLinkClick}>Incarichi</Link>
            </li>
            <li>
              <Link to={"/admin/contracts"} onClick={handleLinkClick}>Contratti</Link>
            </li>
          </ul>
        </div>
        <div className="button-links">
          <div ref={notificationsRef} className="notification-wrapper">
            <button
              className="icon-btn"
              onClick={() => setNotificationsOpen(!notificationsOpen)}
            >
              <FaBell size={20} />
              {notifications.length > 0 && (
                <span className="badge">{notifications.length}</span>
              )}
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
            <img src={logo} alt="logo" />
        </div>
      </div>
    </nav>
  );
}
