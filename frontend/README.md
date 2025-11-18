# 🏠 Agenzia Immobiliare - Progetto Frontend
[![Apri in Figma](https://img.shields.io/badge/Apri%20in-Figma-F24E1E?style=for-the-badge&logo=figma&logoColor=white)](https://www.figma.com/design/TNmhYNmC9YypnMfI8A0aMB/Immobiliaris?node-id=0-1&p=f&t=unnUuIEwlmUX1z64-0)  
Progetto **React** (frontend) per la gestione di un'agenzia immobiliare con parte pubblica e backoffice.

## 🚀 Avvio Rapido
### 1. Scarica i node modules
```powershell
cd .\frontend\
npm install
```
### 2. Avvia il progetto in locale con vite
```powershell
npm run dev
```
### 3. Apri il progetto nel browser
```powershell
localhost:5173
```

## Struttura del progetto

```
public/                         # logo svg immobiliaris
| 
src/
├── assets/
|   |── img                     # Immagini usate nei componenti e che saranno ottimizzate da vite
│   └── styles/                 # Partial SCSS che vanno tutti importati poi nel main
├── components/                 # Button, Modal, Input, ecc.
│   └── formSteps/              # Componenti del form multistep di autovalutazione.
├── context/                    # React Context per stati globali (auth e form)
├── layouts/                    # Layout per aree Public e Private
├── pages/
│   ├── public/                 # Pagine portale pubblico
│   └── private/                # Pagine admin/backoffice
|       |── adminPages/         # Pagine solo per Admin
|       |── agentPages/         # Pagine solo per Agente
|       |── EvaluationsAI.tsx   # Pagine comune sia per Admin che Agente
|       └── Login.tsx           # Pagine comune sia per Admin che Agente
├── services/                   # Chiamate API e gestione endpoint
├── App.tsx                     # Router principale
└── main.tsx                    # Entry point React
```

## Pagine principali

### Public
- Homepage indicizzata
- Onboarding proprietario (multi-step) non indicizzata

### Backoffice
- Login admin / agente immobiliare
- Dashboard admin / agente immobiliare
- Creazione utente - SOLO ADMIN
- Gestione utenti - SOLO ADMIN
- Valutazioni AI
- Incarichi in corso
- Contratti conclusi

---

## Componenti principali

- Navbar (public/private)
- Footer
- Button
- Input
- Label
- CardInput
- Loader
- Searchbar
- ProtectedRoute

---

## Routing

- **Public routes**: gestite tramite `PublicLayout`

  - "/" Homepage
  - "/form" Form autovalutazione

- **Admin routes**: gestite tramite `PrivateLayout`

  - "/admin" Pagine riservate a admin
  - "/agente" Pagine riservate all'agente
  - "/login" Pagina per l'accesso

---

## SCSS
- `main.scss` importa tutto ed è incluso in `main.tsx`
- Partial SCSS (`_nomeFile.scss`) per componenti isolati

---

## API
> Tutte le chiamate sono gestite tramite `services/api.ts`.
---

## Strumenti e librerie

- React + TypeScript
- React Router DOM
- React Icons
- SCSS per stile
- Fetch API (per chiamate HTTP)
- Context + Hooks per gestione stato globale
- Git + GitHub per versionamento

---

## Note per il team

- Tutti i nuovi componenti devono essere tipizzati (`.tsx`)
- Organizzare SCSS in modo partial e importare tutto in `main.scss`
- Usare `PublicLayout` e `PrivateLayout` per le pagine, mai duplicare navbar/footer
- Verificare le chiamate API con mock dati finché il backend non è pronto
- Sviluppare in diversi branch e sentirsi con il team prima di fare il merge su `main`
- Dare nomi di branch sensati tipo feat/nome-feat o fix/nome-fix
- Scrivere commit in inglese e con la seguente terminologia feat: per le feature nuove fix: per risolvere
