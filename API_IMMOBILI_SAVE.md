# 🏠 API POST `/api/immobili/save`

## Salva un nuovo immobile e crea/assegna automaticamente il proprietario

**Autenticazione:** ❌ No (endpoint pubblico)

---

## 📌 Request

### Formato piatto (consigliato):

```json
{
  "via": "Via Roma 123",
  "citta": "Torino",
  "cap": "10100",
  "provincia": "TO",
  "tipologia": "appartamento",
  "metratura": 85,
  "condizioni": "buono",
  "stanze": 3,
  "bagni": 2,
  "piano": 2,
  "riscaldamento": "autonomo",
  "ascensore": true,
  "garage": true,
  "giardino": false,
  "balcone": true,
  "terrazzo": false,
  "cantina": true,
  "prezzo": 250000,
  "descrizione": "Bellissimo appartamento con vista sulla città",
  "nome": "Mario",
  "cognome": "Rossi",
  "email": "mario.rossi@email.com",
  "telefono": "3201234567"
}
```

---

## 🔑 Campi

### Proprietario (obbligatori):
| Campo | Tipo | Descrizione |
|-------|------|-------------|
| `nome` | string | Nome proprietario |
| `cognome` | string | Cognome proprietario |
| `email` | string | Email proprietario (univoca) |
| `telefono` | string | Telefono proprietario |

### Immobile (obbligatori):
| Campo | Tipo | Descrizione |
|-------|------|-------------|
| `via` | string | Indirizzo |
| `citta` | string | Città (Torino, Cuneo, Alessandria, Asti) |
| `tipologia` | string | Tipo immobile (appartamento, villa, ufficio, ecc.) |
| `stanze` | integer | Numero stanze |
| `bagni` | integer | Numero bagni |

### Immobile (opzionali):
| Campo | Tipo | Descrizione |
|-------|------|-------------|
| `cap` | string | Codice postale |
| `provincia` | string | Sigla provincia (es. "TO") |
| `metratura` | integer | Superficie in mq |
| `condizioni` | string | Stato conservazione |
| `piano` | integer | Piano |
| `riscaldamento` | string | Tipo riscaldamento |
| `ascensore` | boolean | Presenza ascensore |
| `garage` | boolean | Presenza garage |
| `giardino` | boolean | Presenza giardino |
| `balcone` | boolean | Presenza balcone |
| `terrazzo` | boolean | Presenza terrazzo |
| `cantina` | boolean | Presenza cantina |
| `prezzo` | integer | Prezzo stimato |
| `descrizione` | string | Descrizione immobile |

---

## 📤 Response (200 OK)

```json
{
  "email": "mario.rossi@email.com",
  "idImmobile": 36,
  "via": "Via Roma 123",
  "citta": "Torino",
  "tipologia": "appartamento"
}
```

### Campi risposta:
| Campo | Tipo | Descrizione |
|-------|------|-------------|
| `email` | string | Email proprietario immobile |
| `idImmobile` | integer | ID generato per l'immobile |
| `via` | string | Indirizzo immobile |
| `citta` | string | Città immobile |
| `tipologia` | string | Tipo immobile |

---

## ⚙️ Logica Backend

### 1️⃣ Ricerca email nel database
```
Se email esiste → Usa User esistente
Se email NON esiste → Crea nuovo User
```

### 2️⃣ Creazione nuovo User (se email non trovata)
- **email**: Dalla richiesta
- **nome, cognome, telefono**: Dalla richiesta
- **password**: `BCrypt(email)` (auto-generata)
- **tipoUtente**: "Cliente" (assegnato automaticamente)
- **dataRegistrazione**: Data/ora attuale

### 3️⃣ Assegnazione proprietario
- User ID viene collegato all'immobile tramite foreign key `id_utente`

### 4️⃣ Calcolo provincia (auto)
Se non fornita, viene calcolata dalla città:
- `Torino` → `TO`
- `Cuneo` → `CN`
- `Alessandria` → `AL`
- `Asti` → `AT`

---

## 📊 Flusso Completo

```
┌─────────────────────────────────────┐
│  Cliente invia JSON Request         │
│  (dati immobile + proprietario)     │
└──────────────┬──────────────────────┘
               ↓
┌─────────────────────────────────────┐
│  Backend riceve richiesta           │
└──────────────┬──────────────────────┘
               ↓
┌─────────────────────────────────────┐
│  Deserializza JSON in DTO           │
└──────────────┬──────────────────────┘
               ↓
┌─────────────────────────────────────┐
│  Converte DTO → Entity Immobile     │
└──────────────┬──────────────────────┘
               ↓
┌─────────────────────────────────────┐
│  Calcola provincia da città         │
└──────────────┬──────────────────────┘
               ↓
┌─────────────────────────────────────┐
│  Ricerca email in UserRepository    │
└──────────────┬──────────────────────┘
               ↓
          ┌────┴────┐
          ↓         ↓
    ┌─────────┐   ┌──────────┐
    │ ESISTE  │   │ NON ESISTE
    └────┬────┘   └────┬─────┘
         │             │
         │         ┌─────────────────────────┐
         │         │  Crea nuovo User:       │
         │         │  • email, nome, cognome │
         │         │  • telefono             │
         │         │  • password = BCrypt()  │
         │         │  • tipo = "Cliente"     │
         │         │  • Salva → genera ID    │
         │         └────────────┬────────────┘
         │                      │
         └──────────────┬───────┘
                        ↓
┌─────────────────────────────────────┐
│  Assegna User all'immobile          │
│  (id_utente = User ID)              │
└──────────────┬──────────────────────┘
               ↓
┌─────────────────────────────────────┐
│  Salva immobile nel database        │
└──────────────┬──────────────────────┘
               ↓
┌─────────────────────────────────────┐
│  Ritorna Response con:              │
│  • email proprietario               │
│  • idImmobile                       │
│  • via, citta, tipologia            │
└─────────────────────────────────────┘
```

---

## ❌ Errori Possibili

### 400 Bad Request
Campi obbligatori mancanti

### 500 Internal Server Error
Errore nel salvataggio (es. vincoli database)

---

## ✅ Validazioni Backend

- ✔️ Email deve essere univoca nel sistema
- ✔️ Provincia calcolata automaticamente dalla città
- ✔️ Password auto-generata per nuovi utenti
- ✔️ TipoUtente "Cliente" assegnato automaticamente
- ✔️ Data registrazione impostata al momento della creazione

---

## 🧪 Esempi di Test

### cURL
```bash
curl -X POST http://localhost:8080/api/immobili/save \
  -H "Content-Type: application/json" \
  -d '{
    "via": "Via Roma 123",
    "citta": "Torino",
    "cap": "10100",
    "tipologia": "appartamento",
    "metratura": 85,
    "stanze": 3,
    "bagni": 2,
    "nome": "Mario",
    "cognome": "Rossi",
    "email": "mario.rossi@email.com",
    "telefono": "3201234567"
  }'
```

### Postman
1. **Method**: POST
2. **URL**: `http://localhost:8080/api/immobili/save`
3. **Headers**: 
   - `Content-Type: application/json`
4. **Body** (raw JSON):
```json
{
  "via": "Via Roma 123",
  "citta": "Torino",
  "cap": "10100",
  "tipologia": "appartamento",
  "metratura": 85,
  "stanze": 3,
  "bagni": 2,
  "nome": "Mario",
  "cognome": "Rossi",
  "email": "mario.rossi@email.com",
  "telefono": "3201234567"
}
```

---

## 📝 Note Importanti

1. **Nessuna autenticazione richiesta** - Endpoint pubblico
2. **Email univoca** - Ogni proprietario è identificato da email
3. **Password auto-generata** - Hash BCrypt dell'email
4. **Transparenza frontend** - Non è necessario gestire registrazione manuale
5. **Provincia auto-calcolata** - Non inviare manualmente dal frontend
