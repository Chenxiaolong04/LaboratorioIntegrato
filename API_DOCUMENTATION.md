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
  "message": "Benvenuto amministratore",
  "user": "admin@test.com",
  "role": "ADMIN",
  "statistics": {
    "contrattiConclusi": 5,
    "valutazioniInCorso": 3,
    "valutazioniConAI": 7,
    "immobiliInValutazione": 12
  }
}
```

**Statistiche globali:**
- `contrattiConclusi`: Numero totale contratti con stato "chiuso"
- `valutazioniInCorso`: Numero totale valutazioni con stato "in_verifica"
- `valutazioniConAI`: Numero totale valutazioni con stato "solo_AI"
- `immobiliInValutazione`: Numero totale immobili con stato "in valutazione"

**Response (403):** Se non hai ROLE_ADMIN

---

## 🏢 Dashboard Agente

### GET `/api/agent/dashboard`
**Richiede:** `ROLE_AGENT`

**Response (200 OK):**
```json
{
  "message": "Benvenuto agente",
  "user": "agente@test.com",
  "role": "AGENT",
  "statistics": {
    "contrattiConclusi": 2,
    "valutazioniInCorso": 1,
    "valutazioniConAI": 3,
    "immobiliInValutazione": 12
  }
}
```

**Statistiche personali dell'agente:**
- `contrattiConclusi`: Contratti conclusi dall'agente (stato "chiuso")
- `valutazioniInCorso`: Valutazioni in verifica dell'agente (stato "in_verifica")
- `valutazioniConAI`: Valutazioni AI assegnate all'agente (stato "solo_AI")
- `immobiliInValutazione`: Totale immobili in valutazione (non filtrato per agente)

**Response (403):** Se non hai ROLE_AGENT

---

## 🔧 Setup React/Frontend

### Axios Configuration
```javascript
import axios from 'axios';

axios.defaults.withCredentials = true; // IMPORTANTE per cookie sessione
axios.defaults.baseURL = 'http://localhost:8080';
```

### Esempio Login
```javascript
const login = async (email, password) => {
  const response = await axios.post('/api/auth/login', { email, password });
  return response.data;
};
```

---

## 📊 Riepilogo Endpoints

| Endpoint | Metodo | Auth | Ruolo | Descrizione |
|----------|--------|------|-------|-------------|
| `/api/auth/login` | POST | ❌ No | - | Login |
| `/api/auth/logout` | POST | ✅ Sì | - | Logout |
| `/api/auth/check` | GET | ❌ No | - | Check auth |
| `/api/auth/user` | GET | ✅ Sì | - | Info utente |
| `/api/admin/dashboard` | GET | ✅ Sì | ADMIN | Dashboard admin |
| `/api/agent/dashboard` | GET | ✅ Sì | AGENT | Dashboard agente |

---

## 🛡️ Codici HTTP

- **200** - OK
- **401** - Non autenticato / Credenziali errate
- **403** - Autenticato ma senza permessi
- **500** - Errore server




