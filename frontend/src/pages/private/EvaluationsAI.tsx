import { useEffect, useState } from "react";
import SearchBar from "../../components/SearchBar";
import Button from "../../components/Button";
import { FaX } from "react-icons/fa6";
import { MdPersonAdd } from "react-icons/md";
import {
  getValutazioniSoloAI,
  deleteValutazioneAI,
  type ValutazioneAI,
} from "../../services/api";

export default function EvaluationsAI() {
  const [valutazioni, setValutazioni] = useState<ValutazioneAI[]>([]);
  const [selected, setSelected] = useState<ValutazioneAI | null>(null);

  const [searchQuery, setSearchQuery] = useState("");
  const [offset, setOffset] = useState(0);
  const [hasMore, setHasMore] = useState(true);

  useEffect(() => {
    (async () => {
      const res = await getValutazioniSoloAI(0, 10);
      setValutazioni(res.valutazioni);
      setOffset(res.nextOffset);
      setHasMore(res.hasMore);
    })();
  }, []);

  async function loadMore() {
    const res = await getValutazioniSoloAI(offset, 10);
    setValutazioni((prev) => [...prev, ...res.valutazioni]);
    setOffset(res.nextOffset);
    setHasMore(res.hasMore);
  }

  async function handleDelete(id: number) {
    if (!confirm("Vuoi davvero eliminare questa valutazione?")) return;

    await deleteValutazioneAI(id);

    setValutazioni((prev) => prev.filter((v) => v.id !== id));
  }

  const filtered = valutazioni.filter((v) =>
    (v.nomeProprietario ?? "").toLowerCase().includes(searchQuery.toLowerCase())
  );

  return (
    <div className="dashboard-container">
      <div className="table-container">
        <h2>Valutazioni AI effettuate</h2>

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
                      <Button
                        className="lightblu"
                        onClick={() => setSelected(row)}
                      >
                        Dettagli
                      </Button>

                      <Button
                        className="blu"
                        title="Assegna agente immobiliare"
                      >
                        <MdPersonAdd size={28} color={"white"} />
                      </Button>

                      <Button
                        className="red"
                        title="Elimina valutazione"
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

                <Button className="blu">
                  <MdPersonAdd size={24} color="white" />
                </Button>

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

      {selected && (
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
