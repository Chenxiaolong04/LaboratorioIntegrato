-- ======================================================
-- DATABASE: Agenzia Immobiliare - Schema completo aggiornato
-- ======================================================

CREATE DATABASE IF NOT EXISTS AgenziaImmobiliare;
USE AgenziaImmobiliare;

-- =========================================
-- 1️⃣ Tabella Tipi_utente
-- =========================================
CREATE TABLE Tipi_utente (
    Id_tipo INT AUTO_INCREMENT PRIMARY KEY,
    Nome VARCHAR(50) NOT NULL UNIQUE,
    Descrizione VARCHAR(255)
);

-- =========================================
-- 2️⃣ Tabella Utenti
-- =========================================
CREATE TABLE Utenti (
    Id_utente INT AUTO_INCREMENT PRIMARY KEY,
    CF CHAR(16),
    Nome VARCHAR(50) NOT NULL,
    Cognome VARCHAR(50) NOT NULL,
    Email VARCHAR(100) NOT NULL UNIQUE,
    Password VARCHAR(255) NOT NULL,
    Telefono VARCHAR(20),
    Via VARCHAR(100),
    Citta VARCHAR(100),
    CAP CHAR(5),
    Data_registrazione DATE DEFAULT (CURRENT_DATE),
    Id_tipo INT,
    FOREIGN KEY (Id_tipo) REFERENCES Tipi_utente(Id_tipo)
);

-- =========================================
-- 3️⃣ Tabelle di supporto per stati
-- =========================================
CREATE TABLE Stati_immobile (
    Id_stato_immobile INT AUTO_INCREMENT PRIMARY KEY,
    Nome VARCHAR(50) NOT NULL UNIQUE,
    Descrizione VARCHAR(255)
);

CREATE TABLE Stati_richiesta (
    Id_stato_richiesta INT AUTO_INCREMENT PRIMARY KEY,
    Nome VARCHAR(50) NOT NULL UNIQUE,
    Descrizione VARCHAR(255)
);

CREATE TABLE Stati_valutazione (
    Id_stato_valutazione INT AUTO_INCREMENT PRIMARY KEY,
    Nome VARCHAR(50) NOT NULL UNIQUE,
    Descrizione VARCHAR(255)
);

CREATE TABLE Stati_contratto (
    Id_stato_contratto INT AUTO_INCREMENT PRIMARY KEY,
    Nome VARCHAR(50) NOT NULL UNIQUE,
    Descrizione VARCHAR(255)
);

-- =========================================
-- 4️⃣ Tabella Immobili
-- =========================================
CREATE TABLE Immobili (
    Id_immobile INT AUTO_INCREMENT PRIMARY KEY,
    Via VARCHAR(255),
    Citta VARCHAR(100),
    CAP CHAR(5),
    Provincia CHAR(2),
    Tipologia VARCHAR(100),
    Metratura INT,
    Condizioni VARCHAR(100),
    Stanze INT,
    Id_stato_immobile INT,
    Piano INT,
    Ascensore BOOLEAN DEFAULT FALSE,
    Garage BOOLEAN DEFAULT FALSE,
    Prezzo INT NULL,
    Descrizione TEXT,
    Data_inserimento DATE DEFAULT (CURRENT_DATE),
    Id_utente INT,
    FOREIGN KEY (Id_stato_immobile) REFERENCES Stati_immobile(Id_stato_immobile),
    FOREIGN KEY (Id_utente) REFERENCES Utenti(Id_utente)
);

-- =========================================
-- 5️⃣ Tabella Richieste
-- =========================================
CREATE TABLE Richieste (
    Id_richiesta INT AUTO_INCREMENT PRIMARY KEY,
    Id_stato_richiesta INT,
    Descrizione TEXT,
    Data DATE DEFAULT (CURRENT_DATE),
    Id_utente INT,
    Id_immobile INT,
    FOREIGN KEY (Id_stato_richiesta) REFERENCES Stati_richiesta(Id_stato_richiesta),
    FOREIGN KEY (Id_utente) REFERENCES Utenti(Id_utente),
    FOREIGN KEY (Id_immobile) REFERENCES Immobili(Id_immobile)
);

-- =========================================
-- 6️⃣ Tabella Valutazioni
-- =========================================
CREATE TABLE Valutazioni (
    Id_valutazione INT AUTO_INCREMENT PRIMARY KEY,
    Prezzo_AI INT NULL,             -- Prezzo stimato dall'AI
    Prezzo_Umano INT NULL,          -- Prezzo confermato dall'agente
    Data_valutazione DATE DEFAULT (CURRENT_DATE),
    Id_stato_valutazione INT,
    Descrizione TEXT,
    Id_agente INT,                             -- Agente che verifica
    Id_immobile INT,                           -- Immobile valutato
    FOREIGN KEY (Id_stato_valutazione) REFERENCES Stati_valutazione(Id_stato_valutazione),
    FOREIGN KEY (Id_agente) REFERENCES Utenti(Id_utente),
    FOREIGN KEY (Id_immobile) REFERENCES Immobili(Id_immobile)
);

-- =========================================
-- 7️⃣ Tabella Contratti
-- =========================================
CREATE TABLE Contratti (
    Id_contratto INT AUTO_INCREMENT PRIMARY KEY,
    Data_invio DATE,
    Data_ricezione DATE,
    Data_inizio DATE,
    Data_fine DATE,
    Id_stato_contratto INT,
    Numero_contratto VARCHAR(50) UNIQUE,
    Percentuale_commissione DECIMAL(5,2),
    Id_valutazione INT,
    Id_utente INT,
    Id_richiesta INT,
    Id_immobile INT,
    Id_agente INT,
    FOREIGN KEY (Id_stato_contratto) REFERENCES Stati_contratto(Id_stato_contratto),
    FOREIGN KEY (Id_valutazione) REFERENCES Valutazioni(Id_valutazione),
    FOREIGN KEY (Id_utente) REFERENCES Utenti(Id_utente),
    FOREIGN KEY (Id_richiesta) REFERENCES Richieste(Id_richiesta),
    FOREIGN KEY (Id_immobile) REFERENCES Immobili(Id_immobile),
    FOREIGN KEY (Id_agente) REFERENCES Utenti(Id_utente)
);

-- =========================================
-- 8️⃣ Tabella Foto
-- =========================================
CREATE TABLE Foto (
    Id_foto INT AUTO_INCREMENT PRIMARY KEY,
    Nome VARCHAR(100),
    Percorso VARCHAR(255),
    Data_caricamento DATE DEFAULT (CURRENT_DATE),
    Copertina BOOLEAN DEFAULT FALSE,
    Id_immobile INT,
    FOREIGN KEY (Id_immobile) REFERENCES Immobili(Id_immobile)
);

-- =========================================
-- 9️⃣ Dati iniziali per gli stati
-- =========================================
INSERT INTO Stati_immobile (Nome, Descrizione) VALUES
('in valutazione', 'Immobile in fase di valutazione'),
('attivo', 'Immobile pubblicato sul sito'),
('venduto', 'Immobile venduto'),
('ritirato', 'Immobile ritirato dal mercato');

INSERT INTO Stati_richiesta (Nome, Descrizione) VALUES
('in attesa', 'Richiesta in attesa di risposta'),
('accettata', 'Richiesta accettata'),
('rifiutata', 'Richiesta rifiutata');

INSERT INTO Stati_valutazione (Nome, Descrizione) VALUES
('solo_AI', 'Valutazione generata dall AI'),
('in_verifica', 'Valutazione in verifica dall agente'),
('approvata', 'Valutazione confermata dall agente');

INSERT INTO Stati_contratto (Nome, Descrizione) VALUES
('bozza', 'Contratto in bozza'),
('attivo', 'Contratto attivo'),
('scaduto', 'Contratto scaduto'),
('chiuso', 'Contratto concluso');

INSERT INTO Tipi_utente (Nome, Descrizione) VALUES
('Cliente', 'Utente che cerca o vende immobili'),
('Agente', 'Agente immobiliare che valuta e gestisce immobili'),
('Amministratore', 'Utente con privilegi di gestione completa');

