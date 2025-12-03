import { useState } from "react";
import SearchBar from "../../../components/SearchBar";
import Button from "../../../components/Button";
import Loader from "../../../components/Loader";

/**
 * Type representing a closed contract.
 */
interface ContrattoChiuso {
  numeroContratto: string;
  nomeProprietario: string;
  dataInizio: string;
  dataFine: string;
  tipo: string;
  via?: string;
  citta?: string;
  dataInserimento?: string;
}

/**
 * Mock static data for closed contracts.
 */
const MOCK_CONTRATTI: ContrattoChiuso[] = [
  {
    numeroContratto: "001",
    nomeProprietario: "Mario Rossi",
    dataInizio: "2025-01-01",
    dataFine: "2025-06-01",
    tipo: "Appartamento",
    via: "Via Torino 12",
    citta: "Torino",
    dataInserimento: "2024-12-15",
  },
  {
    numeroContratto: "002",
    nomeProprietario: "Lucia Bianchi",
    dataInizio: "2025-02-10",
    dataFine: "2025-08-10",
    tipo: "Villa",
    via: "Via Roma 45",
    citta: "Cuneo",
    dataInserimento: "2024-12-20",
  },
  {
    numeroContratto: "003",
    nomeProprietario: "Giovanni Verdi",
    dataInizio: "2025-03-05",
    dataFine: "2025-09-05",
    tipo: "Loft",
    via: "Corso Milano 7",
    citta: "Asti",
    dataInserimento: "2024-12-22",
  },
];

/**
 * ContractsAgent component displays the list of closed contracts for the logged-in agent.
 * Allows searching by owner name, viewing details in a modal, and simulates "load more".
 *
 * @component
 * @returns {JSX.Element} Closed contracts dashboard for the agent.
 */
export default function ContractsAgent() {
  const [searchQuery, setSearchQuery] = useState("");
  const [contratti] = useState<ContrattoChiuso[]>(MOCK_CONTRATTI);
  const [selectedContract, setSelectedContract] =
    useState<ContrattoChiuso | null>(null);
  const [loading] = useState(false);

  // Filter contracts by search query
  const filteredContratti = contratti.filter((c) =>
    (c.nomeProprietario || "").toLowerCase().includes(searchQuery.toLowerCase())
  );

  // Show loader if loading (simulated)
  if (loading && contratti.length === 0) return <Loader />;

  return (
    <div className="dashboard-container">
      <div className="table-container">
        <h2>I miei contratti conclusi</h2>

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
                <th>Data inizio</th>
                <th>Data fine</th>
                <th>Tipo immobile</th>
                <th>Azioni</th>
              </tr>
            </thead>
            <tbody>
              {filteredContratti.map((c) => (
                <tr key={c.numeroContratto}>
                  <td>{c.nomeProprietario}</td>
                  <td>{c.dataInizio}</td>
                  <td>{c.dataFine}</td>
                  <td>{c.tipo}</td>
                  <td>
                    <Button
                      className="lightblu"
                      onClick={() => setSelectedContract(c)}
                    >
                      Dettagli
                    </Button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>

        {/* Card view for mobile */}
        <div className="contract-cards">
          {filteredContratti.map((c) => (
            <div className="contract-card" key={c.numeroContratto}>
              <div className="card-row">
                <b>Proprietario:</b> {c.nomeProprietario}
              </div>
              <div className="card-row">
                <b>Tipo:</b> {c.tipo}
              </div>
              <div className="card-row">
                <b>Data inizio:</b> {c.dataInizio}
              </div>
              <div className="card-row">
                <b>Data fine:</b> {c.dataFine}
              </div>
              <div className="card-actions">
                <Button
                  className="lightblu"
                  onClick={() => setSelectedContract(c)}
                >
                  Dettagli
                </Button>
              </div>
            </div>
          ))}
        </div>

        {/* Load more button (simulated) */}
        <div className="btn-table">
          <Button disabled>Mostra altri contratti</Button>
        </div>
      </div>

      {/* Modal for contract details */}
      {selectedContract && (
        <div
          className="modal-overlay"
          onClick={() => setSelectedContract(null)}
        >
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
              <b>Tipo:</b> {selectedContract.tipo}
            </p>
            <p>
              <b>Indirizzo:</b> {selectedContract.via}, {selectedContract.citta}
            </p>

            <h4>Proprietario</h4>
            <p>
              <b>Nome:</b> {selectedContract.nomeProprietario}
            </p>
            <p>
              <b>Data inserimento:</b> {selectedContract.dataInserimento}
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
