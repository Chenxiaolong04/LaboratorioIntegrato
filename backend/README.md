# 🏢 IMMOBILIARIS - Backend Spring Boot

Sistema backend per la gestione di un'agenzia immobiliare con valutazioni AI, gestione contratti e dashboard statistiche.

---

## 📋 Indice

1. [Tecnologie e Dipendenze](#-tecnologie-e-dipendenze)
2. [Architettura del Database](#-architettura-del-database)
3. [Modello ER - Entità e Relazioni](#-modello-er---entità-e-relazioni)
4. [API REST Documentation](#-api-rest-documentation)
5. [Configurazione e Avvio](#-configurazione-e-avvio)
6. [Sicurezza e Autenticazione](#-sicurezza-e-autenticazione)

---

## 🛠 Tecnologie e Dipendenze

### Framework Core
- **Spring Boot 3.5.7** - Framework principale per applicazioni Java enterprise
- **Java 17** - Versione LTS del linguaggio Java

### Dipendenze Principali

#### Spring Framework
```xml
<!-- Web MVC e REST API -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>

<!-- Persistenza dati con JPA/Hibernate -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>

<!-- Spring Security per autenticazione/autorizzazione -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>

<!-- Template Engine Thymeleaf -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-thymeleaf</artifactId>
</dependency>

<!-- Validazione Bean Validation -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>

<!-- Email (JavaMailSender) -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-mail</artifactId>
</dependency>
```

#### Database
```xml
<!-- MySQL Connector -->
<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
    <scope>runtime</scope>
</dependency>

<!-- H2 Database (in-memory per testing) -->
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>runtime</scope>
</dependency>
```

#### Utilità
```xml
<!-- Lombok - Riduzione boilerplate code -->
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <optional>true</optional>
</dependency>

<!-- iText PDF - Generazione contratti PDF -->
<dependency>
    <groupId>com.itextpdf</groupId>
    <artifactId>itextpdf</artifactId>
    <version>5.5.13.3</version>
</dependency>

<!-- SpringDoc OpenAPI - Documentazione Swagger -->
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.0.2</version>
</dependency>
```

#### Testing
```xml
<!-- Spring Boot Test -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>

<!-- Spring Security Test -->
<dependency>
    <groupId>org.springframework.security</groupId>
    <artifactId>spring-security-test</artifactId>
    <scope>test</scope>
</dependency>
```

---

## 🗄️ Architettura del Database

### Schema: `AgenziaImmobiliare`

Il database è progettato per gestire il ciclo completo di un'agenzia immobiliare: dalla registrazione degli immobili, alle valutazioni AI e umane, fino alla stipula dei contratti.

---

## 📊 Modello ER - Entità e Relazioni

### 1️⃣ **Tipi_utente** (Tabella di Lookup)
Definisce i ruoli degli utenti nel sistema.

**Attributi:**
- `Id_tipo` (PK, INT, AUTO_INCREMENT)
- `Nome` (VARCHAR(50), UNIQUE, NOT NULL) - "Cliente", "Agente", "Amministratore"
- `Descrizione` (VARCHAR(255))

**Valori predefiniti:**
- Cliente: Utente che cerca o vende immobili
- Agente: Agente immobiliare che valuta e gestisce immobili
- Amministratore: Utente con privilegi di gestione completa

---

### 2️⃣ **Utenti** (Entità Principale)
Gestisce tutti gli utenti del sistema (clienti, agenti, admin).

**Attributi:**
- `Id_utente` (PK, INT, AUTO_INCREMENT)
- `CF` (CHAR(16), UNIQUE) - Codice Fiscale
- `Nome` (VARCHAR(50), NOT NULL)
- `Cognome` (VARCHAR(50), NOT NULL)
- `Email` (VARCHAR(100), UNIQUE, NOT NULL)
- `Password` (VARCHAR(255), NOT NULL) - Hash BCrypt
- `Telefono` (VARCHAR(20))
- `Via` (VARCHAR(100))
- `Citta` (VARCHAR(100))
- `CAP` (CHAR(5))
- `Data_registrazione` (DATETIME, DEFAULT CURRENT_TIMESTAMP)
- `Id_tipo` (FK → Tipi_utente, ON DELETE SET NULL)
- `Contratto` (VARCHAR(100)) - Tipo contratto agente: "Indeterminato", "Determinato", "stage"

**Relazioni:**
- **1:N con Immobili** (come proprietario) - `Id_utente` → `Immobili.Id_utente`
- **1:N con Valutazioni** (come agente) - `Id_utente` → `Valutazioni.Id_agente`
- **1:N con Contratti** (come cliente/agente) - `Id_utente` → `Contratti.Id_utente/Id_agente`
- **N:1 con Tipi_utente** - `Id_tipo` → `Tipi_utente.Id_tipo`

---

### 3️⃣ **Stati_immobile** (Tabella di Lookup)
Stati del ciclo di vita di un immobile.

**Attributi:**
- `Id_stato_immobile` (PK, INT, AUTO_INCREMENT)
- `Nome` (VARCHAR(50), UNIQUE, NOT NULL)
- `Descrizione` (VARCHAR(255))

**Valori predefiniti:**
- `attivo`: Immobile con contratto di mandato esclusivo, pubblicato
- `venduto`: Immobile venduto
- `ritirato`: Immobile ritirato dal mercato

---

### 4️⃣ **Immobili** (Entità Principale)
Registrazione degli immobili in gestione.

**Attributi:**
- `Id_immobile` (PK, INT, AUTO_INCREMENT)
- `Via` (VARCHAR(255))
- `Citta` (VARCHAR(100))
- `CAP` (CHAR(5))
- `Provincia` (CHAR(2))
- `Tipologia` (VARCHAR(100)) - "Appartamento", "Villa", "Attico", "Loft"
- `Metratura` (INT)
- `Condizioni` (VARCHAR(100)) - "Nuovo", "Ristrutturato", "Buone condizioni", "Da ristrutturare"
- `Stanze` (INT)
- `Bagni` (INT)
- `Riscaldamento` (VARCHAR(100))
- `Piano` (INT)
- `Ascensore` (BOOLEAN, DEFAULT FALSE)
- `Garage` (BOOLEAN, DEFAULT FALSE)
- `Giardino` (BOOLEAN, DEFAULT FALSE)
- `Balcone` (BOOLEAN, DEFAULT FALSE)
- `Terrazzo` (BOOLEAN, DEFAULT FALSE)
- `Cantina` (BOOLEAN, DEFAULT FALSE)
- `Prezzo` (INT, NULL) - Prezzo finale dopo contratto
- `Descrizione` (TEXT)
- `Data_registrazione` (DATETIME, DEFAULT CURRENT_TIMESTAMP)
- `Id_stato_immobile` (FK → Stati_immobile, ON DELETE CASCADE)
- `Id_utente` (FK → Utenti, ON DELETE CASCADE) - Proprietario

**Relazioni:**
- **N:1 con Utenti** (proprietario) - `Id_utente` → `Utenti.Id_utente`
- **1:N con Valutazioni** - `Id_immobile` → `Valutazioni.Id_immobile`
- **1:N con Foto** - `Id_immobile` → `Foto.Id_immobile`
- **1:N con Contratti** - `Id_immobile` → `Contratti.Id_immobile`
- **N:1 con Stati_immobile** - `Id_stato_immobile` → `Stati_immobile.Id_stato_immobile`

---

### 5️⃣ **Stati_valutazione** (Tabella di Lookup)
Stati del processo di valutazione.

**Attributi:**
- `Id_stato_valutazione` (PK, INT, AUTO_INCREMENT)
- `Nome` (VARCHAR(50), UNIQUE, NOT NULL)
- `Descrizione` (VARCHAR(255))

**Valori predefiniti:**
- `solo_AI`: Valutazione generata automaticamente dall'AI
- `in_verifica`: Valutazione presa in carico da un agente
- `approvata`: Valutazione confermata dall'agente (pronta per contratto)

**Flusso Stati:**
```
solo_AI → in_verifica → approvata
```

---

### 6️⃣ **Valutazioni** (Entità Principale)
Valutazioni degli immobili (AI + umana).

**Attributi:**
- `Id_valutazione` (PK, INT, AUTO_INCREMENT)
- `Prezzo_AI` (INT, NULL) - Prezzo stimato dall'intelligenza artificiale
- `Prezzo_Umano` (INT, NULL) - Prezzo validato dall'agente
- `Data_valutazione` (DATETIME, DEFAULT CURRENT_TIMESTAMP)
- `Id_stato_valutazione` (FK → Stati_valutazione, ON DELETE CASCADE)
- `Descrizione` (TEXT)
- `Id_agente` (FK → Utenti, ON DELETE SET NULL) - Agente assegnato
- `Id_immobile` (FK → Immobili, ON DELETE CASCADE)

**Relazioni:**
- **N:1 con Immobili** - `Id_immobile` → `Immobili.Id_immobile`
- **N:1 con Utenti** (agente) - `Id_agente` → `Utenti.Id_utente`
- **1:N con Contratti** - `Id_valutazione` → `Contratti.Id_valutazione`
- **N:1 con Stati_valutazione** - `Id_stato_valutazione` → `Stati_valutazione.Id_stato_valutazione`

**Cardinalità:**
- Un immobile può avere **1 valutazione**
- Una valutazione appartiene a **1 immobile**
- Un agente può gestire **N valutazioni**
- Una valutazione può essere assegnata a **1 agente** (o NULL se stato = "solo_AI")

---

### 7️⃣ **Stati_contratto** (Tabella di Lookup)
Stati del ciclo di vita dei contratti.

**Attributi:**
- `Id_stato_contratto` (PK, INT, AUTO_INCREMENT)
- `Nome` (VARCHAR(50), UNIQUE, NOT NULL)
- `Descrizione` (VARCHAR(255))

**Valori predefiniti:**
- `bozza`: Contratto in preparazione
- `attivo`: Contratto firmato e attivo
- `scaduto`: Contratto scaduto
- `chiuso`: Contratto concluso con vendita

**Flusso Stati:**
```
bozza → attivo → chiuso/scaduto
```

---

### 8️⃣ **Contratti** (Entità Principale)
Contratti di mediazione immobiliare.

**Attributi:**
- `Id_contratto` (PK, INT, AUTO_INCREMENT)
- `Data_invio` (DATETIME) - Data invio contratto al cliente
- `Data_ricezione` (DATETIME) - Data ricezione contratto firmato
- `Data_inizio` (DATETIME) - Inizio validità contratto
- `Data_fine` (DATETIME) - Fine validità contratto (tipicamente +6 mesi)
- `Data_registrazione` (DATETIME, DEFAULT CURRENT_TIMESTAMP)
- `Id_stato_contratto` (FK → Stati_contratto, ON DELETE CASCADE)
- `Numero_contratto` (VARCHAR(50), UNIQUE) - Es: "C-2025-001"
- `Percentuale_commissione` (DECIMAL(5,2)) - Default 3.00%
- `Id_valutazione` (FK → Valutazioni, ON DELETE CASCADE)
- `Id_utente` (FK → Utenti, ON DELETE CASCADE) - Cliente/Proprietario
- `Id_richiesta` (FK → Richieste, ON DELETE CASCADE, NULL)
- `Id_immobile` (FK → Immobili, ON DELETE CASCADE)
- `Id_agente` (FK → Utenti, ON DELETE SET NULL) - Agente responsabile

**Relazioni:**
- **N:1 con Valutazioni** - `Id_valutazione` → `Valutazioni.Id_valutazione`
- **N:1 con Utenti** (cliente) - `Id_utente` → `Utenti.Id_utente`
- **N:1 con Utenti** (agente) - `Id_agente` → `Utenti.Id_utente`
- **N:1 con Immobili** - `Id_immobile` → `Immobili.Id_immobile`
- **N:1 con Stati_contratto** - `Id_stato_contratto` → `Stati_contratto.Id_stato_contratto`

**Cardinalità:**
- Una valutazione può generare **1 contratto**
- Un contratto è collegato a **1 valutazione**
- Un immobile può avere **1 contratto attivo**
- Un agente può gestire **N contratti**

---

### 9️⃣ **Foto** (Entità Secondaria)
Gestione foto degli immobili.

**Attributi:**
- `Id_foto` (PK, INT, AUTO_INCREMENT)
- `Nome` (VARCHAR(100))
- `Percorso` (VARCHAR(255))
- `Data_caricamento` (DATETIME, DEFAULT CURRENT_TIMESTAMP)
- `Copertina` (BOOLEAN, DEFAULT FALSE) - Foto di copertina
- `Id_immobile` (FK → Immobili, ON DELETE CASCADE)

**Relazioni:**
- **N:1 con Immobili** - `Id_immobile` → `Immobili.Id_immobile`

**Cardinalità:**
- Un immobile può avere **N foto**
- Una foto appartiene a **1 immobile**

---

### 🔟 **Zone** (Tabella di Riferimento)
Dati sui quartieri di Torino per calcolo prezzo medio al mq.

**Attributi:**
- `id_zona` (PK, INT, AUTO_INCREMENT)
- `nome_quartiere` (VARCHAR(50), NOT NULL)
- `cap` (VARCHAR(5), NOT NULL)
- `prezzo_medio_mq` (INT, NOT NULL)

**Uso:**
Utilizzata dall'AI per stimare il valore degli immobili in base al quartiere di Torino.

---

## 📐 Diagramma Cardinalità Principali

```
Tipi_utente (1) ──────< (N) Utenti
                              │
                              ├──────< (N) Immobili (come proprietario)
                              │          │
                              │          ├──────< (N) Foto
                              │          │
                              │          └──────< (1) Valutazioni
                              │                     │
                              ├──────< (N) Valutazioni (come agente)
                              │                     │
                              │                     └──────< (N) Contratti
                              │
                              └──────< (N) Contratti (come cliente/agente)

Stati_immobile (1) ──────< (N) Immobili
Stati_valutazione (1) ──────< (N) Valutazioni
Stati_contratto (1) ──────< (N) Contratti
```

---

## 🔄 Flusso Operativo del Sistema

### 1. **Registrazione Immobile**
```
Cliente → Form registrazione immobile → Backend
                                           ↓
                                   Crea Immobile
                                           ↓
                              Genera Valutazione AI automatica
                                           ↓
                                   Stato: "solo_AI"
```

### 2. **Presa in Carico Agente**
```
Agente → Dashboard valutazioni AI → Seleziona immobile
                                           ↓
                                   Assegna se stesso
                                           ↓
                          Aggiorna Stato: "in_verifica"
                                           ↓
                          Inserisce Prezzo_Umano
                                           ↓
                          Aggiorna Stato: "approvata"
```

### 3. **Generazione Contratto**
```
Sistema → Valutazione approvata → Crea Contratto automatico
                                           ↓
                                   Stato: "bozza"
                                           ↓
                          Genera PDF professionale
                                           ↓
                          Invia al proprietario
                                           ↓
                          Proprietario firma
                                           ↓
                          Stato: "attivo"
```

### 4. **Conclusione Vendita**
```
Agente → Vendita completata → Aggiorna Contratto
                                           ↓
                                   Stato: "chiuso"
                                           ↓
                          Calcolo commissione
                                           ↓
                          Statistiche aggiornate
```

---

## 🔐 Regole di Integrità Referenziale

### ON DELETE CASCADE
Elimina automaticamente i record dipendenti:
- **Immobili** → Valutazioni, Foto, Contratti
- **Valutazioni** → Contratti
- **Stati** → Record correlati

### ON DELETE SET NULL
Imposta NULL nei record dipendenti:
- **Utenti** (agente) → Valutazioni, Contratti (campo agente)
- **Tipi_utente** → Utenti

### ON UPDATE CASCADE
Aggiorna automaticamente le FK quando cambia la PK.

---

## 📡 API REST Documentation

### Base URL
```
http://localhost:8080
```

### Documentazione Interattiva (Swagger)
```
http://localhost:8080/swagger-ui/index.html
```

### OpenAPI JSON
```
http://localhost:8080/v3/api-docs
```

---

## 🔐 Autenticazione

### Endpoint di Autenticazione

#### `POST /api/auth/login`
Login con credenziali JSON.

**Request:**
```json
{
  "email": "admin@immobili.it",
  "password": "admin123"
}
```

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Login effettuato con successo",
  "username": "Luca Bianchi",
  "roles": ["ROLE_ADMIN"]
}
```

**Response (401):**
```json
{
  "success": false,
  "message": "Credenziali non valide"
}
```

---

#### `POST /api/auth/logout`
Logout e invalidazione sessione.

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Logout effettuato con successo"
}
```

---

#### `GET /api/auth/check`
Verifica autenticazione corrente.

**Response (200 OK):**
```json
{
  "authenticated": true
}
```

---

#### `GET /api/auth/user`
Informazioni utente loggato.

**Response (200 OK):**
```json
{
  "username": "Luca Bianchi",
  "roles": ["ROLE_ADMIN"],
  "authenticated": true
}
```

---

## 👨‍💼 API Dashboard Admin

### `GET /api/admin/dashboard`
**Permesso richiesto:** `ROLE_ADMIN`

Dashboard completa con statistiche aziendali.

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
      "nomeAgente": "Mattia Rossi",
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
      "nome": "Mattia",
      "cognome": "Rossi",
      "contrattiConclusi": 5,
      "immobiliInGestione": 3,
      "fatturato": 500000
    }
  ],
  "tempoAIaPresaInCarico": {
    "giorni": 2,
    "ore": 5,
    "minuti": 30,
    "totaleSecondi": 192615
  },
  "tempoPresaInCaricoaContratto": {
    "giorni": 7,
    "ore": 12,
    "minuti": 0,
    "totaleSecondi": 648000
  },
  "valutazionePerformancePresaInCarico": "ottimo",
  "valutazionePerformanceContratto": "buono",
  "immobiliPerTipo": {
    "Appartamento": 35,
    "Villa": 8,
    "Attico": 5,
    "Loft": 2
  }
}
```

**Metriche di Performance:**
- **Eccellente:** ≤3 giorni (presa in carico), ≤7 giorni (contratto)
- **Ottimo:** ≤5 giorni (presa in carico), ≤14 giorni (contratto)
- **Buono:** ≤7 giorni (presa in carico), ≤30 giorni (contratto)
- **Standard:** >7 giorni (presa in carico), >30 giorni (contratto)

---

## 📋 API Valutazioni

### `GET /api/admin/valutazioni/solo-ai?offset=0&limit=10`
**Permesso richiesto:** `ROLE_ADMIN`

Restituisce valutazioni generate solo dall'AI (stato = "solo_AI").

**Query Parameters:**
- `offset` (default: 0): Numero valutazioni già caricate
- `limit` (default: 10): Numero valutazioni da caricare

**Response (200 OK):**
```json
{
  "valutazioni": [
    {
      "id": 5,
      "prezzoAI": 215000,
      "dataValutazione": "03/12/2024 14:30",
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
      "descrizione": "Appartamento luminoso in centro."
    }
  ],
  "nextOffset": 10,
  "hasMore": false,
  "pageSize": 1,
  "agents": [
    {
      "id": 2,
      "nome": "Mattia",
      "cognome": "Rossi",
      "email": "agente@immobili.it"
    }
  ]
}
```

**Campi:**
- `valutazioni`: Array di valutazioni per la pagina corrente
- `nextOffset`: Offset per la prossima richiesta
- `hasMore`: true se ci sono altre valutazioni disponibili
- `pageSize`: Numero di valutazioni ritornate
- `agents`: Lista completa agenti disponibili per assegnazione

**Data Format:**
- `dataValutazione`: Formato "dd/MM/yyyy HH:mm" (usa immobile.dataRegistrazione come fallback)

---

### `GET /api/admin/valutazioni/in-verifica?offset=0&limit=10`
**Permesso richiesto:** `ROLE_ADMIN`

Restituisce valutazioni in verifica da agenti (stato = "in_verifica").

**Response (200 OK):**
```json
{
  "valutazioni": [
    {
      "id": 7,
      "prezzoAI": 215000,
      "prezzoUmano": 220000,
      "dataValutazione": "03/12/2024 14:30",
      "statoValutazione": "in_verifica",
      "descrizione": "Valutazione in corso di verifica",
      "nomeAgente": "Mattia Rossi",
      "emailAgente": "agente@immobili.it",
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
      "descrizione": "Appartamento luminoso in centro."
    }
  ],
  "nextOffset": 10,
  "hasMore": false,
  "pageSize": 1
}
```

**Note:**
- Campo `id` utilizzabile per generare PDF contratto tramite `GET /api/contratti/valutazione/{id}/pdf`

---

### `PUT /api/admin/valutazioni/solo-ai/{id}/assegna-agente`
**Permesso richiesto:** `ROLE_ADMIN`

Assegna un agente a una valutazione AI.

**Request Body:**
```json
{
  "agenteId": 2
}
```

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Agente assegnato con successo alla valutazione 5"
}
```

---

### `DELETE /api/admin/valutazioni/solo-ai/{id}`
**Permesso richiesto:** `ROLE_ADMIN`

Elimina una valutazione AI (e l'immobile associato).

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Valutazione AI eliminata con successo"
}
```

---

### `DELETE /api/admin/valutazioni/in-verifica/{id}`
**Permesso richiesto:** `ROLE_ADMIN`

Elimina una valutazione in verifica.

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Valutazione eliminata con successo"
}
```

---

### `PUT /api/admin/valutazioni/in-verifica/{id}`
**Permesso richiesto:** `ROLE_ADMIN`

Aggiorna prezzo umano di una valutazione.

**Request Body:**
```json
{
  "prezzoUmano": 225000
}
```

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Prezzo umano aggiornato con successo"
}
```

---

## 📄 API Contratti PDF

### `GET /api/contratti/valutazione/{idValutazione}/pdf`
**Permesso:** Pubblico

Genera e restituisce il contratto PDF professionale.

**Parametri:**
- `idValutazione`: ID della valutazione (da GET /api/admin/valutazioni/in-verifica)

**Comportamento:**
- Se contratto esiste → utilizza quello esistente
- Se NON esiste → crea automaticamente nuovo contratto con:
  - Immobile dalla valutazione
  - Proprietario dall'immobile
  - Agente dalla valutazione
  - Data inizio: oggi
  - Data fine: oggi + 6 mesi
  - Commissione: 3% (default)

**Response (200 OK):**
File PDF binario con header:
```
Content-Type: application/pdf
Content-Disposition: attachment; filename="contratto_valutazione_{id}.pdf"
```




**Response Errori:**
- **404:** Valutazione non trovata
- **400:** Valutazione senza immobile o agente
- **500:** Errore generazione PDF

---

## 📝 Struttura PDF Contratto

### Contenuto del Documento

**Intestazione:**
- Nome azienda: IMMOBILIARIS S.R.L.
- Sede, P.IVA, REA, contatti
- Linea separatrice colorata (#2980B9)

**Sezioni:**

1. **LE PARTI**
   - Box centrato con dati Mandante (proprietario)
   - Box centrato con dati Agente
   - Layout con bordi colorati e sfondo grigio

2. **OGGETTO DELL'INCARICO**
   - Tabella dettagli immobile (indirizzo, metratura, caratteristiche)
   - Dotazioni (balcone, garage, cantina, ecc.)

3. **CONDIZIONI DI VENDITA**
   - Prezzo evidenziato in blu (14pt)
   - Dichiarazione immobile libero

4. **DURATA DELL'INCARICO**
   - Date inizio/fine (formato dd/MM/yyyy)
   - Cessazione automatica

5. **COMPENSO (PROVVIGIONE)**
   - Percentuale commissione evidenziata
   - Maturazione diritto provvigione

6. **DICHIARAZIONI DEL MANDANTE**
   - Conformità urbanistica, catastale
   - Impianti a norma, APE

7. **OBBLIGHI E CLAUSOLA PENALE**
   - Obblighi mandante/agente
   - Penale 70% violazione esclusiva

8. **TRATTAMENTO DATI PERSONALI (GDPR)**
   - Informativa Reg. UE 2016/679

**Firme:**
- Sezione centrata con data/luogo
- Spazi firma Mandante e Agente

**Clausole Vessatorie (Pagina 2):**
- Approvazione art. 4, 5, 7 (C.C. 1341-1342)
- Firma aggiuntiva accettazione

**Stile Grafico:**
- Font: Helvetica (18pt titolo, 13pt sottotitoli, 11pt normale)
- Margini: 60pt
- Colore aziendale: #2980B9
- Tabelle centrate con bordi grigi
- Testo giustificato

---

## 🏘️ API Immobili

### `GET /api/admin/immobili?offset=0&limit=12`
**Permesso richiesto:** `ROLE_ADMIN`

Lista immobili con caricamento progressivo.

**Response (200 OK):**
```json
{
  "immobili": [
    {
      "id": 1,
      "via": "Via Roma 12",
      "citta": "Torino",
      "cap": "10100",
      "provincia": "TO",
      "tipologia": "Appartamento",
      "metratura": 85,
      "condizioni": "Buone condizioni",
      "stanze": 3,
      "bagni": 2,
      "piano": 3,
      "prezzo": 220000,
      "descrizione": "Appartamento luminoso",
      "proprietario": {
        "nome": "Luca",
        "cognome": "Bianchi",
        "email": "luca@test.com"
      }
    }
  ],
  "nextOffset": 12,
  "hasMore": true,
  "pageSize": 12
}
```

---

### `POST /api/immobili/save`
**Permesso:** Pubblico

Salva nuovo immobile con auto-creazione proprietario.

**Request Body:**
```json
{
  "via": "Via Torino 10",
  "citta": "Torino",
  "cap": "10121",
  "provincia": "TO",
  "tipologia": "Appartamento",
  "metratura": 70,
  "condizioni": "Buone condizioni",
  "stanze": 2,
  "bagni": 1,
  "piano": 2,
  "ascensore": true,
  "garage": false,
  "proprietarioEmail": "nuovo@cliente.com",
  "proprietarioNome": "Mario",
  "proprietarioCognome": "Rossi",
  "proprietarioTelefono": "3331234567"
}
```

**Comportamento:**
- Se email non esiste → crea utente tipo "Cliente" (password vuota)
- Se email esiste → associa come proprietario
- Genera automaticamente valutazione AI (stato "solo_AI")

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Immobile salvato con successo",
  "immobileId": 25
}
```

---

## 👥 API Utenti

### `GET /api/admin/users`
**Permesso richiesto:** `ROLE_ADMIN`

Lista di tutti gli utenti.

**Response (200 OK):**
```json
[
  {
    "idUtente": 1,
    "nome": "Luca",
    "cognome": "Bianchi",
    "email": "admin@immobili.it",
    "telefono": "+393331234567",
    "tipoUtente": "Amministratore",
    "dataRegistrazione": "2025-01-15T10:30:00"
  }
]
```

---

### `POST /api/users/register`
**Permesso richiesto:** `ROLE_ADMIN`

Registra nuovo utente (admin/agente).

**Request Body:**
```json
{
  "nome": "Marco",
  "cognome": "Verdi",
  "email": "mverdi@immobili.it",
  "password": "password123",
  "telefono": "3339876543",
  "idTipo": 2,
  "contratto": "Indeterminato"
}
```

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Utente registrato con successo"
}
```

---

## 📧 API Email

### `POST /api/mail/send`
**Permesso richiesto:** `ROLE_ADMIN` o `ROLE_AGENT`

Invia email personalizzata.

**Request Body:**
```json
{
  "to": "cliente@example.com",
  "subject": "Conferma appuntamento",
  "text": "Gentile cliente, confermiamo l'appuntamento per il giorno..."
}
```

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Email inviata con successo"
}
```

---

## ⚙️ Configurazione e Avvio

### Prerequisiti
- **Java 17+** installato
- **Maven 3.8+** installato
- **MySQL 8.0+** in esecuzione
- Database `AgenziaImmobiliare` creato (script SQL fornito)

### File di Configurazione

#### `application.properties`
```properties
# Database MySQL
spring.datasource.url=jdbc:mysql://localhost:3306/AgenziaImmobiliare
spring.datasource.username=root
spring.datasource.password=yourpassword
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# Server
server.port=8080

# Email (opzionale per invio contratti)
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your-email@gmail.com
spring.mail.password=your-app-password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

### Comandi di Avvio

#### Avvio con Maven
```bash
cd backend
mvn spring-boot:run
```

#### Build JAR
```bash
mvn clean package
java -jar target/demo-0.0.1-SNAPSHOT.jar
```

#### Accesso Applicazione
- **Backend API:** http://localhost:8080
- **Swagger UI:** http://localhost:8080/swagger-ui/index.html
- **H2 Console (test):** http://localhost:8080/h2-console
- 
### Creazione del DB
Copiare il file che si chiama scriptDB.sql in un DBMS e avviarlo

---

## 🔐 Sicurezza e Autenticazione

### Sistema di Autenticazione
- **Spring Security** con form login e API REST
- **BCrypt** per hash password
- **Session-based** authentication (cookie)

### Ruoli Utente

| Ruolo | Permessi |
|-------|----------|
| **ROLE_ADMIN** | Accesso completo: dashboard, gestione utenti, valutazioni, contratti |
| **ROLE_AGENT** | Gestione valutazioni assegnate, contratti, statistiche proprie |
| **Cliente** | Nessun accesso backend (solo form pubblici) |

### Endpoint Pubblici (Senza Autenticazione)
- `/api/auth/**` - Login, logout, check
- `/api/immobili/save` - Registrazione immobile
- `/api/contratti/valutazione/{id}/pdf` - Download PDF contratto
- `/swagger-ui/**` - Documentazione Swagger
- `/h2-console/**` - Console H2 (solo dev)

### Endpoint Protetti
- `/api/admin/**` → Richiede `ROLE_ADMIN`
- `/api/agent/**` → Richiede `ROLE_AGENT`
- `/api/users/register` → Richiede `ROLE_ADMIN`

### CORS Configuration
CORS abilitato per frontend su `http://localhost:5173` (Vite dev server).

---

## 📊 Credenziali di Test

### Admin
```
Email: admin@immobili.it
Password: admin123
```

### Agente
```
Email: agente@immobili.it
Password: agente123
```

---

## 📁 Struttura Package

```
com.immobiliaris.demo/
├── config/
│   ├── CorsConfig.java           # Configurazione CORS
│   └── SecurityConfig.java       # Configurazione Spring Security
├── controller/
│   ├── api/
│   │   ├── AdminApiController.java   # API Dashboard Admin
│   │   ├── AgentApiController.java   # API Dashboard Agente
│   │   └── AuthApiController.java    # API Autenticazione
│   ├── HomeController.java
│   ├── UserController.java
│   └── MailController.java
├── entity/
│   ├── User.java                 # Entità Utente
│   ├── TipoUtente.java           # Enum tipo utente
│   ├── Immobile.java             # Entità Immobile
│   ├── StatoImmobile.java        # Enum stato immobile
│   ├── Valutazione.java          # Entità Valutazione
│   ├── StatoValutazione.java     # Enum stato valutazione
│   ├── Contratto.java            # Entità Contratto
│   └── StatoContratto.java       # Enum stato contratto
├── repository/
│   ├── UserRepository.java
│   ├── ImmobileRepository.java
│   ├── ValutazioneRepository.java
│   └── ContrattoRepository.java
├── service/
│   ├── CustomUserDetailsService.java  # Spring Security UserDetails
│   ├── UserService.java
│   ├── StatisticsService.java         # Logica dashboard e statistiche
│   ├── EmailService.java              # Invio email
│   └── PdfContrattoService.java       # Generazione PDF contratti
├── dto/
│   └── EmailRequest.java         # DTO richieste email
└── DemoApplication.java          # Main class Spring Boot
```

---

## 🧪 Testing

### Test Automatici
```bash
mvn test
```

### Test Manuali con Swagger
1. Avvia applicazione
2. Vai su http://localhost:8080/swagger-ui/index.html
3. Esegui login via `/api/auth/login`
4. Testa API protette con sessione attiva

---

## 📦 Build e Deploy

### Build Produzione
```bash
mvn clean package -DskipTests
```

### JAR Location
```
target/demo-0.0.1-SNAPSHOT.jar
```

### Deploy su Server
```bash
# Copia JAR su server
scp target/demo-0.0.1-SNAPSHOT.jar user@server:/opt/immobiliaris/

# Avvia con systemd
sudo systemctl start immobiliaris
```

---

## 🐛 Debug e Logging

### Abilitare Log SQL
```properties
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE
```

### Log Livelli
```properties
logging.level.com.immobiliaris=DEBUG
logging.level.org.springframework.security=DEBUG
```

---

## 📞 Supporto

Per domande o problemi:
- **Email:**   

xiao.chen@edu-its.it (Software developer)

dragos.nedelcu@edu-its.it (Frontend developer)

---

## 📄 Licenza

Progetto didattico - ITS Torino © 2025
