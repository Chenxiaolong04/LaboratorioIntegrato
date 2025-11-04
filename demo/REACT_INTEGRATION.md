# Integrazione React + Spring Boot

Questa guida spiega come integrare un frontend React con il backend Spring Boot esistente.

## 📋 Architettura

### Sviluppo
- **Frontend React:** http://localhost:3000 (dev server con hot reload)
- **Backend Spring Boot:** http://localhost:8080 (API REST)
- **Comunicazione:** CORS abilitato, React usa proxy per le chiamate API

### Produzione
- **Un solo server:** Spring Boot su porta 8080
- **Build React:** Compilato e servito come static content da Spring Boot
- **Deploy:** Un unico JAR/WAR contenente frontend + backend

## 🚀 Setup Progetto React

### Opzione 1: Creare nuovo progetto React

```powershell
# Nella cartella padre del progetto Spring Boot
cd C:\Users\Xiao.Chen\Documents\ITS\LaboratorioIntegrato

# Crea nuovo progetto React
npx create-react-app frontend
cd frontend
```

### Opzione 2: Usare progetto React esistente

Se hai già un progetto React, spostalo nella cartella `frontend` accanto al progetto Spring Boot:

```
ITS\LaboratorioIntegrato\
├── demo\                  # Spring Boot
│   ├── src\
│   ├── pom.xml
│   └── ...
└── frontend\              # React
    ├── src\
    ├── public\
    ├── package.json
    └── ...
```

## ⚙️ Configurazione React per Sviluppo

### 1. Aggiungere proxy in `package.json`

Nel file `frontend/package.json`, aggiungi:

```json
{
  "name": "frontend",
  "version": "0.1.0",
  "proxy": "http://localhost:8080",
  "dependencies": {
    ...
  }
}
```

Questo fa sì che le chiamate API (es: `/api/auth/user`) vengano automaticamente inoltrate a Spring Boot.

### 2. Installare dipendenze per autenticazione

```powershell
cd frontend
npm install axios
```

### 3. Esempio componente Login React

Crea `frontend/src/components/Login.js`:

```javascript
import React, { useState } from 'react';
import axios from 'axios';

function Login() {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    try {
      // Chiamata al form login di Spring Security
      const formData = new FormData();
      formData.append('email', email);
      formData.append('password', password);
      
      const response = await axios.post('/login', formData, {
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        withCredentials: true
      });
      
      // Se successo, ottieni info utente
      const userResponse = await axios.get('/api/auth/user', {
        withCredentials: true
      });
      
      console.log('User logged in:', userResponse.data);
      
      // Redirect basato sul ruolo
      const roles = userResponse.data.roles;
      if (roles.includes('ROLE_ADMIN')) {
        window.location.href = '/admin-dashboard';
      } else if (roles.includes('ROLE_AGENT')) {
        window.location.href = '/agent-dashboard';
      }
      
    } catch (err) {
      setError('Credenziali non valide');
      console.error('Login error:', err);
    }
  };

  return (
    <div className="login-container">
      <h1>Login</h1>
      {error && <div className="error">{error}</div>}
      
      <form onSubmit={handleSubmit}>
        <div>
          <label>Email:</label>
          <input 
            type="email" 
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
          />
        </div>
        
        <div>
          <label>Password:</label>
          <input 
            type="password" 
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
          />
        </div>
        
        <button type="submit">Accedi</button>
      </form>
    </div>
  );
}

export default Login;
```

### 4. Esempio componente Dashboard Admin

Crea `frontend/src/components/AdminDashboard.js`:

```javascript
import React, { useEffect, useState } from 'react';
import axios from 'axios';

function AdminDashboard() {
  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchDashboard = async () => {
      try {
        const response = await axios.get('/api/admin/dashboard', {
          withCredentials: true
        });
        setData(response.data);
      } catch (error) {
        console.error('Error fetching dashboard:', error);
        // Redirect a login se non autenticato
        if (error.response?.status === 401) {
          window.location.href = '/login';
        }
      } finally {
        setLoading(false);
      }
    };

    fetchDashboard();
  }, []);

  if (loading) return <div>Caricamento...</div>;
  if (!data) return <div>Errore nel caricamento</div>;

  return (
    <div className="admin-dashboard">
      <h1>{data.message}</h1>
      <p>Utente: {data.user}</p>
      
      <div className="stats">
        <div>Utenti totali: {data.stats.totalUsers}</div>
        <div>Immobili: {data.stats.totalProperties}</div>
        <div>Richieste pendenti: {data.stats.pendingRequests}</div>
      </div>
      
      <button onClick={() => window.location.href = '/logout'}>
        Logout
      </button>
    </div>
  );
}

export default AdminDashboard;
```

### 5. Configurare routing React

In `frontend/src/App.js`:

```javascript
import React from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import Login from './components/Login';
import AdminDashboard from './components/AdminDashboard';
import AgentDashboard from './components/AgentDashboard';

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Navigate to="/login" />} />
        <Route path="/login" element={<Login />} />
        <Route path="/admin-dashboard" element={<AdminDashboard />} />
        <Route path="/agent-dashboard" element={<AgentDashboard />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
```

Installa React Router:
```powershell
npm install react-router-dom
```

## 🏃 Avviare in Sviluppo

### Terminal 1: Avvia Spring Boot
```powershell
cd C:\Users\Xiao.Chen\Documents\ITS\LaboratorioIntegrato\demo
# Dall'IDE: Run DemoApplication
# Oppure: .\mvnw.cmd spring-boot:run
```

### Terminal 2: Avvia React
```powershell
cd C:\Users\Xiao.Chen\Documents\ITS\LaboratorioIntegrato\frontend
npm start
```

Apri il browser su **http://localhost:3000**

## 📦 Build per Produzione

### Configurare Maven per build automatico

Aggiungi al `pom.xml` (prima di `</build>`):

```xml
<plugins>
    <!-- Plugin esistenti... -->
    
    <!-- Frontend Maven Plugin -->
    <plugin>
        <groupId>com.github.eirslett</groupId>
        <artifactId>frontend-maven-plugin</artifactId>
        <version>1.15.1</version>
        <configuration>
            <workingDirectory>../frontend</workingDirectory>
            <installDirectory>target</installDirectory>
        </configuration>
        <executions>
            <!-- Installa Node e npm -->
            <execution>
                <id>install node and npm</id>
                <goals>
                    <goal>install-node-and-npm</goal>
                </goals>
                <configuration>
                    <nodeVersion>v20.11.0</nodeVersion>
                    <npmVersion>10.2.4</npmVersion>
                </configuration>
            </execution>
            
            <!-- Installa dipendenze npm -->
            <execution>
                <id>npm install</id>
                <goals>
                    <goal>npm</goal>
                </goals>
                <configuration>
                    <arguments>install</arguments>
                </configuration>
            </execution>
            
            <!-- Build React -->
            <execution>
                <id>npm run build</id>
                <goals>
                    <goal>npm</goal>
                </goals>
                <configuration>
                    <arguments>run build</arguments>
                </configuration>
            </execution>
        </executions>
    </plugin>
    
    <!-- Copia build React in Spring Boot static -->
    <plugin>
        <artifactId>maven-resources-plugin</artifactId>
        <version>3.3.1</version>
        <executions>
            <execution>
                <id>copy-react-build</id>
                <phase>generate-resources</phase>
                <goals>
                    <goal>copy-resources</goal>
                </goals>
                <configuration>
                    <outputDirectory>${project.build.outputDirectory}/static</outputDirectory>
                    <resources>
                        <resource>
                            <directory>../frontend/build</directory>
                            <filtering>false</filtering>
                        </resource>
                    </resources>
                </configuration>
            </execution>
        </executions>
    </plugin>
</plugins>
```

### Build completo

```powershell
cd C:\Users\Xiao.Chen\Documents\ITS\LaboratorioIntegrato\demo
.\mvnw.cmd clean package
```

Questo:
1. Installa Node.js e npm
2. Esegue `npm install` nel progetto React
3. Esegue `npm run build` (crea build ottimizzato React)
4. Copia i file build in `target/classes/static`
5. Crea il JAR Spring Boot con React incluso

### Eseguire JAR produzione

```powershell
java -jar target/demo-0.0.1-SNAPSHOT.jar
```

Apri http://localhost:8080 — vedrai l'app React servita da Spring Boot!

## 🔒 Note sulla Sicurezza

### In sviluppo
- CSRF disabilitato per semplicità
- CORS permette localhost:3000
- Session-based auth con cookie

### Per produzione
- **Riattiva CSRF** o usa **JWT** per API stateless
- **Limita CORS** agli origin di produzione
- **Usa HTTPS** in produzione
- **Configura HttpOnly cookies** per sicurezza

## 🔧 Troubleshooting

### Errore CORS in sviluppo
- Verifica che `CorsConfig` permetta `http://localhost:3000`
- Controlla che React usi `withCredentials: true` nelle chiamate axios

### Login non funziona da React
- Verifica che il form login invii `Content-Type: application/x-www-form-urlencoded`
- Usa `FormData` o codifica manuale i parametri
- Controlla che i nomi dei campi siano `email` e `password`

### Build Maven fallisce
- Verifica di avere il progetto React nella cartella `../frontend` relativa a `pom.xml`
- Controlla che `package.json` esista e sia valido
- Verifica connessione internet per download Node.js

### Redirect dopo login non funziona
- In Spring Security, configura `successHandler` per API REST che ritorna JSON invece di redirect HTML
- Gestisci redirect lato React dopo ricevere risposta 200

## 📚 API Endpoints Disponibili

| Endpoint | Metodo | Autenticazione | Ruolo | Descrizione |
|----------|--------|----------------|-------|-------------|
| `/login` | POST | No | - | Form login Spring Security |
| `/logout` | POST | Sì | - | Logout |
| `/api/auth/user` | GET | Sì | - | Info utente corrente |
| `/api/auth/check` | GET | No | - | Check autenticazione |
| `/api/admin/dashboard` | GET | Sì | ADMIN | Dashboard amministratore |
| `/api/agent/dashboard` | GET | Sì | AGENT | Dashboard agente |

## 🎯 Prossimi Passi

1. **Implementa JWT** (opzionale) per autenticazione stateless
2. **Aggiungi protezione route** lato React (PrivateRoute component)
3. **Gestisci refresh token** per sessioni lunghe
4. **Implementa logout** corretto con invalidazione sessione
5. **Aggiungi gestione errori** globale in React (axios interceptors)
6. **Implementa loading states** e UX migliore
7. **Aggiungi unit test** per componenti React e API Spring Boot

## 📖 Risorse

- [Spring Boot CORS](https://spring.io/guides/gs/rest-service-cors/)
- [React Proxy](https://create-react-app.dev/docs/proxying-api-requests-in-development/)
- [Axios Documentation](https://axios-http.com/docs/intro)
- [React Router](https://reactrouter.com/)
