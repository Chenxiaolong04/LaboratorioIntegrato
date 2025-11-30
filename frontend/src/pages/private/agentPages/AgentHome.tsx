import { useAuth } from "../../../context/AuthContext";

export default function AgentHome() {
  const { user } = useAuth();

  return (
    <div className="dashboard-container">
      <div className="header-dashboard">
        <h1>Dashboard Immobiliaris</h1>
      </div>
      <div className="general-dashboard">
        <div className="agent-card">
          <div className="agent-info">
            <h2>Ciao {user?.name}</h2>
            <h3>ID #{user?.id}</h3>
          </div>
          <img
            src="https://images.unsplash.com/photo-1627161683077-e34782c24d81?q=80&w=703&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
            alt=""
          />
        </div>
        <div className="general">
          <div className="general-container">
            <div className="title-card">
              <h3>Valutazioni richieste:</h3>
            </div>
            <div className="data-card">
              <h3>348</h3>
            </div>
            <div className="data-card2">
              <h3>+ 8 questo mese</h3>
            </div>
          </div>
          <div className="general-container">
            <div className="title-card">
              <h3>Immobili acquisiti:</h3>
            </div>
            <div className="data-card">
              <h3>67</h3>
            </div>
            <div className="data-card2">
              <h3>12 in esclusiva</h3>
            </div>
          </div>
          <div className="general-container">
            <div className="title-card">
              <h3>Trattative attive:</h3>
            </div>
            <div className="data-card">
              <h3>187</h3>
            </div>
            <div className="data-card2">
              <h3>7 in fase finale</h3>
            </div>
          </div>
          <div className="general-container">
            <div className="title-card">
              <h3>Vendite chiuse:</h3>
            </div>
            <div className="data-card">
              <h3>12</h3>
            </div>
            <div className="data-card2">
              <h3>€ 2.8M valore totale</h3>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
