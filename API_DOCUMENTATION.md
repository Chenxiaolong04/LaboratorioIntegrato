# 📡 API REST - Documentazione Backend

Base URL: `http://localhost:8080`

---

## 🔐 Autenticazione

### POST `/api/auth/login`
Login con credenziali JSON

**Request:**
```json
{
  "email": "admin@test.com",
  "password": "admin123"
}
```

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Login effettuato con successo",
  "username": "Mario Rossi",
  "roles": ["ROLE_ADMIN"]
}
```

**Response (401 Unauthorized):**
```json
{
  "success": false,
  "message": "Credenziali non valide"
}
```

---

### POST `/api/auth/logout`
Logout e invalidazione sessione

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Logout effettuato con successo"
}
```

---

### GET `/api/auth/check`
Verifica se l'utente è autenticato

**Response (200 OK):**
```json
{
  "authenticated": true
}
```

---

### GET `/api/auth/user`
Ottieni informazioni utente loggato

**Response (200 OK):**
```json
{
  "username": "Mario Rossi",
  "roles": ["ROLE_ADMIN"],
  "authenticated": true
}
```

**Response (401):** Se non autenticato

---

## 👨‍💼 Dashboard Admin


### GET `/api/admin/dashboard?offset=0&limit=10`
**Richiede:** `ROLE_ADMIN`

**Query Parameters:**
- `offset` (opzionale, default: 0): quanti immobili sono già stati caricati
- `limit` (opzionale, default: 10): quanti immobili caricare

**Response (200 OK):**
```json
{
  "statistics": {
    "contrattiConclusi": 15,
    "valutazioniInCorso": 8,
    "valutazioniConAI": 5,
    "contrattiConclusiMensili": 3,
    "valutazioniInCorsoMensili": 2,
    "valutazioniConAIMensili": 1
  },
  "immobili": [
    {
      "tipo": "Appartamento",
      "nomeProprietario": "Mario Rossi",
      "dataInserimento": "2025-11-10",
      "statoValutazione": "in_verifica",
      "agenteAssegnato": "Luigi Verdi"
    },
    {
      "tipo": "Villa",
      "nomeProprietario": "Anna Bianchi",
      "dataInserimento": "2025-11-09",
      "statoValutazione": "solo_AI",
      "agenteAssegnato": null
    }
  ],
  "nextOffset": 10,
  "hasMore": true,
  "pageSize": 10
}
```

**Campi risposta:**
- `statistics` (object): Statistiche admin
- `immobili` (array): Array di immobili per questa richiesta
- `nextOffset` (number): Offset da usare per la prossima richiesta ("Carica altri")
- `hasMore` (boolean): `true` se ci sono altri immobili, `false` se sei alla fine
- `pageSize` (number): Numero di immobili ritornati in questa richiesta

**Statistiche totali (dall'inizio):**
- `contrattiConclusi`: Contratti con stato "chiuso"
- `valutazioniInCorso`: Valutazioni con stato "in_verifica"
- `valutazioniConAI`: Valutazioni con stato "solo_AI"

**Statistiche mensili (ultimi 30 giorni):**
- `contrattiConclusiMensili`: Contratti conclusi (basato su `Data_inizio`)
- `valutazioniInCorsoMensili`: Valutazioni in corso (basato su `Data_valutazione`)
- `valutazioniConAIMensili`: Valutazioni con AI (basato su `Data_valutazione`)

**Campi di ogni immobile:**
- `tipo`: Tipologia immobile (Appartamento, Villa, Ufficio, ecc.)
- `nomeProprietario`: Nome completo proprietario
- `dataInserimento`: Data inserimento immobile
- `statoValutazione`: Stato della valutazione dell'immobile (recuperato dalla tabella Valutazioni)
  - `"solo_AI"`: Valutazione effettuata solo dall'intelligenza artificiale
  - `"in_verifica"`: Valutazione in corso di verifica da parte di un agente
  - `"approvata"`: Valutazione approvata dall'agente
- `agenteAssegnato`: Nome completo dell'agente che gestisce la valutazione
  - Viene mostrato **solo** se `statoValutazione` è `"in_verifica"` o `"approvata"`
  - Sarà `null` se lo stato è `"solo_AI"` o se non c'è un agente assegnato

**Nota importante sulla logica di visualizzazione:**
Il sistema recupera lo stato della valutazione dalla tabella `Valutazioni` (non dalla tabella `Immobili`). Per ogni immobile viene cercata la valutazione più recente (ordinata per `Data_valutazione DESC`). L'agente viene mostrato solo quando la valutazione è in fase di verifica umana o è stata approvata, garantendo che le valutazioni AI non mostrino erroneamente un agente assegnato.

**Response (403):** Se non hai ROLE_ADMIN

---

### GET `/api/admin/immobili?page=0&size=10`
**Richiede:** `ROLE_ADMIN`

Endpoint alternativo per paginazione basata su **pagine** (non offset). Utile se preferisci navigare per numero di pagina anziché offset.

**Query Parameters:**
- `page` (opzionale, default: 0): numero pagina (0 = prima pagina)
- `size` (opzionale, default: 10): immobili per pagina

**Response (200 OK):**
```json
{
  "immobili": [
    {
      "tipo": "Appartamento",
      "nomeProprietario": "Mario Rossi",
      "dataInserimento": "2025-11-10",
      "statoValutazione": "in_verifica",
      "agenteAssegnato": "Luigi Verdi"
    }
  ],
  "currentPage": 0,
  "pageSize": 10,
  "totalImmobili": 45,
  "totalPages": 5,
  "hasNext": true,
  "hasPrevious": false
}
```

**Campi risposta:**
- `immobili` (array): Immobili della pagina corrente
- `currentPage` (number): Numero pagina attuale
- `pageSize` (number): Numero immobili per pagina
- `totalImmobili` (number): Numero totale immobili nel sistema
- `totalPages` (number): Numero totale pagine
- `hasNext` (boolean): `true` se esiste pagina successiva
- `hasPrevious` (boolean): `true` se esiste pagina precedente

**Response (403):** Se non hai ROLE_ADMIN

---

### GET `/api/admin/contratti/chiusi?offset=0&limit=10`
**Richiede:** `ROLE_ADMIN`

Restituisce la lista di **contratti conclusi** (stato = "chiuso") con i dettagli degli immobili associati. Supporta caricamento progressivo con offset/limit.

**Query Parameters:**
- `offset` (opzionale, default: 0): quanti contratti sono già stati caricati
- `limit` (opzionale, default: 10): quanti contratti caricare

**Response (200 OK):**
```json
{
  "contratti": [
    {
      "numeroContratto": "C-2025-001",
      "dataInvio": "2025-11-08",
      "dataRicezione": "2025-11-09",
      "dataInizio": "2025-01-15",
      "dataFine": "2025-03-15",
      "valutazioneUmana": 225000,
      "tipo": "Appartamento",
      "nomeProprietario": "Mario Rossi",
      "dataInserimento": "2024-12-20",
      "agenteAssegnato": "Luigi Verdi"
    },
    {
      "numeroContratto": "C-2025-002",
      "dataInvio": "2025-01-25",
      "dataRicezione": "2025-01-26",
      "dataInizio": "2025-02-01",
      "dataFine": "2025-04-01",
      "valutazioneUmana": null,
      "tipo": "Villa",
      "nomeProprietario": "Anna Bianchi",
      "dataInserimento": "2024-12-15",
      "agenteAssegnato": null
    }
  ],
  "nextOffset": 10,
  "hasMore": true,
  "pageSize": 10
}
```

**Campi risposta:**
- `contratti` (array): Array di contratti per questa richiesta
- `nextOffset` (number): Offset da usare per la prossima richiesta
- `hasMore` (boolean): `true` se ci sono altri contratti, `false` se sei alla fine
- `pageSize` (number): Numero di contratti ritornati in questa richiesta

**Campi di ogni contratto:**
- `numeroContratto`: Numero identificativo contratto
- `dataInvio`: Data invio contratto
- `dataRicezione`: Data ricezione contratto
- `dataInizio`: Data inizio contratto
- `dataFine`: Data fine contratto
- `valutazioneUmana`: Prezzo stimato dall'agente (null se non disponibile)
- `tipo`: Tipologia immobile (Appartamento, Villa, Ufficio, ecc.)
- `nomeProprietario`: Nome completo proprietario immobile
- `dataInserimento`: Data inserimento immobile nel sistema
- `agenteAssegnato`: Nome agente che gestisce l'immobile (null se non assegnato)

**Response (403):** Se non hai ROLE_ADMIN

---




### GET `/api/admin/valutazioni/solo-ai?offset=0&limit=10`
**Richiede:** `ROLE_ADMIN`

Restituisce la lista di **valutazioni generate solo dall'AI** (stato = "solo_AI") con i dettagli degli immobili. Supporta caricamento progressivo con offset/limit.

**Query Parameters:**
- `offset` (opzionale, default: 0): quante valutazioni sono già state caricate
- `limit` (opzionale, default: 10): quante valutazioni caricare

**Response (200 OK):**
```json
{
  "valutazioni": [
    {
      "id": 5,
      "prezzoAI": 215000,
      "dataValutazione": "2025-11-12",
      "descrizione": "Valutazione automatica immobile Torino",
      "tipo": "Appartamento",
      "via": "Via Roma 12",
      "citta": "Torino",
      "cap": "10100",
      "provincia": "TO",
      "metratura": 85,
      "condizioni": "Buone condizioni",
      "stanze": 3,
      "bagni": 1,
      "piano": 3,
      "ascensore": true,
      "garage": true,
      "giardino": false,
      "balcone": true,
      "terrazzo": false,
      "cantina": false,
      "riscaldamento": "Centralizzato",
      "nomeProprietario": "Luca Bianchi",
      "emailProprietario": "luca.bianchi@email.com",
      "telefonoProprietario": "3201234567",
      "descrizione": "Appartamento luminoso in centro.",
      "dataInserimento": "2024-12-20"
    }
  ],
  "nextOffset": 10,
  "hasMore": false,
  "pageSize": 1
}
```

**Campi risposta:**
- `valutazioni` (array): Array di valutazioni per questa richiesta
- `nextOffset` (number): Offset da usare per la prossima richiesta
- `hasMore` (boolean): `true` se ci sono altre valutazioni, `false` se sei alla fine
- `pageSize` (number): Numero di valutazioni ritornate in questa richiesta

**Campi di ogni valutazione:**
- `id`: ID valutazione (per operazioni di eliminazione)
- `prezzoAI`: Prezzo stimato dall'AI
- `dataValutazione`: Data della valutazione
- `descrizione`: Note sulla valutazione
- `tipo`: Tipologia immobile
- `via`: Via/indirizzo
- `citta`: Città
- `cap`: CAP
- `provincia`: Provincia (sigla)
- `metratura`: Superficie in mq
- `condizioni`: Stato di conservazione
- `stanze`: Numero stanze
- `bagni`: Numero bagni
- `piano`: Piano
- `ascensore`: Presenza ascensore (true/false)
- `garage`: Presenza garage (true/false)
- `giardino`: Presenza giardino (true/false)
- `balcone`: Presenza balcone (true/false)
- `terrazzo`: Presenza terrazzo (true/false)
- `cantina`: Presenza cantina (true/false)
- `riscaldamento`: Tipo di riscaldamento
- `nomeProprietario`: Nome completo proprietario
- `emailProprietario`: Email proprietario
- `telefonoProprietario`: Telefono proprietario
- `descrizione`: Descrizione immobile
- `dataInserimento`: Data inserimento nel sistema

**Response (403):** Se non hai ROLE_ADMIN

---

### GET `/api/admin/valutazioni/in-verifica?offset=0&limit=10`
**Richiede:** `ROLE_ADMIN`

Restituisce la lista di **valutazioni in verifica** (stato = "in_verifica") con TUTTI i campi della tabella valutazione. Supporta caricamento progressivo con offset/limit.

**Query Parameters:**
- `offset` (opzionale, default: 0): quante valutazioni sono già state caricate
- `limit` (opzionale, default: 10): quante valutazioni caricare

**Response (200 OK):**
```json
{
  "valutazioni": [
    {
      "id": 7,
      "prezzoAI": 215000,
      "prezzoUmano": 220000,
      "dataValutazione": "2025-11-12",
      "statoValutazione": "in_verifica",
      "descrizione": "Valutazione in corso di verifica",
      "nomeAgente": "Luigi Verdi",
      "emailAgente": "luigi.verdi@email.com",
      "tipo": "Appartamento",
      "via": "Via Roma 12",
      "citta": "Torino",
      "cap": "10100",
      "provincia": "TO",
      "metratura": 85,
      "condizioni": "Buone condizioni",
      "stanze": 3,
      "bagni": 1,
      "piano": 3,
      "ascensore": true,
      "garage": true,
      "giardino": false,
      "balcone": true,
      "terrazzo": false,
      "cantina": false,
      "riscaldamento": "Centralizzato",
      "nomeProprietario": "Luca Bianchi",
      "emailProprietario": "luca.bianchi@email.com",
      "telefonoProprietario": "3201234567",
      "descrizione": "Appartamento luminoso in centro.",
      "dataInserimento": "2024-12-20"
    }
  ],
  "nextOffset": 10,
  "hasMore": false,
  "pageSize": 1
}
```

**Campi risposta:**
- `valutazioni` (array): Array di valutazioni per questa richiesta
- `nextOffset` (number): Offset da usare per la prossima richiesta
- `hasMore` (boolean): `true` se ci sono altre valutazioni, `false` se sei alla fine
- `pageSize` (number): Numero di valutazioni ritornate in questa richiesta

**Campi di ogni valutazione (TUTTI i campi della tabella):**
- `id`: ID valutazione (per operazioni di eliminazione)
- `prezzoAI`: Prezzo stimato dall'AI
- `prezzoUmano`: Prezzo stimato dall'agente (null se non completato)
- `dataValutazione`: Data della valutazione
- `statoValutazione`: Stato della valutazione (in_verifica, solo_AI, approvata, ecc.)
- `descrizione`: Note sulla valutazione
- `nomeAgente`: Nome completo agente che effettua la valutazione
- `emailAgente`: Email agente
- `tipo`: Tipologia immobile
- `via`: Via/indirizzo
- `citta`: Città
- `cap`: CAP
- `provincia`: Provincia (sigla)
- `metratura`: Superficie in mq
- `condizioni`: Stato di conservazione
- `stanze`: Numero stanze
- `bagni`: Numero bagni
- `piano`: Piano
- `ascensore`: Presenza ascensore (true/false)
- `garage`: Presenza garage (true/false)
- `giardino`: Presenza giardino (true/false)
- `balcone`: Presenza balcone (true/false)
- `terrazzo`: Presenza terrazzo (true/false)
- `cantina`: Presenza cantina (true/false)
- `riscaldamento`: Tipo di riscaldamento
- `nomeProprietario`: Nome completo proprietario
- `emailProprietario`: Email proprietario
- `telefonoProprietario`: Telefono proprietario
- `descrizione`: Descrizione immobile
- `dataInserimento`: Data inserimento nel sistema

**Response (403):** Se non hai ROLE_ADMIN

---

### DELETE `/api/admin/valutazioni/solo-ai/{id}`
**Richiede:** `ROLE_ADMIN`

Elimina una valutazione AI per ID riga per riga dall'elenco.

**Path Parameters:**
- `id` (required): ID della valutazione AI da eliminare

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Valutazione AI eliminata con successo"
}
```

**Response (403):** Se non hai ROLE_ADMIN

---

### DELETE `/api/admin/valutazioni/in-verifica/{id}`
**Richiede:** `ROLE_ADMIN`

Elimina una valutazione in verifica per ID riga per riga dall'elenco.

**Path Parameters:**
- `id` (required): ID della valutazione in verifica da eliminare

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Valutazione in verifica eliminata con successo"
}
```

**Response (403):** Se non hai ROLE_ADMIN

---

### PUT `/api/admin/valutazioni/in-verifica/{id}`
**Richiede:** `ROLE_ADMIN`

Modifica i campi di una valutazione in verifica e dell'immobile collegato per ID.

**Path Parameters:**
- `id` (required): ID della valutazione da modificare

**Request Body:**
```json
{
  "prezzoAI": 220000,
  "prezzoUmano": 225000,
  "dataValutazione": "2024-01-15",
  "descrizione": "Valutazione aggiornata dall'admin",
  "tipo": "Appartamento",
  "via": "Via Roma 123",
  "citta": "Milano",
  "cap": "20100",
  "provincia": "MI",
  "metratura": 85,
  "condizioni": "Buone",
  "stanze": 3,
  "bagni": 2,
  "piano": 2,
  "ascensore": true,
  "garage": true,
  "giardino": false,
  "balcone": true,
  "terrazzo": false,
  "cantina": true,
  "riscaldamento": "Autonomo",
  "descrizioneImmobile": "Appartamento ristrutturato"
}
```

**Campi modificabili della valutazione:**
- `prezzoAI` (opzionale): Nuovo prezzo AI
- `prezzoUmano` (opzionale): Nuovo prezzo umano
- `dataValutazione` (opzionale): Nuova data valutazione (formato ISO: YYYY-MM-DD)
- `descrizione` (opzionale): Nuova descrizione valutazione

**Campi modificabili dell'immobile:**
- `tipo` (opzionale): Tipologia immobile
- `via` (opzionale): Indirizzo
- `citta` (opzionale): Città
- `cap` (opzionale): CAP
- `provincia` (opzionale): Provincia (sigla)
- `metratura` (opzionale): Superficie in mq
- `condizioni` (opzionale): Stato di conservazione
- `stanze` (opzionale): Numero stanze
- `bagni` (opzionale): Numero bagni
- `piano` (opzionale): Piano
- `ascensore` (opzionale): Presenza ascensore (boolean)
- `garage` (opzionale): Presenza garage (boolean)
- `giardino` (opzionale): Presenza giardino (boolean)
- `balcone` (opzionale): Presenza balcone (boolean)
- `terrazzo` (opzionale): Presenza terrazzo (boolean)
- `cantina` (opzionale): Presenza cantina (boolean)
- `riscaldamento` (opzionale): Tipo di riscaldamento
- `descrizioneImmobile` (opzionale): Descrizione immobile

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Valutazione aggiornata con successo"
}
```

**Response (403):** Se non hai ROLE_ADMIN

---

### PUT `/api/admin/valutazioni/solo-ai/{id}/assegna-agente`
**Richiede:** `ROLE_ADMIN`

Assegna un agente (già presente nel DB) a una valutazione con stato "solo_AI". Quando l'agente viene assegnato, lo stato della valutazione passa automaticamente a "in_verifica".

**Path Parameters:**
- `id` (required): ID della valutazione AI da aggiornare

**Request Body:**
```json
{
  "idAgente": 123
}
```

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Agente assegnato con successo"
}
```

**Errori:**
- `500` - Valutazione non trovata, agente non trovato, oppure la valutazione non è di tipo solo_AI

---

## 🏠 Immobili

### POST `/api/immobili/save`
Salva un nuovo immobile nel database.

**Autenticazione:** ❌ No

**Request:**
```json
{
  "via": "Via Roma 10",
  "citta": "Torino",
  "cap": "10154",           // (opzionale, fornito dal frontend se indirizzo valido)
  "tipologia": "Appartamento",
  "metratura": 85,
  "condizioni": "Buone condizioni",
  "stanze": 3,
  "bagni": 1,
  "riscaldamento": "Centralizzato",
  "id_stato_immobile": 2,
  "piano": 3,
  "ascensore": true,
  "garage": true,
  "giardino": false,
  "balcone": true,
  "terrazzo": false,
  "cantina": false
}
```

**Nota:**  
- Il campo `cap` viene inserito dal frontend solo se la validazione indirizzo lo restituisce.  
- Il campo `provincia` viene aggiunto dal backend in automatico in base alla città (`Torino`→`TO`, `Cuneo`→`CN`, ecc.).  
- L’utente non deve mai inserire manualmente la provincia.

**Response (200 OK):**
```json
{
  "id_immobile": 123,
  "via": "Via Roma 10",
  "citta": "Torino",
  "cap": "10154",
  "provincia": "TO",
  "tipologia": "Appartamento",
  "metratura": 85,
  "condizioni": "Buone condizioni",
  "stanze": 3,
  "bagni": 1,
  "riscaldamento": "Centralizzato",
  "id_stato_immobile": 2,
  "piano": 3,
  "ascensore": true,
  "garage": true,
  "giardino": false,
  "balcone": true,
  "terrazzo": false,
  "cantina": false,
  "prezzo": null,
  "descrizione": null,
  "data_inserimento": "2025-11-26"
}
```

**Logica backend:**  
- La provincia viene impostata automaticamente in base alla città (`Torino` → `TO`, `Cuneo` → `CN`, ecc.).  
- Il CAP può essere calcolato tramite validazione indirizzo o tabella zone.

---

## 📍 Validazione Indirizzo

### POST `/api/address/validate`
Valida un indirizzo usando l'API Geoapify

**Regole di validazione:**
- La città deve essere una tra: Torino, Cuneo, Alessandria, Asti (case insensitive).
- La via deve iniziare con un tipo valido (es: via, corso, viale, piazza, ecc.) e non deve contenere il nome della città.
- Se la città non è tra quelle accettate, la risposta sarà sempre `valid: false` e suggerimenti vuoti.
- Restituisce solo vie che iniziano con la stringa inserita (es: "Via Ernesto L" → tutte le vie che iniziano così).

**Autenticazione:** ❌ No

**Request:**
```json
{
  "via": "Via Ernesto L",
  "citta": "Torino"
}
```

**Response (200 OK) - Indirizzo valido:**
```json
{
  "valid": true,
  "suggestions": [
    {
      "displayName": "Via Ernesto Lancia, Torino, 10154",
      "via": "Via Ernesto Lancia",
      "citta": "Torino",
      "cap": "10154",
      "lat": 45.0801,
      "lon": 7.6622
    }
  ]
}
```

**Response (200 OK) - Indirizzo non trovato:**
```json
{
  "valid": false,
  "suggestions": []
}
```

**Campi della risposta:**
 - `valid` (boolean): `true` se l'indirizzo è stato trovato, `false` altrimenti
 - `suggestions` (array): Lista di indirizzi trovati con dettagli

**Campi di ogni suggestion:**
 - `displayName`: Nome completo dell'indirizzo formattato
 - `via`: Nome della via estratto
 - `citta`: Nome della città estratto
 - `cap`: Codice postale estratto
 - `lat`: Latitudine (coordinate geografiche)
 - `lon`: Longitudine (coordinate geografiche)

---

### GET `/api/address/test`
Verifica che l'endpoint di validazione indirizzi sia raggiungibile

**Response (200 OK):**
```
"Address validation API is working! ✅"
```

---

## 📧 Invio Email

### POST `/api/mail/send`
Invia una email tramite backend

**Autenticazione:** ❌ No (⚠️ solo per test - in produzione proteggi con ruoli ADMIN/AGENT)

**Request:**
```json
{
  "to": "destinatario@example.com",
  "subject": "Oggetto della mail",
  "message": "Corpo del messaggio"
}
```

**Response (200 OK):**
```
"Email inviata con successo ✅"
```

**Errori:**
- `400 Bad Request` - Body JSON mancante o malformato
- `500 Internal Server Error` - Errore SMTP (credenziali, connessione)


---

### GET `/api/mail/test`
Verifica che l'endpoint mail sia raggiungibile

**Response (200 OK):**
```
"Mail endpoint raggiungibile! ✅"
```

---

## 📨 Configurazione SMTP

### Gmail (con App Password)
```properties
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your-email@gmail.com
spring.mail.password=your-app-password
```

**Come ottenere App Password:**
1. Google Account → Sicurezza → Verifica in 2 passaggi (attiva)
2. Cerca "Password per le app" → Genera nuova password
3. Copia codice (16 caratteri) e usa in `application.properties`

### Mailtrap (testing)
```properties
spring.mail.host=smtp.mailtrap.io
spring.mail.port=2525
spring.mail.username=YOUR_MAILTRAP_USER
spring.mail.password=YOUR_MAILTRAP_PASS
```

---

## 🔍 Logica di Gestione Valutazioni e Agenti

### Stato Valutazione
Lo **stato della valutazione** non è memorizzato direttamente nella tabella `Immobili`, ma viene recuperato dalla tabella `Valutazioni`. Per ogni immobile:

1. Il sistema cerca la valutazione più recente nella tabella `Valutazioni` (ordinata per `Data_valutazione DESC`)
2. Recupera lo stato della valutazione dal campo `Id_stato_valutazione` che fa riferimento alla tabella `Stati_valutazione`

### Stati Valutazione Disponibili
- **`solo_AI`**: Valutazione generata esclusivamente dall'intelligenza artificiale, senza intervento umano
- **`in_verifica`**: Valutazione in fase di controllo da parte di un agente
- **`approvata`**: Valutazione verificata e approvata dall'agente

### Logica di Visualizzazione Agente
L'agente viene mostrato **solo** quando la valutazione richiede o ha ricevuto un intervento umano:

- ✅ **Mostra agente** se `statoValutazione` è `"in_verifica"` o `"approvata"`
  - L'agente viene recuperato dal campo `Id_agente` nella tabella `Valutazioni`
  - Visualizza: Nome e cognome completo dell'agente
  
- ❌ **NON mostra agente** se `statoValutazione` è `"solo_AI"`
  - Il campo `agenteAssegnato` sarà `null`
  - Questo garantisce che valutazioni automatiche non mostrino erroneamente un agente

### Implementazione Tecnica
- **Repository utilizzato**: `ValutazioneJpaRepository` con metodo Spring Data JPA
- **Query method**: `findByImmobileIdOrderByDataValutazioneDesc(Integer immobileId)`
- **Vantaggi**:
  - Usa l'ORM di Spring invece di query SQL dirette
  - Gestione automatica delle relazioni JPA (`StatoValutazione` e `User`)
  - Ordinamento automatico per data più recente
  - Type-safe e manutenibile

### Esempio Pratico
```java
// Nel frontend, quando visualizzi un immobile:
if (immobile.statoValutazione === "solo_AI") {
  // Mostra badge "Valutato da AI"
  // Non mostrare nome agente
} else if (immobile.statoValutazione === "in_verifica") {
  // Mostra badge "In verifica"
  // Mostra: "Agente: {immobile.agenteAssegnato}"
} else if (immobile.statoValutazione === "approvata") {
  // Mostra badge "Approvata"
  // Mostra: "Verificato da: {immobile.agenteAssegnato}"
}
```

---






