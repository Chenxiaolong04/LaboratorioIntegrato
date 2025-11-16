import { useEffect, useState } from "react";
import SearchBar from "../../../components/SearchBar";
import { getUsers, type Users } from "../../../services/api";
import Button from "../../../components/Button";
import { FaX } from "react-icons/fa6";

export default function Users() {
  const [searchQuery, setSearchQuery] = useState("");
  // const [users, setUsers] = useState<Users[]>([]);
  const [selectedUser, setSelectedUser] = useState<Users | null>(null);

  const fakeUsers = [
    {
      idUtente: 1,
      nome: "Admin",
      cognome: "Test",
      email: "admin@test.com",
      telefono: "3201234567",
      via: "Via Roma 1",
      citta: "Milano",
      cap: "20100",
      dataRegistrazione: "2025-11-01",
      tipoUtente: {
        idTipo: 1,
        nomeTipo: "Admin",
      },
    },
    {
      idUtente: 2,
      nome: "Agent",
      cognome: "Verdi",
      email: "agent@test.com",
      telefono: "3209876543",
      via: "Via Milano 2",
      citta: "Roma",
      cap: "00100",
      dataRegistrazione: "2025-11-02",
      tipoUtente: {
        idTipo: 2,
        nomeTipo: "Agent",
      },
    },
  ];

  // useEffect(() => {
  //   (async () => {
  //     try {
  //       const res = await getUsers();
  //       setUsers(res);
  //     } catch (err) {
  //       console.error("Errore caricamento utenti:", err);
  //     }
  //   })();
  // }, []);

  // const filteredUsers = users.filter((u) =>
  //   (u.nome + " " + u.cognome).toLowerCase().includes(searchQuery.toLowerCase())
  // );

  // MOMENTO USO I DATI FINTI PERCHÉ IL BACKEND NON È PRONTO
  const filteredUsers = fakeUsers.filter((u) =>
    (u.nome + " " + u.cognome).toLowerCase().includes(searchQuery.toLowerCase())
  );

  return (
    <div className="dashboard-container">
      <div className="table-container">
        <h2>Gestione utenti</h2>

        <div className="filter-buttons">
          <SearchBar
            placeholder="Cerca un'utente"
            onSearch={(query) => setSearchQuery(query)}
          />
        </div>

        <div className="table-wrapper">
          <table className="alerts-table">
            <thead>
              <tr>
                <th>Nome</th>
                <th>Cognome</th>
                <th>Email</th>
                <th>Telefono</th>
                <th>Ruolo</th>
                <th>Azioni</th>
              </tr>
            </thead>
            <tbody>
              {filteredUsers.map((row) => (
                <tr key={row.idUtente}>
                  <td>{row.nome || "-"}</td>
                  <td>{row.cognome || "-"}</td>
                  <td>{row.email || "-"}</td>
                  <td>{row.telefono || "-"}</td>
                  <td>{row.tipoUtente.nomeTipo || "-"}</td>
                  <td>
                    <div className="action-buttons">
                      <Button
                        className="lightblu"
                        title="Maggiori dettagli sull'utente"
                        onClick={() => setSelectedUser(row)}
                      >
                        Dettagli
                      </Button>
                      <Button className="red" title="Elimina utente">
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
          {filteredUsers.map((row) => (
            <div className="assignment-card" key={row.idUtente}>
              <div className="card-row">
                <b>Nome:</b>
                <span>{row.nome || "-"}</span>
              </div>
              <div className="card-row">
                <b>Cognome:</b>
                <span>{row.cognome || "-"}</span>
              </div>
              <div className="card-row">
                <b>Email:</b>
                <span>{row.email || "-"}</span>
              </div>
              <div className="card-row">
                <b>Telefono:</b>
                <span>{row.telefono || "-"}</span>
              </div>
              <div className="card-row">
                <b>Ruolo:</b>
                <span>{row.tipoUtente.nomeTipo || "-"}</span>
              </div>

              <div className="card-actions">
                <Button
                  className="lightblu"
                  title="Maggiori dettagli sull'utente"
                  onClick={() => setSelectedUser(row)}
                >
                  Dettagli
                </Button>
                <Button className="red" title="Elimina incarico">
                  <FaX />
                </Button>
              </div>
            </div>
          ))}
        </div>
      </div>
      {selectedUser && (
        <div className="modal-overlay" onClick={() => setSelectedUser(null)}>
          <div className="modal" onClick={(e) => e.stopPropagation()}>
            <h3>Dettagli Utente</h3>

            <p>
              <b>ID:</b> {selectedUser.idUtente}
            </p>
            <p>
              <b>Nome:</b> {selectedUser.nome}
            </p>
            <p>
              <b>Cognome:</b> {selectedUser.cognome}
            </p>
            <p>
              <b>Email:</b> {selectedUser.email}
            </p>
            <p>
              <b>Telefono:</b> {selectedUser.telefono}
            </p>

            <h4>Extra:</h4>
            <p>
              <b>Via:</b> {selectedUser.via}
            </p>
            <p>
              <b>Città:</b> {selectedUser.citta}
            </p>
            <p>
              <b>CAP:</b> {selectedUser.cap}
            </p>

            <h4>Importanti:</h4>
            <p>
              <b>Data registrazione:</b> {selectedUser.dataRegistrazione}
            </p>
            <p>
              <b>Ruolo:</b> {selectedUser.tipoUtente.nomeTipo}
            </p>

            <Button className="red" onClick={() => setSelectedUser(null)}>
              Chiudi
            </Button>
          </div>
        </div>
      )}
    </div>
  );
}
