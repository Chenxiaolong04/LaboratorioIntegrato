import { useState } from "react";
import { FaCheckCircle } from "react-icons/fa";
import { FaSquareArrowUpRight } from "react-icons/fa6";
import { TbProgressCheck } from "react-icons/tb";
import { PiWarningCircleBold } from "react-icons/pi";
import PrivateLayout from "../../layouts/PrivateLayout";
import { Link } from "react-router-dom";
import Button from "../../components/Button";

const filterOptions = [
  { label: "Vendite concluse", value: "vendite" },
  { label: "Incarichi in corso", value: "incarichi" },
  { label: "Valutazioni AI", value: "valutazioni" },
];

export default function DashboardAdmin() {
  const [filter, setFilter] = useState<string | null>(null);
  const [dropdownOpen, setDropdownOpen] = useState(false);

  const data = [
    {
      tipo: "vendite",
      id: "#345435",
      data: "08/11/2025 - 16:40:02",
      agente: "Marco Bianchi",
    },
    {
      tipo: "incarichi",
      id: "#123456",
      data: "07/11/2025 - 14:20:01",
      agente: "Luca Rossi",
    },
    {
      tipo: "valutazioni",
      id: "#987654",
      data: "06/11/2025 - 09:10:12",
      agente: "Anna Verdi",
    },
  ];

  const filteredData = filter ? data.filter((d) => d.tipo === filter) : data;

  return (
    <PrivateLayout>
      <div className="header-dashboard">
        <h1>Dashboard Immobiliaris</h1>
      </div>
      <div className="dashboard-container">
          <div className="general-latest-container">
            <div className="general-dashboard">
              <div className="general-container">
                <div className="title-card">
                  <span>
                    <FaCheckCircle size={32} color="green" />
                  </span>
                  <h3>Vendite concluse</h3>
                </div>
                <div className="data-card">
                  <h3>400</h3>
                  <Link to="/dashboard-admin">
                    <FaSquareArrowUpRight size={50} />
                  </Link>
                </div>
              </div>
              <div className="general-container">
                <div className="title-card">
                  <span>
                    <TbProgressCheck size={32} color="orange" />
                  </span>
                  <h3>Incarichi in corso</h3>
                </div>
                <div className="data-card">
                  <h3>150</h3>
                  <Link to="/dashboard-admin">
                    <FaSquareArrowUpRight size={50} />
                  </Link>
                </div>
              </div>
              <div className="general-container">
                <div className="title-card">
                  <span>
                    <PiWarningCircleBold size={32} color="gray" />
                  </span>
                  <h3>Valutazioni AI effettuate</h3>
                </div>
                <div className="data-card">
                  <h3>2000</h3>
                  <Link to="/dashboard-admin">
                    <FaSquareArrowUpRight size={50} />
                  </Link>
                </div>
              </div>
            </div>
            <div className="latest-alerts">
              <h2>Ultimi avvisi</h2>
              <div className="filter-buttons">
                <div className="dropdown">
                  <Button
                    onClick={() => setDropdownOpen(!dropdownOpen)}
                    className="lightblu"
                  >
                    Filtra
                  </Button>
                  {dropdownOpen && (
                    <ul className="dropdown-menu">
                      {filterOptions.map((opt) => (
                        <li
                          key={opt.value}
                          className={filter === opt.value ? "active" : ""}
                          onClick={() => {
                            setFilter(opt.value);
                            setDropdownOpen(false);
                          }}
                        >
                          {opt.label}
                        </li>
                      ))}
                    </ul>
                  )}
                </div>
                {filter && (
                  <Button className="remove-filter" onClick={() => setFilter(null)}>
                    Rimuovi filtro
                  </Button>
                )}
              </div>
              <div className="table-wrapper">
                <table className="alerts-table">
                  <thead>
                    <tr>
                      <th>Tipo</th>
                      <th>ID</th>
                      <th>Data</th>
                      <th>Agente assegnato</th>
                      <th>Azioni</th>
                    </tr>
                  </thead>
                  <tbody>
                    {filteredData.map((row, i) => (
                      <tr key={i}>
                        <td>
                          <div className="cell-content">
                            {row.tipo === "vendite" && (
                              <FaCheckCircle size={32} color="green" />
                            )}
                            {row.tipo === "incarichi" && (
                              <TbProgressCheck size={32} color="orange" />
                            )}
                            {row.tipo === "valutazioni" && (
                              <PiWarningCircleBold size={32} color="gray" />
                            )}
                            <h3>
                              {filterOptions.find((f) => f.value === row.tipo)
                                ?.label || row.tipo}
                            </h3>
                          </div>
                        </td>
                        <td>{row.id}</td>
                        <td>{row.data}</td>
                        <td>{row.agente}</td>
                        <td>
                          <Button className="lightblu">Dettagli</Button>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            </div>
          </div>
          <div className="stats-today">
            <h2>Statistiche di oggi</h2>
            <div className="card">
              <h3>Vendite concluse</h3>
              <p>+ 5</p>
            </div>
            <div className="card">
              <h3>Incarichi nuovi</h3>
              <p>+ 5</p>
            </div>
            <div className="card">
              <h3>Valutazioni AI effettutate</h3>
              <p>+ 5</p>
            </div>
          </div>
      </div>
    </PrivateLayout>
  );
}
