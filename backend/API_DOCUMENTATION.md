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

### GET `/api/auth/user`
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



