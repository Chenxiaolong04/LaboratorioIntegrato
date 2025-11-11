import { Navigate } from "react-router-dom";

interface ProtectedRouteProps {
  children: React.ReactNode;
}

export default function ProtectedRoute({ children }: ProtectedRouteProps) {
  //   const isAuthenticated = localStorage.getItem("token");
  //   oppure prendi lo stato da Redux o Context

  //   if (!isAuthenticated) {
  //     return <Navigate to="/login" replace />;
  //   }

  return <>{children}</>;
}
