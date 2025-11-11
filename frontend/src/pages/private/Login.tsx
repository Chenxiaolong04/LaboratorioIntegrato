import { useState } from "react";
import TestImage from "../../assets/img/test-image.jpg";
import { Link, useNavigate } from "react-router-dom";
import { loginUser } from "../../services/api";
import { useAuth } from "../../context/AuthContext";
import Button from "../../components/Button";
import Input from "../../components/Input";

export default function Login() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const navigate = useNavigate();
  const { login } = useAuth();

  const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    setError("");

    try {
      const userData = await loginUser(email, password);

      if (!userData.success) {
        setError(userData.message || "Credenziali non valide");
        return;
      }

      login({
        id: 0,
        name: userData.username,
        email: userData.username,
        role: userData.roles.includes("ROLE_ADMIN") ? "admin" : "agente",
      });

      if (userData.roles.includes("ROLE_ADMIN")) {
        navigate("/admin");
      } else {
        navigate("/agente");
      }
    } catch (err) {
      console.error(err);
      setError("Errore durante il login");
    }
  };

  return (
    <section className="login-page">
      <Link to={"/"}>
        <img src={TestImage} alt="logo" />
      </Link>
      <div className="login-container">
        <h1>
          Bentornato! <br /> Felice di rivederti
        </h1>

        <form className="login-form" onSubmit={handleSubmit}>
          <div className="input-button">
            <Input
              name="email"
              type="email"
              placeholder="Inserisci la tua email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              required
            />
          </div>
          <div className="input-button">
            <Input
              name="password"
              type="password"
              placeholder="Inserisci la tua password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
            />
          </div>

          {error && <p className="error-message">{error}</p>}

          <Button type="submit" className="lightblu">
            Accedi
          </Button>
        </form>
      </div>
    </section>
  );
}
