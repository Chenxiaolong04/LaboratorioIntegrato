# 🏠 Agenzia Immobiliare - Progetto Backend

Progetto **Spring Boot** (backend) per la gestione di un'agenzia immobiliare con autenticazione e API REST.

---

## 📋 Prerequisiti

- ✅ **JDK 17** (o superiore)
- ✅ **Maven 3.8+** (o usa il wrapper `mvnw.cmd` incluso)
- ✅ **MySQL 8.0+** con database e utenti già configurati

---

## 🗄️ Database MySQL

Il progetto si aspetta:
- Database: `AgenziaImmobiliare`
- Utente: `ITS_2025` / Password: `its_2025`

Verifica configurazione in: `backend/src/main/resources/application.properties`

Spring Boot creerà/aggiornerà automaticamente le tabelle al primo avvio (`spring.jpa.hibernate.ddl-auto=update`).

---

## 🚀 Avvio Rapido

### 1. Avvia Backend Spring Boot

**Windows PowerShell:**
```powershell
cd backend
.\mvnw.cmd spring-boot:run
```

**Windows Command Prompt:**
```cmd
cd backend
mvnw.cmd spring-boot:run
```

**Oppure con Maven globale:**
```bash
cd backend
mvn spring-boot:run
```

**Se ricevi "Accesso negato"**, esegui il terminale come **Amministratore**.

Il backend sarà su: **http://localhost:8080**

---

### 2. Test API

Puoi testare le API con:
- Browser: `http://localhost:8080/api/auth/check`
- Postman/Insomnia
- cURL: `curl http://localhost:8080/api/auth/check`

Per accedere usa le credenziali degli utenti presenti nel database.

Il sistema riconosce automaticamente i ruoli da `Tipi_utente.Nome`:
- **"Amministratore"** → Accesso API admin
- **"Agente"** → Accesso API agente
- **"Cliente"** → Accesso base

---

## 📂 Struttura Progetto

```
LaboratorioIntegrato/
├── README.md                          # Questo file
├── Modello DB.png                     # Diagramma database
└── backend/                           # Backend Spring Boot
    ├── src/main/java/com/immobiliaris/imobiliaris/
    │   ├── config/                    # Spring Security, CORS
    │   ├── controller/api/            # REST API
    │   ├── entity/                    # User, TipoUtente
    │   ├── repository/                # JPA Repository
    │   └── service/                   # CustomUserDetailsService
    ├── src/main/resources/
    │   └── application.properties     # Configurazione DB MySQL
    ├── mvnw.cmd                       # Maven wrapper Windows
    ├── mvnw                           # Maven wrapper Linux/Mac
    └── pom.xml                        # Dipendenze Maven
```

---



## 🔧 Risoluzione Problemi

### ❌ "Accesso negato" quando avvio mvnw.cmd

**Causa:** Policy di sicurezza Windows (AppLocker, SmartScreen, Antivirus).

**Soluzioni:**
1. Esegui il terminale come **Amministratore**
2. Disabilita temporaneamente l'antivirus
3. Usa Maven globale: `mvn spring-boot:run` nella cartella `backend/`

---

### ❌ Errore connessione database

**Causa:** MySQL non avviato o credenziali errate.

**Verifica:**
```bash
mysql -u ITS_2025 -p
# Password: its_2025
```

Controlla anche `backend/src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/AgenziaImmobiliare?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=ITS_2025
spring.datasource.password=its_2025
```

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

## 🛠️ Dettagli Tecnici

### Backend (Spring Boot 3.5.7)
- **Java 17**
- **Spring Security** con form login e session-based authentication
- **JPA/Hibernate** per gestione database MySQL
- **CSRF disabilitato** per API REST
- **CORS configurato** per accettare richieste da `http://localhost:3000`
- **BCrypt** per criptazione password (10 rounds)

---

## 📝 Note Importanti

- Le **password** devono essere criptate con **BCrypt** nel database (10 rounds)
- L'autenticazione usa **sessioni Spring Security** con cookie `JSESSIONID`
- **CORS** è configurato per accettare richieste da `http://localhost:3000`
- Puoi integrare qualsiasi frontend (React, Vue, Angular) usando le API REST

---

## ✅ Checklist Pre-Avvio

- [ ] MySQL avviato con database `AgenziaImmobiliare`
- [ ] Utenti e ruoli presenti nel database con password BCrypt
- [ ] JDK 17 installato e configurato
- [ ] Maven installato (o usa il wrapper `mvnw.cmd` incluso)

---

## 🎯 Quick Start

```powershell
# Windows PowerShell
cd backend
.\mvnw.cmd spring-boot:run

# Oppure con Maven globale
cd backend
mvn spring-boot:run

# Backend disponibile su: http://localhost:8080
# Test API: http://localhost:8080/api/auth/check
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
