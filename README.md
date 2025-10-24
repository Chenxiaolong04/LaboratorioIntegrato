# Immobiliaris - Frontend
[![Apri in Figma](https://img.shields.io/badge/Apri%20in-Figma-F24E1E?style=for-the-badge&logo=figma&logoColor=white)](https://www.figma.com/design/TNmhYNmC9YypnMfI8A0aMB/Immobiliaris?node-id=0-1&p=f&t=unnUuIEwlmUX1z64-0)


## Struttura del progetto

```
src/
├── assets/
|   |── images            # Immagini usate nei componenti (logo, icone comuni vanno in public/images)
│   └── styles/           # SCSS va tutto nel main, volendo si divide tutto poi in moduli e importa in main
├── components/           
│   ├── common/           # Button, Modal, Input, ecc.
│   ├── public/           # Componenti specifici area pubblica
│   └── backoffice/       # Componenti specifici admin
├── layouts/              # Layout per aree Public e Admin
├── pages/
│   ├── public/           # Pagine portale pubblico
│   └── private/          # Pagine admin/backoffice
├── services/             # Chiamate API e gestione endpoint
├── store/                # React Context per stati globali (es. auth)
├── App.tsx               # Router principale
└── main.tsx              # Entry point React
```

---

## Pagine principali

### Public
- Home
- Onboarding proprietario (multi-step)
- Valutazione inviata
- Contratto in esclusiva
- Chi siamo
- Contatti

### Backoffice
- Login admin
- Dashboard
- Dettaglio immobile
- Profilo utente/admin

---

## Componenti principali

- Navbar (public/admin)
- ecc...

---

## Routing

- **Public routes**: gestite tramite `PublicLayout`

  - 
  - 
  - 

- **Admin routes**: gestite tramite `AdminLayout`

  -
  - 
  -

---

## SCSS
- `main.scss` importa tutto ed è incluso in `main.tsx`
- Moduli SCSS (`*.module.scss`) per componenti isolati

---

## API previste

- 
-
-

> Tutte le chiamate sono gestite tramite `services/api.ts`.

---

## Strumenti e librerie

- React + TypeScript
- React Router DOM
- SCSS / CSS Modules
- Fetch API / Axios (per chiamate HTTP)
- Context + Hooks per gestione stato globale
- Git + GitHub per versionamento
- Strumenti di linting e formatting consigliati: ESLint + Prettier

---

## Note per il team

- Tutti i nuovi componenti devono essere tipizzati (`.tsx`)
- Organizzare SCSS in modo modulare e importare tutto in `main.scss`
- Usare `PublicLayout` e `AdminLayout` per le pagine, mai duplicare navbar/footer
- Verificare le chiamate API con mock dati finché il backend non è pronto
- Branch `frontend` → sviluppare qui prima di fare merge su `main`
- Creare branch a partire da frontend con un nome della feature che si sta sviluppando o con il nome proprio
