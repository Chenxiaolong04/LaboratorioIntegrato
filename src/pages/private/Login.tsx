import { useState } from "react";
import TestImage from "../../assets/img/test-image.jpg";
import { Link } from "react-router-dom";

export default function Login() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

  const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    console.log("Email:", email);
    console.log("Password:", password);
    // TODO: aggiungi qui logica di autenticazione o redirect
  };

  return (
      <section className="login-page">
        <Link to={'/'}><img src={TestImage} alt="logo" /></Link>
        <div className="login-container">
            <h1>
              Bentornato! <br /> Felice di rivederti
            </h1>
            <form className="login-form" onSubmit={handleSubmit}>
              <div className="input-button">
                <input
                  type="email"
                  placeholder="Inserisci la tua email"
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                  required
                />
              </div>
              <div className="input-button">
                <input
                  type="password"
                  placeholder="Inserisci la tua password"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  required
                />
              </div>
              <button type="submit" className="login-submit">
                Accedi
              </button>
            </form>
        </div>
      </section>
  );
}
