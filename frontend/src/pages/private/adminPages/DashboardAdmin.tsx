import { Outlet } from "react-router-dom";
import PrivateLayout from "../../../layouts/PrivateLayout";

export default function DashboardAdmin() {
  return (
    <PrivateLayout>
      <div className="dashboard-admin">
        <Outlet />
      </div>
    </PrivateLayout>
  );
}
