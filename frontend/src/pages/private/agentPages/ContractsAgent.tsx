import { useEffect, useState } from "react";
import SearchBar from "../../../components/SearchBar";
import Button from "../../../components/Button";
import Loader from "../../../components/Loader";
import { useAuth } from "../../../context/AuthContext";
import {
  getContrattiChiusiByAgente,
  type ContrattoChiuso,
  type ContrattiResponse,
} from "../../../services/api";

export default function ContractsAgent() {
  const { user } = useAuth();
  const [searchQuery, setSearchQuery] = useState("");
  const [contratti, setContratti] = useState<ContrattoChiuso[]>([]);
  const [selectedContract, setSelectedContract] = useState<ContrattoChiuso | null>(null);
  const [nextOffset, setNextOffset] = useState(0);
  const [hasMore, setHasMore] = useState(true);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    if (!user) return;

    (async () => {
      setLoading(true);
      try {
        const res: ContrattiResponse = await getContrattiChiusiByAgente(0, 10, String(user.id));
        setContratti(res.contratti);
        setNextOffset(res.nextOffset);
        setHasMore(res.hasMore);
      } catch (err) {
        console.error("Errore caricamento contratti:", err);
      } finally {
        setLoading(false);
      }
    })();
  }, [user]);

  const handleLoadMore = async () => {
    if (!hasMore || !user) return;
    setLoading(true);
    try {
      const res: ContrattiResponse = await getContrattiChiusiByAgente(nextOffset, 10, String(user.id));
      setContratti((prev) => [...prev, ...res.contratti]);
      setNextOffset(res.nextOffset);
      setHasMore(res.hasMore);
    } catch (err) {
      console.error("Errore caricamento contratti:", err);
    } finally {
      setLoading(false);
    }
  };

  const filteredContratti = contratti.filter((c) =>
    (c.nomeProprietario || "").toLowerCase().includes(searchQuery.toLowerCase())
  );

  if (loading && contratti.length === 0) return <Loader />;

  return (
    <div className="dashboard-container">
      <div className="table-container">
        <h2>I miei contratti conclusi</h2>

        <div className="filter-buttons">
          <SearchBar placeholder="Cerca un proprietario" onSearch={setSearchQuery} />
        </div>

        <div className="table-wrapper">
          <table className="alerts-table">
            <thead>
              <tr>
                <th>Nome proprietario</th>
                <th>Data inizio</th>
                <th>Data fine</th>
                <th>Tipo immobile</th>
                <th>Azioni</th>
              </tr>
            </thead>
            <tbody>
              {filteredContratti.map((c) => (
                <tr key={c.numeroContratto}>
                  <td>{c.nomeProprietario || "-"}</td>
                  <td>{c.dataInizio || "-"}</td>
                  <td>{c.dataFine || "-"}</td>
                  <td>{c.tipo || "-"}</td>
                  <td>
                    <Button className="lightblu" onClick={() => setSelectedContract(c)}>
                      Dettagli
                    </Button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>

        <div className="contract-cards">
          {filteredContratti.map((c) => (
            <div className="contract-card" key={c.numeroContratto}>
              <div className="card-row">
                <b>Proprietario:</b> {c.nomeProprietario || "-"}
              </div>
              <div className="card-row">
                <b>Tipo:</b> {c.tipo || "-"}
              </div>
              <div className="card-row">
                <b>Data inizio:</b> {c.dataInizio || "-"}
              </div>
              <div className="card-row">
                <b>Data fine:</b> {c.dataFine || "-"}
              </div>
              <div className="card-actions">
                <Button className="lightblu" onClick={() => setSelectedContract(c)}>
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

      {selectedContract && (
        <div className="modal-overlay" onClick={() => setSelectedContract(null)}>
          <div className="modal" onClick={(e) => e.stopPropagation()}>
            <h3>Dettagli Contratto</h3>
            <p><b>Numero contratto:</b> {selectedContract.numeroContratto}</p>
            <p><b>Data inizio:</b> {selectedContract.dataInizio}</p>
            <p><b>Data fine:</b> {selectedContract.dataFine}</p>

            <h4>Immobile</h4>
            <p><b>Tipo:</b> {selectedContract.tipo || "-"}</p>
            <p><b>Indirizzo:</b> {selectedContract.via || "-"}, {selectedContract.citta || "-"}</p>

            <h4>Proprietario</h4>
            <p><b>Nome:</b> {selectedContract.nomeProprietario || "-"}</p>
            <p><b>Data inserimento:</b> {selectedContract.dataInserimento || "-"}</p>

            <Button className="red" onClick={() => setSelectedContract(null)}>
              Chiudi
            </Button>
          </div>
        </div>
      )}
    </div>
  );
}
