# 🏠 Immobiliaris - Portale Immobiliare Digitale

## Progetto laboratorio integrato ITS ICT 2024/2026 Gruppo 6 SixWave

Immobiliaris è un portale digitale dedicato alla valutazione degli immobili sul territorio piemontese. Il progetto nasce con l’obiettivo di supportare l’agenzia nell’ampliamento verso un pubblico più giovane (35–55 anni), offrendo strumenti moderni, processi automatizzati e un’esperienza utente semplice, veloce e intuitiva.

---

## 👥 Membri del Gruppo

### Web Developer
- Dragos Nedelcu Andrei (n3dydr4gos) - coordinatore gruppo WEB
- Mattia Fiore (fiorematti)
- Rodrigo Aguirre (therealrodry)

### Software Developer
- Xiaolong Chen (Chenxiaolong04) - coordinatore gruppo Software e Team Leader del progetto
- Simone Crivello (simonecrivello)
- Angelo Jimenez (AngJim)

### Digital Strategist
- Sara Auriemma (saraauriemma)
- Beatrice Giletta (beatricegiletta)
- Luca Omegna (lucaomegna-cmyk)

---

## 🎯 Obiettivi del Progetto

- Creare un portale web moderno dedicato ai proprietari che vogliono
  vendere casa.
- Offrire un sistema di onboarding guidato tramite form multistep.
- Fornire una valutazione dell'immobile entro 72 ore, supportata da
  strumenti digitali.
- Permettere all'amministrazione interna di gestire le richieste
  tramite un'area riservata (dashboard).
- Integrare strategie di comunicazione e lead generation per attrarre
  nuovi clienti.
- Definire una brand identity coerente e professionale.

---

## 📋 Prerequisiti per l'avvio del progetto

- ✅ **JDK v17** (o superiore)
- ✅ **Maven v3.8+** (o usa il wrapper `mvnw.cmd` incluso)
- ✅ **MySQL v8.0+** con database e utenti già configurati
- ✅ **Nodejs v18+ e NPM v9+**
- ✅ **Vite v5+**

## 🚀 Avvio Rapido
### 1. Scarica il progetto in formato zip da GitHub o esegui sul terminale questo comando se hai git bash installato:
```powershell
git clone https://github.com/Chenxiaolong04/LaboratorioIntegrato.git
```
### 2. Dentro alla cartella ./frontend scarica i node modules da terminale:
```powershell
cd .\frontend\
npm install
```
### 3. Avvia il progetto in locale con vite
```powershell
npm run dev
```
### 4. Apri il progetto nel browser
```powershell
http://localhost:5173
```

### 6. Spostati nella cartella ./backend e modifica il file application.properties che si trova in ./backend/src/main/resources/application.properties
 o crealo se non lo vedi, cambia nome utente e password con i tuoi di mysql
```powershell
spring.datasource.username=TUO_USERNAME
spring.datasource.password=LA_TUA_PASSWORD
```

### 7. Torna nella cartella ./backend e copia il codice MYSQL dentro al file ScriptDB, dopodichè eseguilo per esempio utilizzando MySQLWorkbench

### 8. Crea una nuova finestra di terminale e spostati nella cartella ./backend del progetto, dopodiche lancia il comando 
```powershell
mvnw.cmd spring-boot:run
```

### 9. Se l'ultimo messaggio nella finestra terminale dove hai eseguito il backend è application started, il backend è attivo e funzionante sulla porta:

```powershell
http://localhost:8080
```


## 🛠️ Funzionalità Principali

### 🔹 Per gli utenti (proprietari)

- Form multistep per inviare i dati del proprio immobile
- Descrizione caratteristiche dell'immobile (tipologia, stato,
  superficie, indirizzo...)
- Invio della richiesta di valutazione
- Ricezione di una risposta entro 72 ore

### 🔹 Per l'amministratore (dashboard)

- Visualizzazione delle richieste ricevute
- Stato avanzamento valutazioni
- Gestione immobili, incarichi e vendite
- Statistiche e dati riassuntivi (overview attività)

### 🔹 Per l'agente (dashboard)

- Visualizzazione delle richieste ricevute
- Stato avanzamento valutazioni
- Incarichi e vendite assegnati all'agente
- Statistiche e dati riassuntivi (overview attività)
---

## 🧭 Area Geografica Target

Città medio-grandi del Piemonte: - Torino - Cuneo - Alessandria - Asti

---

## 📣 Strategia Digitale Collegata

Il progetto include anche: - Realizzazione della brand identity (logo,
colori, linee guida) - Contenuti ottimizzati SEO - Piano editoriale e
comunicazione social - Campagne paid (Meta, Google) per generare
traffico e conversioni - Integrazione con strumenti di CRM e Marketing
Automation

---

## 🧱 Stack Tecnologico Utilizzato

- **Frontend:** HTML5/SCSS/TypeScript/React
- **Backend:** Java + Spring Boot
- **Database:** MySQL
- **Versionamento:** GitHub
- **Documentazione:** README.md, JSDoc
- **Gestione progetto:** GitHub Projects

---

## 📂 Struttura della Repository

    /frontend     → codice interfaccia utente e portale pubblico
    /backend      → API, logica server e gestione delle valutazioni

---

## 🚀 Finalità del Progetto

- Fornire al cliente un portale moderno, veloce e affidabile
- Digitalizzare il processo di acquisizione immobili
- Semplificare la gestione interna tramite dashboard amministrativa
- Migliorare la presenza online e la generazione di contatti
  qualificati

---

## 📑 Note Finali

- Il progetto include una presentazione finale al cliente.
- Ogni parte del team contribuisce con un ruolo specifico (Digital,
  Web Developer, Software Developer).
- Tutte le scelte tecniche e progettuali seguono gli obiettivi del
  brief ricevuto.
