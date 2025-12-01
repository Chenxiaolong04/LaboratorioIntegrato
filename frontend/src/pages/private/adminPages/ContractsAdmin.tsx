import { useEffect, useState } from "react";
import SearchBar from "../../../components/SearchBar";
import Button from "../../../components/Button";
import {
  getContrattiChiusi,
  type ContrattoChiuso,
} from "../../../services/api";
import Loader from "../../../components/Loader";

/**
 * @typedef {object} ContrattoChiuso - Represents a closed contract object fetched from the API.
 * @property {string} numeroContratto - The unique identifier/number of the contract.
 * @property {string} nomeProprietario - The name of the property owner.
 * @property {string} dataInizio - The contract start date.
 * @property {string} dataFine - The contract end date.
 * @property {string} agenteAssegnato - The name of the assigned agent.
 * @property {string} tipo - The type of property (e.g., Appartamento, Villa).
 * @property {string} dataInserimento - The date the contract was entered into the system.
 */

/**
 * ContractsAdmin component.
 * Manages the view for displaying, filtering, and loading closed contracts.
 * @returns {JSX.Element} The ContractsAdmin dashboard component.
 */
export default function ContractsAdmin() {
  /**
   * State for storing the current search query for filtering contracts.
   * @type {[string, function(string): void]}
   */
  const [searchQuery, setSearchQuery] = useState("");

  /**
   * State for storing the list of fetched closed contracts.
   * @type {[ContrattoChiuso[], function(ContrattoChiuso[]): void]}
   */
  const [contratti, setContratti] = useState<ContrattoChiuso[]>([]);

  /**
   * State for storing the currently selected contract for modal display.
   * @type {[ContrattoChiuso | null, function(ContrattoChiuso | null): void]}
   */
  const [selectedContract, setSelectedContract] =
    useState<ContrattoChiuso | null>(null);

  /**
   * State for the offset used in pagination for the next batch of contracts.
   * @type {[number, function(number): void]}
   */
  const [nextOffset, setNextOffset] = useState(0);

  /**
   * State indicating if there are more contracts to load.
   * @type {[boolean, function(boolean): void]}
   */
  const [hasMore, setHasMore] = useState(true);

  /**
   * State indicating if data is currently being loaded.
   * @type {[boolean, function(boolean): void]}
   */
  const [loading, setLoading] = useState(false);

  /**
   * useEffect hook to fetch the initial set of closed contracts when the component mounts.
   */
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

  /**
   * Handles loading the next batch of closed contracts (pagination).
   * Appends the new contracts to the existing list.
   * @async
   * @returns {Promise<void>}
   */
  async function handleLoadMore() {
    const res = await getContrattiChiusi(nextOffset, 10);
    setContratti((prev) => [...prev, ...res.contratti]);
    setNextOffset(res.nextOffset);
    setHasMore(res.hasMore);
  }

  /**
   * Filters the list of contracts based on the current search query
   * (matching against the owner's name).
   * @type {ContrattoChiuso[]}
   */
  const filteredContratti = contratti.filter((c) =>
    (c.nomeProprietario || "").toLowerCase().includes(searchQuery.toLowerCase())
  );

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
                    <td>{row.dataInizio || "-"}</td>
                    <td>{row.dataFine || "-"}</td>
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
                  <span>{row.dataInizio || "-"}</span>
                </div>

                <div className="card-row">
                  <b>Data fine:</b>
                  <span>{row.dataFine || "-"}</span>
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
        /**
         * Modal overlay for displaying contract details.
         * Closes when clicking the overlay.
         */
        <div
          className="modal-overlay"
          onClick={() => setSelectedContract(null)}
        >
          {/** Modal content, prevents closing when clicking inside. */}
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