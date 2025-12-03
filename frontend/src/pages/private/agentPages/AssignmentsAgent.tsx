import { useState } from "react";
import { FaX } from "react-icons/fa6";
import Button from "../../../components/Button";
import SearchBar from "../../../components/SearchBar";
import Loader from "../../../components/Loader";

/**
 * Type representing an assignment.
 */
interface Incarichi {
  id: number;
  nomeProprietario: string;
  prezzoAI: number;
  dataInserimento: string;
  descrizione: string;
  nomeAgente: string;
}

/**
 * Mock static data for assignments.
 */
const MOCK_INCARICHI: Incarichi[] = [
  {
    id: 1,
    nomeProprietario: "Mario Rossi",
    prezzoAI: 250000,
    dataInserimento: "2025-01-15",
    descrizione: "Appartamento in centro città",
    nomeAgente: "Luca",
  },
  {
    id: 2,
    nomeProprietario: "Lucia Bianchi",
    prezzoAI: 420000,
    dataInserimento: "2025-02-01",
    descrizione: "Villa con giardino",
    nomeAgente: "Luca",
  },
  {
    id: 3,
    nomeProprietario: "Giovanni Verdi",
    prezzoAI: 180000,
    dataInserimento: "2025-03-05",
    descrizione: "Loft ristrutturato",
    nomeAgente: "Luca",
  },
];

/**
 * AssignmentsAgent component displays the list of assignments for the logged-in agent.
 * Allows searching by owner name, deleting assignments, and viewing details in a modal.
 *
 * @component
 * @returns {JSX.Element} Assignment dashboard for the agent.
 */
export default function AssignmentsAgent() {
  const [searchQuery, setSearchQuery] = useState("");
  const [incarichi, setIncarichi] = useState<Incarichi[]>(MOCK_INCARICHI);
  const [selectedIncarico, setSelectedIncarico] = useState<Incarichi | null>(
    null
  );
  const [loading] = useState(false);

  /**
   * Handles deleting an assignment (mocked).
   * @param {number} id - The ID of the assignment to delete.
   */
  const handleDelete = (id: number) => {
    if (!confirm("Vuoi davvero eliminare questo incarico?")) return;
    setIncarichi((prev) => prev.filter((v) => v.id !== id));
  };

  // Filter assignments by search query
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

          {/* Search bar */}
          <div className="filter-buttons">
            <SearchBar
              placeholder="Cerca un proprietario"
              onSearch={setSearchQuery}
            />
          </div>

          {/* Table view */}
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
                    <td>{row.nomeProprietario}</td>
                    <td>{row.prezzoAI} €</td>
                    <td>{row.dataInserimento}</td>
                    <td>
                      <div className="action-buttons">
                        <Button
                          className="lightblu"
                          onClick={() => setSelectedIncarico(row)}
                        >
                          Dettagli
                        </Button>
                        <Button
                          title="Cancella incarico"
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

          {/* Card view for mobile */}
          <div className="assignments-cards">
            {filteredIncarichi.map((row) => (
              <div className="assignment-card" key={row.id}>
                <div className="card-row">
                  <b>Proprietario:</b> <span>{row.nomeProprietario}</span>
                </div>
                <div className="card-row">
                  <b>Data inserimento:</b> <span>{row.dataInserimento}</span>
                </div>
                <div className="card-row">
                  <b>Prezzo AI:</b> <span>{row.prezzoAI} €</span>
                </div>
                <div className="card-actions">
                  <Button
                    className="lightblu"
                    onClick={() => setSelectedIncarico(row)}
                  >
                    Dettagli
                  </Button>
                  <Button
                    title="Cancella incarico"
                    className="red"
                    onClick={() => handleDelete(row.id)}
                  >
                    <FaX />
                  </Button>
                </div>
              </div>
            ))}
          </div>

          {/* Load more button (mocked) */}
          <div className="btn-table">
            <Button disabled>Mostra altri incarichi</Button>
          </div>
        </div>
      )}

      {/* Modal for assignment details */}
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
            <p>
              <b>Data inserimento:</b> {selectedIncarico.dataInserimento}
            </p>
            <p>
              <b>Agente assegnato:</b> {selectedIncarico.nomeAgente}
            </p>
            <Button
              title="Chiudi i dettagli"
              className="red"
              onClick={() => setSelectedIncarico(null)}
            >
              Chiudi
            </Button>
          </div>
        </div>
      )}
    </div>
  );
}
