import { Outlet } from "react-router-dom";
import PrivateLayout from "../../../layouts/PrivateLayout";



export default function DashboardAgent() {
  


  return (
    <PrivateLayout>
      <div className="dashboard">
        <div className="header-dashboard">
          
        </div>
        <Outlet />
      </div>
    </PrivateLayout>
  );
}
