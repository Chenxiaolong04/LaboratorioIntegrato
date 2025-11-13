import { useEffect, useState } from "react";
import SearchBar from "../../../components/SearchBar";
import Button from "../../../components/Button";
import {
  getContrattiChiusi,
  type ContrattoChiuso,
} from "../../../services/api";

export default function ContractsAdmin() {
  const [searchQuery, setSearchQuery] = useState("");
  const [contratti, setContratti] = useState<ContrattoChiuso[]>([]);
  const [nextOffset, setNextOffset] = useState(0);
  const [hasMore, setHasMore] = useState(true);
  const [loading, setLoading] = useState(false);

  // Caricamento iniziale
  useEffect(() => {
    fetchContratti(0);
  }, []);

  const fetchContratti = async (offset: number) => {
    setLoading(true);
    try {
      const data = await getContrattiChiusi(offset, 10);
      setContratti((prev) => [...prev, ...data.contratti]);
      setNextOffset(data.nextOffset);
      setHasMore(data.hasMore);
    } catch (err) {
      console.error("Errore caricamento contratti:", err);
    } finally {
      setLoading(false);
    }
  };

  const handleLoadMore = () => {
    if (hasMore) fetchContratti(nextOffset);
  };

  const filteredContratti = contratti.filter((c) =>
    (c.nomeProprietario || "").toLowerCase().includes(searchQuery.toLowerCase())
  );

  return (
    <div className="dashboard-container">
      <div className="table-container">
        <h2>Contratti conclusi</h2>

        <div className="filter-buttons">
          <SearchBar
            placeholder="Cerca un proprietario"
            onSearch={(query) => setSearchQuery(query)}
          />
        </div>

        <div className="table-wrapper">
          <table className="alerts-table">
            <thead>
              <tr>
                <th>Nome proprietario</th>
                <th>Data inizio</th>
                <th>Data fine</th>
                <th>Agente assegnato</th>
                <th>Azioni</th>
              </tr>
            </thead>
            <tbody>
              {filteredContratti.map((c, i) => (
                <tr key={i}>
                  <td>{c.nomeProprietario || "-"}</td>
                  <td>{c.dataInizio || "-"}</td>
                  <td>{c.dataFine || "-"}</td>
                  <td>{c.agenteAssegnato || "-"}</td>
                  <td>
                    <div className="action-buttons">
                      <Button className="lightblu" title="Maggiori dettagli sul contratto">Dettagli</Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>

        {hasMore && (
          <div className="btn-table">
            <Button onClick={handleLoadMore} disabled={loading}>
              {loading ? "Caricamento..." : "Mostra altri contratti"}
            </Button>
          </div>
        )}
      </div>
    </div>
  );
}
