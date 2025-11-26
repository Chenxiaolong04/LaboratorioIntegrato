import { Navigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

interface ProtectedRouteProps {
  children: React.ReactNode;
  role: "admin" | "agente";
}

export default function ProtectedRoute({ children, role }: ProtectedRouteProps) {
  const { user } = useAuth();

  // Se non c’è utente loggato, reindirizza al login
  if (!user) {
    return <Navigate to="/login" replace />;
  }

  // Se è specificato un ruolo e l’utente non ce l’ha, reindirizza
  if (role && user.role !== role) {
    return <Navigate to="/login" replace />;
  }

  // Altrimenti mostra il contenuto protetto
  return <>{children}</>;
}
