import { useEffect, useState } from "react";
import SearchBar from "../../../components/SearchBar";
import Button from "../../../components/Button";
import { FaX } from "react-icons/fa6";
import {
  deleteIncarichi,
  getIncarichi,
  type Incarichi,
} from "../../../services/api";
import Loader from "../../../components/Loader";

/**
 * @typedef {object} Incarichi - Represents an assignment object fetched from the API.
 * @property {number} id - The unique identifier of the assignment.
 * @property {string} nomeProprietario - The name of the property owner.
 * @property {number} prezzoAI - The AI-estimated price.
 * @property {string} dataInserimento - The date the assignment was created.
 * @property {string} nomeAgente - The name of the assigned agent.
 * @property {string} descrizione - The description of the assignment.
 * @property {string} tipo - The type of property.
 * @property {string} via - The street address of the property.
 * @property {string} citta - The city of the property.
 * @property {string} provincia - The province of the property.
 * @property {number} metratura - The property size in square meters.
 * @property {string} condizioni - The condition of the property.
 * @property {number} stanze - The number of rooms.
 * @property {number} bagni - The number of bathrooms.
 * @property {string} piano - The floor of the property.
 * @property {string} emailProprietario - The owner's email address.
 * @property {string} telefonoProprietario - The owner's phone number.
 */

/**
 * AssignmentsAdmin component.
 * Manages the view for displaying, filtering, loading more, and deleting assignments (incarichi).
 * @returns {JSX.Element} The AssignmentsAdmin dashboard component.
 */
export default function AssignmentsAdmin() {
  /**
   * State for storing the current search query for filtering assignments.
   * @type {[string, function(string): void]}
   */
  const [searchQuery, setSearchQuery] = useState("");

  /**
   * State for storing the list of fetched assignments.
   * @type {[Incarichi[], function(Incarichi[]): void]}
   */
  const [incarichi, setIncarichi] = useState<Incarichi[]>([]);

  /**
   * State for storing the currently selected assignment for modal display.
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
   * State indicating if there are more assignments to load.
   * @type {[boolean, function(boolean): void]}
   */
  const [hasMore, setHasMore] = useState(true);

  /**
   * State indicating if data is currently being loaded.
   * @type {[boolean, function(boolean): void]}
   */
  const [loading, setLoading] = useState(false);

  /**
   * useEffect hook to fetch the initial set of assignments when the component mounts.
   */
  useEffect(() => {
    (async () => {
      setLoading(true);
      try {
        const res = await getIncarichi(0, 10);
        setIncarichi(res.valutazioni);
        setNextOffset(res.nextOffset);
        setHasMore(res.hasMore);
      } catch (err) {
        console.error("Errore caricamento incarichi:", err);
      } finally {
        setLoading(false);
      }
    })();
  }, []);

  /**
   * Handles loading the next batch of assignments (pagination).
   * Appends the new assignments to the existing list.
   * @async
   * @returns {Promise<void>}
   */
  async function handleLoadMore() {
    const res = await getIncarichi(nextOffset, 10);
    setIncarichi((prev) => [...prev, ...res.valutazioni]);
    setNextOffset(res.nextOffset);
    setHasMore(res.hasMore);
  }

  /**
   * Handles the deletion of a specific assignment by ID.
   * Prompts the user for confirmation before deletion.
   * Updates the state to remove the deleted assignment from the list.
   * @async
   * @param {number} id - The ID of the assignment to delete.
   * @returns {Promise<void>}
   */
  async function handleDelete(id: number) {
    if (!confirm("Vuoi davvero eliminare questo incarico?")) return;

    await deleteIncarichi(id);

    setIncarichi((prev) => prev.filter((v) => v.id !== id));
  }

  /**
   * Filters the list of assignments based on the current search query
   * (matching against the owner's name).
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
          <h2>Incarichi in corso</h2>

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
                  <th>Prezzo AI</th>
                  <th>Data</th>
                  <th>Agente assegnato</th>
                  <th>Azioni</th>
                </tr>
              </thead>
              <tbody>
                {filteredIncarichi.map((row) => (
                  <tr key={row.id}>
                    <td>{row.nomeProprietario || "-"}</td>
                    <td>{row.prezzoAI || "-"}</td>
                    <td>{row.dataInserimento || "-"}</td>
                    <td>{row.nomeAgente || "-"}</td>
                    <td>
                      <div className="action-buttons">
                        <Button
                          className="lightblu"
                          title="Maggiori dettagli sull'incarico"
                          onClick={() => setSelectedIncarico(row)}
                        >
                          Dettagli
                        </Button>
                        <Button
                          className="blu"
                          title="Genera contratto dall'incarico"
                        >
                          Genera contratto
                        </Button>
                        <Button
                          className="red"
                          title="Elimina incarico"
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
                  <b>Agente assegnato:</b>
                  <span>{row.nomeAgente || "-"}</span>
                </div>

                <div className="card-actions">
                  <Button
                    className="lightblu"
                    title="Maggiori dettagli sull'incarico"
                    onClick={() => setSelectedIncarico(row)}
                  >
                    Dettagli
                  </Button>
                  <Button
                    className="red"
                    title="Elimina incarico"
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
         * Modal overlay for displaying assignment details.
         * Closes when clicking the overlay.
         */
        <div
          className="modal-overlay"
          onClick={() => setSelectedIncarico(null)}
        >
          {/** Modal content, prevents closing when clicking inside. */}
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

            <h4>Dati immobile</h4>
            <p>
              <b>Tipo:</b> {selectedIncarico.tipo}
            </p>
            <p>
              <b>Indirizzo:</b> {selectedIncarico.via}, {selectedIncarico.citta}
            </p>
            <p>
              <b>Provincia:</b> {selectedIncarico.provincia}
            </p>
            <p>
              <b>Metratura:</b> {selectedIncarico.metratura} m²
            </p>
            <p>
              <b>Condizioni:</b> {selectedIncarico.condizioni} m²
            </p>
            <p>
              <b>Stanze:</b> {selectedIncarico.stanze}
            </p>
            <p>
              <b>Bagni:</b> {selectedIncarico.bagni}
            </p>
            <p>
              <b>Piano:</b> {selectedIncarico.piano}
            </p>

            <h4>Proprietario</h4>
            <p>
              <b>Nome:</b> {selectedIncarico.nomeProprietario}
            </p>
            <p>
              <b>Email:</b> {selectedIncarico.emailProprietario}
            </p>
            <p>
              <b>Telefono:</b> {selectedIncarico.telefonoProprietario}
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