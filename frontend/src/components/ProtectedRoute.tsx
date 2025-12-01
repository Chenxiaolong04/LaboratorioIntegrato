import { Navigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

interface ProtectedRouteProps {
  /** The content to render if access is allowed */
  children: React.ReactNode;
  /** The required role to access this route ("admin" or "agente") */
  role: "admin" | "agente";
}

/**
 * Component to protect routes based on authentication and user role.
 *
 * Redirects to the login page if the user is not authenticated or
 * does not have the required role.
 *
 * @param {ProtectedRouteProps} props - Props containing children and role.
 * @returns {JSX.Element} The protected content or a redirect to login.
 */
export default function ProtectedRoute({ children, role }: ProtectedRouteProps) {
  const { user } = useAuth();

  // If no user is logged in, redirect to login
  if (!user) {
    return <Navigate to="/login" replace />;
  }

  // If a role is specified and the user doesn't have it, redirect to login
  if (role && user.role !== role) {
    return <Navigate to="/login" replace />;
  }

  // Otherwise render the protected content
  return <>{children}</>;
}
