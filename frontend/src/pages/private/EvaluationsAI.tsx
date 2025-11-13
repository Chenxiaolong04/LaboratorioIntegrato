import { useState } from "react";
import SearchBar from "../../components/SearchBar";
import Button from "../../components/Button";
import { FaX } from "react-icons/fa6";
import { MdPersonAdd } from "react-icons/md";

const data = [
  {
    proprietario: "Giovanni Esposito",
    data: "08/11/2025 - 16:40:02",
    prezzoAI: "400.000 €",
    indirizzo: "Via Jacopo Durandi 8",
    tipologia: "Appartamento",
  },
  {
    proprietario: "Carla Rossi",
    data: "07/11/2025 - 14:20:01",
    prezzoAI: "400.000 €",
    indirizzo: "Via Jacopo Durandi 8",
    tipologia: "Appartamento",
  },
  {
    proprietario: "Anna Verdi",
    data: "06/11/2025 - 09:10:12",
    prezzoAI: "400.000 €",
    indirizzo: "Via Jacopo Durandi 8",
    tipologia: "Appartamento",
  },
  {
    proprietario: "Anna Verdi",
    data: "06/11/2025 - 09:10:12",
    prezzoAI: "400.000 €",
    indirizzo: "Via Jacopo Durandi 8",
    tipologia: "Appartamento",
  },
  {
    proprietario: "Anna Verdi",
    data: "06/11/2025 - 09:10:12",
    prezzoAI: "400.000 €",
    indirizzo: "Via Jacopo Durandi 8",
    tipologia: "Appartamento",
  },
  {
    proprietario: "Giovanni Esposito",
    data: "08/11/2025 - 16:40:02",
    prezzoAI: "400.000 €",
    indirizzo: "Via Jacopo Durandi 8",
    tipologia: "Appartamento",
  },
  {
    proprietario: "Carla Rossi",
    data: "07/11/2025 - 14:20:01",
    prezzoAI: "400.000 €",
    indirizzo: "Via Jacopo Durandi 8",
    tipologia: "Appartamento",
  },
  {
    proprietario: "Anna Verdi",
    data: "06/11/2025 - 09:10:12",
    prezzoAI: "400.000 €",
    indirizzo: "Via Jacopo Durandi 8",
    tipologia: "Appartamento",
  },
];

export default function EvaluationsAI() {
  const [searchQuery, setSearchQuery] = useState("");
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
                    <td>{row.prezzoAI}</td>
                    <td>{row.indirizzo}</td>
                    <td>{row.tipologia}</td>
                    <td>
                      <div className="action-buttons">
                        <Button className="lightblu" title="Maggiori dettagli sulla valutazione">Dettagli</Button>
                        <Button className="blu" title="Assegna agente immobiliare"><MdPersonAdd size={28} color={'white'}/></Button>
                        <Button className="red" title="Elimina valutazione"><FaX /></Button>
                      </div>
                    </td>
                  </tr>
                ))}
            </tbody>
          </table>
        </div>

        <div className="btn-table">
          <Button>Mostra altre valutazioni</Button>
        </div>
      </div>
    </div>
  );
}
