import { useEffect, useState } from "react";
import SearchBar from "../../../components/SearchBar";
import Button from "../../../components/Button";
import { FaX } from "react-icons/fa6";
import {
  deleteIncarichi,
  getIncarichi,
  generateContractFromValutazione,
  type Incarichi,
} from "../../../services/api";
import Loader from "../../../components/Loader";
import {} from "../../../services/api";

export default function AssignmentsAdmin() {
  const [searchQuery, setSearchQuery] = useState("");
  const [incarichi, setIncarichi] = useState<Incarichi[]>([]);
  const [selectedIncarico, setSelectedIncarico] = useState<Incarichi | null>(
    null
  );
  const [nextOffset, setNextOffset] = useState(0);
  const [hasMore, setHasMore] = useState(true);
  const [loading, setLoading] = useState(false);

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

  async function handleLoadMore() {
    const res = await getIncarichi(nextOffset, 10);
    setIncarichi((prev) => [...prev, ...res.valutazioni]);
    setNextOffset(res.nextOffset);
    setHasMore(res.hasMore);
  }

  async function handleDelete(id: number) {
    if (!confirm("Vuoi davvero eliminare questo incarico?")) return;

    await deleteIncarichi(id);

    setIncarichi((prev) => prev.filter((v) => v.id !== id));
  }

  const filteredIncarichi = incarichi.filter((c) =>
    (c.nomeProprietario || "").toLowerCase().includes(searchQuery.toLowerCase())
  );

  async function handleGenerateContract(id: number) {
    if (!confirm("Generare il contratto e inviarlo via email?")) return;

    try {
      const res = await generateContractFromValutazione(id);

      alert(
        `✔ Contratto generato e inviato!\n\n` +
          `Email proprietario: ${res.destinatari.proprietario}\n` +
          `Email agente: ${res.destinatari.agente}`
      );
    } catch (error: unknown) {
      console.error(error);
      alert("Errore durante la generazione del contratto");
    }
  }

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
                  <th>Agente assegnato</th>
                  <th>Azioni</th>
                </tr>
              </thead>
              <tbody>
                {filteredIncarichi.map((row) => (
                  <tr key={row.id}>
                    <td>{row.nomeProprietario || "-"}</td>
                    <td>{row.prezzoAI || "-"}</td>
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
                          onClick={() => handleGenerateContract(row.id)}
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
                    className="blu"
                    title="Genera contratto dall'incarico"
                    onClick={() => handleGenerateContract(row.id)}
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
