import { Outlet } from "react-router-dom";
import PrivateLayout from "../../../layouts/PrivateLayout";
import { useAuth } from "../../../context/AuthContext";

/**
 * DashboardAgent component.
 * This component serves as the main layout and entry point for the agent's section of the application.
 * It ensures the user is authenticated via PrivateLayout and displays a welcome message
 * along with rendering nested routes using the Outlet component.
 * @returns {JSX.Element} The agent dashboard layout.
 */
export default function DashboardAgent() {
  /**
   * Retrieves the current authenticated user object from the authentication context.
   * @type {object}
   */
  const { user } = useAuth();

  return (
    <PrivateLayout>
      <div className="dashboard">
        <div className="header-dashboard">
          {/* Displays a welcome message personalized with the agent's name. */}
          <h1>Bentornato {user?.name}</h1>
        </div>
        {/* Renders the content of the currently matched nested route (e.g., AgentHome, ContractsAgent, etc.). */}
        <Outlet />
      </div>
    </PrivateLayout>
  );
}