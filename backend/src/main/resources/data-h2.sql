-- Inserisci le macroaree urbane di Torino
INSERT INTO MacroareaUrbana (nome_macroarea, NTN, NTN_var, IMI, IMI_diff, quota_NTN, quotazione_media, quotazione_var)
VALUES
('COLLINARE OLTREPO', 516, -4.0, 2.4, -0.10, 3.4, 2582, 0.7),
('NUOVA TORINO', 2553, 3.7, 2.9, 0.10, 16.9, 2313, 1.6),
('PERIFERIA NORD', 3656, 1.6, 3.1, 0.05, 24.2, 1338, -5.7),
('PERIFERIA SUD-OVEST', 5153, 2.6, 3.0, 0.07, 34.1, 1816, -3.5),
('TORINO STORICA', 1228, -3.8, 2.8, -0.11, 8.1, 2930, 1.9),
('VECCHIA TORINO', 1993, 0.7, 3.1, 0.02, 13.2, 1892, 2.7),
('TORINO', 15118, 1.6, 3.0, 0.05, 100.0, 1928, -1.2);

-- Stati valutazione
INSERT INTO Stati_valutazione (nome, descrizione)
VALUES
('In corso', 'Valutazione in corso'),
('Completata', 'Valutazione completata'),
('Rifiutata', 'Valutazione rifiutata');SELECT * FROM MacroareaUrbana;