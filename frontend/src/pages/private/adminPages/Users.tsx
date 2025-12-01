import { useEffect, useState } from "react";
import SearchBar from "../../../components/SearchBar";
import {
  getUsers,
  updateUser,
  type UpdateUserRequest,
  type Users,
} from "../../../services/api";
import Button from "../../../components/Button";
import { FaX } from "react-icons/fa6";
import InputGroup from "../../../components/InputGroup";

/**
 * Users component.
 * Manages the display, searching, viewing, and editing of user data for administrative purposes.
 * It fetches the list of users and handles update operations through modals.
 * @returns {JSX.Element} The Users management component.
 */
export default function Users() {
  /**
   * State for storing the current search query used to filter users by name/surname.
   * @type {[string, function(string): void]}
   */
  const [searchQuery, setSearchQuery] = useState("");

  /**
   * State storing the user object currently being edited in the modal. Null if no user is being edited.
   * @type {[Users | null, function(Users | null): void]}
   */
  const [editingUser, setEditingUser] = useState<Users | null>(null);

  /**
   * State storing the form data for the user update request, used in the edit modal.
   * @type {[UpdateUserRequest | null, function(UpdateUserRequest | null): void]}
   */
  const [editFormData, setEditFormData] = useState<UpdateUserRequest | null>(
    null
  );

  /**
   * State storing the list of all fetched user objects.
   * @type {[Users[], function(Users[]): void]}
   */
  const [users, setUsers] = useState<Users[]>([]);

  /**
   * State storing the user object whose details are currently displayed in the view modal.
   * @type {[Users | null, function(Users | null): void]}
   */
  const [selectedUser, setSelectedUser] = useState<Users | null>(null);

  /**
   * useEffect hook to fetch the initial list of users when the component mounts.
   */
  useEffect(() => {
    (async () => {
      try {
        const res = await getUsers();
        setUsers(res);
      } catch (err) {
        console.error("Errore caricamento utenti:", err);
      }
    })();
  }, []);

  /**
   * Filters the list of users based on the current search query.
   * The search is performed against the concatenated full name (name + surname).
   * @type {Users[]}
   */
  const filteredUsers = users.filter((u) =>
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
                  <td>{row.tipoUtente.nome || "-"}</td>
                  <td>
                    <div className="action-buttons">
                      <Button
                        className="lightblu"
                        title="Maggiori dettagli sull'utente"
                        onClick={() => setSelectedUser(row)}
                      >
                        Dettagli
                      </Button>
                      <Button
                        className="blu"
                        title="Modifica dati dell'utente"
                        onClick={() => {
                          setEditingUser(row);
                          setEditFormData({
                            nome: row.nome,
                            cognome: row.cognome,
                            email: row.email,
                            telefono: row.telefono,
                            via: row.via,
                            tipoUtente: { idTipo: row.tipoUtente.idTipo },
                          });
                        }}
                      >
                        Modifica
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
                <span>{row.tipoUtente.nome || "-"}</span>
              </div>

              <div className="card-actions">
                <Button
                  className="lightblu"
                  title="Maggiori dettagli sull'utente"
                  onClick={() => setSelectedUser(row)}
                >
                  Dettagli
                </Button>
                <Button
                  className="blu"
                  title="Modifica dati dell'utente"
                  onClick={() => {
                    setEditingUser(row);
                    setEditFormData({
                      nome: row.nome,
                      cognome: row.cognome,
                      email: row.email,
                      telefono: row.telefono,
                      via: row.via,
                      tipoUtente: { idTipo: row.tipoUtente.idTipo },
                    });
                  }}
                >
                  Modifica
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
        /**
         * Modal overlay for displaying detailed information about a selected user.
         * Closes when clicking the overlay.
         */
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
              <b>Ruolo:</b> {selectedUser.tipoUtente.nome}
            </p>

            <Button className="red" onClick={() => setSelectedUser(null)}>
              Chiudi
            </Button>
          </div>
        </div>
      )}

      {editingUser && editFormData && (
        /**
         * Modal overlay for editing the data of a selected user.
         * Closes when clicking the overlay outside the form.
         */
        <div className="modal-overlay" onClick={() => setEditingUser(null)}>
          <div className="modal" onClick={(e) => e.stopPropagation()}>
            <h3>Modifica Utente</h3>

            <form
              onSubmit={async (e) => {
                e.preventDefault();

                if (!editingUser || !editFormData) return;

                // Check if at least one field has been modified
                const hasChanges =
                  editFormData.nome !== editingUser.nome ||
                  editFormData.cognome !== editingUser.cognome ||
                  editFormData.email !== editingUser.email ||
                  editFormData.telefono !== editingUser.telefono ||
                  editFormData.via !== editingUser.via ||
                  editFormData.tipoUtente?.idTipo !==
                    editingUser.tipoUtente.idTipo;

                if (!hasChanges) {
                  alert("Devi modificare almeno un campo prima di salvare.");
                  return;
                }

                try {
                  const updated = await updateUser(
                    editingUser.idUtente,
                    editFormData
                  );
                  // Update the local state with the modified user data
                  setUsers((prev) =>
                    prev.map((u) =>
                      u.idUtente === updated.idUtente ? updated : u
                    )
                  );
                  setEditingUser(null); // Close the modal on success
                } catch (err) {
                  console.error("Errore aggiornamento:", err);
                  alert("Errore durante la modifica dell'utente.");
                }
              }}
            >
              <div className="row">
                <InputGroup
                  label="Nome"
                  name="nome"
                  value={editFormData.nome || ""}
                  required={false}
                  onChange={(e) =>
                    setEditFormData((prev) => ({
                      ...prev,
                      nome: e.target.value,
                    }))
                  }
                />
                <InputGroup
                  label="Cognome"
                  name="cognome"
                  value={editFormData.cognome || ""}
                  required={false}
                  onChange={(e) =>
                    setEditFormData((prev) => ({
                      ...prev,
                      cognome: e.target.value,
                    }))
                  }
                />
              </div>
              <div className="row">
                <InputGroup
                  label="Email"
                  name="email"
                  type="email"
                  value={editFormData.email || ""}
                  required={false}
                  onChange={(e) =>
                    setEditFormData((prev) => ({
                      ...prev,
                      email: e.target.value,
                    }))
                  }
                />
                <InputGroup
                  label="Telefono"
                  name="telefono"
                  value={editFormData.telefono || ""}
                  required={false}
                  onChange={(e) =>
                    setEditFormData((prev) => ({
                      ...prev,
                      telefono: e.target.value,
                    }))
                  }
                />
              </div>
              <div className="row">
                <InputGroup
                  label="Via"
                  name="via"
                  value={editFormData.via || ""}
                  required={false}
                  onChange={(e) =>
                    setEditFormData((prev) => ({
                      ...prev,
                      via: e.target.value,
                    }))
                  }
                />
                <div className="input-group">
                  <label htmlFor="role">Ruolo</label>
                  <select
                    id="role"
                    value={editFormData.tipoUtente?.idTipo}
                    required={false}
                    className="btn gray"
                    onChange={(e) => {
                      const idTipo = parseInt(e.target.value);
                      setEditFormData((prev) => ({
                        ...prev,
                        tipoUtente: {
                          idTipo,
                          nome: idTipo === 1 ? "Admin" : "Agente Immobiliare",
                          role: idTipo === 1 ? "ADMIN" : "AGENTE",
                        },
                      }));
                    }}
                  >
                    <option value={1}>Admin</option>
                    <option value={2}>Agente Immobiliare</option>
                  </select>
                </div>
              </div>

              <Button type="submit" className="blu">
                Salva modifiche
              </Button>
              <Button
                type="button"
                className="red"
                onClick={() => setEditingUser(null)}
              >
                Annulla
              </Button>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}