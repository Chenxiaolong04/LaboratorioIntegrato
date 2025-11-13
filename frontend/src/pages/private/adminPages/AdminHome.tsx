import { useEffect, useState } from "react";
import { FaCheckCircle, FaFilter } from "react-icons/fa";
import { FaSquareArrowUpRight } from "react-icons/fa6";
import { TbProgressCheck } from "react-icons/tb";
import { PiWarningCircleBold } from "react-icons/pi";
import Button from "../../../components/Button";
import { Link } from "react-router-dom";
import SearchBar from "../../../components/SearchBar";
import {
  getAdminDashboard,
  type DashboardData,
  type Immobile,
} from "../../../services/api";

const filterOptions = [
  { label: "Contratti conclusi", value: "contratti" },
  { label: "Incarichi in corso", value: "incarichi" },
  { label: "Valutazioni AI", value: "valutazioni" },
];

export default function AdminHome() {
  const [filter, setFilter] = useState<string | null>(null);
  const [dropdownOpen, setDropdownOpen] = useState(false);
  const [searchQuery, setSearchQuery] = useState("");
  const [statistics, setStatistics] = useState<
    DashboardData["statistics"] | null
  >(null);
  const [immobili, setImmobili] = useState<Immobile[]>([]);
  const [nextOffset, setNextOffset] = useState(0);
  const [hasMore, setHasMore] = useState(true);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    console.log('prova')
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

  if (!statistics) return <p>Caricamento...</p>;

  const mappedData = immobili.map((i) => ({
    tipo:
      i.tipo.toLowerCase() === "appartamento"
        ? "contratti"
        : i.tipo.toLowerCase() === "villa"
        ? "incarichi"
        : "valutazioni",
    proprietario: i.nomeProprietario,
    data: i.dataInserimento,
    agente: i.agenteAssegnato || "-",
  }));

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
        <div className="general-dashboard">
          <div className="general-container">
            <div className="title-card">
              <span>
                <FaCheckCircle size={32} color="green" />
              </span>
              <h3>Contratti conclusi</h3>
            </div>
            <div className="data-card">
              <h3>{statistics.contrattiConclusi}</h3>
              <Link to="/admin/contratti">
                <FaSquareArrowUpRight size={50} />
              </Link>
            </div>
          </div>

          <div className="general-container">
            <div className="title-card">
              <span>
                <TbProgressCheck size={32} color="orange" />
              </span>
              <h3>Incarichi in corso</h3>
            </div>
            <div className="data-card">
              <h3>{statistics.valutazioniInCorso}</h3>
              <Link to="/admin/incarichi">
                <FaSquareArrowUpRight size={50} />
              </Link>
            </div>
          </div>

          <div className="general-container">
            <div className="title-card">
              <span>
                <PiWarningCircleBold size={32} color="gray" />
              </span>
              <h3>Valutazioni AI effettuate</h3>
            </div>
            <div className="data-card">
              <h3>{statistics.valutazioniConAI}</h3>
              <Link to="/admin/valutazioniAI">
                <FaSquareArrowUpRight size={50} />
              </Link>
            </div>
          </div>
        </div>

        {/* Tabella avvisi */}
        <div className="table-container">
          <h2>Ultimi avvisi</h2>
          <div className="filter-buttons">
            <SearchBar
              placeholder="Cerca un proprietario"
              onSearch={(query) => setSearchQuery(query)}
            />

            <div className="dropdown">
              <Button
                onClick={() => setDropdownOpen(!dropdownOpen)}
                className="lightblu"
              >
                <FaFilter color={"white"} />
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
                      <div className="cell-content">
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
                          {filterOptions.find((f) => f.value === row.tipo)
                            ?.label || row.tipo}
                        </h3>
                      </div>
                    </td>
                    <td>{row.proprietario}</td>
                    <td>{row.data}</td>
                    <td>{row.agente}</td>
                    <td>
                      <Button className="lightblu">Dettagli</Button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>

          {hasMore && (
            <div className="btn-table">
              <Button onClick={handleLoadMore} disabled={loading}>
                {loading ? "Caricamento..." : "Mostra altri avvisi"}
              </Button>
            </div>
          )}
        </div>
      </div>

      {/* Statistiche mensili */}
      <div className="stats-today">
        <h2>Statistiche mensili</h2>
        <div className="card">
          <h3>Contratti conclusi</h3>
          <p>+ {statistics.contrattiConclusiMensili}</p>
        </div>
        <div className="card">
          <h3>Incarichi nuovi</h3>
          <p>+ {statistics.valutazioniInCorsoMensili}</p>
        </div>
        <div className="card">
          <h3>Valutazioni AI effettuate</h3>
          <p>+ {statistics.valutazioniConAIMensili}</p>
        </div>
      </div>
    </div>
  );
}
