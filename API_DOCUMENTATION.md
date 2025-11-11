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
  "username": "admin@test.com",
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

### POST `/api/users/register`
Registrazione nuovo utente (Admin o Agente)

**Implementato da:** Simone

**Request:**
```json
{
  "nome": "Mario",
  "cognome": "Rossi",
  "email": "mario.rossi@example.com",
  "password": "Password123!",
  "tipoUtenteId": 1
}
```

**Parameters:**
- `nome` (required): Nome dell'utente
- `cognome` (required): Cognome dell'utente
- `email` (required, unique): Email dell'utente
- `password` (required): Password (verrà hashata con BCrypt)
- `tipoUtenteId` (required): ID del tipo utente
  - `1` = ADMIN
  - `2` = AGENT

**Response (200 OK):**
```json
{
  "idUtente": 5,
  "nome": "Mario",
  "cognome": "Rossi",
  "email": "mario.rossi@example.com",
  "dataRegistrazione": "2025-11-11",
  "tipoUtente": {
    "idTipo": 1,
    "nomeTipo": "Admin"
  }
}
```

**Response (400 Bad Request):**
```json
{
  "success": false,
  "message": "Email già registrata"
}
```

**Response (500 Server Error):**
```json
{
  "success": false,
  "message": "Tipo utente non trovato"
}
```

**Note:**
- La password viene automaticamente hashata con BCrypt
- La data di registrazione viene impostata automaticamente
- L'email deve essere unica nel sistema
- Questo endpoint è accessibile **senza autenticazione**

---

### GET `/api/users`
Ottieni lista di tutti gli utenti

**Implementato da:** Simone

**Richiede:** `ROLE_ADMIN`

**Response (200 OK):**
```json
[
  {
    "idUtente": 1,
    "nome": "Admin",
    "cognome": "Test",
    "email": "admin@test.com",
    "telefono": "3201234567",
    "via": "Via Roma 1",
    "citta": "Milano",
    "cap": "20100",
    "dataRegistrazione": "2025-11-01",
    "tipoUtente": {
      "idTipo": 1,
      "nomeTipo": "Admin"
    }
  },
  {
    "idUtente": 2,
    "nome": "Agent",
    "cognome": "Verdi",
    "email": "agent@test.com",
    "telefono": "3209876543",
    "via": "Via Milano 2",
    "citta": "Roma",
    "cap": "00100",
    "dataRegistrazione": "2025-11-02",
    "tipoUtente": {
      "idTipo": 2,
      "nomeTipo": "Agent"
    }
  }
]
```

**Response (403):** Se non hai ROLE_ADMIN

---

### GET `/api/users/{id}`
Ottieni dettagli di un utente specifico

**Implementato da:** Simone

**Richiede:** `ROLE_ADMIN`

**Path Parameters:**
- `id` (required): ID dell'utente

**Response (200 OK):**
```json
{
  "idUtente": 1,
  "nome": "Admin",
  "cognome": "Test",
  "email": "admin@test.com",
  "telefono": "3201234567",
  "via": "Via Roma 1",
  "citta": "Milano",
  "cap": "20100",
  "dataRegistrazione": "2025-11-01",
  "tipoUtente": {
    "idTipo": 1,
    "nomeTipo": "Admin"
  }
}
```

**Response (404 Not Found):**
```json
{
  "message": "Utente non trovato"
}
```

**Response (403):** Se non hai ROLE_ADMIN

---

### PUT `/api/users/{id}`
Aggiorna dati di un utente

**Implementato da:** Simone

**Richiede:** `ROLE_ADMIN`

**Path Parameters:**
- `id` (required): ID dell'utente da aggiornare

**Request:**
```json
{
  "nome": "Mario",
  "cognome": "Bianchi",
  "email": "mario.bianchi@example.com",
  "password": "NewPassword123!",
  "telefono": "3305555555",
  "via": "Via Napoli 10",
  "citta": "Napoli",
  "cap": "80100",
  "tipoUtente": {
    "idTipo": 2,
    "nomeTipo": "Agent"
  }
}
```

**Parameters:**
- `nome` (optional): Nuovo nome
- `cognome` (optional): Nuovo cognome
- `email` (optional): Nuova email
- `password` (optional): Nuova password (verrà hashata)
- `telefono` (optional): Nuovo numero di telefono
- `via` (optional): Nuovo indirizzo
- `citta` (optional): Nuova città
- `cap` (optional): Nuovo CAP
- `tipoUtente` (optional): Nuovo tipo utente

**Response (200 OK):**
```json
{
  "idUtente": 1,
  "nome": "Mario",
  "cognome": "Bianchi",
  "email": "mario.bianchi@example.com",
  "telefono": "3305555555",
  "via": "Via Napoli 10",
  "citta": "Napoli",
  "cap": "80100",
  "dataRegistrazione": "2025-11-01",
  "tipoUtente": {
    "idTipo": 2,
    "nomeTipo": "Agent"
  }
}
```

**Response (404 Not Found):**
```json
{
  "message": "Utente non trovato"
}
```

**Response (403):** Se non hai ROLE_ADMIN

**Note:**
- Se la password non viene fornita, rimane invariata
- Solo i campi forniti vengono aggiornati
Ottieni informazioni utente loggato

**Response (200 OK):**
```json
{
  "username": "admin@test.com",
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
    "contrattiConclusi": 15,
    "valutazioniInCorso": 8,
    "valutazioniConAI": 5,
    "contrattiConclusiMensili": 3,
    "valutazioniInCorsoMensili": 2,
    "valutazioniConAIMensili": 1
  },
  "ultimi10Immobili": [
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
    // ... altri 8 immobili (totale 10)
  ]
}
```

**Statistiche:**
- **Totali (dall'inizio):**
  - `contrattiConclusi`: Contratti con stato "chiuso"
  - `valutazioniInCorso`: Valutazioni con stato "in_verifica"
  - `valutazioniConAI`: Valutazioni con stato "solo_AI"
- **Mensili (ultimi 30 giorni):**
  - `contrattiConclusiMensili`: Contratti conclusi nell'ultimo mese (basato su `Data_inizio`)
  - `valutazioniInCorsoMensili`: Valutazioni in corso nell'ultimo mese (basato su `Data_valutazione`)
  - `valutazioniConAIMensili`: Valutazioni con AI nell'ultimo mese (basato su `Data_valutazione`)

**Ultimi 10 Immobili:**
- `tipo`: Tipologia immobile (Appartamento, Villa, Ufficio, ecc.)
- `nomeProprietario`: Nome completo proprietario
- `dataInserimento`: Data inserimento immobile
- `agenteAssegnato`: Nome agente che gestisce l'immobile (dalla tabella Valutazioni, `null` se non assegnato)

**Response (403):** Se non hai ROLE_ADMIN

---

### GET `/api/admin/immobili`
**Richiede:** `ROLE_ADMIN`

API per paginazione immobili (carica altri immobili oltre i primi 10 della dashboard)

**Query Parameters:**
- `page` (opzionale, default: 0): Numero pagina (0-based)
- `size` (opzionale, default: 10): Elementi per pagina

**Esempi:**
```
GET /api/admin/immobili?page=0&size=10  → Primi 10 immobili
GET /api/admin/immobili?page=1&size=10  → Immobili 11-20
GET /api/admin/immobili?page=2&size=10  → Immobili 21-30
```

**Response (200 OK):**
```json
{
  "immobili": [
    {
      "tipo": "Appartamento",
      "nomeProprietario": "Paolo Verdi",
      "dataInserimento": "2025-11-07",
      "agenteAssegnato": "Carlo Rossi"
    },
    {
      "tipo": "Villa",
      "nomeProprietario": "Laura Neri",
      "dataInserimento": "2025-11-06",
      "agenteAssegnato": null
    }
    // ... altri immobili (fino a 10)
  ],
  "currentPage": 1,
  "pageSize": 10,
  "totalImmobili": 45,
  "totalPages": 5,
  "hasNext": true,
  "hasPrevious": true
}
```

**Campi Paginazione:**
- `immobili`: Array immobili della pagina corrente
- `currentPage`: Numero pagina corrente (0-based)
- `pageSize`: Elementi per pagina
- `totalImmobili`: Totale immobili nel database
- `totalPages`: Totale pagine (usa questo per creare pulsanti [1][2][3][4][5])
- `hasNext`: `true` se esiste pagina successiva
- `hasPrevious`: `true` se esiste pagina precedente

**Response (403):** Se non hai ROLE_ADMIN

---

## 🏢 Dashboard Agente

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
- `contrattiConclusi`: Contratti conclusi dall'agente (stato "chiuso")
- `valutazioniInCorso`: Valutazioni in verifica dell'agente (stato "in_verifica")
- `valutazioniConAI`: Valutazioni AI assegnate all'agente (stato "solo_AI")

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
| `/api/admin/dashboard` | GET | ✅ Sì | ADMIN | Dashboard admin (statistiche + ultimi 10 immobili) |
| `/api/admin/immobili` | GET | ✅ Sì | ADMIN | Lista immobili paginata (query: page, size) |
| `/api/agent/dashboard` | GET | ✅ Sì | AGENT | Dashboard agente (statistiche personali) |

---

## 🛡️ Codici HTTP

- **200** - OK
- **401** - Non autenticato / Credenziali errate
- **403** - Autenticato ma senza permessi
- **500** - Errore server

---

## 📝 Note Importanti

### Paginazione
- Dashboard ritorna sempre **primi 10 immobili**
- Usa `/api/admin/immobili?page=X` per caricare altri immobili
- `totalPages` indica quanti pulsanti creare: [1] [2] [3] [4] [5]
- `page` è 0-based: page=0 → immobili 1-10, page=1 → immobili 11-20

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
   
2. Dashboard
   GET /api/admin/dashboard → Statistiche + primi 10 immobili
   
3. Carica Altri (opzionale)
   GET /api/admin/immobili?page=1 → Immobili 11-20
   GET /api/admin/immobili?page=2 → Immobili 21-30
   
4. Logout
   POST /api/auth/logout → Sessione invalidata
```

---



