import { useEffect, useState } from "react";
import SearchBar from "../../../components/SearchBar";
import Button from "../../../components/Button";
import {
  getContrattiChiusi,
  type ContrattoChiuso,
} from "../../../services/api";
import Loader from "../../../components/Loader";

export default function ContractsAdmin() {
  const [searchQuery, setSearchQuery] = useState("");
  const [contratti, setContratti] = useState<ContrattoChiuso[]>([]);
  const [selectedContract, setSelectedContract] =
    useState<ContrattoChiuso | null>(null);
  const [nextOffset, setNextOffset] = useState(0);
  const [hasMore, setHasMore] = useState(true);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    (async () => {
      setLoading(true);
      try {
        const res = await getContrattiChiusi(0, 10);
        setContratti(res.contratti);
        setNextOffset(res.nextOffset);
        setHasMore(res.hasMore);
      } catch (err) {
        console.error("Errore caricamento contratti:", err);
      } finally {
        setLoading(false);
      }
    })();
  }, []);

  async function handleLoadMore() {
    const res = await getContrattiChiusi(nextOffset, 10);
    setContratti((prev) => [...prev, ...res.contratti]);
    setNextOffset(res.nextOffset);
    setHasMore(res.hasMore);
  }

  const filteredContratti = contratti.filter((c) =>
    (c.nomeProprietario || "").toLowerCase().includes(searchQuery.toLowerCase())
  );

  function formatDate(isoDate?: string) {
    if (!isoDate) return "-";
    const date = new Date(isoDate);
    // Opzioni: giorno mese anno
    return date.toLocaleDateString("it-IT", {
      day: "2-digit",
      month: "2-digit",
      year: "numeric",
    });
  }

  return (
    <div className="dashboard-container">
      {loading && contratti.length === 0 ? (
        <Loader />
      ) : (
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
                {filteredContratti.map((row) => (
                  <tr key={row.numeroContratto}>
                    <td>{row.nomeProprietario || "-"}</td>
                    <td>{formatDate(row.dataInizio)}</td>
                    <td>{formatDate(row.dataFine)}</td>
                    <td>{row.agenteAssegnato || "-"}</td>
                    <td>
                      <div className="action-buttons">
                        <Button
                          className="lightblu"
                          title="Maggiori dettagli sul contratto"
                          onClick={() => setSelectedContract(row)}
                        >
                          Dettagli
                        </Button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>

          <div className="contract-cards">
            {filteredContratti.map((row) => (
              <div className="contract-card" key={row.numeroContratto}>
                <div className="card-row">
                  <b>Proprietario:</b>
                  <span>{row.nomeProprietario || "-"}</span>
                </div>

                <div className="card-row">
                  <b>Data inizio:</b>
                  <span>{formatDate(row.dataInizio)}</span>
                </div>

                <div className="card-row">
                  <b>Data fine:</b>
                  <span>{formatDate(row.dataFine)}</span>
                </div>

                <div className="card-row">
                  <b>Agente assegnato:</b>
                  <span>{row.agenteAssegnato || "-"}</span>
                </div>

                <div className="card-actions">
                  <Button
                    className="lightblu"
                    onClick={() => setSelectedContract(row)}
                  >
                    Dettagli
                  </Button>
                </div>
              </div>
            ))}
          </div>

          {hasMore && (
            <div className="btn-table">
              <Button onClick={handleLoadMore} disabled={loading}>
                {loading ? "Caricamento..." : "Mostra altri contratti"}
              </Button>
            </div>
          )}
        </div>
      )}

      {selectedContract && (
        <div
          className="modal-overlay"
          onClick={() => setSelectedContract(null)}
        >
          <div className="modal" onClick={(e) => e.stopPropagation()}>
            <h3>Dettagli Contratto</h3>
            <p>
              <b>Numero contratto:</b> {selectedContract.numeroContratto}
            </p>
            <p>
              <b>Data inizio:</b> {formatDate(selectedContract.dataInizio)}
            </p>
            <p>
              <b>Data fine:</b> {formatDate(selectedContract.dataFine)}
            </p>

            <h4>Immobile</h4>
            <p>
              <b>Tipo:</b> {selectedContract.tipo || "-"}
            </p>
            <p>
              <b>Nome proprietario: </b>
              {selectedContract.nomeProprietario || "-"}
            </p>
            <p>
              <b>Data inserimento:</b> {selectedContract.dataInserimento || "-"}
            </p>
            <p>
              <b>Agente assegnato:</b> {selectedContract.agenteAssegnato || "-"}
            </p>

            <Button className="red" onClick={() => setSelectedContract(null)}>
              Chiudi
            </Button>
          </div>
        </div>
      )}
    </div>
  );
}
