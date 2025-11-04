import React, { useEffect, useState } from 'react';
import { Navigate } from 'react-router-dom';
import axios from 'axios';

function ProtectedRoute({ children, requiredRole }) {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(false);

  useEffect(() => {
    // Verifica se l'utente è autenticato chiamando Java
    const checkAuth = async () => {
      try {
        const response = await axios.get('/api/auth/user', {
          withCredentials: true
        });
        setUser(response.data);
        setError(false);
      } catch (err) {
        console.error('Utente non autenticato:', err);
        setUser(null);
        setError(true);
      } finally {
        setLoading(false);
      }
    };

    checkAuth();
  }, []);

  // Mostra "Caricamento..." mentre verifica
  if (loading) {
    return (
      <div style={{ 
        display: 'flex', 
        justifyContent: 'center', 
        alignItems: 'center', 
        height: '100vh' 
      }}>
        <h2>Caricamento...</h2>
      </div>
    );
  }

  // Se non è autenticato, reindirizza al login
  if (error || !user) {
    return <Navigate to="/login" replace />;
  }

  // Se è richiesto un ruolo specifico, controlla (user.roles è un array)
  if (requiredRole && user.roles && !user.roles.includes(requiredRole)) {
    console.warn(`Accesso negato. Ruolo richiesto: ${requiredRole}, Ruoli utente:`, user.roles);
    return <Navigate to="/login" replace />;
  }

  // Se tutto ok, mostra il componente
  return children;
}

export default ProtectedRoute;
