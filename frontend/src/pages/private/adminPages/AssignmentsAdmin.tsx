import { useState } from "react";
import SearchBar from "../../../components/SearchBar";
import Button from "../../../components/Button";
import { FaX } from "react-icons/fa6";

const data = [
  {
    proprietario: "Giovanni Esposito",
    data: "08/11/2025 - 16:40:02",
    agente: "Marco Bianchi",
  },
  {
    proprietario: "Carla Rossi",
    data: "07/11/2025 - 14:20:01",
    agente: "Luca Rossi",
  },
  {
    proprietario: "Anna Verdi",
    data: "06/11/2025 - 09:10:12",
    agente: "Paolo Conti",
  },
  {
    proprietario: "Anna Verdi",
    data: "06/11/2025 - 09:10:12",
    agente: "Paolo Conti",
  },
  {
    proprietario: "Anna Verdi",
    data: "06/11/2025 - 09:10:12",
    agente: "Paolo Conti",
  },
  {
    proprietario: "Giovanni Esposito",
    data: "08/11/2025 - 16:40:02",
    agente: "Marco Bianchi",
  },
  {
    proprietario: "Carla Rossi",
    data: "07/11/2025 - 14:20:01",
    agente: "Luca Rossi",
  },
  {
    proprietario: "Anna Verdi",
    data: "06/11/2025 - 09:10:12",
    agente: "Paolo Conti",
  },
];

export default function AssignmentsAdmin() {
  const [searchQuery, setSearchQuery] = useState("");
  return (
    <div className="dashboard-container">
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
                <th>Data</th>
                <th>Agente assegnato</th>
                <th>Azioni</th>
              </tr>
            </thead>
            <tbody>
              {data
                .filter((row) =>
                  row.proprietario
                    .toLowerCase()
                    .includes(searchQuery.toLowerCase())
                )
                .map((row, i) => (
                  <tr key={i}>
                    <td>{row.proprietario}</td>
                    <td>{row.data}</td>
                    <td>{row.agente}</td>
                    <td>
                      <div className="action-buttons">
                        <Button className="lightblu" title="Maggiori dettagli sull'incarico">Dettagli</Button>
                        <Button className="red" title="Elimina incarico"><FaX /></Button>
                      </div>
                    </td>
                  </tr>
                ))}
            </tbody>
          </table>
        </div>

        <div className="btn-table">
          <Button>Mostra altri incarichi</Button>
        </div>
      </div>
    </div>
  );
}
