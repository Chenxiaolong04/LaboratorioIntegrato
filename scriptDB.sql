-- ======================================================
-- DATABASE: Agenzia Immobiliare - Schema completo aggiornato con CASCADE
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
        ON DELETE SET NULL ON UPDATE CASCADE
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
    Bagni INT,
    Riscaldamento VARCHAR(100),
    Id_stato_immobile INT,
    Piano INT,
    Ascensore BOOLEAN DEFAULT FALSE,
    Garage BOOLEAN DEFAULT FALSE,
    Giardino BOOLEAN DEFAULT FALSE,
    Balcone BOOLEAN DEFAULT FALSE,
    Terrazzo BOOLEAN DEFAULT FALSE,
    Cantina BOOLEAN DEFAULT FALSE,
    Prezzo INT NULL,
    Descrizione TEXT,
    Data_inserimento DATE DEFAULT (CURRENT_DATE),
    Id_utente INT,
    FOREIGN KEY (Id_stato_immobile) REFERENCES Stati_immobile(Id_stato_immobile)
        ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (Id_utente) REFERENCES Utenti(Id_utente)
        ON DELETE CASCADE ON UPDATE CASCADE
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
    FOREIGN KEY (Id_stato_richiesta) REFERENCES Stati_richiesta(Id_stato_richiesta)
        ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (Id_utente) REFERENCES Utenti(Id_utente)
        ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (Id_immobile) REFERENCES Immobili(Id_immobile)
        ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (Id_agente) REFERENCES Utenti(Id_utente)
        ON DELETE SET NULL ON UPDATE CASCADE
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
    FOREIGN KEY (Id_stato_valutazione) REFERENCES Stati_valutazione(Id_stato_valutazione)
        ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (Id_agente) REFERENCES Utenti(Id_utente)
        ON DELETE SET NULL ON UPDATE CASCADE,
    FOREIGN KEY (Id_immobile) REFERENCES Immobili(Id_immobile)
        ON DELETE CASCADE ON UPDATE CASCADE
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
    FOREIGN KEY (Id_stato_contratto) REFERENCES Stati_contratto(Id_stato_contratto)
        ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (Id_valutazione) REFERENCES Valutazioni(Id_valutazione)
        ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (Id_utente) REFERENCES Utenti(Id_utente)
        ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (Id_richiesta) REFERENCES Richieste(Id_richiesta)
        ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (Id_immobile) REFERENCES Immobili(Id_immobile)
        ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (Id_agente) REFERENCES Utenti(Id_utente)
        ON DELETE SET NULL ON UPDATE CASCADE
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
        ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE zone (
    id_zona INT PRIMARY KEY AUTO_INCREMENT,     
    nome_quartiere VARCHAR(50) NOT NULL,        
    cap VARCHAR(5) NOT NULL               
);


INSERT INTO zone (nome_quartiere, cap) VALUES
('Centro', '10121'),
('Centro Storico/Quadrilatero Confine', '10122'),
('Quadrilatero Romano', '10122'),
('Centro Ovest/San Salvario Confine', '10123'),
('Vanchiglia', '10124'),
('Borgo Vanchiglia', '10124'),
('San Salvario', '10125'),
('Nizza Millefonti', '10126'),
('Lingotto', '10126'),
('Crocetta Est/Borgo Po Ovest', '10127'),
('Crocetta', '10128'),
('Centro Postale/Grandi Utenti', '10129'),
('Collina Ovest', '10131'),
('Sassi', '10132'),
('Madonna del Pilone', '10133'),
('Cavoretto', '10133'),
('Borgo Po', '10133'),
('Filadelfia', '10134'),
('Mirafiori Nord', '10135'),
('Mirafiori Sud', '10135'),
('Santa Rita Sud Confine', '10136'),
('Santa Rita', '10137'),
('Cenisia', '10138'),
('Cenisia/Borgo San Paolo Confine', '10139'),
('Borgo San Paolo', '10141'),
('Pozzo Strada', '10141'),
('Pozzo Strada/Parella Confine', '10142'),
('Cit Turin', '10143'),
('San Donato', '10144'),
('Campidoglio', '10144'),
('Parella', '10145'),
('Parella Nord/Lucento Sud', '10146'),
('Borgo Vittoria', '10147'),
('Lucento', '10148'),
('Borgata Lesna', '10149'),
('Madonna di Campagna', '10149'),
('Caselle Postali/Servizi Centrali', '10150'),
('Vallette', '10151'),
('Aurora', '10152'),
('Borgo Dora', '10152'),
('Valdocco', '10152'),
('Regio Parco', '10153'),
('Barriera di Milano', '10154'),
('Rebaudengo', '10155'),
('Barca', '10156'),
('Bertolla', '10156'),
('Falchera', '10156'),
('Villaretto', '10156');




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

-- =========================================
-- 👤 Inserimento Utenti
-- =========================================
INSERT INTO Utenti (CF, Nome, Cognome, Email, Password, Telefono, Via, Citta, CAP, Id_tipo)
VALUES ('RSSMRA85M01H503Z', 'Luca', 'Bianchi', 'admin@immobili.it', '$2a$12$lN5DUIlcGJFE2GPRbqPcp.WFr6aDYqzDpfOIW2jMLU02sFYlbS/42', '+393331234567', 'Via Italia 12', 'Torino', '10100', 3);

-- Agenti
INSERT INTO Utenti (CF, Nome, Cognome, Email, Password, Telefono, Via, Citta, CAP, Id_tipo)
VALUES 
('RSSMRA85201H501Z', 'Mattia', 'Rossi', 'agente@immobili.it', '$2a$12$Ktx9k/ambYBsY8XDj2O7KO7Y94ARdywfk8LBfUiIhFyzf6A6jyny.', '+393331444111', 'Via Po 2', 'Torino', '10123', 2),
('RSSMRA85M01H501Z', 'Luca', 'Rossi', 'lrossi@immobili.it', '$2a$12$Ktx9k/ambYBsY8XDj2O7KO7Y94ARdywfk8LBfUiIhFyzf6A6jyny.', '+393331111111', 'Via Po 22', 'Torino', '10123', 2),
('VRDGPP90T10H501Y', 'Giulia', 'Verdi', 'gverdi@immobili.it', '$2a$12$Ktx9k/ambYBsY8XDj2O7KO7Y94ARdywfk8LBfUiIhFyzf6A6jyny.', '+393332222222', 'Via Milano 10', 'Milano', '20100', 2),
('BNCLRA88R20H501T', 'Laura', 'Bianchi', 'lbianchi@immobili.it', '$2a$12$Ktx9k/ambYBsY8XDj2O7KO7Y94ARdywfk8LBfUiIhFyzf6A6jyny.', '+393333333333', 'Via Roma 33', 'Cuneo', '12100', 2),
('FRNMRC92D15H501Z', 'Marco', 'Ferrari', 'mferrari@immobili.it', '$2a$12$Ktx9k/ambYBsY8XDj2O7KO7Y94ARdywfk8LBfUiIhFyzf6A6jyny.', '+393334444444', 'Corso Francia 100', 'Rivoli', '10098', 2);

-- Clienti
INSERT INTO Utenti (CF, Nome, Cognome, Email, Password, Telefono, Via, Citta, CAP, Id_tipo)
VALUES
('PLLMRA99H20H501B', 'Mario', 'Pellegrini', 'mpellegrini@immobili.it', '$2a$12$0yHJNq5eSsKZfh8iL3jIKuq2QCqCpvlMl4oUVxX3Rcb0sF6Xf0m4y', '+393335555555', 'Via Nizza 44', 'Torino', '10126', 1),
('CNTLRA95E01H501X', 'Lara', 'Conti', 'lconti@immobili.it', '$2a$12$0yHJNq5eSsKZfh8iL3jIKuq2QCqCpvlMl4oUVxX3Rcb0sF6Xf0m4y', '+393336666666', 'Via Dante 77', 'Milano', '20121', 1);

-- =========================================
-- IMMOBILI
-- =========================================
INSERT INTO Immobili (Via, Citta, CAP, Provincia, Tipologia, Metratura, Condizioni, Stanze, Id_stato_immobile, Piano, Ascensore, Garage, Prezzo, Descrizione, Id_utente)
VALUES
('Via Roma 12', 'Torino', '10100', 'TO', 'Appartamento', 85, 'Buone condizioni', 3, 2, 3, TRUE, TRUE, 220000, 'Appartamento luminoso in centro.', 2),
('Via Milano 5', 'Torino', '10121', 'TO', 'Monolocale', 40, 'Ristrutturato', 1, 2, 1, FALSE, FALSE, 120000, 'Ottimo per studenti.', 2),
('Corso Francia 101', 'Rivoli', '10098', 'TO', 'Attico', 120, 'Ottime condizioni', 4, 1, 5, TRUE, TRUE, 350000, 'Attico con terrazzo panoramico.', 2),
('Via Po 18', 'Torino', '10123', 'TO', 'Bilocale', 55, 'Buono', 2, 1, 2, FALSE, FALSE, 180000, 'Perfetto per coppie.', 2),
('Via Garibaldi 200', 'Milano', '20100', 'MI', 'Appartamento', 95, 'Da ristrutturare', 4, 1, 3, FALSE, TRUE, 300000, 'Zona centrale.', 3),
('Via Nizza 300', 'Torino', '10126', 'TO', 'Trilocale', 70, 'Buone condizioni', 3, 2, 4, TRUE, FALSE, 210000, 'Vicino metropolitana.', 3),
('Via Kennedy 45', 'Moncalieri', '10024', 'TO', 'Villetta', 200, 'Ottime condizioni', 6, 2, 0, FALSE, TRUE, 480000, 'Villetta di pregio.', 4),
('Corso Giulio 90', 'Ivrea', '10015', 'TO', 'Appartamento', 80, 'Buono', 3, 1, 1, TRUE, TRUE, 160000, 'Zona tranquilla.', 4),
('Via Dante 33', 'Cuneo', '12100', 'CN', 'Villa', 300, 'Nuovo', 8, 2, 0, FALSE, TRUE, 600000, 'Villa con giardino.', 5),
('Corso Umbria 15', 'Torino', '10139', 'TO', 'Loft', 60, 'Ristrutturato', 2, 1, 1, FALSE, FALSE, 190000, 'Stile industriale moderno.', 5),
('Via Verdi 1', 'Asti', '14100', 'AT', 'Bilocale', 50, 'Ottimo', 2, 2, 3, FALSE, TRUE, 150000, 'Bilocale moderno.', 5),
('Via Solferino 55', 'Milano', '20121', 'MI', 'Attico', 140, 'Ottimo', 5, 1, 6, TRUE, TRUE, 950000, 'Attico di lusso.', 5);

-- =========================================
-- VALUTAZIONI
-- =========================================
INSERT INTO Valutazioni (Prezzo_AI, Id_stato_valutazione, Id_agente, Id_immobile, Descrizione)
VALUES
(215000, 1, NULL, 1, 'Valutazione automatica immobile Torino'),
(350000, 1, NULL, 3, 'Valutazione AI attico Rivoli'),
(600000, 1, NULL, 9, 'Valutazione AI villa Cuneo'),
(480000, 2, 2, 7, 'Agente sta verificando villetta Moncalieri'),
(950000, 2, 3, 12, 'In verifica attico Milano'),
(210000, 2, 4, 6, 'In verifica trilocale Torino'),
(300000, 2, 5, 5, 'In verifica appartamento Milano');

-- =========================================
-- CONTRATTI
-- =========================================
INSERT INTO Contratti (Data_inizio, Data_fine, Id_stato_contratto, Numero_contratto, Percentuale_commissione, Id_valutazione, Id_utente, Id_richiesta, Id_immobile, Id_agente)
VALUES
('2025-11-10', '2026-12-10', 4, 'C-2025-001', 3.00, 1, 6, NULL, 1, 2),
('2025-02-01', '2025-02-28', 4, 'C-2025-002', 3.00, 2, 6, NULL, 3, 2),
('2025-02-15', '2025-03-15', 4, 'C-2025-003', 3.00, 3, 7, NULL, 7, 3),
('2025-03-10', '2025-04-10', 4, 'C-2025-004', 3.50, 4, 7, NULL, 12, 3);

-- =========================================
-- 🔟 Tabella MacroareaUrbana
-- =========================================
CREATE TABLE MacroareaUrbana (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome_macroarea VARCHAR(100) NOT NULL UNIQUE,
    NTN INT,
    NTN_var DOUBLE,
    IMI DOUBLE,
    IMI_diff DOUBLE,
    quota_NTN DOUBLE,
    quotazione_media DOUBLE NOT NULL,
    quotazione_var DOUBLE,
    CAP_da CHAR(5),
    CAP_a CHAR(5)
);

-- =========================================
-- Inserimento Macroaree Torino con range CAP
-- =========================================
INSERT INTO MacroareaUrbana (nome_macroarea, NTN, NTN_var, IMI, IMI_diff, quota_NTN, quotazione_media, quotazione_var, CAP_da, CAP_a) VALUES
('Centro Torino', 150, 2.5, 180, 1.8, 60.0, 6500, 3.2, '10100', '10129'),
('San Salvario', 120, 1.8, 140, 1.2, 50.0, 4800, 2.1, '10123', '10126'),
('Aurora', 100, 1.2, 120, 0.9, 45.0, 3800, 1.5, '10152', '10156'),
('Crocetta', 110, 1.5, 130, 1.1, 48.0, 4200, 1.8, '10127', '10129'),
('Lingotto', 105, 1.3, 125, 1.0, 46.0, 4000, 1.6, '10126', '10126'),
('Mirafiori', 90, 0.8, 110, 0.7, 40.0, 3200, 0.9, '10135', '10136'),
('Barriera di Milano', 85, 0.5, 100, 0.4, 35.0, 2800, 0.3, '10154', '10156'),
('Collina', 95, 0.9, 115, 0.8, 42.0, 3500, 1.1, '10131', '10133'),
('Vanchiglia', 108, 1.4, 128, 1.05, 47.0, 4100, 1.7, '10124', '10125'),
('Parella', 98, 1.0, 118, 0.85, 43.0, 3600, 1.2, '10145', '10149');



