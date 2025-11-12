-- ======================================================
-- DATABASE: Agenzia Immobiliare - Schema completo aggiornato
-- ======================================================
DROP DATABASE IF EXISTS AgenziaImmobiliare;
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
    CF CHAR(16) UNIQUE,
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
    Id_agente INT NULL,         
    FOREIGN KEY (Id_stato_richiesta) REFERENCES Stati_richiesta(Id_stato_richiesta),
    FOREIGN KEY (Id_utente) REFERENCES Utenti(Id_utente),
    FOREIGN KEY (Id_immobile) REFERENCES Immobili(Id_immobile),
    FOREIGN KEY (Id_agente) REFERENCES Utenti(Id_utente)
);

-- =========================================
-- 6️⃣ Tabella Valutazioni
-- =========================================
CREATE TABLE Valutazioni (
    Id_valutazione INT AUTO_INCREMENT PRIMARY KEY,
    Prezzo_AI INT NULL,             
    Prezzo_Umano INT NULL,         
    Data_valutazione DATE DEFAULT (CURRENT_DATE),
    Id_stato_valutazione INT,
    Descrizione TEXT,
    Id_agente INT,                            
    Id_immobile INT,                          
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

INSERT INTO utenti (CF, Nome, Cognome, Email, Password, Telefono, Via, Citta, CAP, Id_tipo)
VALUES (
    'RSSMRA85M01H503Z',
    'Luca',
    'Bianchi',
    'admin@immobili.it',
    '$2a$12$MnZPV4BBQ/2OLceM4s7FkOf9Ibn37s9MUnHTtpm9vHkXRokGruwOS',
    '+393331234567',
    'Via Italia 12',
    'Torino',
    '10100',
    3
);

INSERT INTO utenti (CF, Nome, Cognome, Email, Password, Telefono, Via, Citta, CAP, Id_tipo)
VALUES (
    'RSSMRA85M01H501Z',
    'Luca',
    'Bianchi',
    'agente@immobili.it',
    '$2a$12$9m8A6gdj4vrz6o1WVjwJ9.8X4ouF37Bk862IFmIIGdqadPAl3vxSi',
    '+393331234567',
    'Via Italia 12',
    'Torino',
    '10100',
    2
);

-- ✅ POPOLAMENTO COMPLETO DATABASE AGENZIA IMMOBILIARE
-- Contiene: 12 immobili, valutazioni, contratti conclusi per dashboard admin


-------------------------------------------------------
-- ✅ INSERIMENTO 12 IMMOBILI
-------------------------------------------------------
INSERT INTO Immobili (Via, Citta, CAP, Provincia, Tipologia, Metratura, Condizioni, Stanze, Id_stato_immobile, Piano, Ascensore, Garage, Prezzo, Descrizione, Id_utente)
VALUES
('Via Roma 12', 'Torino', '10100', 'TO', 'Appartamento', 85, 'Buone condizioni', 3, 2, 3, TRUE, TRUE, 220000, 'Appartamento luminoso in centro.', 2),
('Via Milano 5', 'Torino', '10121', 'TO', 'Monolocale', 40, 'Ristrutturato', 1, 2, 1, FALSE, FALSE, 120000, 'Ottimo per studenti.', 2),
('Corso Francia 101', 'Rivoli', '10098', 'TO', 'Attico', 120, 'Ottime condizioni', 4, 1, 5, TRUE, TRUE, 350000, 'Attico con terrazzo panoramico.', 2),
('Via Po 18', 'Torino', '10123', 'TO', 'Bilocale', 55, 'Buono', 2, 1, 2, FALSE, FALSE, 180000, 'Perfetto per coppie.', 2),
('Via Garibaldi 200', 'Milano', '20100', 'MI', 'Appartamento', 95, 'Da ristrutturare', 4, 1, 3, FALSE, TRUE, 300000, 'Zona centrale.', 2),
('Via Nizza 300', 'Torino', '10126', 'TO', 'Trilocale', 70, 'Buone condizioni', 3, 2, 4, TRUE, FALSE, 210000, 'Vicino metropolitana.', 2),
('Via Kennedy 45', 'Moncalieri', '10024', 'TO', 'Villetta', 200, 'Ottime condizioni', 6, 2, 0, FALSE, TRUE, 480000, 'Villetta di pregio.', 2),
('Corso Giulio 90', 'Ivrea', '10015', 'TO', 'Appartamento', 80, 'Buono', 3, 1, 1, TRUE, TRUE, 160000, 'Zona tranquilla.', 2),
('Via Dante 33', 'Cuneo', '12100', 'CN', 'Villa', 300, 'Nuovo', 8, 2, 0, FALSE, TRUE, 600000, 'Villa con giardino.', 2),
('Corso Umbria 15', 'Torino', '10139', 'TO', 'Loft', 60, 'Ristrutturato', 2, 1, 1, FALSE, FALSE, 190000, 'Stile industriale moderno.', 2),
('Via Verdi 1', 'Asti', '14100', 'AT', 'Bilocale', 50, 'Ottimo', 2, 2, 3, FALSE, TRUE, 150000, 'Bilocale moderno.', 2),
('Via Solferino 55', 'Milano', '20121', 'MI', 'Attico', 140, 'Ottimo', 5, 1, 6, TRUE, TRUE, 950000, 'Attico di lusso.', 2);

-------------------------------------------------------
-- ✅ VALUTAZIONI (AI e In verifica)
-------------------------------------------------------

-- Valutazioni generate solo da AI (Id_stato_valutazione = 1)
INSERT INTO Valutazioni (Prezzo_AI, Id_stato_valutazione, Id_agente, Id_immobile, Descrizione)
VALUES
(215000, 1, NULL, 1, 'Valutazione automatica immobile Torino'),
(350000, 1, NULL, 3, 'Valutazione AI attico Rivoli'),
(600000, 1, NULL, 9, 'Valutazione AI villa Cuneo');

-- Valutazioni in verifica (Id_stato_valutazione = 2)
INSERT INTO Valutazioni (Prezzo_AI, Id_stato_valutazione, Id_agente, Id_immobile, Descrizione)
VALUES
(480000, 2, 2, 7, 'Agente sta verificando villetta Moncalieri'),
(950000, 2, 2, 12, 'In verifica attico Milano');

-------------------------------------------------------
-- ✅ CONTRATTI CONCLUSI (Id_stato_contratto = 4)
-------------------------------------------------------
INSERT INTO Contratti (Data_inizio, Data_fine, Id_stato_contratto, Numero_contratto, Percentuale_commissione, Id_valutazione, Id_utente, Id_richiesta, Id_immobile, Id_agente)
VALUES
('2025-11-10', '2026-012-10', 4, 'C-2025-001', 3.00, 1, 2, NULL, 1, 2),
('2025-02-01', '2025-02-28', 4, 'C-2025-002', 3.00, 2, 2, NULL, 3, 2),
('2025-02-15', '2025-03-15', 4, 'C-2025-003', 3.00, 3, 2, NULL, 7, 2);



