import { useState, type ChangeEvent, type FormEvent } from "react";
import InputGroup from "../../../components/InputGroup";
import Button from "../../../components/Button";
import { registerUser } from "../../../services/api";
import { FaCheckCircle } from "react-icons/fa";

interface UserRole {
  idTipo: number;
  nome: string;
  role: string;
}

interface CreateUserFormData {
  name: string;
  surname: string;
  email: string;
  password: string;
  telefono: string;
  tipoUtente: UserRole;
}

export default function CreateUser() {
  const [showModal, setShowModal] = useState(false);
  const [modalMessage, setModalMessage] = useState("");

  const [formData, setFormData] = useState<CreateUserFormData>({
    name: "",
    surname: "",
    email: "",
    password: "",
    telefono: "",
    tipoUtente: {
      idTipo: 1,
      nome: "Admin",
      role: "ADMIN",
    },
  });

  const handleChange = (
    e: ChangeEvent<HTMLInputElement | HTMLSelectElement>
  ) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    try {
      await registerUser(
        formData.name,
        formData.surname,
        formData.email,
        formData.password,
        formData.telefono,
        formData.tipoUtente
      );

      setModalMessage(
        `L'utente "${formData.name}" è stato creato con successo!`
      );
      setShowModal(true);

      // Reset form
      setFormData({
        name: "",
        surname: "",
        email: "",
        password: "",
        telefono: "",
        tipoUtente: {
          idTipo: 1,
          nome: "Admin",
          role: "ADMIN",
        },
      });
    } catch (error) {
      console.error("Errore durante la registrazione:", error);

      setModalMessage("Errore durante la creazione dell'utente.");
      setShowModal(true);
    }
  };

  return (
    <section className="create-user">
      <h2>Crea un profilo</h2>

      <form onSubmit={handleSubmit} className="create-user-form">
        <InputGroup
          label="Nome"
          name="name"
          value={formData.name}
          onChange={handleChange}
          required={true}
          placeholder="Inserisci il nome"
        />

        <InputGroup
          label="Cognome"
          name="surname"
          value={formData.surname}
          onChange={handleChange}
          required={true}
          placeholder="Inserisci il cognome"
        />

        <InputGroup
          label="Email"
          name="email"
          type="email"
          value={formData.email}
          onChange={handleChange}
          required={true}
          placeholder="Inserisci l'email"
        />

        <InputGroup
          label="Password"
          name="password"
          type="password"
          value={formData.password}
          onChange={handleChange}
          required={true}
          placeholder="Inserisci la password"
        />

        <InputGroup
          label="Telefono"
          name="telefono"
          type="tel"
          value={formData.telefono}
          onChange={handleChange}
          required={true}
          placeholder="Inserisci il numero di telefono"
        />

        <div className="input-group">
          <label htmlFor="role">Ruolo *</label>
          <select
            id="role"
            name="tipoUtente"
            value={formData.tipoUtente.role}
            onChange={(e) => {
              const value = e.target.value;

              setFormData((prev) => ({
                ...prev,
                tipoUtente:
                  value === "ADMIN"
                    ? { idTipo: 1, nome: "Admin", role: "ADMIN" }
                    : { idTipo: 2, nome: "Agente Immobiliare", role: "AGENTE" },
              }));
            }}
            className="input-form"
            required
          >
            <option value="ADMIN">Admin</option>
            <option value="AGENTE">Agente Immobiliare</option>
          </select>
        </div>

        <Button type="submit" className="blu">
          Crea Utente
        </Button>
      </form>

      {showModal && (
        <div className="modal-overlay" onClick={() => setShowModal(false)}>
          <div className="modal" onClick={(e) => e.stopPropagation()}>
            <FaCheckCircle size={150} color="#348AA7" />
            <p>{modalMessage}</p>

            <Button
              onClick={() => setShowModal(false)}
              className="modal-btn blu"
            >
              Chiudi
            </Button>
          </div>
        </div>
      )}
    </section>
  );
}
