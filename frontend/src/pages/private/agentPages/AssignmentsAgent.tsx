import { useEffect, useState } from "react";
import { FaX } from "react-icons/fa6";
import Button from "../../../components/Button";
import SearchBar from "../../../components/SearchBar";
import Loader from "../../../components/Loader";
import { useAuth } from "../../../context/AuthContext";
import {
  getIncarichi,
  deleteIncarichi,
  type Incarichi,
} from "../../../services/api";

/**
 * AssignmentsAgent component.
 * Displays and manages the list of assignments (incarichi) specific to the logged-in agent.
 * Allows viewing details, searching, and deleting assignments.
 * @returns {JSX.Element} The Assignments Agent dashboard view.
 */
export default function AssignmentsAgent() {
  /**
   * Retrieves the current authenticated user from the authentication context.
   * @type {object}
   */
  const { user } = useAuth();

  /**
   * State for storing the current search query used to filter assignments.
   * @type {[string, function(string): void]}
   */
  const [searchQuery, setSearchQuery] = useState("");

  /**
   * State for storing the list of assignments (Incarichi) for the current agent.
   * @type {[Incarichi[], function(Incarichi[]): void]}
   */
  const [incarichi, setIncarichi] = useState<Incarichi[]>([]);

  /**
   * State storing the assignment object whose details are currently displayed in the modal.
   * @type {[Incarichi | null, function(Incarichi | null): void]}
   */
  const [selectedIncarico, setSelectedIncarico] = useState<Incarichi | null>(
    null
  );

  /**
   * State for the offset used in pagination for the next batch of assignments.
   * @type {[number, function(number): void]}
   */
  const [nextOffset, setNextOffset] = useState(0);

  /**
   * State indicating if there are more assignments to load via pagination.
   * @type {[boolean, function(boolean): void]}
   */
  const [hasMore, setHasMore] = useState(true);

  /**
   * State indicating if data is currently being loaded.
   * @type {[boolean, function(boolean): void]}
   */
  const [loading, setLoading] = useState(false);

  /**
   * useEffect hook to fetch the initial list of assignments upon component mount
   * or when the user object changes. It filters results to only show the logged-in agent's assignments.
   */
  useEffect(() => {
    if (!user) return;

    (async () => {
      setLoading(true);
      try {
        const res = await getIncarichi(0, 10); // only offset and limit are used in call
        // filter assignments for the logged-in agent
        const mieiIncarichi = res.valutazioni.filter(
          (v) => v.nomeAgente === user.name
        );
        setIncarichi(mieiIncarichi);
        setNextOffset(res.nextOffset);
        setHasMore(res.hasMore);
      } catch (err) {
        console.error("Errore caricamento incarichi:", err);
      } finally {
        setLoading(false);
      }
    })();
  }, [user]);

  /**
   * Handles loading the next page of assignments (pagination).
   * Appends the new data to the existing list, filtering for the current agent.
   * @async
   * @returns {Promise<void>}
   */
  async function handleLoadMore() {
    if (!user) return;
    setLoading(true);
    try {
      const res = await getIncarichi(nextOffset, 10);
      const mieiIncarichi = res.valutazioni.filter(
        (v) => v.nomeAgente === user.name
      );
      setIncarichi((prev) => [...prev, ...mieiIncarichi]);
      setNextOffset(res.nextOffset);
      setHasMore(res.hasMore);
    } catch (err) {
      console.error("Errore caricamento incarichi:", err);
    } finally {
      setLoading(false);
    }
  }

  /**
   * Handles the deletion of a specific assignment by its ID.
   * Requires user confirmation before proceeding.
   * @async
   * @param {number} id - The ID of the assignment to delete.
   * @returns {Promise<void>}
   */
  async function handleDelete(id: number) {
    if (!confirm("Vuoi davvero eliminare questo incarico?")) return;
    try {
      await deleteIncarichi(id);
      // Remove the deleted assignment from the local state
      setIncarichi((prev) => prev.filter((v) => v.id !== id));
    } catch (err) {
      console.error("Errore eliminazione incarico:", err);
    }
  }

  /**
   * Filters the list of assignments based on the current search query, matching against the owner's name.
   * @type {Incarichi[]}
   */
  const filteredIncarichi = incarichi.filter((c) =>
    (c.nomeProprietario || "").toLowerCase().includes(searchQuery.toLowerCase())
  );

  return (
    <div className="dashboard-container">
      {loading && incarichi.length === 0 ? (
        <Loader />
      ) : (
        <div className="table-container">
          <h2>I miei incarichi</h2>

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
                  <th>Prezzo AI</th>
                  <th>Data</th>
                  <th>Azioni</th>
                </tr>
              </thead>
              <tbody>
                {filteredIncarichi.map((row) => (
                  <tr key={row.id}>
                    <td>{row.nomeProprietario || "-"}</td>
                    <td>{row.prezzoAI || "-"}</td>
                    <td>{row.dataInserimento || "-"}</td>
                    <td>
                      <div className="action-buttons">
                        <Button
                          className="lightblu"
                          onClick={() => setSelectedIncarico(row)}
                        >
                          Dettagli
                        </Button>
                        <Button
                          className="red"
                          onClick={() => handleDelete(row.id)}
                        >
                          <FaX />
                        </Button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>

          <div className="assignments-cards">
            {filteredIncarichi.map((row) => (
              <div className="assignment-card" key={row.id}>
                <div className="card-row">
                  <b>Proprietario:</b>
                  <span>{row.nomeProprietario || "-"}</span>
                </div>
                <div className="card-row">
                  <b>Data inserimento:</b>
                  <span>{row.dataInserimento || "-"}</span>
                </div>
                <div className="card-row">
                  <b>Prezzo AI:</b>
                  <span>{row.prezzoAI || "-"}</span>
                </div>
                <div className="card-actions">
                  <Button
                    className="lightblu"
                    onClick={() => setSelectedIncarico(row)}
                  >
                    Dettagli
                  </Button>
                  <Button
                    className="red"
                    onClick={() => handleDelete(row.id)}
                  >
                    <FaX />
                  </Button>
                </div>
              </div>
            ))}
          </div>

          {hasMore && (
            <div className="btn-table">
              <Button onClick={handleLoadMore} disabled={loading}>
                {loading ? "Caricamento..." : "Mostra altri incarichi"}
              </Button>
            </div>
          )}
        </div>
      )}

      {selectedIncarico && (
        /**
         * Modal overlay for displaying detailed information about a selected assignment.
         */
        <div
          className="modal-overlay"
          onClick={() => setSelectedIncarico(null)}
        >
          <div className="modal" onClick={(e) => e.stopPropagation()}>
            <h3>Dettagli Incarico</h3>
            <p>
              <b>ID:</b> {selectedIncarico.id}
            </p>
            <p>
              <b>Descrizione:</b> {selectedIncarico.descrizione}
            </p>
            <p>
              <b>Prezzo AI:</b> {selectedIncarico.prezzoAI} €
            </p>
            <p>
              <b>Data inserimento:</b> {selectedIncarico.dataInserimento}
            </p>
            <p>
              <b>Agente assegnato:</b> {selectedIncarico.nomeAgente || "-"}
            </p>
            <Button className="red" onClick={() => setSelectedIncarico(null)}>
              Chiudi
            </Button>
          </div>
        </div>
      )}
    </div>
  );
}