# 🏠 Agenzia Immobiliare - Progetto Fullstack

Progetto con **Spring Boot** (backend) e **React** (frontend) per la gestione di un'agenzia immobiliare.

---

## 📋 Prerequisiti

- ✅ **JDK 17** (o superiore)
- ✅ **Maven 3.8+** (o usa il wrapper `mvnw.cmd` incluso)
- ✅ **MySQL 8.0+** con database e utenti già configurati
- ✅ **Node.js 16+** e **npm**

---

## 🗄️ Database MySQL

Il progetto si aspetta:
- Database: `AgenziaImmobiliare`
- Utente: `ITS_2025` / Password: `its_2025`

Verifica configurazione in: `demo/src/main/resources/application.properties`

Spring Boot creerà/aggiornerà automaticamente le tabelle al primo avvio (`spring.jpa.hibernate.ddl-auto=update`).

---

## 🚀 Avvio Rapido

### 1. Avvia Backend Spring Boot

**Windows PowerShell:**
```powershell
cd demo
.\start-backend.ps1
```

**Windows Command Prompt:**
```cmd
cd demo
start-backend.bat
```

**Se ricevi "Accesso negato"**, esegui come **Amministratore**.

Il backend sarà su: **http://localhost:8080**

---

### 2. Avvia Frontend React

```bash
cd frontend
npm install    # solo la prima volta
npm start
```

Il frontend sarà su: **http://localhost:3000**

---

### 3. Login

Apri il browser su **http://localhost:3000** e accedi con le credenziali degli utenti nel tuo database.

Il sistema riconosce automaticamente i ruoli da `Tipi_utente.Nome`:
- **"Amministratore"** → Dashboard admin (`/admin-dashboard`)
- **"Agente"** → Dashboard agente (`/agent-dashboard`)
- **"Cliente"** → Accesso base

---

## 📂 Struttura Progetto

```
LaboratorioIntegrato/
├── README.md                          # Questo file
├── demo/                              # Backend Spring Boot
│   ├── src/main/java/com/immobiliaris/demo/
│   │   ├── config/                    # Spring Security, CORS
│   │   ├── controller/api/            # REST API per React
│   │   ├── entity/                    # User, TipoUtente
│   │   ├── repository/                # JPA Repository
│   │   └── service/                   # CustomUserDetailsService
│   ├── src/main/resources/
│   │   └── application.properties     # Configurazione DB
│   ├── start-backend.bat              # Script avvio Windows CMD
│   ├── start-backend.ps1              # Script avvio PowerShell
│   └── pom.xml
│
└── frontend/                          # Frontend React
    ├── src/
    │   ├── components/
    │   │   ├── Login.js               # Form login
    │   │   ├── ProtectedRoute.js      # Guard per route protette
    │   │   ├── AdminDashboard.js      # Dashboard admin
    │   │   └── AgentDashboard.js      # Dashboard agente
    │   ├── App.js                     # Router principale
    │   └── index.css                  # Stili
    ├── package.json                   # Proxy -> http://localhost:8080
    └── start-react.bat                # Script avvio React
```

---

## 🔌 API Endpoints

### Autenticazione
- `POST /login` - Login (Spring Security form)
- `POST /logout` - Logout
- `GET /api/auth/user` - Info utente autenticato
- `GET /api/auth/check` - Verifica stato autenticazione

### Admin (solo ROLE_ADMIN)
- `GET /api/admin/dashboard` - Dashboard amministratore

### Agent (solo ROLE_AGENT)
- `GET /api/agent/dashboard` - Dashboard agente

---

## 🔧 Risoluzione Problemi

### ❌ "Accesso negato" quando avvio mvnw.cmd

**Causa:** Policy di sicurezza Windows (AppLocker, SmartScreen, Antivirus).

**Soluzioni:**
1. Esegui il terminale come **Amministratore**
2. Disabilita temporaneamente l'antivirus
3. Usa Maven globale: `mvn spring-boot:run` nella cartella `demo/`

---

### ❌ Errore connessione database

**Causa:** MySQL non avviato o credenziali errate.

**Verifica:**
```bash
mysql -u ITS_2025 -p
# Password: its_2025
```

Controlla anche `demo/src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/AgenziaImmobiliare?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=ITS_2025
spring.datasource.password=its_2025
```

---

### ❌ Frontend non comunica con backend

**Causa:** Backend non avviato o proxy non configurato.

**Verifica:**
1. Backend sia avviato su `http://localhost:8080`
2. Nel `frontend/package.json` ci sia: `"proxy": "http://localhost:8080"`
3. Riavvia il frontend dopo aver modificato il proxy

---

### ❌ Login fallisce con 401

**Causa:** Credenziali errate o password non criptata correttamente.

**Verifica:**
- L'utente esista nel database
- La password nel DB sia criptata con **BCrypt** (rounds = 10)
- I log del backend per dettagli dell'errore

**Per generare password BCrypt:**
- Online: https://bcrypt-generator.com/ (usa 10 rounds)
- Oppure da codice Java con `BCryptPasswordEncoder`

---

### ❌ "Cannot find module" su React

**Causa:** Dipendenze non installate.

**Soluzione:**
```bash
cd frontend
npm install
```

---

## 🛠️ Dettagli Tecnici

### Backend (Spring Boot 3.5.7)
- **Java 17**
- **Spring Security** con form login
- **JPA/Hibernate** per database MySQL
- **CSRF disabilitato** per API REST
- **CORS configurato** per `http://localhost:3000`
- **Session-based authentication** (cookie `JSESSIONID`)

### Frontend (React 18)
- **React Router** per navigazione
- **Axios** per chiamate API (con `withCredentials: true`)
- **Proxy configurato** su backend (`localhost:8080`)
- **Protected Routes** con verifica ruoli

---

## 📝 Note Importanti

- Le **password** devono essere criptate con **BCrypt** nel database
- Il **proxy** (`package.json`) funziona solo in development
- **CORS** è configurato per accettare `http://localhost:3000`
- Le **sessioni** usano cookie (`withCredentials: true` su Axios)

---

## ✅ Checklist Pre-Avvio

- [ ] MySQL avviato con database `AgenziaImmobiliare`
- [ ] Utenti e ruoli presenti nel database
- [ ] JDK 17 installato e configurato
- [ ] Node.js e npm installati
- [ ] Dipendenze frontend installate (`npm install`)

---

## 🎯 Quick Start

```bash
# Terminal 1 - Backend
cd demo
.\start-backend.ps1       # o start-backend.bat

# Terminal 2 - Frontend
cd frontend
npm install               # solo prima volta
npm start

# Browser
# Apri http://localhost:3000 e fai login
```

---

## 📧 Supporto

Per problemi:
1. Verifica i **log del backend** nel terminale
2. Apri la **console del browser** (F12) per errori React
3. Controlla che **MySQL sia avviato** e gli utenti esistano
4. Verifica che le **password siano criptate BCrypt**

---

## 📅 Link Meet

- Meet Generale: https://meet.google.com/oop-qhkv-qiq
- Meet Backend: https://meet.google.com/cre-qsxn-fzy
- Meet Frontend: https://meet.google.com/pjq-wvqf-wim
- Meet Digital strategist: https://meet.google.com/tct-qggm-cyc

---

**Progetto pronto! 🚀**
