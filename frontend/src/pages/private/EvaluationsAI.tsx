import { useEffect, useState } from "react";
import SearchBar from "../../components/SearchBar";
import Button from "../../components/Button";
import { FaX } from "react-icons/fa6";
import {
  getValutazioniSoloAI,
  deleteValutazioneAI,
  assignIncaricoToMe,
  type ValutazioneAI,
} from "../../services/api";
import Loader from "../../components/Loader";
import { useAuth } from "../../context/AuthContext";

/**
 * EvaluationsAI component.
 * Displays a list of property evaluations generated solely by the AI, available for agents to view
 * and potentially take on as assignments (incarichi).
 * @returns {JSX.Element} The AI Evaluations view.
 */
export default function EvaluationsAI() {
  /**
   * Retrieves the current authenticated user object from the authentication context.
   * Used to check user role and ID for assigning tasks.
   * @type {object}
   */
  const { user } = useAuth();

  /**
   * State for storing the list of AI evaluations, extended with a flag indicating if an assignment has been taken.
   * @type {[(ValutazioneAI & { incaricoAssegnato?: boolean })[], function(Array<ValutazioneAI & { incaricoAssegnato?: boolean }>): void]}
   */
  const [valutazioni, setValutazioni] = useState<
    (ValutazioneAI & { incaricoAssegnato?: boolean })[]
  >([]);

  /**
   * State for the currently selected evaluation to display details in a modal.
   * @type {[ValutazioneAI | null, function(ValutazioneAI | null): void]}
   */
  const [selected, setSelected] = useState<ValutazioneAI | null>(null);

  /**
   * State for the current search query, used to filter evaluations by owner name.
   * @type {[string, function(string): void]}
   */
  const [searchQuery, setSearchQuery] = useState("");

  /**
   * State for the offset used in pagination for the next batch of evaluations.
   * @type {[number, function(number): void]}
   */
  const [offset, setOffset] = useState(0);

  /**
   * State indicating if there are more evaluations to load via pagination.
   * @type {[boolean, function(boolean): void]}
   */
  const [hasMore, setHasMore] = useState(true);

  /**
   * State indicating if data is currently being loaded.
   * @type {[boolean, function(boolean): void]}
   */
  const [loading, setLoading] = useState(false);

  /**
   * useEffect hook for initial data loading. Fetches the first page of AI evaluations.
   */
  useEffect(() => {
    (async () => {
      setLoading(true);
      try {
        const res = await getValutazioniSoloAI(0, 10);
        // Initialize incaricoAssegnato flag to false for all fetched evaluations
        setValutazioni(
          res.valutazioni.map((v) => ({ ...v, incaricoAssegnato: false }))
        );
        setOffset(res.nextOffset);
        setHasMore(res.hasMore);
      } catch (err) {
        console.error("Errore caricamento valutazioni AI:", err);
      } finally {
        setLoading(false);
      }
    })();
  }, []);

  /**
   * Loads the next page of AI evaluations via pagination.
   * @async
   * @returns {Promise<void>}
   */
  async function loadMore() {
    setLoading(true);
    try {
      const res = await getValutazioniSoloAI(offset, 10);
      setValutazioni((prev) => [
        ...prev,
        // Append new evaluations, initializing the assignment flag
        ...res.valutazioni.map((v) => ({ ...v, incaricoAssegnato: false })),
      ]);
      setOffset(res.nextOffset);
      setHasMore(res.hasMore);
    } catch (err) {
      console.error("Errore caricamento valutazioni AI:", err);
    } finally {
      setLoading(false);
    }
  }

  /**
   * Handles the deletion of a specific AI evaluation.
   * Requires user confirmation before proceeding.
   * @async
   * @param {number} id - The ID of the evaluation to delete.
   * @returns {Promise<void>}
   */
  async function handleDelete(id: number) {
    if (!confirm("Vuoi davvero eliminare questa valutazione?")) return;
    try {
      await deleteValutazioneAI(id);
      // Remove the deleted evaluation from the local state
      setValutazioni((prev) => prev.filter((v) => v.id !== id));
    } catch (err) {
      console.error("Errore eliminazione valutazione:", err);
    }
  }

  /**
   * Allows a logged-in agent to take responsibility for an AI evaluation, converting it into an assignment.
   * Updates the local state upon successful assignment.
   * @async
   * @param {ValutazioneAI & { incaricoAssegnato?: boolean }} row - The evaluation object to be assigned.
   * @returns {Promise<void>}
   */
  async function handleTakeAssignment(
    row: ValutazioneAI & { incaricoAssegnato?: boolean }
  ) {
    if (!user) return;
    try {
      const res = await assignIncaricoToMe(row.id, String(user.id), user.name);
      if (res.success) {
        // Update the state to mark the assignment as taken for this evaluation
        setValutazioni((prev) =>
          prev.map((v) =>
            v.id === row.id ? { ...v, incaricoAssegnato: true } : v
          )
        );
        alert("Incarico preso con successo!");
      } else {
        alert("Errore: " + res.message);
      }
    } catch (err) {
      console.error(err);
      alert("Errore nel prendere l'incarico.");
    }
  }

  /**
   * Filters the evaluations list based on the current search query, matching against the owner's name.
   * @type {Array<ValutazioneAI & { incaricoAssegnato?: boolean }>}
   */
  const filtered = valutazioni.filter((v) =>
    (v.nomeProprietario ?? "").toLowerCase().includes(searchQuery.toLowerCase())
  );

  return (
    <div className="dashboard-container">
      {loading && valutazioni.length === 0 ? (
        <Loader />
      ) : (
        <div className="table-container">
          <h2>Valutazioni AI effettuate</h2>
          <div className="filter-buttons">
            <SearchBar
              placeholder="Cerca un proprietario"
              onSearch={setSearchQuery}
            />
          </div>

          {/* Table */}
          <div className="table-wrapper">
            <table className="alerts-table">
              <thead>
                <tr>
                  <th>Nome proprietario</th>
                  <th>Data</th>
                  <th>Prezzo stimato da AI</th>
                  <th>Indirizzo</th>
                  <th>Tipologia</th>
                  <th>Azioni</th>
                </tr>
              </thead>
              <tbody>
                {filtered.map((row) => (
                  <tr key={row.id}>
                    <td>{row.nomeProprietario || "—"}</td>
                    <td>{row.dataValutazione?.split("T")[0]}</td>
                    <td>{row.prezzoAI ? row.prezzoAI + " €" : "—"}</td>
                    <td>{row.via ? `${row.via}, ${row.citta}` : "—"}</td>
                    <td>{row.tipo || "—"}</td>
                    <td>
                      <div className="action-buttons">
                        <Button className="lightblu" onClick={() => setSelected(row)}>
                          Dettagli
                        </Button>
                        {user?.role === "agente" && (
                          <Button
                            className="blu"
                            disabled={!!row.incaricoAssegnato}
                            onClick={() => handleTakeAssignment(row)}
                          >
                            {row.incaricoAssegnato
                              ? "Incarico preso"
                              : "Prendi incarico"}
                          </Button>
                        )}
                        <Button className="red" onClick={() => handleDelete(row.id)}>
                          <FaX />
                        </Button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>

          {/* Cards responsive */}
          <div className="evaluations-cards">
            {filtered.map((row) => (
              <div className="evaluation-card" key={row.id}>
                <div className="card-row">
                  <b>Nome:</b> {row.nomeProprietario || "—"}
                </div>
                <div className="card-row">
                  <b>Data:</b> {row.dataValutazione?.split("T")[0]}
                </div>
                <div className="card-row">
                  <b>Prezzo AI:</b> {row.prezzoAI ? row.prezzoAI + " €" : "—"}
                </div>
                <div className="card-row">
                  <b>Indirizzo:</b> {row.via ? `${row.via}, ${row.citta}` : "—"}
                </div>
                <div className="card-row">
                  <b>Tipologia:</b> {row.tipo || "—"}
                </div>
                <div className="card-actions">
                  <Button className="lightblu" onClick={() => setSelected(row)}>
                    Dettagli
                  </Button>
                  {user?.role === "agente" && (
                    <Button
                      className="blu"
                      disabled={!!row.incaricoAssegnato}
                      onClick={() => handleTakeAssignment(row)}
                    >
                      {row.incaricoAssegnato
                        ? "Incarico preso"
                        : "Prendi incarico"}
                    </Button>
                  )}
                  <Button className="red" onClick={() => handleDelete(row.id)}>
                    <FaX />
                  </Button>
                </div>
              </div>
            ))}
          </div>

          {hasMore && (
            <div className="btn-table">
              <Button onClick={loadMore}>Mostra altre valutazioni</Button>
            </div>
          )}
        </div>
      )}

      {/* Modale dettagli */}
      {selected && (
        /**
         * Modal overlay for displaying detailed information about a selected AI evaluation.
         */
        <div className="modal-overlay" onClick={() => setSelected(null)}>
          <div className="modal" onClick={(e) => e.stopPropagation()}>
            <h3>Dettagli Valutazione</h3>
            <p>
              <b>ID:</b> {selected.id}
            </p>
            <p>
              <b>Descrizione:</b> {selected.descrizione}
            </p>
            <p>
              <b>Prezzo AI:</b> {selected.prezzoAI} €
            </p>
            <h4>Dati immobile</h4>
            <p>
              <b>Tipo:</b> {selected.tipo}
            </p>
            <p>
              <b>Indirizzo:</b> {selected.via}, {selected.citta}
            </p>
            <p>
              <b>Metratura:</b> {selected.metratura} m²
            </p>
            <p>
              <b>Stanze:</b> {selected.stanze}
            </p>
            <p>
              <b>Bagni:</b> {selected.bagni}
            </p>
            <p>
              <b>Piano:</b> {selected.piano}
            </p>
            <h4>Proprietario</h4>
            <p>
              <b>Nome:</b> {selected.nomeProprietario}
            </p>
            <p>
              <b>Email:</b> {selected.emailProprietario}
            </p>
            <p>
              <b>Telefono:</b> {selected.telefonoProprietario}
            </p>
            <Button className="red" onClick={() => setSelected(null)}>
              Chiudi
            </Button>
          </div>
        </div>
      )}
    </div>
  );
}