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


export default function EvaluationsAI() {
  const { user } = useAuth();
  const [valutazioni, setValutazioni] = useState<(ValutazioneAI & { incaricoAssegnato?: boolean })[]>([]);
  const [selected, setSelected] = useState<ValutazioneAI | null>(null);
  const [searchQuery, setSearchQuery] = useState("");
  const [offset, setOffset] = useState(0);
  const [hasMore, setHasMore] = useState(true);
  const [loading, setLoading] = useState(false);


  // Caricamento iniziale
  useEffect(() => {
    (async () => {
      setLoading(true);
      try {
        const res = await getValutazioniSoloAI(0, 10);
        setValutazioni(res.valutazioni.map(v => ({ ...v, incaricoAssegnato: false })));
        setOffset(res.nextOffset);
        setHasMore(res.hasMore);
      } catch (err) {
        console.error("Errore caricamento valutazioni AI:", err);
      } finally {
        setLoading(false);
      }
    })();
  }, []);


  // Carica più valutazioni
  async function loadMore() {
    setLoading(true);
    try {
      const res = await getValutazioniSoloAI(offset, 10);
      setValutazioni(prev => [
        ...prev,
        ...res.valutazioni.map(v => ({ ...v, incaricoAssegnato: false }))
      ]);
      setOffset(res.nextOffset);
      setHasMore(res.hasMore);
    } catch (err) {
      console.error("Errore caricamento valutazioni AI:", err);
    } finally {
      setLoading(false);
    }
  }


  // Elimina valutazione
  async function handleDelete(id: number) {
    if (!confirm("Vuoi davvero eliminare questa valutazione?")) return;
    try {
      await deleteValutazioneAI(id);
      setValutazioni(prev => prev.filter(v => v.id !== id));
    } catch (err) {
      console.error("Errore eliminazione valutazione:", err);
    }
  }


  // Prendi incarico
  async function handleTakeAssignment(row: ValutazioneAI & { incaricoAssegnato?: boolean }) {
    if (!user) return;
    try {
      const res = await assignIncaricoToMe(row.id, String(user.id), user.name);
      if (res.success) {
        setValutazioni(prev =>
          prev.map(v => v.id === row.id ? { ...v, incaricoAssegnato: true } : v)
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


  // Filtro ricerca
  const filtered = valutazioni.filter(v =>
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
            <SearchBar placeholder="Cerca un proprietario" onSearch={setSearchQuery} />
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
                {filtered.map(row => (
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
                          <Button className="blu" disabled={!!row.incaricoAssegnato} onClick={() => handleTakeAssignment(row)}>
                            {row.incaricoAssegnato ? "Incarico preso" : "Prendi incarico"}
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
            {filtered.map(row => (
              <div className="evaluation-card" key={row.id}>
                <div className="card-row"><b>Nome:</b> {row.nomeProprietario || "—"}</div>
                <div className="card-row"><b>Data:</b> {row.dataValutazione?.split("T")[0]}</div>
                <div className="card-row"><b>Prezzo AI:</b> {row.prezzoAI ? row.prezzoAI + " €" : "—"}</div>
                <div className="card-row"><b>Indirizzo:</b> {row.via ? `${row.via}, ${row.citta}` : "—"}</div>
                <div className="card-row"><b>Tipologia:</b> {row.tipo || "—"}</div>
                <div className="card-actions">
                  <Button className="lightblu" onClick={() => setSelected(row)}>Dettagli</Button>
                  {user?.role === "agente" && (
                    <Button className="blu" disabled={!!row.incaricoAssegnato} onClick={() => handleTakeAssignment(row)}>
                      {row.incaricoAssegnato ? "Incarico preso" : "Prendi incarico"}
                    </Button>
                  )}
                  <Button className="red" onClick={() => handleDelete(row.id)}><FaX /></Button>
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
        <div className="modal-overlay" onClick={() => setSelected(null)}>
          <div className="modal" onClick={e => e.stopPropagation()}>
            <h3>Dettagli Valutazione</h3>
            <p><b>ID:</b> {selected.id}</p>
            <p><b>Descrizione:</b> {selected.descrizione}</p>
            <p><b>Prezzo AI:</b> {selected.prezzoAI} €</p>
            <h4>Dati immobile</h4>
            <p><b>Tipo:</b> {selected.tipo}</p>
            <p><b>Indirizzo:</b> {selected.via}, {selected.citta}</p>
            <p><b>Metratura:</b> {selected.metratura} m²</p>
            <p><b>Stanze:</b> {selected.stanze}</p>
            <p><b>Bagni:</b> {selected.bagni}</p>
            <p><b>Piano:</b> {selected.piano}</p>
            <h4>Proprietario</h4>
            <p><b>Nome:</b> {selected.nomeProprietario}</p>
            <p><b>Email:</b> {selected.emailProprietario}</p>
            <p><b>Telefono:</b> {selected.telefonoProprietario}</p>
            <Button className="red" onClick={() => setSelected(null)}>Chiudi</Button>
          </div>
        </div>
      )}
    </div>
  );
}
