import { FaBuilding, FaHome, FaTree, FaWarehouse } from "react-icons/fa";
import { useAuth } from "../../../context/AuthContext";
import {
  getAdminDashboard,
  type AdminDashboardData,
} from "../../../services/api";
import { useEffect, useState } from "react";
import Loader from "../../../components/Loader";

// Dati statici per il grafico (Performance Mensile)
// const monthlyPerformanceData = [
//   { month: "Giu", sales: 48 },
//   { month: "Lug", sales: 61 },
//   { month: "Ago", sales: 42 },
//   { month: "Set", sales: 55 },
//   { month: "Ott", sales: 67 },
//   { month: "Nov", sales: 72 },
// ];

// Dati statici per la classifica (Top Performers)
// const topPerformers = [
//   { rank: 1, name: "Marco Bellini", sales: 15, value: "4.2M" },
//   { rank: 2, name: "Giulia Ferri", sales: 12, value: "3.5M" },
//   { rank: 3, name: "Alice Bianchi", sales: 11, value: "2.9M" },
// ];

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

const MAX_BAR_HEIGHT_PX = 200;

const getBarHeight = (sales: number, maxSales: number): string => {
  const heightInPx = (sales / maxSales) * MAX_BAR_HEIGHT_PX;
  return `${heightInPx}px`;
};

// Dati statici per la comparativa agenti
// const agentData = [
//   {
//     initials: "MB",
//     name: "Marco Bellini",
//     role: "Agente Senior - Torino",
//     color: "#00A4BA",
//     immobili: 52,
//     vendite: 15,
//     fatturato: "€4.2M",
//   },
//   {
//     initials: "GF",
//     name: "Giulia Ferri",
//     role: "Agente Senior - Cuneo",
//     color: "#546E7A",
//     immobili: 46,
//     vendite: 12,
//     fatturato: "€3.5M",
//   },
//   {
//     initials: "AB",
//     name: "Alice Bianchi",
//     role: "Agente - Torino",
//     color: "#FF8A3C",
//     immobili: 41,
//     vendite: 11,
//     fatturato: "€2.9M",
//   },
//   {
//     initials: "SR",
//     name: "Sara Ricci",
//     role: "Agente - Novara",
//     color: "#E9573F",
//     immobili: 34,
//     vendite: 9,
//     fatturato: "€2.4M",
//   },
//   {
//     initials: "LM",
//     name: "Luca Martini",
//     role: "Agente Senior - Alessandria",
//     color: "#6AA84F",
//     immobili: 28,
//     vendite: 7,
//     fatturato: "€1.8M",
//   },
// ];

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
type PerformanceLevel = "Ottimo" | "Eccellente" | "Buono" | "Standard";

const getPerformanceClass = (level: PerformanceLevel): string =>
  `performance-${level.toLowerCase()}`;

// Dati statici per la distribuzione del portfolio
// const portfolioDistribution = [
//   {
//     IconComponent: FaBuilding,
//     count: 120,
//     label: "Appartamenti",
//   },
//   {
//     IconComponent: FaHome,
//     count: 43,
//     label: "Ville",
//   },
//   {
//     IconComponent: FaWarehouse,
//     count: 20,
//     label: "Attici",
//   },
//   {
//     IconComponent: FaTree,
//     count: 11,
//     label: "Loft",
//   },
// ];

export default function AdminHome() {
  const { user } = useAuth();
  const [data, setData] = useState<AdminDashboardData | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    async function fetchData() {
      try {
        const res = await getAdminDashboard();
        // console.log("Dati ricevuti dal backend:", res);
        setData(res);
      } catch (err: unknown) {
        if (err instanceof Error) {
          setError(err.message);
        } else {
          setError("Errore sconosciuto");
        }
      } finally {
        setLoading(false);
      }
    }

    fetchData();
  }, []);

  if (loading) return <Loader />;
  if (error) return <div>Errore: {error}</div>;
  if (!data) return null;

  const monthlyPerformanceData = data.contrattiPerMese.map((m) => ({
    month: m.mese,
    sales: m.numeroContratti,
  }));
  const maxSales = Math.max(...monthlyPerformanceData.map((d) => d.sales), 1);

  const topPerformers = data.top3Agenti.map((a, index) => ({
    rank: index + 1,
    name: a.nomeAgente,
    sales: a.numeroContratti,
    value: a.fatturato,
  }));

  const colorsAvatar = ["#6AA84F", "#E9573F", "#FF8A3C", "#546E7A", "#00A4BA"];

  const agentData = data.agenti.map((a) => {
    const randomColor =
      colorsAvatar[Math.floor(Math.random() * colorsAvatar.length)];

    return {
      initials: `${a.nome[0]}${a.cognome[0]}`,
      name: `${a.nome} ${a.cognome}`,
      role: "Agente",
      color: randomColor,
      immobili: a.immobiliInGestione,
      vendite: a.contrattiConclusi,
      fatturato: `€${a.fatturato}`,
    };
  });

  const portfolioDistribution = Object.entries(data.immobiliPerTipo).map(
    ([tipo, count]) => ({
      IconComponent:
        tipo === "Appartamento"
          ? FaBuilding
          : tipo === "Villa"
          ? FaHome
          : tipo === "Attico"
          ? FaWarehouse
          : FaTree,
      count,
      label: tipo,
    })
  );

  return (
    <div className="dashboard-container">
      <div className="header-dashboard">
        <h1>Ciao {user?.name}</h1>
        <h3>Dashboard ADMIN</h3>
      </div>
      <div className="general-dashboard">
        <div className="general-container">
          <div className="title-card">
            <h3>Totale Immobili:</h3>
          </div>
          <div className="data-card">
            <h3>{data.statistics.totaleImmobili}</h3>
          </div>
          <div className="data-card2">
            <h3>{data.statistics.immobiliInVerifica} in verifica</h3>
          </div>
        </div>

        <div className="general-container">
          <div className="title-card">
            <h3>Contratti Conclusi:</h3>
          </div>
          <div className="data-card">
            <h3>{data.statistics.contrattiConclusi}</h3>
          </div>
          <div className="data-card2">
            <h3>{data.statistics.fatturatoTotale}€ valore totale</h3>
          </div>
        </div>

        <div className="general-container">
          <div className="title-card">
            <h3>Valutazioni richieste:</h3>
          </div>
          <div className="data-card">
            <h3>{data.statistics.immobiliRegistratiMensili}</h3>
          </div>
          <div className="data-card2">
            <h3>
              +{data.statistics.immobiliRegistratiSettimanali} questa settimana
            </h3>
          </div>
        </div>

        <div className="general-container">
          <div className="title-card">
            <h3>Agenti attivi:</h3>
          </div>
          <div className="data-card">
            <h3>{data.statistics.totaleAgenti}</h3>
          </div>
          <div className="data-card2">
            <h3>{data.statistics.agentiStage} in training</h3>
          </div>
        </div>
      </div>

      {/* CONTAINER GRAFICO E CLASSIFICA */}
      <div className="performance-container">
        <div className="chart-section">
          <h2>Performance mensile team - vendite concluse</h2>
          <div className="bar-chart-mock">
            {monthlyPerformanceData.map((d) => (
              <div key={d.month} className="bar-wrapper">
                <div className="bar-value">{d.sales}</div>
                <div
                  className="bar"
                  style={{ height: getBarHeight(d.sales, maxSales) }}
                ></div>
                <div className="bar-label">{d.month}</div>
              </div>
            ))}
          </div>
        </div>

        {/* Classifica Top Performers */}
        <div className="top-performers-section">
          <h2>Top performers del mese</h2>
          <div className="performer-list">
            {topPerformers.length === 0 ? (
              <p>Nessun dato</p>
            ) : (
              topPerformers.map((p) => (
                <div key={p.rank} className="performer-item">
                  <div
                    className="rank-badge"
                    style={{ backgroundColor: getRankColor(p.rank) }}
                  >
                    {p.rank}
                  </div>
                  <div className="details">
                    <span>{p.name}</span>
                    <span>
                      {p.sales} vendite - €{p.value} fatturato
                    </span>
                  </div>
                </div>
              ))
            )}
          </div>
        </div>
      </div>

      {/* tabella 2: Comparativa Performance Agenti */}
      <div className="agent-comparison-container">
        <h2 className="comparison-title">Comparativa Performance Agenti</h2>
        <div className="agent-list">
          {agentData.map((a, i) => (
            <div key={i} className="agent-item">
              <div className="agent-info">
                <div
                  className="agent-avatar"
                  style={{ backgroundColor: a.color }}
                >
                  {a.initials}
                </div>
                <div className="agent-details">
                  <span className="agent-name">{a.name}</span>
                  <span className="agent-role">{a.role}</span>
                </div>
              </div>
              <div className="agent-metrics">
                <div className="metric-item">
                  <span className="metric-value">{a.immobili}</span>
                  <span className="metric-label">Immobili</span>
                </div>
                <div className="metric-item">
                  <span className="metric-value">{a.vendite}</span>
                  <span className="metric-label">Vendite</span>
                </div>
                <div className="metric-item metric-fatturato">
                  <span className="metric-value">{a.fatturato}</span>
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
          <div className="table-header">
            <div className="header-cell phase">Fase</div>
            <div className="header-cell time">Tempo medio</div>
            <div className="header-cell performance">Performance</div>
          </div>

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
          {portfolioDistribution.map((item, i) => (
            <div key={i} className="distribution-item">
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
