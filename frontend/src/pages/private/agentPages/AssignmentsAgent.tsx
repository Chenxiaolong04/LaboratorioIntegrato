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

export default function AssignmentsAgent() {
  const { user } = useAuth();
  const [searchQuery, setSearchQuery] = useState("");
  const [incarichi, setIncarichi] = useState<Incarichi[]>([]);
  const [selectedIncarico, setSelectedIncarico] = useState<Incarichi | null>(null);
  const [nextOffset, setNextOffset] = useState(0);
  const [hasMore, setHasMore] = useState(true);
  const [loading, setLoading] = useState(false);

useEffect(() => {
  if (!user) return;

  (async () => {
    setLoading(true);
    try {
      const res = await getIncarichi(0, 10); // solo offset e limit
      // filtro gli incarichi per l'agente loggato
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


  async function handleDelete(id: number) {
    if (!confirm("Vuoi davvero eliminare questo incarico?")) return;
    try {
      await deleteIncarichi(id);
      setIncarichi((prev) => prev.filter((v) => v.id !== id));
    } catch (err) {
      console.error("Errore eliminazione incarico:", err);
    }
  }

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
        <div
          className="modal-overlay"
          onClick={() => setSelectedIncarico(null)}
        >
          <div className="modal" onClick={(e) => e.stopPropagation()}>
            <h3>Dettagli Incarico</h3>
            <p><b>ID:</b> {selectedIncarico.id}</p>
            <p><b>Descrizione:</b> {selectedIncarico.descrizione}</p>
            <p><b>Prezzo AI:</b> {selectedIncarico.prezzoAI} €</p>
            <p><b>Data inserimento:</b> {selectedIncarico.dataInserimento}</p>
            <p><b>Agente assegnato:</b> {selectedIncarico.nomeAgente || "-"}</p>
            <Button className="red" onClick={() => setSelectedIncarico(null)}>
              Chiudi
            </Button>
          </div>
        </div>
      )}
    </div>
  );
}
