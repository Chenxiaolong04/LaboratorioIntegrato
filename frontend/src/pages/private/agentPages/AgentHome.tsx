// AgentHome.tsx
import React from "react";
// Assicurati che il percorso sia corretto:
import "../../../assets/styles/_agentHome.scss";

import SearchBar from "../../../components/SearchBar"; 

// 1. Link della Navigazione SUPERIORE
const topNavLinks = [
  { name: 'Dashboard', href: '/agent/dashboard' },
  { name: 'I miei contratti', href: '/agent/contracts' },
  { name: 'I miei incarichi', href: '/agent/my-assignments' },
  { name: 'Le mie valutazioni AI', href: '/agent/my-evaluations' },
];

// 2. Link della Navigazione SECONDARIA (Blu-Verde)
const mainNavLinks = [
    { name: 'Dashboard', href: '/agent/dashboard' },
    { name: 'Valutazioni AI', href: '/agent/evaluations' },
    { name: 'Incarichi', href: '/agent/assignments' },
    { name: 'Vendite', href: '/agent/sales' },
    { name: 'Immobili', href: '/agent/properties' },
];

const AgentHome: React.FC = () => {
    const handleSearch = (query: string) => {
        console.log("Ricerca avviata con query:", query);
    };

    return (
        <div className="agent-home">
            
            {/* 1. TOP BAR (Bianca/Trasparente) */}
            <div className="top-bar">
                <nav className="top-bar__nav">
                    <ul className="top-bar__links">
                        {topNavLinks.map((link) => (
                            <li key={link.name} className="top-bar__item">
                                <a href={link.href} className="top-bar__link">
                                    {link.name}
                                </a>
                            </li>
                        ))}
                    </ul>
                </nav>
                
                <div className="top-bar__icons">
                    {/* Icone delle Notifiche e Utente/Logo */}
                    <div className="icon-notification top-bar-icon">🔔<span className="badge">3</span></div>
                    <div className="icon-user-logo top-bar-icon">[Logo Utente]</div>
                </div>
            </div>

            {/* 2. MIDDLE HEADER (Fascia Blu-Verde e Sfondo Casa) */}
            <header className="middle-header">
                <div className="middle-header__content">
                    
                    <div className="middle-header__top-row">
                        {/* Menu Hamburger */}
                        <button className="menu-button">
                            <span className="icon-hamburger">&#9776;</span>
                        </button>

                        {/* Link Secondari (Dashboard, Valutazioni AI, ecc.) */}
                        <nav className="main-navbar">
                            <ul className="main-navbar__links">
                                {mainNavLinks.map((link) => (
                                    <li key={link.name} className="main-navbar__item">
                                        <a href={link.href} className="main-navbar__link">
                                            {link.name}
                                        </a>
                                    </li>
                                ))}
                            </ul>
                        </nav>
                        
                        {/* Area Notifiche/Profilo a Destra */}
                        <div className="middle-header__right-icons">
                             {/* Nota: l'immagine ha anche qui un'icona di notifica, ma con stile diverso */}
                            <div className="icon-notification middle-header-icon">🔔<span className="badge">3</span></div>
                            <div className="icon-user-logo middle-header-icon">[Profilo]</div>
                        </div>
                    </div>

                    {/* Titolo e SearchBar (nella parte bassa del Middle Header) */}
                    <div className="middle-header__bottom-row">
                        <h1 className="middle-header__title">Dashboard Immobiliaris - Agente</h1>
                        
                        <div className="middle-header__search-container">
                            <SearchBar onSearch={handleSearch} /> 
                        </div>
                    </div>
                </div>
                
                {/* L'immagine di sfondo va in CSS per l'elemento .middle-header */}
            </header>

            {/* 3. Contenuto Principale */}
            <main className="agent-main-content">
                {/* Qui andranno le tue card */}
            </main>
        </div>
    );
};

export default AgentHome;