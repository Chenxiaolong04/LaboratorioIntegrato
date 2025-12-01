import { useState, type ChangeEvent, type FormEvent } from "react";
import InputGroup from "../../../components/InputGroup";
import Button from "../../../components/Button";
import { registerUser } from "../../../services/api";
import { FaCheckCircle } from "react-icons/fa";

/**
 * Interface representing the structure of a user role.
 * @interface UserRole
 * @property {number} idTipo - The numeric ID of the user type/role.
 * @property {string} nome - The descriptive name of the role (e.g., "Admin").
 * @property {string} role - The standardized role identifier (e.g., "ADMIN").
 */
interface UserRole {
  idTipo: number;
  nome: string;
  role: string;
}

/**
 * Interface representing the structure of the data collected from the create user form.
 * @interface CreateUserFormData
 * @property {string} name - The user's first name.
 * @property {string} surname - The user's last name.
 * @property {string} email - The user's email address.
 * @property {string} password - The user's password.
 * @property {string} telefono - The user's phone number.
 * @property {UserRole} tipoUtente - The selected user role object.
 */
interface CreateUserFormData {
  name: string;
  surname: string;
  email: string;
  password: string;
  telefono: string;
  tipoUtente: UserRole;
}

/**
 * CreateUser component.
 * Allows administrators to create new user profiles by registering them via an API call.
 * @returns {JSX.Element} The Create User form section, including a success/error modal.
 */
export default function CreateUser() {
  /**
   * State to control the visibility of the success/error modal.
   * @type {[boolean, function(boolean): void]}
   */
  const [showModal, setShowModal] = useState(false);

  /**
   * State to hold the message displayed inside the modal (success or error).
   * @type {[string, function(string): void]}
   */
  const [modalMessage, setModalMessage] = useState("");

  /**
   * State to manage the form data for the new user, initialized with default 'Admin' role.
   * @type {[CreateUserFormData, function(CreateUserFormData): void]}
   */
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

  /**
   * Generic handler for input changes (text fields and select).
   * Updates the corresponding field in the formData state.
   * Note: The 'tipoUtente' field is handled separately in the select's onChange.
   * @param {ChangeEvent<HTMLInputElement | HTMLSelectElement>} e - The change event object.
   */
  const handleChange = (
    e: ChangeEvent<HTMLInputElement | HTMLSelectElement>
  ) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  /**
   * Handles the form submission: registers the user via the API, shows a success/error modal,
   * and resets the form data on success.
   * @async
   * @param {FormEvent<HTMLFormElement>} e - The form submission event.
   * @returns {Promise<void>}
   */
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
              /** Logic to update the entire 'tipoUtente' object based on the selected 'role' value. */
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
        /** Modal overlay for showing registration success or failure. */
        <div className="modal-overlay" onClick={() => setShowModal(false)}>
          {/** Modal content, prevents closing when clicking inside. */}
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