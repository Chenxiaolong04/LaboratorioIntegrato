import { Outlet } from "react-router-dom";
import PrivateLayout from "../../../layouts/PrivateLayout";

export default function DashboardAdmin() {
  

  return (
    <PrivateLayout>
      <div className="dashboard">
        <div className="header-dashboard">
          <h1>Dashboard Immobiliaris - Super Admin</h1>
        </div>
      <Outlet />
      </div>
    </PrivateLayout>
  );
}
