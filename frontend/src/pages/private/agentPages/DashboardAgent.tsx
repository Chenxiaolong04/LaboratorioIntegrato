import { Outlet } from "react-router-dom";
import PrivateLayout from "../../../layouts/PrivateLayout";
import { useAuth } from "../../../context/AuthContext";

export default function DashboardAgent() {
  const { user } = useAuth();

  return (
    <PrivateLayout>
      <div className="dashboard">
        <div className="header-dashboard">
          <h1>Bentornato {user?.name} Dashboard Agente</h1>
        </div>
        <Outlet />
      </div>
    </PrivateLayout>
  );
}
