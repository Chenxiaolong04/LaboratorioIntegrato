import { useEffect, useState } from "react";
// --- UNUSED IMPORTS IN THE VISIBLE PART (Table/Filters) ---
/* import { FaCheckCircle, FaFilter } from "react-icons/fa";
import { PiWarningCircleBold } from "react-icons/pi";
import { TbProgressCheck } from "react-icons/tb"; // Not imported, but would have been used */
import { FaCheckCircle } from "react-icons/fa";
import { PiWarningCircleBold } from "react-icons/pi";
// --------------------------------------------------------------------
import Button from "../../../components/Button";
import { FaBuilding, FaHome, FaTree, FaWarehouse } from 'react-icons/fa';


// --- DEFINITIONS FOR MOCK API/TABLE NOT USED IN THE CURRENT VIEW ---
/*
type Immobile = {
  tipo: string;
  nomeProprietario: string;
  dataInserimento: string;
  agenteAssegnato: string;
};

type AdminDashboardData = {
  statistics: any;
  immobili: Immobile[];
  nextOffset: number;
  hasMore: boolean;
};

// Mock functions for services (SIMULATED API)
async function getAdminDashboard(offset: number, limit: number): Promise<AdminDashboardData> {
    await new Promise(resolve => setTimeout(resolve, 500)); // Simulation of delay
    const mockImmobili: Immobile[] = [
        { tipo: "Appartamento", nomeProprietario: "Rossi Mario", dataInserimento: "2024-10-20", agenteAssegnato: "Marco Bellini" },
        { tipo: "Villa", nomeProprietario: "Bianchi Anna", dataInserimento: "2024-10-18", agenteAssegnato: "Giulia Ferri" },
        { tipo: "Appartamento", nomeProprietario: "Verdi Luca", dataInserimento: "2024-10-15", agenteAssegnato: "Alice Bianchi" },
    ];
    return {
        statistics: {},
        immobili: mockImmobili.slice(offset, offset + limit),
        nextOffset: offset + limit,
        hasMore: offset + limit < mockImmobili.length,
    };
}
const Loader = () => <div style={{textAlign: 'center', padding: '50px'}}>Caricamento...</div>;


const filterOptions = [
  { label: "Contratti conclusi", value: "contratti" },
  { label: "Incarichi in corso", value: "incarichi" },
  { label: "Valutazioni AI", value: "valutazioni" },
];
*/
// ----------------------------------------------------------------------------------------


/**
 * Static data for the Monthly Performance chart.
 * @type {{month: string, sales: number}[]}
 */
const monthlyPerformanceData = [
  { month: "Giu", sales: 48 },
  { month: "Lug", sales: 61 },
  { month: "Ago", sales: 42 },
  { month: "Set", sales: 55 },
  { month: "Ott", sales: 67 },
  { month: "Nov", sales: 72 },
];

/**
 * Static data for the Top Performers ranking.
 * @type {{rank: number, name: string, sales: number, value: string}[]}
 */
const topPerformers = [
  { rank: 1, name: "Marco Bellini", sales: 15, value: "4.2M" },
  { rank: 2, name: "Giulia Ferri", sales: 12, value: "3.5M" },
  { rank: 3, name: "Alice Bianchi", sales: 11, value: "2.9M" },
];

/**
 * Helper function to get the badge color based on the rank.
 * @param {number} rank - The performer's rank (1, 2, 3, etc.).
 * @returns {string} The CSS color code for the badge.
 */
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

// ** CRUCIAL LOGIC FOR BAR HEIGHT (in pixels) **
/** Maximum sales value for scaling the bar chart. */
const MAX_SALES_SCALE = 75;
/** Maximum height of the bar in pixels. */
const MAX_BAR_HEIGHT_PX = 200;

/**
 * Calculates the bar height in pixels based on current sales value.
 * Uses a fixed maximum scale for normalization.
 * @param {number} sales - The number of sales for the current month.
 * @returns {string} The calculated height string with 'px' suffix (e.g., "150px").
 */
const getBarHeight = (sales: number): string => {
  // Calculate height: (current sales / fixed maximum) * maximum height in pixels
  const heightInPx = (sales / MAX_SALES_SCALE) * MAX_BAR_HEIGHT_PX;
  return `${heightInPx}px`;
};

/**
 * Static data for agent performance comparison.
 * @type {{initials: string, name: string, role: string, color: string, immobili: number, vendite: number, fatturato: string}[]}
 */
const agentData = [
  {
    initials: 'MB',
    name: 'Marco Bellini',
    role: 'Agente Senior - Torino',
    color: '#00A4BA',
    immobili: 52,
    vendite: 15,
    fatturato: '€4.2M'
  },
  {
    initials: 'GF',
    name: 'Giulia Ferri',
    role: 'Agente Senior - Cuneo',
    color: '#546E7A',
    immobili: 46,
    vendite: 12,
    fatturato: '€3.5M'
  },
  {
    initials: 'AB',
    name: 'Alice Bianchi',
    role: 'Agente - Torino',
    color: '#FF8A3C',
    immobili: 41,
    vendite: 11,
    fatturato: '€2.9M'
  },
  {
    initials: 'SR',
    name: 'Sara Ricci',
    role: 'Agente - Novara',
    color: '#E9573F',
    immobili: 34,
    vendite: 9,
    fatturato: '€2.4M'
  },
  {
    initials: 'LM',
    name: 'Luca Martini',
    role: 'Agente Senior - Alessandria',
    color: '#6AA84F',
    immobili: 28,
    vendite: 7,
    fatturato: '€1.8M'
  },
]


/**
 * Type definition for performance levels.
 * @typedef {'Ottimo' | 'Eccellente' | 'Buono' | 'Standard'} PerformanceLevel
 */
type PerformanceLevel = 'Ottimo' | 'Eccellente' | 'Buono' | 'Standard';

/**
 * Static data for the average operational time table.
 * @type {{fase: string, tempoMedio: string, performance: PerformanceLevel}[]}
 */
const operationalData = [
  {
    fase: 'Valutazione AI - Valutazione agente',
    tempoMedio: '2.3 giorni',
    performance: 'Ottimo' as PerformanceLevel
  },
  {
    fase: 'Valutazione - Esclusiva',
    tempoMedio: '5.7 giorni',
    performance: 'Eccellente' as PerformanceLevel
  },
  {
    fase: 'Pubblicazione - Primo contatto',
    tempoMedio: '5.7 giorni',
    performance: 'Buono' as PerformanceLevel
  },
  {
    fase: 'Prima visita - Offerta',
    tempoMedio: '8.4 giorni',
    performance: 'Ottimo' as PerformanceLevel
  },
  {
    fase: 'Offerta - Preliminare',
    tempoMedio: '12.1 giorni',
    performance: 'Eccellente' as PerformanceLevel
  },
  {
    fase: 'Preliminare - Rogito',
    tempoMedio: '45 giorni',
    performance: 'Standard' as PerformanceLevel
  },
];

/**
 * Function to get the CSS class name based on the performance level.
 * @param {PerformanceLevel} level - The performance level string.
 * @returns {string} The corresponding CSS class string.
 */
const getPerformanceClass = (level: PerformanceLevel): string => {
  return `performance-${level.toLowerCase()}`;
};

/**
 * Static data for the property portfolio distribution.
 * @type {{IconComponent: React.ElementType, count: number, label: string}[]}
 */
const portfolioDistribution = [
  {
    IconComponent: FaBuilding, 
    count: 120,
    label: 'Appartamenti'
  },
  {
    IconComponent: FaHome, 
    count: 43,
    label: 'Ville'
  },
  {
    IconComponent: FaWarehouse, 
    count: 20,
    label: 'Attici'
  },
  {
    IconComponent: FaTree, 
    count: 11,
    label: 'Loft'
  },
];

/**
 * AdminHome component.
 * Displays the main administrative dashboard with various statistics, charts, and tables.
 * Note: Much of the initial data fetching logic is commented out as the component uses static data.
 * @returns {JSX.Element} The Admin Home Dashboard.
 */
export default function AdminHome() {

  // --- CONTROL STATES AND DYNAMIC DATA NOT USED WITH STATIC DATA ---
  /* const [filter, setFilter] = useState<string | null>(null);
  const [dropdownOpen, setDropdownOpen] = useState(false);
  const [searchQuery, setSearchQuery] = useState("");
  const [statistics, setStatistics] = useState<AdminDashboardData["statistics"] | null>(null);
  const [selected, setSelected] = useState<Immobile | null>(null);
  const [immobili, setImmobili] = useState<Immobile[]>([]);
  const [nextOffset, setNextOffset] = useState(0);
  const [hasMore, setHasMore] = useState(true);
  const [loading, setLoading] = useState(false);
  
  const [selected, setSelected] = useState(false); // Keeping only selected for the modal (even if unused)

  // --- DATA LOADING EFFECTS AND FUNCTIONS NOT USED WITH STATIC DATA ---
  /*
  useEffect(() => {
    async function fetchInitialData() {
      setLoading(true);
      try {
        const data = await getAdminDashboard(0, 10);
        setStatistics(data.statistics);
        setImmobili(data.immobili);
        setNextOffset(data.nextOffset);
        setHasMore(data.hasMore);
      } catch (err) {
        console.error(err);
      } finally {
        setLoading(false);
      }
    }
    fetchInitialData();
  }, []);

  const handleLoadMore = async () => {
    if (!hasMore) return;
    setLoading(true);
    try {
      const data = await getAdminDashboard(nextOffset, 10);
      setImmobili((prev) => [...prev, ...data.immobili]);
      setNextOffset(data.nextOffset);
      setHasMore(data.hasMore);
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };
  
  if (!statistics) return <Loader />;

  const mappedData = immobili.map((i) => ({
    tipo: i.tipo.toLowerCase() === "appartamento" ? "contratti" :
          i.tipo.toLowerCase() === "villa" ? "incarichi" : "valutazioni",
    proprietario: i.nomeProprietario,
    data: i.dataInserimento,
    agente: i.agenteAssegnato || "-",
  }));

  const filteredData = mappedData
    .filter((d) => (filter ? d.tipo === filter : true))
    .filter((d) => d.proprietario.toLowerCase().includes(searchQuery.toLowerCase()));
  */


  return (
    <div className="dashboard">

      <div className="dashboard-container">
        {/* CARDS 2x2 */}
        <div className="general-dashboard">
          <div className="general-container">
            <div className="title-card"><h3>Totale Immobili:</h3></div>
            <div className="data-card"><h3>348</h3></div>
            <div className="data-card2"><h3>142 in vendita attiva</h3></div>
          </div>

          <div className="general-container">
            <div className="title-card"><h3>Contratti Conclusi:</h3></div>
            <div className="data-card">
              <h3>67</h3>
              <FaCheckCircle size={35} color="#00C853" />
            </div>
            <div className="data-card2"><h3>€ 18.5M Valore Totale</h3></div>
          </div>

          <div className="general-container">
            <div className="title-card"><h3>Valutazioni richieste:</h3></div>
            <div className="data-card">
              <h3>187</h3>
              <PiWarningCircleBold size={35} color="#263238" />
            </div>
            <div className="data-card2"><h3>+34 Questa Settimana</h3></div>
          </div>

          <div className="general-container">
            <div className="title-card"><h3>Agenti attivi:</h3></div>
            <div className="data-card"><h3>12</h3></div>
            <div className="data-card2"><h3>3 in training</h3></div>
          </div>
        </div>

        {/* CHART AND RANKING CONTAINER */}
        <div className="performance-container">
          {/* Monthly Performance Chart */}
          <div className="chart-section">
            <h2>Performance mensile team - vendite concluse</h2>
            <div className="bar-chart-mock">
              {monthlyPerformanceData.map((data) => (
                <div key={data.month} className="bar-wrapper">
                  <div className="bar-value">{data.sales}</div>
                  {/* APPLICATION OF HEIGHT IN PIXELS */}
                  <div className="bar" style={{ height: getBarHeight(data.sales) }}></div>
                  <div className="bar-label">{data.month}</div>
                </div>
              ))}
            </div>
          </div>

          {/* Top Performers Ranking */}
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
                    <span>{performer.sales} vendite - €{performer.value} fatturato</span>
                  </div>
                </div>
              ))}
            </div>
          </div>
        </div>

        {/* table 2: Agent Performance Comparison */}
        <div className="agent-comparison-container">
          <h2 className="comparison-title">Comparativa Performance Agenti</h2>
          <div className="agent-list">
            {agentData.map((agent, index) => (
              <div key={index} className="agent-item">

                {/* Avatar and Agent Details Section */}
                <div className="agent-info">
                  <div className="agent-avatar" style={{ backgroundColor: agent.color }}>
                    {agent.initials}
                  </div>
                  <div className="agent-details">
                    <span className="agent-name">{agent.name}</span>
                    <span className="agent-role">{agent.role}</span>
                  </div>
                </div>

                {/* Metrics Section */}
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


        {/* table 3: Operational Analysis - Average Times */}
        <div className="operational-analysis-container">
          <h2 className="analysis-title">Analisi operativa - Tempi medi</h2>

          <div className="analysis-table">
            {/* Table Header */}
            <div className="table-header">
              <div className="header-cell phase">Fase</div>
              <div className="header-cell time">Tempo medio</div>
              <div className="header-cell performance">Performance</div>
            </div>

            {/* Data Rows */}
            <div className="table-body">
              {operationalData.map((item, index) => (
                <div key={index} className="table-row">
                  <div className="cell phase">{item.fase}</div>
                  <div className="cell time">{item.tempoMedio}</div>
                  <div className={`cell performance ${getPerformanceClass(item.performance)}`}>
                    {item.performance}
                  </div>
                </div>
              ))}
            </div>

          </div>
        </div>


        {/* table 4: Portfolio Distribution */}
        <div className="portfolio-distribution-container">
          <h2 className="distribution-title">Distribuzione portfolio per tipologia immobile</h2>

          <div className="distribution-grid">
            {portfolioDistribution.map((item, index) => (
              <div key={index} className="distribution-item">
                {/* Use of IconComponent */}
                <div className="item-icon">
                  <item.IconComponent size={40} color="#546E7A" />
                </div>
                <div className="item-count">{item.count}</div>
                <div className="item-label">{item.label}</div>
              </div>
            ))}
          </div>
        </div>


        {/* ALERT TABLE CONTAINER (COMMENTED OUT) */}
        {/* <div className="table-wrapper-container">
          <div className="table-container">
          <h2>Ultimi avvisi</h2>

          // FILTERS
          <div className="filter-buttons">
            <SearchBar placeholder="Cerca un proprietario" onSearch={setSearchQuery} />
            <div className="dropdown">
              <Button onClick={() => setDropdownOpen(!dropdownOpen)} className="blu">
                <FaFilter color="white" />
              </Button>
              {dropdownOpen && (
                <ul className="dropdown-menu">
                  {filterOptions.map(opt => (
                    <li
                      key={opt.value}
                      className={filter === opt.value ? "active" : ""}
                      onClick={() => { setFilter(opt.value); setDropdownOpen(false); }}
                    >
                      {opt.label}
                    </li>
                  ))}
                </ul>
              )}
            </div>
            {filter && <Button className="remove-filter" onClick={() => setFilter(null)}>Rimuovi filtro</Button>}
          </div>

          // TABLE
          <div className="table-wrapper">
            <table className="alerts-table">
              <thead>
                <tr>
                  <th>Tipo</th>
                  <th>Nome proprietario</th>
                  <th>Data</th>
                  <th>Agente assegnato</th>
                  <th>Azioni</th>
                </tr>
              </thead>
              <tbody>
                {filteredData.map((row, i) => (
                  <tr key={i}>
                    <td>
                      <div className="cell-content">
                        {row.tipo === "contratti" && <FaCheckCircle size={32} color="#2ECC71" />}
                        {row.tipo === "incarichi" && <TbProgressCheck size={32} color="#FFA726" />}
                        {row.tipo === "valutazioni" && <PiWarningCircleBold size={32} color="#546E7A" />}
                        <h3>{filterOptions.find(f => f.value === row.tipo)?.label || row.tipo}</h3>
                      </div>
                    </td>
                    <td>{row.proprietario}</td>
                    <td>{row.data}</td>
                    <td>{row.agente}</td>
                    <td>
                      <Button className="lightblu" onClick={() => setSelected(immobili[i])}>Dettagli</Button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>

          // MOBILE CARDS
          <div className="alerts-cards">
            {filteredData.map((row, i) => (
              <div className="alert-card" key={i}>
                <div className="card-row"><b>Tipo:</b> {filterOptions.find(f => f.value === row.tipo)?.label || row.tipo}</div>
                <div className="card-row"><b>Nome proprietario:</b> {row.proprietario || "—"}</div>
                <div className="card-row"><b>Data:</b> {row.data || "—"}</div>
                <div className="card-row"><b>Agente assegnato:</b> {row.agente || "—"}</div>
                <div className="card-actions">
                  <Button className="lightblu" onClick={() => setSelected(immobili[i])}>Dettagli</Button>
                </div>
              </div>
            ))}
          </div>

          // LOAD MORE
          {hasMore && (
            <div className="btn-table">
              <Button onClick={handleLoadMore} disabled={loading} className="blu">
                {loading ? "Caricamento..." : "Mostra altri avvisi"}
              </Button>
            </div>
          )}
        </div>
      </div>
      */}

        {/* MODAL (COMMENTED OUT - USES selected STATE BUT NOT THE DATA) 
        {selected && (
          <div className="modal-overlay" onClick={() => setSelected(false)}>
            <div className="modal" onClick={e => e.stopPropagation()}>
              <h3>Dettagli Avviso</h3>
              {/* THE DATA HERE IS COMMENTED OUT AS THE VARIABLES NO LONGER EXIST 
              {/* <div className="card-row"><b>Tipo:</b> {filterOptions.find(f => f.value === selected.tipo)?.label || selected.tipo}</div>
              <div className="card-row"><b>Nome proprietario:</b> {selected.nomeProprietario || "—"}</div>
              <div className="card-row"><b>Data:</b> {selected.dataInserimento || "—"}</div>
              <div className="card-row"><b>Agente assegnato:</b> {selected.agenteAssegnato || "—"}</div> 
              <div className="card-actions">
                <Button className="red" onClick={() => setSelected(false)}>Chiudi</Button>
              </div>
            </div>
          </div>
        )}
        */}


      </div>
    </div>
  );
}