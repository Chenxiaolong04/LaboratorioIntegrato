import { FaEdit, FaHome, FaHandshake } from "react-icons/fa";
import { useAuth } from "../../../context/AuthContext";

export default function AgentHome() {
  const { user } = useAuth();

  const monthlyPerformanceData = [
    { month: "Giu", sales: 48 },
    { month: "Lug", sales: 61 },
    { month: "Ago", sales: 42 },
    { month: "Set", sales: 55 },
    { month: "Ott", sales: 67 },
    { month: "Nov", sales: 72 },
  ];

  const MAX_SALES_SCALE = 75;
  const MAX_BAR_HEIGHT_PX = 200;

  const getBarHeight = (sales: number): string => {
    const heightInPx = (sales / MAX_SALES_SCALE) * MAX_BAR_HEIGHT_PX;
    return `${heightInPx}px`;
  };

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
            src="https://images.unsplash.com/photo-1647580427155-0483906cb9de?q=80&w=928&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
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

      <div className="performance-container">
        <div className="chart-section">
          <h2>Performance mensili - acquisizioni e vendite</h2>
          <div className="bar-chart-mock">
            {monthlyPerformanceData.map((data) => (
              <div key={data.month} className="bar-wrapper">
                <div className="bar-value">{data.sales}</div>
                <div
                  className="bar"
                  style={{ height: getBarHeight(data.sales) }}
                ></div>
                <div className="bar-label">{data.month}</div>
              </div>
            ))}
          </div>
        </div>
        <div className="pipeline-section">
          <h2>Pipeline comparativa</h2>
          {[
            { label: "Richieste valutazioni", value: 120 },
            { label: "In trattativa", value: 48 },
            { label: "Contratti conclusi", value: 22 },
          ].map((step) => (
            <div key={step.label} className="pipeline-row">
              <div className="pipeline-label">{step.label}</div>
              <div className="pipeline-bar-wrapper">
                <div
                  className="pipeline-bar"
                  style={{
                    width: `${(step.value / 120) * 100}%`,
                  }}
                ></div>
              </div>
              <div className="pipeline-value">{step.value}</div>
            </div>
          ))}
        </div>
      </div>

      <div className="next-activities">
        <h2>Prossime attività</h2>

        {[
          {
            date: "02 Dic 2025",
            time: "15:30",
            type: "Visita immobile",
            owner: "Marco Bianchi",
            address: "Via Roma 24, Torino",
          },
          {
            date: "03 Dic 2025",
            time: "10:00",
            type: "Valutazione appartamento",
            owner: "Laura Neri",
            address: "Corso Francia 98, Torino",
          },
          {
            date: "05 Dic 2025",
            time: "17:15",
            type: "Firma preliminare",
            owner: "Giulia Rinaldi",
            address: "Via Po 12, Torino",
          },
        ].map((act, i) => (
          <div key={i} className="activity-card">
            <div className="activity-date">
              <h3>{act.date}</h3>
              <span>{act.time}</span>
            </div>

            <div className="activity-info">
              <h4>{act.type}</h4>
              <p>
                <strong>Proprietario:</strong> {act.owner}
              </p>
              <p>
                <strong>Indirizzo:</strong> {act.address}
              </p>
            </div>
          </div>
        ))}
      </div>

      <div className="month-properties">
        <h2>Immobili in gestione - mese corrente</h2>

        <div className="three-stats">
          {[
            {
              icon: <FaEdit size={40} />,
              value: 14,
              label: "In valutazione",
            },
            {
              icon: <FaHandshake size={40} />,
              value: 9,
              label: "In trattativa",
            },
            {
              icon: <FaHome size={40} />,
              value: 5,
              label: "Venduti",
            },
          ].map((item, i) => (
            <div key={i} className="stat-card">
              <div className="icon">{item.icon}</div>
              <h3 className="number">{item.value}</h3>
              <p className="label">{item.label}</p>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}
