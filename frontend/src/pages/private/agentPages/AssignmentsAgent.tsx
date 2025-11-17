import { useEffect, useState } from "react";
import SearchBar from "../../../components/SearchBar";
import Button from "../../../components/Button";
import {
  getIncarichiByAgente, // <-- NUOVA API: Filtra per l'agente loggato
  deleteIncarichi,      // Mantenuta per simulare una possibile cancellazione (se permessa)
  type Incarichi,
  type incarichiResponse,
} from "../../../services/api";

// --- SIMULAZIONE ID AGENTE LOGGATO ---
// Deve essere recuperato dal contesto o dal token di autenticazione.
const AGENTE_CORRENTE_ID = "ID_O_NOME_AGENTE_LOGGATO"; 
// ------------------------------------

export default function AssignmentsAgent() {
  const [searchQuery, setSearchQuery] = useState("");
  const [incarichi, setIncarichi] = useState<Incarichi[]>([]);
  const [selectedIncarico, setSelectedIncarico] =
    useState<Incarichi | null>(null);
  const [nextOffset, setNextOffset] = useState(0);
  const [hasMore, setHasMore] = useState(true);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    (async () => {
      if (!AGENTE_CORRENTE_ID) return; // Protezione

      setLoading(true);
      try {
        // !!! CHIAMATA API FILTRATA: mostriamo solo gli incarichi dell'agente corrente
        const res: incarichiResponse = await getIncarichiByAgente(0, 10, AGENTE_CORRENTE_ID); 
        
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

  async function handleLoadMore() {
    if (!hasMore || !AGENTE_CORRENTE_ID) return;

    setLoading(true);
    try {
      // !!! CHIAMATA API FILTRATA: mostriamo solo gli incarichi dell'agente corrente
      const res: incarichiResponse = await getIncarichiByAgente(
        nextOffset,
        10,
        AGENTE_CORRENTE_ID
      );
      setIncarichi((prev) => [...prev, ...res.valutazioni]);
      setNextOffset(res.nextOffset);
      setHasMore(res.hasMore);
    } catch (err) {
      console.error("Errore caricamento incarichi:", err);
    } finally {
      setLoading(false);
    }
  }

  async function handleDelete(id: number) {
    if (!confirm("Sei sicuro di voler eliminare questo incarico?")) return;
    try {
      await deleteIncarichi(id);
      setIncarichi((prev) => prev.filter((v) => v.id !== id));
      alert("Incarico eliminato con successo.");
    } catch (error) {
      console.error("Errore durante l'eliminazione:", error);
      alert("Si è verificato un errore durante l'eliminazione dell'incarico.");
    }
  }

  // Filtriamo per Proprietario o Stato Valutazione
  const filteredIncarichi = incarichi.filter((v) =>
    (v.nomeProprietario || "")
      .toLowerCase()
      .includes(searchQuery.toLowerCase()) ||
    (v.statoValutazione || "").toLowerCase().includes(searchQuery.toLowerCase())
  );

  return (
    <div className="dashboard-container">
      <div className="table-container">
        <h2>I miei incarichi in corso</h2>

        <div className="filter-buttons">
          <SearchBar
            placeholder="Cerca proprietario o stato"
            onSearch={(query) => setSearchQuery(query)}
          />
        </div>

        <div className="table-wrapper">
          <table className="alerts-table">
            <thead>
              <tr>
                <th>Nome proprietario</th>
                <th>Prezzo AI</th>
                <th>Prezzo Umano</th>
                <th>Stato</th>
                {/* Rimuoviamo Agente Assegnato, perché in questa vista è sempre l'agente corrente */}
                <th>Azioni</th>
              </tr>
            </thead>
            <tbody>
              {filteredIncarichi.map((row) => (
                <tr key={row.id}>
                  <td>{row.nomeProprietario || "-"}</td>
                  <td>{row.prezzoAI ? `${row.prezzoAI} €` : "-"}</td>
                  <td>{row.prezzoUmano ? `${row.prezzoUmano} €` : "-"}</td>
                  <td>{row.statoValutazione || "-"}</td>
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
                        className="red"
                        title="Elimina incarico"
                        onClick={() => handleDelete(row.id)}
                      >
                        Elimina
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>

        {/* Vista mobile (incarichi-cards) */}
        <div className="incarichi-cards">
          {filteredIncarichi.map((row) => (
            <div className="incarico-card" key={row.id}>
              <div className="card-row">
                <b>Proprietario:</b>
                <span>{row.nomeProprietario || "-"}</span>
              </div>
              <div className="card-row">
                <b>Prezzo AI:</b>
                <span>{row.prezzoAI ? `${row.prezzoAI} €` : "-"}</span>
              </div>
              <div className="card-row">
                <b>Prezzo Umano:</b>
                <span>{row.prezzoUmano ? `${row.prezzoUmano} €` : "-"}</span>
              </div>
              <div className="card-row">
                <b>Stato:</b>
                <span>{row.statoValutazione || "-"}</span>
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
                  Elimina
                </Button>
              </div>
            </div>
          ))}
        </div>

        {hasMore && (
          <div className="btn-table">
            <Button onClick={handleLoadMore} disabled={loading} className="blu">
              {loading ? "Caricamento..." : "Mostra altri incarichi"}
            </Button>
          </div>
        )}
      </div>

      {/* Modale Dettagli Incarico */}
      {selectedIncarico && (
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
              <b>Prezzo AI:</b> {selectedIncarico.prezzoAI} €
            </p>
            <p>
              <b>Prezzo Umano:</b> {selectedIncarico.prezzoUmano} €
            </p>
            <p>
              <b>Stato:</b> {selectedIncarico.statoValutazione}
            </p>
            {/* ... Aggiungere altri dettagli rilevanti come l'indirizzo e il proprietario ... */}
            
            <h4>Immobile</h4>
            <p>
              <b>Tipo:</b> {selectedIncarico.tipo}
            </p>
            <p>
              <b>Indirizzo:</b> {selectedIncarico.via}, {selectedIncarico.citta}
            </p>
            
            <h4>Proprietario</h4>
            <p>
              <b>Nome:</b> {selectedIncarico.nomeProprietario}
            </p>
            <p>
              <b>Email:</b> {selectedIncarico.emailProprietario}
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