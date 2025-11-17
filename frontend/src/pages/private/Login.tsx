import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { loginUser, type LoginResponse } from "../../services/api";
import { useAuth } from "../../context/AuthContext";
import Button from "../../components/Button";
import Input from "../../components/Input";
import Loader from "../../components/Loader";

export default function Login() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();
  const { login } = useAuth();

  const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    setError("");
    setLoading(true);

    const timeout = new Promise((res) => setTimeout(res, 2000));

    try {
      let userData: LoginResponse | null = null;

      try {
        userData = await loginUser(email, password);
      } catch {
        await timeout;
        setError("Si è verificato un errore del server. Riprova più tardi.");
        return;
      }

      await timeout;

      if (!userData.success) {
        setError("Email o password non corretti.");
        return;
      }

      login({
        id: 0,
        name: userData.username,
        email: userData.username,
        role: userData.roles.includes("ROLE_ADMIN") ? "admin" : "agente",
      });

      navigate(userData.roles.includes("ROLE_ADMIN") ? "/admin" : "/agente");
    } finally {
      setLoading(false);
    }
  };

  if (loading) return <Loader />;

  return (
    <section className="login-page">
      <div className="container">
        <Link to={"/"}>
          <img src='./logo.svg' alt="logo immobiliaris" />
        </Link>
        <div className="login-container">
          <h1>Accedi al tuo account</h1>
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
      </div>
    </section>
  );
}
