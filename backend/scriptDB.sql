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
    Data_registrazione DATETIME DEFAULT CURRENT_TIMESTAMP,
    Id_tipo INT,
    Contratto VARCHAR(100),
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
    Data_registrazione DATETIME DEFAULT CURRENT_TIMESTAMP,
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
    Data_richiesta DATETIME DEFAULT CURRENT_TIMESTAMP,
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
    Data_valutazione DATETIME DEFAULT CURRENT_TIMESTAMP,
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
    Data_invio DATETIME,
    Data_ricezione DATETIME,
    Data_inizio DATETIME,
    Data_fine DATETIME,
    Data_registrazione DATETIME DEFAULT CURRENT_TIMESTAMP,
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
    Data_caricamento DATETIME DEFAULT CURRENT_TIMESTAMP,
    Copertina BOOLEAN DEFAULT FALSE,
    Id_immobile INT,
    FOREIGN KEY (Id_immobile) REFERENCES Immobili(Id_immobile)
        ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE zone (
    id_zona INT PRIMARY KEY AUTO_INCREMENT,     
    nome_quartiere VARCHAR(50) NOT NULL,        
    cap VARCHAR(5) NOT NULL,
    prezzo_medio_mq INT NOT NULL              
);


INSERT INTO zone (nome_quartiere, cap, prezzo_medio_mq) VALUES 
('Centro', '10121', 4200),
('Centro Storico/Quadrilatero Confine', '10122', 3400),
('Quadrilatero Romano', '10122', 3600),
('Centro Ovest/San Salvario Confine', '10123', 3100),
('Vanchiglia', '10124', 2900),
('Borgo Vanchiglia', '10124', 2800),
('San Salvario', '10125', 2700),
('Nizza Millefonti', '10126', 2100),
('Lingotto', '10126', 2000),
('Crocetta Est/Borgo Po Ovest', '10127', 3200),
('Crocetta', '10128', 3700),
('Centro Postale/Grandi Utenti', '10129', 3000),
('Collina Ovest', '10131', 3900),
('Sassi', '10132', 3100),
('Madonna del Pilone', '10132', 3300),
('Cavoretto', '10133', 3800),
('Borgo Po', '10133', 4100),
('Filadelfia', '10134', 2300),
('Mirafiori Nord', '10135', 1800),
('Mirafiori Sud', '10135', 1500),
('Santa Rita Sud Confine', '10136', 1900),
('Santa Rita', '10137', 2200),
('Cenisia', '10138', 2500),
('Cenisia/Borgo San Paolo Confine', '10139', 2400),
('Borgo San Paolo', '10141', 2300),
('Pozzo Strada', '10141', 2200),
('Pozzo Strada/Parella Confine', '10142', 2150),
('Cit Turin', '10143', 3400),
('San Donato', '10144', 2600),
('Campidoglio', '10144', 2500),
('Parella', '10145', 2200),
('Parella Nord/Lucento Sud', '10146', 1800),
('Borgo Vittoria', '10147', 1600),
('Lucento', '10148', 1500),
('Borgata Lesna', '10149', 1700),
('Madonna di Campagna', '10149', 1550),
('Caselle Postali/Servizi Centrali', '10150', 2000),
('Vallette', '10151', 1350),
('Aurora', '10152', 1700),
('Borgo Dora', '10152', 1800),
('Valdocco', '10152', 2100),
('Regio Parco', '10153', 2300),
('Barriera di Milano', '10154', 1250),
('Rebaudengo', '10155', 1300),
('Barca', '10156', 1400),
('Bertolla', '10156', 1600),
('Falchera', '10156', 1200),
('Villaretto', '10156', 1300);




-- =========================================
-- 9️⃣ Dati iniziali per gli stati
-- =========================================
INSERT INTO Stati_immobile (Nome, Descrizione) VALUES
('attivo', 'Immobile con contratto di mandato esclusivo, pubblicato sul sito'),
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

-- Agenti (alcuni con contratto normale, altri in stage)
INSERT INTO Utenti (CF, Nome, Cognome, Email, Password, Telefono, Via, Citta, CAP, Id_tipo, Contratto)
VALUES 
('RSSMRA85201H501Z', 'Mattia', 'Rossi', 'agente@immobili.it', '$2a$12$Ktx9k/ambYBsY8XDj2O7KO7Y94ARdywfk8LBfUiIhFyzf6A6jyny.', '+393331444111', 'Via Po 2', 'Torino', '10123', 2, 'Indeterminato'),
('RSSMRA85M01H501Z', 'Luca', 'Rossi', 'lrossi@immobili.it', '$2a$12$Ktx9k/ambYBsY8XDj2O7KO7Y94ARdywfk8LBfUiIhFyzf6A6jyny.', '+393331111111', 'Via Po 22', 'Torino', '10123', 2, 'stage'),
('VRDGPP90T10H501Y', 'Giulia', 'Verdi', 'gverdi@immobili.it', '$2a$12$Ktx9k/ambYBsY8XDj2O7KO7Y94ARdywfk8LBfUiIhFyzf6A6jyny.', '+393332222222', 'Via Milano 10', 'Milano', '20100', 2, 'Determinato'),
('BNCLRA88R20H501T', 'Laura', 'Bianchi', 'lbianchi@immobili.it', '$2a$12$Ktx9k/ambYBsY8XDj2O7KO7Y94ARdywfk8LBfUiIhFyzf6A6jyny.', '+393333333333', 'Via Roma 33', 'Cuneo', '12100', 2, 'stage'),
('FRNMRC92D15H501Z', 'Marco', 'Ferrari', 'mferrari@immobili.it', '$2a$12$Ktx9k/ambYBsY8XDj2O7KO7Y94ARdywfk8LBfUiIhFyzf6A6jyny.', '+393334444444', 'Corso Francia 100', 'Rivoli', '10098', 2, 'Indeterminato');

-- Clienti
INSERT INTO Utenti (CF, Nome, Cognome, Email, Password, Telefono, Via, Citta, CAP, Id_tipo)
VALUES
('PLLMRA99H20H501B', 'Mario', 'Pellegrini', 'mpellegrini@immobili.it', '$2a$12$0yHJNq5eSsKZfh8iL3jIKuq2QCqCpvlMl4oUVxX3Rcb0sF6Xf0m4y', '+393335555555', 'Via Nizza 44', 'Torino', '10126', 1),
('CNTLRA95E01H501X', 'Lara', 'Conti', 'lconti@immobili.it', '$2a$12$0yHJNq5eSsKZfh8iL3jIKuq2QCqCpvlMl4oUVxX3Rcb0sF6Xf0m4y', '+393336666666', 'Via Dante 77', 'Milano', '20121', 1);

-- =========================================
-- IMMOBILI (con date di registrazione diverse per testare statistiche)
-- =========================================
INSERT INTO Immobili (Via, Citta, CAP, Provincia, Tipologia, Metratura, Condizioni, Stanze, Bagni, Riscaldamento, Id_stato_immobile, Piano, Ascensore, Garage, Giardino, Balcone, Prezzo, Descrizione, Data_registrazione, Id_utente)
VALUES
-- Immobili con contratto di mandato venduti (stato = NULL, gestito da valutazioni)
('Via Roma 12', 'Torino', '10100', 'TO', 'Appartamento', 85, 'Buone condizioni', 3, 2, 'Autonomo', NULL, 3, TRUE, TRUE, FALSE, TRUE, 220000, 'Appartamento luminoso in centro.', '2025-11-01 10:00:00', 2),
('Via Milano 5', 'Torino', '10121', 'TO', 'Appartamento', 40, 'Ristrutturato', 1, 1, 'Centralizzato', NULL, 1, FALSE, FALSE, FALSE, FALSE, 120000, 'Ottimo per studenti.', '2025-10-28 14:30:00', 2),
('Corso Francia 101', 'Rivoli', '10098', 'TO', 'Attico', 120, 'Ottime condizioni', 4, 3, 'Autonomo', NULL, 5, TRUE, TRUE, TRUE, TRUE, 350000, 'Attico con terrazzo panoramico.', '2025-10-29 09:00:00', 2),
('Via Po 18', 'Torino', '10123', 'TO', 'Appartamento', 55, 'Buono', 2, 1, 'Centralizzato', NULL, 2, FALSE, FALSE, FALSE, TRUE, 180000, 'Perfetto per coppie.', '2025-11-28 16:00:00', 2),
('Via Garibaldi 200', 'Milano', '20100', 'MI', 'Appartamento', 95, 'Da ristrutturare', 4, 2, 'Centralizzato', NULL, 3, FALSE, TRUE, FALSE, FALSE, 300000, 'Zona centrale.', '2025-11-11 11:00:00', 3),
('Via Nizza 300', 'Torino', '10126', 'TO', 'Appartamento', 70, 'Buone condizioni', 3, 2, 'Autonomo', NULL, 4, TRUE, FALSE, FALSE, TRUE, 210000, 'Vicino metropolitana.', '2025-11-06 15:30:00', 3),
('Via Kennedy 45', 'Moncalieri', '10024', 'TO', 'Villa', 200, 'Ottime condizioni', 6, 3, 'Autonomo', NULL, 0, FALSE, TRUE, TRUE, TRUE, 480000, 'Villa di pregio.', '2025-11-04 10:00:00', 4),
('Corso Giulio 90', 'Ivrea', '10015', 'TO', 'Appartamento', 80, 'Buono', 3, 1, 'Centralizzato', NULL, 1, TRUE, TRUE, FALSE, FALSE, 160000, 'Zona tranquilla.', '2025-11-01 12:00:00', 4),
('Via Dante 33', 'Cuneo', '12100', 'CN', 'Villa', 300, 'Nuovo', 8, 4, 'Autonomo', NULL, 0, FALSE, TRUE, TRUE, TRUE, 600000, 'Villa con giardino.', '2025-11-03 09:30:00', 5),
('Corso Umbria 15', 'Torino', '10139', 'TO', 'Loft', 60, 'Ristrutturato', 2, 1, 'Autonomo', NULL, 1, FALSE, FALSE, FALSE, FALSE, 190000, 'Stile industriale moderno.', '2025-11-08 14:00:00', 5),
('Via Verdi 1', 'Asti', '14100', 'AT', 'Appartamento', 50, 'Ottimo', 2, 1, 'Centralizzato', NULL, 3, FALSE, TRUE, FALSE, TRUE, 150000, 'Appartamento moderno.', '2025-10-18 11:00:00', 5),
('Via Solferino 55', 'Milano', '20121', 'MI', 'Attico', 140, 'Ottimo', 5, 3, 'Autonomo', NULL, 6, TRUE, TRUE, FALSE, TRUE, 950000, 'Attico di lusso.', '2025-10-08 10:00:00', 5),
('Via Cavour 88', 'Torino', '10128', 'TO', 'Appartamento', 90, 'Buono', 3, 2, 'Autonomo', NULL, 2, TRUE, TRUE, FALSE, TRUE, 240000, 'Appartamento in Crocetta.', '2025-09-12 10:00:00', 2),
('Corso Vittorio 45', 'Asti', '14100', 'AT', 'Villa', 180, 'Ottime condizioni', 5, 3, 'Autonomo', NULL, 0, FALSE, TRUE, TRUE, FALSE, 420000, 'Villa con ampi spazi.', '2025-08-05 11:00:00', 4),
('Via Lagrange 22', 'Torino', '10123', 'TO', 'Attico', 75, 'Ristrutturato', 3, 2, 'Centralizzato', NULL, 3, TRUE, FALSE, FALSE, TRUE, 195000, 'Attico elegante.', '2025-07-12 09:00:00', 3),
('Piazza San Carlo 10', 'Torino', '10121', 'TO', 'Attico', 110, 'Lusso', 4, 2, 'Autonomo', NULL, 5, TRUE, TRUE, FALSE, TRUE, 480000, 'Attico in piazza centrale.', '2025-07-08 14:00:00', 2),

-- Immobili con contratto di mandato attivi (non ancora venduti)
('Via Sacchi 20', 'Torino', '10128', 'TO', 'Loft', 60, 'Buono', 2, 1, 'Centralizzato', NULL, 2, TRUE, FALSE, FALSE, TRUE, 175000, 'Loft vicino stazione.', '2025-11-29 10:00:00', 2),
('Corso Moncalieri 88', 'Torino', '10133', 'TO', 'Villa', 250, 'Ottime condizioni', 7, 4, 'Autonomo', NULL, 0, FALSE, TRUE, TRUE, TRUE, 850000, 'Villa panoramica su collina.', '2025-11-28 14:00:00', 3),

-- Immobili senza contratto di mandato (valutazioni solo AI o in verifica)
('Via Nizza 150', 'Torino', '10126', 'TO', 'Villa', 100, 'Ristrutturato', 4, 2, 'Autonomo', NULL, 3, TRUE, TRUE, FALSE, FALSE, NULL, 'Villa moderna.', '2025-11-27 09:00:00', 4),
('Via XX Settembre 30', 'Torino', '10121', 'TO', 'Appartamento', 35, 'Buono', 1, 1, 'Centralizzato', NULL, 1, FALSE, FALSE, FALSE, FALSE, NULL, 'Appartamento in centro.', '2025-11-26 16:00:00', 2);

-- =========================================
-- VALUTAZIONI (con Date_valutazione impostate solo quando prese in carico)
-- =========================================
INSERT INTO Valutazioni (Prezzo_AI, Prezzo_Umano, Data_valutazione, Id_stato_valutazione, Id_agente, Id_immobile, Descrizione)
VALUES
-- Valutazioni approvate per immobili con contratto (ID 1-16)
-- Date valutazione SUCCESSIVE alla registrazione immobile
(220000, 225000, '2025-11-02 09:00:00', 3, 2, 1, 'Valutazione approvata appartamento Torino'),
(120000, 118000, '2025-10-29 10:30:00', 3, 2, 2, 'Valutazione approvata appartamento'),
(350000, 352000, '2025-10-30 10:00:00', 3, 2, 3, 'Valutazione approvata attico Rivoli'),
(180000, 182000, '2025-11-29 11:00:00', 3, 4, 4, 'Valutazione approvata appartamento Torino'),
(300000, 295000, '2025-11-12 14:00:00', 3, 5, 5, 'Valutazione approvata appartamento Milano'),
(210000, 215000, '2025-11-07 10:00:00', 3, 4, 6, 'Valutazione approvata appartamento Torino'),
(480000, 490000, '2025-11-05 09:00:00', 3, 5, 7, 'Valutazione approvata villa Moncalieri'),
(160000, 165000, '2025-11-02 11:00:00', 3, 4, 8, 'Valutazione approvata appartamento Ivrea'),
(600000, 595000, '2025-11-04 15:30:00', 3, 5, 9, 'Valutazione approvata villa Cuneo'),
(190000, 195000, '2025-11-09 10:00:00', 3, 5, 10, 'Valutazione approvata loft Torino'),
(150000, 152000, '2025-10-19 10:00:00', 3, 2, 11, 'Valutazione approvata appartamento Asti'),
(950000, 920000, '2025-10-09 12:00:00', 3, 2, 12, 'Valutazione approvata attico Milano'),
(240000, 242000, '2025-09-13 10:00:00', 3, 2, 13, 'Valutazione approvata appartamento Crocetta'),
(420000, 415000, '2025-08-06 09:00:00', 3, 3, 14, 'Valutazione approvata villa Asti'),
(195000, 198000, '2025-07-13 14:00:00', 3, 4, 15, 'Valutazione approvata attico Torino'),
(480000, 475000, '2025-07-09 10:00:00', 3, 2, 16, 'Valutazione approvata attico Piazza San Carlo'),

-- Valutazioni in verifica per immobili senza contratto (ID 17-18)
(175000, 178000, '2025-11-29 11:00:00', 2, 2, 17, 'In verifica loft Torino'),
(850000, 870000, '2025-11-28 15:00:00', 2, 3, 18, 'In verifica villa collina'),

-- Valutazioni solo AI per immobili appena registrati (ID 19-20)
(280000, NULL, NULL, 1, NULL, 19, 'Valutazione automatica villa Nizza'),
(95000, NULL, NULL, 1, NULL, 20, 'Valutazione automatica appartamento centro');

-- =========================================
-- CONTRATTI (contratti chiusi distribuiti nei mesi per testare statistiche)
-- =========================================
-- Contratti chiusi nel mese di dicembre 2025 (per top3Agenti del mese corrente)
INSERT INTO Contratti (Data_invio, Data_ricezione, Data_inizio, Data_fine, Id_stato_contratto, Numero_contratto, Percentuale_commissione, Id_valutazione, Id_utente, Id_richiesta, Id_immobile, Id_agente)
VALUES
('2025-11-26 10:00:00', '2025-11-27 14:00:00', '2025-12-01 09:00:00', '2026-12-01 09:00:00', 4, 'C-2025-DIC-001', 3.00, 1, 6, NULL, 1, 2),
('2025-11-27 11:00:00', '2025-11-28 10:00:00', '2025-12-01 10:00:00', '2026-12-01 10:00:00', 4, 'C-2025-DIC-002', 3.00, 2, 6, NULL, 2, 3),
('2025-11-28 09:00:00', '2025-11-29 15:00:00', '2025-12-01 14:00:00', '2026-12-01 14:00:00', 4, 'C-2025-DIC-003', 3.50, 4, 7, NULL, 4, 4),

-- Contratti chiusi nel mese di novembre 2025
('2025-10-28 10:00:00', '2025-10-29 14:00:00', '2025-11-08 09:00:00', '2026-11-08 09:00:00', 4, 'C-2025-001', 3.00, 3, 6, NULL, 3, 2),
('2025-10-30 11:00:00', '2025-10-31 10:00:00', '2025-11-10 10:00:00', '2026-11-10 10:00:00', 4, 'C-2025-002', 3.00, 8, 6, NULL, 8, 4),
('2025-11-03 09:00:00', '2025-11-04 15:00:00', '2025-11-12 14:00:00', '2026-11-12 14:00:00', 4, 'C-2025-003', 3.50, 7, 7, NULL, 7, 5),
('2025-11-05 14:00:00', '2025-11-06 09:30:00', '2025-11-15 11:00:00', '2026-11-15 11:00:00', 4, 'C-2025-004', 3.00, 6, 7, NULL, 6, 4),
('2025-11-10 10:00:00', '2025-11-11 16:00:00', '2025-11-18 15:00:00', '2026-11-18 15:00:00', 4, 'C-2025-005', 3.50, 5, 6, NULL, 5, 5),

-- Contratti chiusi in ottobre 2025
('2025-10-05 10:00:00', '2025-10-06 14:00:00', '2025-10-22 11:00:00', '2026-10-22 11:00:00', 4, 'C-2025-009', 3.00, 13, 6, NULL, 13, 2),
('2025-10-15 09:00:00', '2025-10-16 15:00:00', '2025-10-28 10:00:00', '2026-10-28 10:00:00', 4, 'C-2025-010', 3.50, 12, 7, NULL, 12, 2),
('2025-10-08 11:00:00', '2025-10-09 09:00:00', '2025-10-25 14:00:00', '2026-10-25 14:00:00', 4, 'C-2025-011', 3.00, 11, 6, NULL, 11, 4),

-- Contratti chiusi in settembre 2025
('2025-09-05 11:00:00', '2025-09-06 09:00:00', '2025-09-18 14:00:00', '2026-09-18 14:00:00', 4, 'C-2025-012', 3.00, 9, 6, NULL, 9, 5),
('2025-09-15 10:00:00', '2025-09-16 15:00:00', '2025-09-28 10:00:00', '2026-09-28 10:00:00', 4, 'C-2025-013', 3.50, 10, 7, NULL, 10, 5),

-- Contratti chiusi in agosto 2025
('2025-07-28 10:00:00', '2025-07-29 14:00:00', '2025-08-12 09:00:00', '2026-08-12 09:00:00', 4, 'C-2025-014', 3.50, 14, 7, NULL, 14, 3),

-- Contratti chiusi in luglio 2025
('2025-07-01 09:00:00', '2025-07-02 14:00:00', '2025-07-18 11:00:00', '2026-07-18 11:00:00', 4, 'C-2025-015', 3.50, 15, 7, NULL, 15, 4),
('2025-07-15 10:00:00', '2025-07-16 15:00:00', '2025-07-30 09:00:00', '2026-07-30 09:00:00', 4, 'C-2025-016', 3.00, 16, 6, NULL, 16, 2);

