import { useEffect, useState } from "react";
import { FaCheckCircle, FaFilter } from "react-icons/fa";
import { FaSquareArrowUpRight } from "react-icons/fa6";
import { TbProgressCheck } from "react-icons/tb";
import { PiWarningCircleBold } from "react-icons/pi";
import Button from "../../../components/Button";
import { Link } from "react-router-dom";
import SearchBar from "../../../components/SearchBar";
import { useAuth } from "../../../context/AuthContext";
import {
  getAgenteDashboard,
  type AgenteDashboardData,
  type Immobile,
} from "../../../services/api";
import Loader from "../../../components/Loader";

/**
 * Defines the available filter options for the alerts table.
 * @type {Array<{label: string, value: string}>}
 */
const filterOptions = [
  { label: "Miei Contratti", value: "contratti" },
  { label: "Miei Incarichi", value: "incarichi" },
  { label: "Valutazioni AI (Generali)", value: "valutazioni" },
];

/**
 * AgentHome component.
 * Displays the dashboard for an agent, including key statistics, a list of recent alerts/properties,
 * and monthly performance statistics.
 * @returns {JSX.Element} The Agent Home dashboard.
 */
export default function AgentHome() {
  /**
   * Retrieves the current authenticated user from the authentication context.
   * @type {object}
   */
  const { user } = useAuth();

  /**
   * State for the currently applied filter (e.g., 'contratti', 'incarichi'). Null for no filter.
   * @type {[string | null, function(string | null): void]}
   */
  const [filter, setFilter] = useState<string | null>(null);

  /**
   * State to control the visibility of the filter dropdown menu.
   * @type {[boolean, function(boolean): void]}
   */
  const [dropdownOpen, setDropdownOpen] = useState(false);

  /**
   * State for the current search query used to filter the alerts table.
   * @type {[string, function(string): void]}
   */
  const [searchQuery, setSearchQuery] = useState("");

  /**
   * State for storing the overall statistics data fetched from the dashboard API.
   * @type {[AgenteDashboardData["statistics"] | null, function(AgenteDashboardData["statistics"] | null): void]}
   */
  const [statistics, setStatistics] = useState<
    AgenteDashboardData["statistics"] | null
  >(null);

  /**
   * State for the currently selected property (Immobile) to display details in a modal.
   * @type {[Immobile | null, function(Immobile | null): void]}
   */
  const [selected, setSelected] = useState<Immobile | null>(null);

  /**
   * State for storing the list of properties/alerts to display in the table.
   * @type {[Immobile[], function(Immobile[]): void]}
   */
  const [immobili, setImmobili] = useState<Immobile[]>([]);

  /**
   * State for the offset used in pagination for the next batch of properties.
   * @type {[number, function(number): void]}
   */
  const [nextOffset, setNextOffset] = useState(0);

  /**
   * State indicating if there are more properties to load via pagination.
   * @type {[boolean, function(boolean): void]}
   */
  const [hasMore, setHasMore] = useState(true);

  /**
   * State indicating if data is currently being loaded.
   * @type {[boolean, function(boolean): void]}
   */
  const [loading, setLoading] = useState(false);

  /**
   * useEffect hook to fetch initial dashboard statistics and property list when the component mounts
   * or when the user object changes.
   */
  useEffect(() => {
    if (!user) return;

    /**
     * Fetches the initial data for the agent dashboard.
     * @async
     * @returns {Promise<void>}
     */
    async function fetchInitialData() {
      setLoading(true);
      try {
        const data = await getAgenteDashboard(0, 10);
        setStatistics(data.statistics);
        setImmobili(data.immobili || []);
        setNextOffset(data.nextOffset ?? 0);
        setHasMore(data.hasMore ?? false);
      } catch (err) {
        console.error(err);
      } finally {
        setLoading(false);
      }
    }

    fetchInitialData();
  }, [user]);

  /**
   * Handles loading the next batch of properties/alerts (pagination).
   * Appends the new data to the existing list.
   * @async
   * @returns {Promise<void>}
   */
  const handleLoadMore = async () => {
    if (!hasMore) return;
    setLoading(true);
    try {
      const data = await getAgenteDashboard(nextOffset, 10);
      setImmobili((prev) => [...prev, ...(data.immobili || [])]);
      setNextOffset(data.nextOffset ?? 0);
      setHasMore(data.hasMore ?? false);
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  /**
   * Display a loader while initial statistics are being fetched.
   */
  if (!statistics) return <Loader />;

  /**
   * Maps the raw property data (Immobile) to a simpler, standardized format for the table,
   * categorizing properties based on their 'tipo' (type).
   * @type {Array<{tipo: string, proprietario: string, data: string, agente: string}>}
   */
  const mappedData = (immobili || []).map((i) => ({
    tipo:
      i.tipo?.toLowerCase() === "appartamento"
        ? "contratti"
        : i.tipo?.toLowerCase() === "villa"
        ? "incarichi"
        : "valutazioni",
    proprietario: i.nomeProprietario || "-",
    data: i.dataInserimento || "-",
    agente: i.agenteAssegnato || "-",
  }));

  /**
   * Filters the mapped data first by the selected filter type, and then by the search query
   * across multiple fields (type, owner, agent, date).
   * @type {Array<object>}
   */
  const filteredData = mappedData
    .filter((d) => (filter ? d.tipo === filter : true))
    .filter((d) => {
      const query = searchQuery.toLowerCase();
      return (
        d.tipo.toLowerCase().includes(query) ||
        d.proprietario.toLowerCase().includes(query) ||
        d.agente.toLowerCase().includes(query) ||
        d.data.toLowerCase().includes(query)
      );
    });

  return (
    <div className="dashboard-container">
      <div className="general-latest-container">
        {/* Dashboard cards */}
        <div className="general-dashboard">
          {[
            {
              icon: <FaCheckCircle size={36} color="green" />,
              title: "Contratti conclusi",
              value: statistics.mieiContrattiConclusi,
              link: "/agente/contratti",
            },
            {
              icon: <TbProgressCheck size={36} color="orange" />,
              title: "Incarichi in corso",
              value: statistics.mieiIncarichiInCorso,
              link: "/agente/incarichi",
            },
            {
              icon: <PiWarningCircleBold size={36} color="gray" />,
              title: "Valutazioni AI",
              value: statistics.valutazioniConAI,
              link: "/agente/valutazioniAI",
            },
          ].map((card, idx) => (
            <div className="general-container" key={idx}>
              <div className="title-card">
                {card.icon}
                <h3>{card.title}</h3>
              </div>
              <div className="data-card">
                <h3>{card.value}</h3>
                <Link to={card.link}>
                  <FaSquareArrowUpRight size={50} color="white" />
                </Link>
              </div>
            </div>
          ))}
        </div>

        {/* Table */}
        <div className="table-container">
          <h2>Ultimi avvisi</h2>
          <div className="filter-buttons">
            <SearchBar placeholder="Cerca un proprietario" onSearch={setSearchQuery} />
            <div className="dropdown">
              <Button
                onClick={() => setDropdownOpen(!dropdownOpen)}
                className="blu"
              >
                <FaFilter color="white" />
              </Button>
              {dropdownOpen && (
                <ul className="dropdown-menu">
                  {filterOptions.map((opt) => (
                    <li
                      key={opt.value}
                      className={filter === opt.value ? "active" : ""}
                      onClick={() => {
                        setFilter(opt.value);
                        setDropdownOpen(false);
                      }}
                    >
                      {opt.label}
                    </li>
                  ))}
                </ul>
              )}
            </div>
            {filter && (
              <Button className="remove-filter" onClick={() => setFilter(null)}>
                Rimuovi filtro
              </Button>
            )}
          </div>

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
                      {row.tipo === "contratti" && (
                        <FaCheckCircle size={32} color="green" />
                      )}
                      {row.tipo === "incarichi" && (
                        <TbProgressCheck size={32} color="orange" />
                      )}
                      {row.tipo === "valutazioni" && (
                        <PiWarningCircleBold size={32} color="gray" />
                      )}
                      <h3>
                        {filterOptions.find((f) => f.value === row.tipo)?.label ||
                          row.tipo}
                      </h3>
                    </td>
                    <td>{row.proprietario}</td>
                    <td>{row.data}</td>
                    <td>{row.agente}</td>
                    <td>
                      <Button
                        className="blu"
                        onClick={() => setSelected(immobili[i] || null)}
                      >
                        Dettagli
                      </Button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>

          <div className="alerts-cards">
            {filteredData.map((row, i) => (
              <div className="alert-card" key={i}>
                <div className="card-row">
                  <b>Tipo:</b>{" "}
                  {filterOptions.find((f) => f.value === row.tipo)?.label ||
                    row.tipo}
                </div>
                <div className="card-row">
                  <b>Nome proprietario:</b> {row.proprietario || "—"}
                </div>
                <div className="card-row">
                  <b>Data:</b> {row.data || "—"}
                </div>
                <div className="card-row">
                  <b>Agente assegnato:</b> {row.agente || "—"}
                </div>
                <div className="card-actions">
                  <Button
                    className="blu"
                    onClick={() => setSelected(immobili[i] || null)}
                  >
                    Dettagli
                  </Button>
                </div>
              </div>
            ))}
          </div>

          {hasMore && (
            <div className="btn-table">
              <Button
                onClick={handleLoadMore}
                disabled={loading}
                className="blu"
              >
                {loading ? "Caricamento..." : "Mostra altri avvisi"}
              </Button>
            </div>
          )}
        </div>

        {selected && (
          /**
           * Modal overlay for displaying details of a selected property/alert.
           */
          <div className="modal-overlay" onClick={() => setSelected(null)}>
            <div className="modal" onClick={(e) => e.stopPropagation()}>
              <h3>Dettagli Avviso</h3>
              <div className="card-row">
                <b>Tipo:</b>{" "}
                {filterOptions.find((f) => f.value === selected.tipo)?.label ||
                  selected.tipo}
              </div>
              <div className="card-row">
                <b>Nome proprietario:</b> {selected.nomeProprietario || "—"}
              </div>
              <div className="card-row">
                <b>Data:</b> {selected.dataInserimento || "—"}
              </div>
              <div className="card-row">
                <b>Agente assegnato:</b> {selected.agenteAssegnato || "—"}
              </div>
              <div className="card-actions">
                <Button className="red" onClick={() => setSelected(null)}>
                  Chiudi
                </Button>
              </div>
            </div>
          </div>
        )}
      </div>

      <div className="stats-today">
        <h2>Statistiche mensili</h2>
        <div className="card">
          <h3>Contratti conclusi</h3>
          <p>+ {statistics.mieiContrattiConclusiMensili}</p>
        </div>
        <div className="card">
          <h3>Incarichi nuovi</h3>
          <p>+ {statistics.mieiIncarichiNuoviMensili}</p>
        </div>
        <div className="card">
          <h3>Valutazioni AI</h3>
          <p>+ {statistics.valutazioniConAIMensili}</p>
        </div>
      </div>
    </div>
  );
}