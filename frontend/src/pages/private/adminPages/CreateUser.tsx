import { useState, type ChangeEvent, type FormEvent } from "react";
import InputGroup from "../../../components/InputGroup";
import Button from "../../../components/Button";

type UserRole = "admin" | "agente";

interface CreateUserFormData {
  name: string;
  email: string;
  password: string;
  role: UserRole;
}

export default function CreateUser() {
  const [formData, setFormData] = useState<CreateUserFormData>({
    name: "",
    email: "",
    password: "",
    role: "agente",
  });

  const handleChange = (
    e: ChangeEvent<HTMLInputElement | HTMLSelectElement>
  ) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    // TODO: invia i dati al backend
    console.log("Dati utente creato:", formData);
    alert(`Utente "${formData.name}" di tipo "${formData.role}" creato!`);
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

        <div className="input-group">
          <label htmlFor="role">Ruolo</label>
          <select
            id="role"
            name="role"
            value={formData.role}
            onChange={handleChange}
            className="input-form"
            required
          >
            <option value="admin">Admin</option>
            <option value="agente">Agente Immobiliare</option>
          </select>
        </div>

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

        <Button type="submit" className="blu">
          Crea Utente
        </Button>
      </form>
    </section>
  );
}
