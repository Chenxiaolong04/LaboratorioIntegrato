# 📡 API REST - Documentazione Backend


Base URL: `http://localhost:8080`

**Aggiornamento importante:**
- Tutte le API che restituiscono immobili, contratti o valutazioni NON includono più il campo `dataInserimento`/`data_inserimento` nel JSON di risposta.
- Quando si salva un immobile, se l'email del proprietario non esiste, viene creato automaticamente un utente di tipo **cliente** (senza password) e associato all'immobile.
- Se l'utente esiste già, viene associato come proprietario.

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


### GET `/api/admin/dashboard`
**Richiede:** `ROLE_ADMIN`

**Response (200 OK):**
```json
{
  "statistics": {
    "totaleImmobili": 50,
    "immobiliInVerifica": 8,
    "contrattiConclusi": 15,
    "fatturatoTotale": 2500000,
    "immobiliRegistratiMensili": 12,
    "immobiliRegistratiSettimanali": 3,
    "totaleAgenti": 5,
    "agentiStage": 2
  },
  "top3Agenti": [
    {
      "nomeAgente": "Luigi Verdi",
      "numeroContratti": 8,
      "fatturato": 800000
    }
  ],
  "contrattiPerMese": [
    {
      "mese": "11/2025",
      "numeroContratti": 5,
      "totalePrezzoImmobili": 750000
    }
  ],
  "agenti": [
    {
      "nome": "Luigi",
      "cognome": "Verdi",
      "contrattiConclusi": 5,
      "immobiliInGestione": 3,
      "fatturato": 500000
    }
  ],
  "tempoAIaPresaInCarico": {
    "giorni": 2,
    "ore": 5,
    "minuti": 30,
    "secondi": 15,
    "totaleSecondi": 192615
  },
  "tempoPresaInCaricoaContratto": {
    "giorni": 7,
    "ore": 12,
    "minuti": 0,
    "secondi": 0,
    "totaleSecondi": 648000
  },
  "valutazionePerformancePresaInCarico": "ottimo",
  "valutazionePerformanceContratto": "buono",
  "immobiliPerTipo": {
    "Appartamento": 8,
    "Villa": 3,
    "Attico": 3,
    "Loft": 2
  }
}
```

**Campi risposta:**
- `statistics` (object): Statistiche admin
- `top3Agenti` (array): Top 3 agenti per numero di contratti conclusi
- `contrattiPerMese` (array): Contratti stipulati per mese negli ultimi 6 mesi
- `agenti` (array): Lista di tutti gli agenti con statistiche complete
- `tempoAIaPresaInCarico` (object): Tempo medio AI → presa in carico agente
- `tempoPresaInCaricoaContratto` (object): Tempo medio presa in carico → contratto firmato
- `valutazionePerformancePresaInCarico` (string): Performance fase registrazione → presa in carico (eccellente ≤3d, ottimo ≤5d, buono ≤7d, standard >7d)
- `valutazionePerformanceContratto` (string): Performance fase presa in carico → contratto (eccellente ≤7d, ottimo ≤14d, buono ≤30d, standard >30d)
- `immobiliPerTipo` (object): Conteggio immobili per tipologia (solo: Appartamento, Villa, Attico, Loft)

**Statistiche generali:**
- `totaleImmobili`: Totale immobili nel database
- `immobiliInVerifica`: Immobili con valutazione in stato "in_verifica"
- `contrattiConclusi`: Contratti con stato "chiuso"
- `fatturatoTotale`: Somma di prezzoUmano da tutte le valutazioni dei contratti conclusi
- `immobiliRegistratiMensili`: Immobili registrati negli ultimi 30 giorni
- `immobiliRegistratiSettimanali`: Immobili registrati negli ultimi 7 giorni
- `totaleAgenti`: Numero di agenti (esclusi quelli con contratto "stage")
- `agentiStage`: Numero di agenti con contratto "stage"

**Campi di ogni agente:**
- `nome`: Nome dell'agente
- `cognome`: Cognome dell'agente
- `contrattiConclusi`: Numero di contratti con stato "chiuso" gestiti dall'agente
- `immobiliInGestione`: Numero di valutazioni con stato "in_verifica" assegnate all'agente
- `fatturato`: Somma dei `Prezzo_Umano` delle valutazioni collegate ai contratti chiusi dell'agente

**Nota sugli agenti:**
Gli agenti sono ordinati in modo decrescente per numero di contratti conclusi. Il fatturato viene calcolato sommando il campo `Prezzo_Umano` dalla tabella Valutazioni per tutti i contratti chiusi dell'agente.

**Tempi di processo e performance:**
- `tempoAIaPresaInCarico`: Tempo medio tra la registrazione dell'immobile (valutazione AI) e la presa in carico da parte di un agente
  - `giorni`: Numero di giorni
  - `ore`: Numero di ore (0-23)
  - `minuti`: Numero di minuti (0-59)
  - `secondi`: Numero di secondi (0-59)
  - `totaleSecondi`: Tempo totale in secondi
- `tempoPresaInCaricoaContratto`: Tempo medio tra la presa in carico da parte dell'agente e la firma del contratto
  - Stessa struttura di `tempoAIaPresaInCarico`
- `valutazionePerformancePresaInCarico`: Valutazione della velocità di presa in carico (eccellente se ≤3 giorni, ottimo se ≤5 giorni, buono se ≤7 giorni, standard se >7 giorni)
- `valutazionePerformanceContratto`: Valutazione della velocità di firma contratto dopo presa in carico (eccellente se ≤7 giorni, ottimo se ≤14 giorni, buono se ≤30 giorni, standard se >30 giorni)

**Response (403):** Se non hai ROLE_ADMIN

---

### GET `/api/admin/immobili?offset=0&limit=12`
**Richiede:** `ROLE_ADMIN`

Restituisce tutti gli immobili con dettagli completi inclusi **prezzoAI** e **prezzoUmano** dalla valutazione più recente. Supporta caricamento progressivo con offset/limit.

**Query Parameters:**
- `offset` (opzionale, default: 0): quanti immobili sono già stati caricati
- `limit` (opzionale, default: 12): quanti immobili caricare

**Response (200 OK):**
```json
{
  "immobili": [
    {
      "id": 123,
      "via": "Via Roma 12",
      "citta": "Torino",
      "cap": "10100",
      "provincia": "TO",
      "tipologia": "Appartamento",
      "metratura": 85,
      "condizioni": "Buone condizioni",
      "stanze": 3,
      "bagni": 1,
      "riscaldamento": "Centralizzato",
      "piano": 3,
      "ascensore": true,
      "garage": true,
      "giardino": false,
      "balcone": true,
      "terrazzo": false,
      "cantina": false,
      "prezzo": null,
      "descrizione": "Appartamento luminoso in centro",
      "dataRegistrazione": "2025-11-10T14:30:00",
      "statoImmobile": "disponibile",
      "nomeProprietario": "Luca Bianchi",
      "emailProprietario": "luca.bianchi@email.com",
      "telefonoProprietario": "3201234567",
      "prezzoAI": 215000,
      "prezzoUmano": 220000,
      "dataValutazione": "2025-11-12T10:15:00",
      "descrizioneValutazione": "Valutazione completata",
      "statoValutazione": "approvata",
      "agenteAssegnato": "Mattia Rossi"
    }
  ],
  "nextOffset": 10,
  "hasMore": true,
  "pageSize": 10,
  "total": 45
}
```

**Campi risposta:**
- `immobili` (array): Array di immobili con dettagli completi
- `nextOffset` (number): Offset da usare per la prossima richiesta
- `hasMore` (boolean): `true` se ci sono altri immobili, `false` se sei alla fine
- `pageSize` (number): Numero di immobili ritornati in questa richiesta
- `total` (number): Numero totale di immobili nel sistema

**Campi di ogni immobile (dalla tabella Immobili):**
- `id`: ID immobile
- `via`: Indirizzo
- `citta`: Città
- `cap`: CAP
- `provincia`: Provincia (sigla)
- `tipologia`: Tipo immobile (Appartamento, Villa, ecc.)
- `metratura`: Superficie in mq
- `condizioni`: Stato di conservazione
- `stanze`: Numero stanze
- `bagni`: Numero bagni
- `riscaldamento`: Tipo di riscaldamento
- `piano`: Piano
- `ascensore`: Presenza ascensore (boolean)
- `garage`: Presenza garage (boolean)
- `giardino`: Presenza giardino (boolean)
- `balcone`: Presenza balcone (boolean)
- `terrazzo`: Presenza terrazzo (boolean)
- `cantina`: Presenza cantina (boolean)
- `prezzo`: Prezzo immobile (può essere null)
- `descrizione`: Descrizione immobile
- `dataRegistrazione`: Data registrazione immobile
- `statoImmobile`: Stato dell'immobile (es: disponibile, venduto, ecc.)

**Campi proprietario:**
- `nomeProprietario`: Nome completo proprietario
- `emailProprietario`: Email proprietario
- `telefonoProprietario`: Telefono proprietario

**Campi valutazione (dalla valutazione più recente):**
- `prezzoAI`: Prezzo stimato dall'AI (null se nessuna valutazione)
- `prezzoUmano`: Prezzo stimato dall'agente (null se non completato)
- `dataValutazione`: Data della valutazione (null se nessuna valutazione)
- `descrizioneValutazione`: Descrizione della valutazione (null se nessuna valutazione)
- `statoValutazione`: Stato della valutazione (solo_AI, in_verifica, approvata) (null se nessuna valutazione)
- `agenteAssegnato`: Nome completo agente che gestisce la valutazione (null se non assegnato o stato solo_AI)

**Nota importante:**
Questa API restituisce **tutti i dati** disponibili per ogni immobile, inclusi i prezzi AI e umani dalla tabella Valutazioni. Gli immobili sono ordinati per data registrazione discendente (più recenti prima).

**Response (403):** Se non hai ROLE_ADMIN

---

### GET `/api/admin/immobili (vecchia versione)?page=0&size=10`
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
      "dataRegistrazione": "2025-11-10",
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
      // "dataRegistrazione": "2024-12-20", // campo non più restituito
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
      // "dataRegistrazione": "2024-12-15", // campo non più restituito
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
// `dataRegistrazione`: Data registrazione immobile (non restituito nel JSON)
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
      // "dataRegistrazione": "2024-12-20" // campo non più restituito
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
// `dataRegistrazione`: Data registrazione immobile (non restituito nel JSON)

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
      // "dataRegistrazione": "2024-12-20" // campo non più restituito
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
// `dataRegistrazione`: Data registrazione immobile (non restituito nel JSON)

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

## 👤 CRUD Utenti

Tutte le API richiedono il ruolo `ROLE_ADMIN`.

### POST `/api/users/register`
Crea un nuovo utente.

**Request:**
```json
{
  "nome": "Mario",
  "cognome": "Rossi",
  "email": "mario.rossi@email.com",
  "password": "password123",
  "telefono": "3201234567",
  "via": "Via Roma 10",
  "citta": "Torino",
  "cap": "10100",
  "tipoUtente": { "idTipo": 1 }
}
```
**Response (200 OK):**
Restituisce l'utente creato.

---

### GET `/api/users`
Restituisce la lista di tutti gli utenti.

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "nome": "Mario",
    "cognome": "Rossi",
    "email": "mario.rossi@email.com",
    ...
  },
  ...
]
```

---

### GET `/api/users/{id}`
Restituisce i dati di un utente specifico.

**Response (200 OK):**
```json
{
  "id": 1,
  "nome": "Mario",
  "cognome": "Rossi",
  "email": "mario.rossi@email.com",
  ...
}
```

---

### PUT `/api/users/{id}`
Aggiorna i dati di un utente specifico.

**Request:**
```json
{
  "nome": "Mario",
  "cognome": "Rossi",
  "email": "nuova@email.com",
  ...
}
```
**Response (200 OK):**
Restituisce l'utente aggiornato.

---

### DELETE `/api/users/{id}`
Elimina l'utente con l'id specificato.

**Response (200 OK):**
Nessun contenuto (204 No Content).

**Response (404):**
Se l'utente non esiste.

---

### PUT `/api/users/profile`
Aggiorna il profilo personale dell'utente autenticato.

**Request:**
```json
{
  "nome": "Mario",
  "cognome": "Rossi",
  ...
}
```
**Response (200 OK):**
Restituisce il profilo aggiornato.

---

### GET `/api/users/profile`
Restituisce il profilo personale dell'utente autenticato.

**Response (200 OK):**
```json
{
  "id": 1,
  "nome": "Mario",
  "cognome": "Rossi",
  "email": "mario.rossi@email.com",
  ...
}
```

---

## 🏠 Immobili

### POST `/api/immobili/save`
Endpoint completo per registrazione immobile: **salva immobile → valuta automaticamente → invia mail riepilogativa**.

**Autenticazione:** ❌ No

**Request:**
```json
{
  "via": "Via Roma 123",
  "citta": "Torino",
  "cap": "10100",
  "tipologia": "Appartamento",
  "metratura": 80,
  "stanze": 3,
  "bagni": 1,
  "condizioni": "Buone condizioni",
  "riscaldamento": "Centralizzato",
  "piano": 3,
  "ascensore": true,
  "garage": true,
  "giardino": false,
  "balcone": true,
  "terrazzo": false,
  "cantina": false,
  "nome": "Mario",
  "cognome": "Rossi",
  "email": "mario.rossi@email.com",
  "telefono": "3331234567"
}
```

**Nota:**  
- Il campo `cap` viene inserito dal frontend solo se la validazione indirizzo lo restituisce.  
- Il campo `provincia` viene aggiunto dal backend in automatico in base alla città (`Torino`→`TO`, `Cuneo`→`CN`, ecc.).  
- L’utente non deve mai inserire manualmente la provincia.

**Logica proprietario:**  
Se l'email del proprietario non esiste nel sistema, viene creato automaticamente un utente di tipo **cliente** (senza password) e associato all'immobile. Se l'utente esiste già, viene semplicemente associato come proprietario. Questo processo è trasparente per il frontend: non è necessario gestire la registrazione manuale del proprietario.

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
  // "dataRegistrazione": "2025-11-26" // campo non più restituito
}
```

**Logica backend:**  
- La provincia viene impostata automaticamente in base alla città (`Torino` → `TO`, `Cuneo` → `CN`, ecc.).  
- Il CAP può essere calcolato tramite validazione indirizzo o tabella zone.

---

## 📊 Valutazione Immobili
La **valutazione immobiliare** consiste nel calcolo automatico del prezzo dell'immobile tramite algoritmo AI avanzato.

### 💰 Logica di Calcolo Algoritmo AI

Quando viene registrato un immobile, il sistema calcola il prezzo stimato (`prezzoAI`) utilizzando un **algoritmo multi-fattoriale** che considera:

#### 1. **Quotazione Base (da CAP)**
Il sistema recupera il prezzo medio al mq dalla tabella `zone` in base al CAP dell'immobile:
- Centro Torino (10121): 4.200 €/mq
- Crocetta (10128): 3.700 €/mq
- Mirafiori Sud (10135): 1.500 €/mq
- E oltre 30 zone mappate...

**Se il CAP non è mappato, la valutazione restituisce 0.**

#### 2. **Coefficiente di Efficienza Funzionale (C_Funzionale)**
Verifica l'adeguatezza di bagni e stanze rispetto alla metratura:
- **Bagni**: Penalità -5% se bagni < (metratura / 70)
- **Stanze**: Penalità -3% se stanze > (metratura / 20)

#### 3. **Coefficiente Qualitativo (C_Qualitativo)**
Combina condizioni e tipologia:

**Condizioni:**
- Nuovo: +15%
- Ottimo: +10%
- Buono: 0% (neutro)
- Da ristrutturare: -25%

**Tipologia:**
- Villa: +30%
- Attico/Loft: +12%
- Appartamento: 0% (neutro)

#### 4. **Moltiplicatore Accessori e Caratteristiche (M_Finale)**
Somma percentuali per ogni dotazione presente:
- **Garage**: +25% (bonus maggiorato)
- **Terrazzo**: +12%
- **Giardino**: +10%
- **Ascensore**: +8%
- **Balcone**: +5%
- **Cantina**: +3%

**Riscaldamento:**
- Teleriscaldamento: +8%
- Autonomo: +5%
- Centralizzato (obsoleto): -3%

**Piano/Altezza:**
- **Villa multi-livello** (piano > 1): +10% fisso
- **Appartamento/Loft/Attico**: +2% per ogni piano

#### 5. **Formula Finale**
```
Prezzo_AI = (metratura × quotazione_CAP) 
            × C_Funzionale 
            × C_Qualitativo 
            × M_Finale
```

**Esempio di calcolo:**
```
Immobile: Appartamento 85 mq, CAP 10128 (Crocetta), Piano 3
Condizioni: Buone, Con: Ascensore + Balcone + Garage
Bagni: 2, Stanze: 3, Riscaldamento: Autonomo

1. Base: 85 × 3.700 = 314.500 €
2. C_Funzionale: 1.00 (bagni OK) × 1.00 (stanze OK) = 1.00
3. C_Qualitativo: 1.00 (buono) × 1.00 (appartamento) = 1.00
4. M_Finale: 1 + 0.08 (ascensore) + 0.05 (balcone) + 0.25 (garage) + 0.05 (autonomo) + 0.06 (3 piani) = 1.49
5. Prezzo_AI = 314.500 × 1.00 × 1.00 × 1.49 = 468.605 €
```

**Esempio di risposta:**
```json
{
  "idImmobile": 123,
  "prezzoAI": 468605
}
```

> **Nota**: L'algoritmo restituisce sempre un valore intero. Se metratura ≤ 0 o CAP non mappato, restituisce 0.

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
      "displayName": "Via Ernesto Lancia 5, Torino, 10154",
      "via": "Via Ernesto Lancia",
      "civico": "5",
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
  
- ❌ **NON mostrare agente** se `statoValutazione` è `"solo_AI"`
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


## Tabella Riassuntiva delle API

| Metodo | Rotta                  | Descrizione                        | Permessi richiesti |
|--------|------------------------|------------------------------------|--------------------|
| POST   | /api/auth/login        | Login utente                       | Nessuno            |
| GET    | /api/users             | Elenco utenti                      | ADMIN              |
| GET    | /api/users/{id}        | Dettaglio utente                   | ADMIN              |
| POST   | /api/users             | Creazione nuovo utente             | ADMIN              |
| PUT    | /api/users/{id}        | Modifica utente                    | ADMIN              |
| DELETE | /api/users/{id}        | Eliminazione utente                | ADMIN              |
| GET    | /api/statistics        | Statistiche immobiliari            | ADMIN, AGENT       |
| POST   | /api/mail/send         | Invio email                        | ADMIN, AGENT       |
| GET    | /api/immobili          | Elenco immobili                    | ADMIN, AGENT       |
| GET    | /api/immobili/{id}     | Dettaglio immobile                 | ADMIN, AGENT       |
| POST   | /api/immobili          | Creazione nuovo immobile           | ADMIN              |
| PUT    | /api/immobili/{id}     | Modifica immobile                  | ADMIN              |
| DELETE | /api/immobili/{id}     | Eliminazione immobile              | ADMIN              |
| GET    | /api/contratti         | Elenco contratti                   | ADMIN, AGENT       |
| GET    | /api/contratti/{id}    | Dettaglio contratto                | ADMIN, AGENT       |
| POST   | /api/contratti         | Creazione nuovo contratto          | ADMIN              |
| PUT    | /api/contratti/{id}    | Modifica contratto                 | ADMIN              |
| DELETE | /api/contratti/{id}    | Eliminazione contratto             | ADMIN              |
| GET    | /api/contratti/valutazione/{idValutazione}/pdf| Genera contratto PDF e invia email | Tutti       |
| GET    | /api/contratti/valutazione/{idValutazione}/pdf/preview | Genera PDF e invia email   | Tutti       |
| GET    | /api/contratti/test    | Test endpoint controller           | Tutti              |
| GET    | /api/valutazioni       | Elenco valutazioni                 | ADMIN, AGENT       |
| GET    | /api/valutazioni/{id}  | Dettaglio valutazione              | ADMIN, AGENT       |
| POST   | /api/valutazioni       | Creazione nuova valutazione        | ADMIN, AGENT       |
| PUT    | /api/valutazioni/{id}  | Modifica valutazione               | ADMIN, AGENT       |
| DELETE | /api/valutazioni/{id}  | Eliminazione valutazione           | ADMIN              |
| GET    | /api/home              | Pagina home                        | Nessuno            |
| GET    | /api/welcome           | Pagina di benvenuto                | Nessuno            |
| GET    | /api/error             | Gestione errori                    | Nessuno            |

---

## 📄 Generazione e Invio Contratti PDF via Email

### 📋 Panoramica
Il sistema genera automaticamente contratti di mediazione immobiliare in formato PDF professionale e li invia **esclusivamente via email** al proprietario e all'agente. Il PDF **non viene scaricato localmente**, ma inviato solo come allegato email.

---

### GET `/api/contratti/valutazione/{idValutazione}/pdf`
Genera il contratto PDF dalla valutazione e lo invia via email (solo invio, nessun download)

**Parametri:**
- `idValutazione`: ID della valutazione (dalla tabella `valutazioni`)

**Comportamento:**
- Se esiste già un contratto per la valutazione → lo utilizza
- Se NON esiste → crea automaticamente un nuovo contratto con:
  - Immobile dalla valutazione
  - Proprietario dall'immobile
  - Agente dalla valutazione
  - Data inizio: oggi
  - Data fine: +6 mesi (default)
  - Commissione: 3% (default)

**Funzionalità:**
1. ✅ Genera contratto PDF professionale completo
2. ✉️ Invia email al **proprietario** con PDF allegato
3. ✉️ Invia email all'**agente** con copia PDF
4. ✅ Restituisce conferma JSON (nessun download locale)

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Contratto generato e inviato via email con successo",
  "destinatari": {
    "proprietario": "mario.rossi@example.com",
    "agente": "agente@immobiliaris.demo"
  },
  "valutazioneId": 23,
  "contrattoId": 1
}
```

**Response (404):**
```json
{
  "error": "Valutazione non trovata con id: {idValutazione}"
}
```

**Response (400):**
```json
{
  "error": "La valutazione non ha un immobile associato"
}
```
oppure
```json
{
  "error": "La valutazione non ha un agente assegnato. Assegnare prima un agente."
}
```

**Response (500):**
```json
{
  "error": "Errore invio email: {messaggio}"
}
```

**Note:**
- Il PDF NON viene più scaricato localmente
- Il PDF viene inviato SOLO via email ai destinatari
- Gli errori email bloccano la risposta (status 500)

---

### GET `/api/contratti/valutazione/{idValutazione}/pdf/preview`
Stesso comportamento di `/pdf` (genera e invia via email, nessun download)

**Response:** Identico all'endpoint `/pdf`

**Prerequisiti:**
- La valutazione deve esistere
- La valutazione deve avere un immobile associato
- La valutazione deve avere un agente assegnato
- Se il contratto non esiste, viene creato automaticamente

---

### 📧 Email Inviate Automaticamente

**Email al Proprietario:**
- **Oggetto**: 📄 Contratto di Mediazione Immobiliare - [Via Immobile]
- **Contenuto**: Messaggio personalizzato con saluto, dettagli immobile, spiegazione contratto
- **Allegato**: PDF completo del contratto
- **Design**: HTML professionale con logo IMMOBILIARIS, box evidenziato, footer contatti

**Email all'Agente:**
- **Oggetto**: 📄 [COPIA] Contratto di Mediazione - [Via Immobile]
- **Contenuto**: Conferma invio al proprietario + autorizzazione a procedere con promozione immobile
- **Allegato**: Stessa copia del PDF

---

### 📄 Contenuto PDF Contratto (11 Sezioni)

1. **Intestazione Azienda** - IMMOBILIARIS S.R.L., sede Torino, P.IVA, REA, contatti
2. **Le Parti** - Mandante (venditore) e Agente con dati completi
3. **Oggetto Incarico** - Immobile dettagliato (via, CAP, tipologia, metratura, stanze, bagni, piano, ascensore, dotazioni)
4. **Condizioni Vendita** - Prezzo richiesto (da valutazione umana o AI)
5. **Durata Incarico** - Date inizio/fine, modalità cessazione
6. **Compenso Provvigione** - Percentuale + IVA (default 3%), maturazione diritto
7. **Dichiarazioni Mandante** - Conformità urbanistica, catastale, impianti, APE
8. **Obblighi e Clausola Penale** - Esclusiva, penale 70% per violazione, obblighi agente
9. **Trattamento Dati GDPR** - Consenso Reg. UE 2016/679
10. **Firme** - Spazi firma mandante e agente con timbro
11. **Clausole Vessatorie** - Approvazione specifica art. 1341-1342 C.C.

---

### 🛠️ Implementazione Tecnica

**Servizio PDF:** `PdfContrattoService.java`
- Libreria: iText PDF 5.5.13.3
- Layout: A4 professionale, font multipli (titoli 16pt, sottotitoli 12pt, normale 10pt)
- Tabelle per dati immobile, formattazione italiana date (dd/MM/yyyy)

**Servizio Email:** `EmailService.java`
- Metodo: `sendContrattoPdf(Contratto contratto, byte[] pdfBytes)`
- Template HTML con logo inline (`static/logo.png`)
- Allegato PDF via `ByteArrayResource`
- Doppio invio (proprietario + agente)

**Controller:** `ContrattoApiController.java`
- Endpoint: `/api/contratti/{id}/pdf` e `/pdf/preview`
- Risposta JSON (no download binario)
- Errori: 404 contratto non trovato, 500 errore email

---

### ⚠️ Requisiti e Configurazione

**Requisiti database:**
- Contratto completo con relazioni: `immobile`, `utente` (proprietario), `agente`, `valutazione`
- Email valide per proprietario e agente
- Prezzo disponibile: priorità `prezzo_umano` > `prezzo_ai`

**Configurazione SMTP (`application.properties`):**
```properties
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=xiao.chen@edu-its.it
spring.mail.password=[app-password]
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

**⚠️ IMPORTANTE:**
- Il PDF **NON viene mai scaricato localmente** dal backend
- Il PDF esiste **SOLO come allegato email**
- Errori invio email → status 500 (bloccante)
- File `static/logo.png` deve esistere in resources

---

### 🧪 Esempio Utilizzo

```bash
curl http://localhost:8080/api/contratti/1/pdf

# Risposta JSON:
{
  "success": true,
  "message": "Contratto generato e inviato via email con successo",
  "destinatari": {
    "proprietario": "mario.rossi@example.com",
    "agente": "agente@immobiliaris.demo"
  }
}
```

**Workflow:** API Call → Genera PDF → Email Proprietario → Email Agente → Risposta JSON ✅

---






