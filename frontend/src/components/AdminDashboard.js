import React, { useEffect, useState } from 'react';
import axios from 'axios';

function AdminDashboard() {
  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchDashboard = async () => {
      try {
        const response = await axios.get('/api/admin/dashboard', {
          withCredentials: true
        });
        setData(response.data);
      } catch (err) {
        console.error('Error fetching dashboard:', err);
        setError(err.message);
        // Redirect a login se non autenticato
        if (err.response?.status === 401 || err.response?.status === 403) {
          window.location.href = '/login';
        }
      } finally {
        setLoading(false);
      }
    };

    fetchDashboard();
  }, []);

  const handleLogout = async () => {
    try {
      await axios.post('/logout', {}, { withCredentials: true });
      window.location.href = '/login';
    } catch (err) {
      console.error('Logout error:', err);
      window.location.href = '/login';
    }
  };

  if (loading) return <div className="loading">Caricamento dashboard...</div>;
  if (error) return <div className="container"><div className="error">Errore: {error}</div></div>;
  if (!data) return <div className="container"><div className="error">Nessun dato disponibile</div></div>;

  return (
    <div className="container">
      <div className="dashboard">
        <h1>{data.message}</h1>
        <p><strong>Utente:</strong> {data.user}</p>
        <p><strong>Ruolo:</strong> {data.role}</p>
        
        <button className="btn btn-danger" onClick={handleLogout}>
          Logout
        </button>
      </div>
    </div>
  );
}

export default AdminDashboard;
