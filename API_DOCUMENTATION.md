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
      "agenteAssegnato": "Luigi Verdi"
    },
    {
      "tipo": "Villa",
      "nomeProprietario": "Anna Bianchi",
      "dataInserimento": "2025-11-09",
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
- `agenteAssegnato`: Nome agente che gestisce (null se non assegnato)

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

### GET `/api/agent/dashboard`
**Richiede:** `ROLE_AGENT`

**Response (200 OK):**
```json
{
  "statistics": {
    "contrattiConclusi": 2,
    "valutazioniInCorso": 1,
    "valutazioniConAI": 3
  }
}
```

**Statistiche personali dell'agente:**
**Note:** 
- Le statistiche dell'agente sono **solo totali**, senza statistiche mensili
- L'agente vede solo le **proprie statistiche**, non quelle globali

**Response (403):** Se non hai ROLE_AGENT

---

## 📊 Riepilogo Endpoints

| Endpoint | Metodo | Auth | Ruolo | Descrizione |
|----------|--------|------|-------|-------------|
| `/api/auth/login` | POST | ❌ No | - | Login con credenziali |
| `/api/auth/logout` | POST | ✅ Sì | - | Logout e invalida sessione |
| `/api/auth/check` | GET | ❌ No | - | Verifica autenticazione |
| `/api/auth/user` | GET | ✅ Sì | - | Info utente loggato |
| `/api/admin/dashboard` | GET | ✅ Sì | ADMIN | Dashboard admin (statistiche + immobili con offset/limit per load-more) |
| `/api/admin/immobili` | GET | ✅ Sì | ADMIN | Immobili paginated (pagina-based pagination) |
| `/api/admin/contratti/chiusi` | GET | ✅ Sì | ADMIN | Lista contratti conclusi con dettagli immobili (offset/limit load-more) |
| `/api/admin/valutazioni/solo-ai` | GET | ✅ Sì | ADMIN | Lista valutazioni generate solo da AI (offset/limit load-more) |
| `/api/admin/valutazioni/solo-ai/{id}` | DELETE | ✅ Sì | ADMIN | Elimina valutazione AI per ID |
| `/api/admin/valutazioni/in-verifica` | GET | ✅ Sì | ADMIN | Lista valutazioni in verifica con TUTTI i campi (offset/limit load-more) |
| `/api/admin/valutazioni/in-verifica/{id}` | DELETE | ✅ Sì | ADMIN | Elimina valutazione in verifica per ID |
| `/api/admin/valutazioni/in-verifica/{id}` | PUT | ✅ Sì | ADMIN | Modifica valutazione e immobile per ID |
| `/api/agent/dashboard` | GET | ✅ Sì | AGENT | Dashboard agente (statistiche personali) |
| `/api/mail/send` | POST | ❌ No* | - | Invia email (⚠️ proteggere in prod) |
| `/api/mail/test` | GET | ❌ No | - | Verifica mail endpoint |

---

## 🛡️ Codici HTTP

- **200** - OK
- **401** - Non autenticato / Credenziali errate
- **403** - Autenticato ma senza permessi
- **500** - Errore server

---

## 📝 Note Importanti

### Caricamento progressivo ("Carica altri")
- Usa `/api/admin/dashboard?offset=X&limit=10` con il valore di `nextOffset` dalla risposta precedente
- Ogni richiesta aggiunge `pageSize` nuovi immobili
- Continua finché `hasMore` è `true`

### Statistiche Mensili
- Statistiche con suffisso `Mensili` contano dati **ultimi 30 giorni**
- Query SQL: `WHERE Data >= DATE_SUB(CURDATE(), INTERVAL 1 MONTH)`

### Campo `agenteAssegnato`
- Può essere `null` se l'immobile non ha agente
- Frontend: `agenteAssegnato || "Non assegnato"`

### Autenticazione
- Usa **sempre** `withCredentials: true` in Axios
- Backend usa **sessioni con cookie** (non JWT)
- CORS configurato per `http://localhost:3000`

---

## 🔄 Flusso Tipico

```
1. Login
   POST /api/auth/login → Cookie sessione salvato
   
2. Dashboard Admin
   GET /api/admin/dashboard?offset=0&limit=10 → Statistiche + primi 10 immobili
   
3. Carica altri immobili
   GET /api/admin/dashboard?offset=10&limit=10 → Immobili 11-20
   GET /api/admin/dashboard?offset=20&limit=10 → Immobili 21-30
   (Continua finché hasMore = false)
   
4. Visualizza contratti chiusi
   GET /api/admin/contratti/chiusi?offset=0&limit=10 → Contratti conclusi con dettagli immobili
   GET /api/admin/contratti/chiusi?offset=10&limit=10 → Prossimi 10 contratti
   (Continua finché hasMore = false)
   
5. Visualizza valutazioni solo AI
   GET /api/admin/valutazioni/solo-ai?offset=0&limit=10 → Valutazioni generate solo da AI
   GET /api/admin/valutazioni/solo-ai?offset=10&limit=10 → Prossime 10 valutazioni
   (Continua finché hasMore = false)
   
6. Logout
   POST /api/auth/logout → Sessione invalidata
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



