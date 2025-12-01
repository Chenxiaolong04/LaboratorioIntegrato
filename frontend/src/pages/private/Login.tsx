import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { loginUser, type LoginResponse } from "../../services/api";
import { useAuth } from "../../context/AuthContext";
import Button from "../../components/Button";
import Input from "../../components/Input";
import Loader from "../../components/Loader";

/**
 * Login component.
 * Provides a form for users (agents/admins) to log into the application.
 * Handles authentication, displays loading states, manages errors, and redirects upon successful login.
 * @returns {JSX.Element} The Login page view.
 */
export default function Login() {
  /**
   * State for the user's input email address.
   * @type {[string, function(string): void]}
   */
  const [email, setEmail] = useState("");

  /**
   * State for the user's input password.
   * @type {[string, function(string): void]}
   */
  const [password, setPassword] = useState("");

  /**
   * State for displaying error messages to the user.
   * @type {[string, function(string): void]}
   */
  const [error, setError] = useState("");

  /**
   * State indicating if the login process is currently in progress.
   * @type {[boolean, function(boolean): void]}
   */
  const [loading, setLoading] = useState(false);

  /**
   * Hook for programmatic navigation.
   * @type {function(string): void}
   */
  const navigate = useNavigate();

  /**
   * Retrieves the login function from the authentication context.
   * @type {{ login: function(object): void }}
   */
  const { login } = useAuth();

  /**
   * Handles the submission of the login form.
   * Authenticates the user and manages state updates (loading, error, user session, redirection).
   * @async
   * @param {React.FormEvent<HTMLFormElement>} e - The form submission event.
   * @returns {Promise<void>}
   */
  const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    setError("");
    setLoading(true);

    /**
     * Helper promise to enforce a minimum delay for a better user experience (simulated network latency).
     * @type {Promise<void>}
     */
    const timeout = new Promise((res) => setTimeout(res, 2000));

    try {
      let userData: LoginResponse | null = null;

      try {
        userData = await loginUser(email, password);
      } catch {
        // Handle network/server error during the API call
        await timeout;
        setError("Si è verificato un errore del server. Riprova più tardi.");
        return;
      }

      await timeout; // Wait for the remaining timeout period

      // Handle failed login due to incorrect credentials
      if (!userData.success) {
        setError("Email o password non corretti.");
        return;
      }

      // Successful login: set user session data
      login({
        id: 0, // Placeholder ID, assuming ID is not returned or not critical here
        name: userData.username,
        email: userData.username,
        // Determine user role based on roles array from the backend
        role: userData.roles.includes("ROLE_ADMIN") ? "admin" : "agente",
      });

      // Redirect the user based on their assigned role
      navigate(userData.roles.includes("ROLE_ADMIN") ? "/admin" : "/agente");
    } finally {
      setLoading(false);
    }
  };

  /**
   * Display a loader while the login process is ongoing.
   */
  if (loading) return <Loader />;

  return (
    <section className="login-page">
      <div className="container">
        <Link to={"/"}>
          <img src="./logo.svg" alt="logo immobiliaris" />
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