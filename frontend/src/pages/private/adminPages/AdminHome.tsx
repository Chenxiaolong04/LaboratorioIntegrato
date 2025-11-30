import { FaBuilding, FaHome, FaTree, FaWarehouse } from "react-icons/fa";

// Dati statici per il grafico (Performance Mensile)
const monthlyPerformanceData = [
  { month: "Giu", sales: 48 },
  { month: "Lug", sales: 61 },
  { month: "Ago", sales: 42 },
  { month: "Set", sales: 55 },
  { month: "Ott", sales: 67 },
  { month: "Nov", sales: 72 },
];

// Dati statici per la classifica (Top Performers)
const topPerformers = [
  { rank: 1, name: "Marco Bellini", sales: 15, value: "4.2M" },
  { rank: 2, name: "Giulia Ferri", sales: 12, value: "3.5M" },
  { rank: 3, name: "Alice Bianchi", sales: 11, value: "2.9M" },
];

// Funzione helper per ottenere il colore del badge (per il rank)
const getRankColor = (rank: number): string => {
  switch (rank) {
    case 1:
      return "#ffd700"; // Gold
    case 2:
      return "#c0c0c0"; // Silver
    case 3:
      return "#cd7f32"; // Bronze
    default:
      return "#2b7a78";
  }
};

const MAX_SALES_SCALE = 75;
const MAX_BAR_HEIGHT_PX = 200;

const getBarHeight = (sales: number): string => {
  const heightInPx = (sales / MAX_SALES_SCALE) * MAX_BAR_HEIGHT_PX;
  return `${heightInPx}px`;
};

// Dati statici per la comparativa agenti
const agentData = [
  {
    initials: "MB",
    name: "Marco Bellini",
    role: "Agente Senior - Torino",
    color: "#00A4BA",
    immobili: 52,
    vendite: 15,
    fatturato: "€4.2M",
  },
  {
    initials: "GF",
    name: "Giulia Ferri",
    role: "Agente Senior - Cuneo",
    color: "#546E7A",
    immobili: 46,
    vendite: 12,
    fatturato: "€3.5M",
  },
  {
    initials: "AB",
    name: "Alice Bianchi",
    role: "Agente - Torino",
    color: "#FF8A3C",
    immobili: 41,
    vendite: 11,
    fatturato: "€2.9M",
  },
  {
    initials: "SR",
    name: "Sara Ricci",
    role: "Agente - Novara",
    color: "#E9573F",
    immobili: 34,
    vendite: 9,
    fatturato: "€2.4M",
  },
  {
    initials: "LM",
    name: "Luca Martini",
    role: "Agente Senior - Alessandria",
    color: "#6AA84F",
    immobili: 28,
    vendite: 7,
    fatturato: "€1.8M",
  },
];

type PerformanceLevel = "Ottimo" | "Eccellente" | "Buono" | "Standard";

// Dati statici per la tabella dei tempi medi
const operationalData = [
  {
    fase: "Valutazione AI - Valutazione agente",
    tempoMedio: "2.3 giorni",
    performance: "Ottimo" as PerformanceLevel,
  },
  {
    fase: "Valutazione - Esclusiva",
    tempoMedio: "5.7 giorni",
    performance: "Eccellente" as PerformanceLevel,
  },
  {
    fase: "Pubblicazione - Primo contatto",
    tempoMedio: "5.7 giorni",
    performance: "Buono" as PerformanceLevel,
  },
  {
    fase: "Prima visita - Offerta",
    tempoMedio: "8.4 giorni",
    performance: "Ottimo" as PerformanceLevel,
  },
  {
    fase: "Offerta - Preliminare",
    tempoMedio: "12.1 giorni",
    performance: "Eccellente" as PerformanceLevel,
  },
  {
    fase: "Preliminare - Rogito",
    tempoMedio: "45 giorni",
    performance: "Standard" as PerformanceLevel,
  },
];

// Funzione per ottenere la classe CSS basata sul livello di performance
const getPerformanceClass = (level: PerformanceLevel): string => {
  return `performance-${level.toLowerCase()}`;
};

// Dati statici per la distribuzione del portfolio
const portfolioDistribution = [
  {
    IconComponent: FaBuilding,
    count: 120,
    label: "Appartamenti",
  },
  {
    IconComponent: FaHome,
    count: 43,
    label: "Ville",
  },
  {
    IconComponent: FaWarehouse,
    count: 20,
    label: "Attici",
  },
  {
    IconComponent: FaTree,
    count: 11,
    label: "Loft",
  },
];

export default function AdminHome() {
  return (
    <div className="dashboard-container">
      <div className="header-dashboard">
        <h1>Bentornato Dragos</h1>
        <h3>Dashboard ADMIN</h3>
      </div>
      <div className="general-dashboard">
        <div className="general-container">
          <div className="title-card">
            <h3>Totale Immobili:</h3>
          </div>
          <div className="data-card">
            <h3>348</h3>
          </div>
          <div className="data-card2">
            <h3>142 in vendita attiva</h3>
          </div>
        </div>

        <div className="general-container">
          <div className="title-card">
            <h3>Contratti Conclusi:</h3>
          </div>
          <div className="data-card">
            <h3>67</h3>
          </div>
          <div className="data-card2">
            <h3>€ 18.5M Valore Totale</h3>
          </div>
        </div>

        <div className="general-container">
          <div className="title-card">
            <h3>Valutazioni richieste:</h3>
          </div>
          <div className="data-card">
            <h3>187</h3>
          </div>
          <div className="data-card2">
            <h3>+34 Questa Settimana</h3>
          </div>
        </div>

        <div className="general-container">
          <div className="title-card">
            <h3>Agenti attivi:</h3>
          </div>
          <div className="data-card">
            <h3>12</h3>
          </div>
          <div className="data-card2">
            <h3>3 in training</h3>
          </div>
        </div>
      </div>

      {/* CONTAINER GRAFICO E CLASSIFICA */}
      <div className="performance-container">
        {/* Grafico Performance Mensile */}
        <div className="chart-section">
          <h2>Performance mensile team - vendite concluse</h2>
          <div className="bar-chart-mock">
            {monthlyPerformanceData.map((data) => (
              <div key={data.month} className="bar-wrapper">
                <div className="bar-value">{data.sales}</div>
                {/* APPLICAZIONE DELL'ALTEZZA IN PIXEL */}
                <div
                  className="bar"
                  style={{ height: getBarHeight(data.sales) }}
                ></div>
                <div className="bar-label">{data.month}</div>
              </div>
            ))}
          </div>
        </div>

        {/* Classifica Top Performers */}
        <div className="top-performers-section">
          <h2>Top performers del mese</h2>
          <div className="performer-list">
            {topPerformers.map((performer) => (
              <div key={performer.rank} className="performer-item">
                <div
                  className="rank-badge"
                  style={{ backgroundColor: getRankColor(performer.rank) }}
                >
                  {performer.rank}
                </div>
                <div className="details">
                  <span>{performer.name}</span>
                  <span>
                    {performer.sales} vendite - €{performer.value} fatturato
                  </span>
                </div>
              </div>
            ))}
          </div>
        </div>
      </div>

      {/* tabella 2: Comparativa Performance Agenti */}
      <div className="agent-comparison-container">
        <h2 className="comparison-title">Comparativa Performance Agenti</h2>
        <div className="agent-list">
          {agentData.map((agent, index) => (
            <div key={index} className="agent-item">
              {/* Sezione Avatar e Dettagli Agente */}
              <div className="agent-info">
                <div
                  className="agent-avatar"
                  style={{ backgroundColor: agent.color }}
                >
                  {agent.initials}
                </div>
                <div className="agent-details">
                  <span className="agent-name">{agent.name}</span>
                  <span className="agent-role">{agent.role}</span>
                </div>
              </div>

              {/* Sezione Metriche */}
              <div className="agent-metrics">
                <div className="metric-item">
                  <span className="metric-value">{agent.immobili}</span>
                  <span className="metric-label">Immobili</span>
                </div>
                <div className="metric-item">
                  <span className="metric-value">{agent.vendite}</span>
                  <span className="metric-label">Vendite</span>
                </div>
                <div className="metric-item metric-fatturato">
                  <span className="metric-value">{agent.fatturato}</span>
                  <span className="metric-label">Fatturato</span>
                </div>
              </div>
            </div>
          ))}
        </div>
      </div>

      {/* tabella 3: Analisi operativa - Tempi medi */}
      <div className="operational-analysis-container">
        <h2 className="analysis-title">Analisi operativa - Tempi medi</h2>

        <div className="analysis-table">
          {/* Intestazione della tabella */}
          <div className="table-header">
            <div className="header-cell phase">Fase</div>
            <div className="header-cell time">Tempo medio</div>
            <div className="header-cell performance">Performance</div>
          </div>

          {/* Righe dei dati */}
          <div className="table-body">
            {operationalData.map((item, index) => (
              <div key={index} className="table-row">
                <div className="cell-container">
                  <div className="cell phase">{item.fase}</div>
                  <div className="cell time">{item.tempoMedio}</div>
                </div>
                <div
                  className={`cell performance ${getPerformanceClass(
                    item.performance
                  )}`}
                >
                  {item.performance}
                </div>
              </div>
            ))}
          </div>
        </div>
      </div>

      {/* tabella 4: Distribuzione portfolio */}
      <div className="portfolio-distribution-container">
        <h2 className="distribution-title">
          Distribuzione portfolio per tipologia immobile
        </h2>

        <div className="distribution-grid">
          {portfolioDistribution.map((item, index) => (
            <div key={index} className="distribution-item">
              {/* Utilizzo del componente IconComponent */}
              <div className="item-icon">
                <item.IconComponent size={40} color="#546E7A" />
              </div>
              <div className="item-count">{item.count}</div>
              <div className="item-label">{item.label}</div>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}
