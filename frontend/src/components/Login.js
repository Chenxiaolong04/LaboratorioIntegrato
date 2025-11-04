import React, { useState } from 'react';
import axios from 'axios';

function Login() {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);
    
    try {
      // Crea FormData per il form login di Spring Security
      const formData = new URLSearchParams();
      formData.append('email', email);
      formData.append('password', password);
      
      // Chiamata al form login di Spring Security
      await axios.post('/login', formData, {
        headers: { 
          'Content-Type': 'application/x-www-form-urlencoded' 
        },
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
      } else {
        window.location.href = '/';
      }
      
    } catch (err) {
      console.error('Login error:', err);
      if (err.response?.status === 401 || err.response?.status === 403) {
        setError('Email o password non validi');
      } else {
        setError('Errore di connessione. Verifica che il server sia avviato.');
      }
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="login-container">
      <h1>Agenzia Immobiliare</h1>
      <h2 style={{textAlign: 'center', color: '#666', fontSize: '16px', marginTop: '-20px', marginBottom: '30px'}}>
        Login
      </h2>
      
      {error && <div className="error">{error}</div>}
      
      <form onSubmit={handleSubmit}>
        <div>
          <label htmlFor="email">Email:</label>
          <input 
            type="email" 
            id="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
            disabled={loading}
            placeholder="tua.email@example.com"
          />
        </div>
        
        <div>
          <label htmlFor="password">Password:</label>
          <input 
            type="password" 
            id="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
            disabled={loading}
            placeholder="Password"
          />
        </div>
        
        <button type="submit" disabled={loading}>
          {loading ? 'Accesso in corso...' : 'Accedi'}
        </button>
      </form>
      
      <div style={{marginTop: '20px', textAlign: 'center', fontSize: '12px', color: '#999'}}>
        <p>Server Spring Boot deve essere avviato su localhost:8080</p>
      </div>
    </div>
  );
}

export default Login;
