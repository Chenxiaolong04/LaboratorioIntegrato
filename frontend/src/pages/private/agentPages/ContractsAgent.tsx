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

/**
 * ContractsAgent component.
 * Displays and manages the list of closed contracts associated with the logged-in agent.
 * Allows viewing details, searching, and loading more contracts via pagination.
 * @returns {JSX.Element} The Closed Contracts Agent dashboard view.
 */
export default function ContractsAgent() {
  /**
   * Retrieves the current authenticated user from the authentication context.
   * @type {object}
   */
  const { user } = useAuth();

  /**
   * State for storing the current search query used to filter contracts by owner name.
   * @type {[string, function(string): void]}
   */
  const [searchQuery, setSearchQuery] = useState("");

  /**
   * State for storing the list of closed contracts (ContrattoChiuso) for the current agent.
   * @type {[ContrattoChiuso[], function(ContrattoChiuso[]): void]}
   */
  const [contratti, setContratti] = useState<ContrattoChiuso[]>([]);

  /**
   * State storing the contract object whose details are currently displayed in the modal.
   * @type {[ContrattoChiuso | null, function(ContrattoChiuso | null): void]}
   */
  const [selectedContract, setSelectedContract] = useState<ContrattoChiuso | null>(null);

  /**
   * State for the offset used in pagination for the next batch of contracts.
   * @type {[number, function(number): void]}
   */
  const [nextOffset, setNextOffset] = useState(0);

  /**
   * State indicating if there are more contracts to load via pagination.
   * @type {[boolean, function(boolean): void]}
   */
  const [hasMore, setHasMore] = useState(true);

  /**
   * State indicating if data is currently being loaded.
   * @type {[boolean, function(boolean): void]}
   */
  const [loading, setLoading] = useState(false);

  /**
   * useEffect hook to fetch the initial list of closed contracts upon component mount
   * or when the user object changes, using the agent's ID for filtering.
   */
  useEffect(() => {
    if (!user) return;

    (async () => {
      setLoading(true);
      try {
        const res: ContrattiResponse = await getContrattiChiusiByAgente(
          0,
          10,
          String(user.id)
        );
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

  /**
   * Handles loading the next page of contracts (pagination).
   * Appends the new data to the existing list.
   * @async
   * @returns {Promise<void>}
   */
  const handleLoadMore = async () => {
    if (!hasMore || !user) return;
    setLoading(true);
    try {
      const res: ContrattiResponse = await getContrattiChiusiByAgente(
        nextOffset,
        10,
        String(user.id)
      );
      setContratti((prev) => [...prev, ...res.contratti]);
      setNextOffset(res.nextOffset);
      setHasMore(res.hasMore);
    } catch (err) {
      console.error("Errore caricamento contratti:", err);
    } finally {
      setLoading(false);
    }
  };

  /**
   * Filters the list of contracts based on the current search query, matching against the owner's name.
   * @type {ContrattoChiuso[]}
   */
  const filteredContratti = contratti.filter((c) =>
    (c.nomeProprietario || "").toLowerCase().includes(searchQuery.toLowerCase())
  );

  /**
   * Renders a loading spinner if data is being fetched for the first time.
   */
  if (loading && contratti.length === 0) return <Loader />;

  return (
    <div className="dashboard-container">
      <div className="table-container">
        <h2>I miei contratti conclusi</h2>

        <div className="filter-buttons">
          <SearchBar
            placeholder="Cerca un proprietario"
            onSearch={setSearchQuery}
          />
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
        /**
         * Modal overlay for displaying detailed information about a selected closed contract.
         */
        <div className="modal-overlay" onClick={() => setSelectedContract(null)}>
          <div className="modal" onClick={(e) => e.stopPropagation()}>
            <h3>Dettagli Contratto</h3>
            <p>
              <b>Numero contratto:</b> {selectedContract.numeroContratto}
            </p>
            <p>
              <b>Data inizio:</b> {selectedContract.dataInizio}
            </p>
            <p>
              <b>Data fine:</b> {selectedContract.dataFine}
            </p>

            <h4>Immobile</h4>
            <p>
              <b>Tipo:</b> {selectedContract.tipo || "-"}
            </p>
            <p>
              <b>Indirizzo:</b> {selectedContract.via || "-"},{" "}
              {selectedContract.citta || "-"}
            </p>

            <h4>Proprietario</h4>
            <p>
              <b>Nome:</b> {selectedContract.nomeProprietario || "-"}
            </p>
            <p>
              <b>Data inserimento:</b> {selectedContract.dataInserimento || "-"}
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