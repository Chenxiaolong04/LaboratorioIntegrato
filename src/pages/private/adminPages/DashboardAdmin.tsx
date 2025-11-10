import { Outlet } from "react-router-dom";
import PrivateLayout from "../../../layouts/PrivateLayout";

export default function DashboardAdmin() {

  return (
    <PrivateLayout>
      <div className="header-dashboard">
        <h1>Bentornato Dragos</h1>
      </div>
      <Outlet />
    </PrivateLayout>
  );
}
