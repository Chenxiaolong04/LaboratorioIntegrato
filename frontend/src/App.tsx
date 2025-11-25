import "./assets/styles/main.scss";
import { Route, Routes } from "react-router-dom";
import ProtectedRoute from "./components/ProtectedRoute";
import Home from "./pages/public/Home";
import MultiStepForm from "./pages/public/MultiStepForm";
import Login from "./pages/private/Login";
import DashboardAdmin from "./pages/private/adminPages/DashboardAdmin";
import AdminHome from "./pages/private/adminPages/AdminHome";
import Users from "./pages/private/adminPages/Users";
import CreateUser from "./pages/private/adminPages/CreateUser";
import ContractsAdmin from "./pages/private/adminPages/ContractsAdmin";
import AssignmentsAdmin from "./pages/private/adminPages/AssignmentsAdmin";
import EvaluationsAI from "./pages/private/EvaluationsAI";
import DashboardAgent from "./pages/private/agentPages/DashboardAgent";
import AgentHome from "./pages/private/agentPages/AgentHome";
import ContractsAgent from "./pages/private/agentPages/ContractsAgent";
import AssignmentsAgent from "./pages/private/agentPages/AssignmentsAgent";
import FormIntro from "./components/FormCheck";

function App() {
  return (
    <>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/form-check" element={<FormIntro />} />
        <Route path="/form" element={<MultiStepForm />} />
        <Route path="/login" element={<Login />} />

        <Route
          path="/admin"
          element={
            <ProtectedRoute role="admin">
              <DashboardAdmin />
            </ProtectedRoute>
          }
        >
          <Route index element={<AdminHome />} />
          <Route path="users" element={<Users />} />
          <Route path="create-user" element={<CreateUser />} />
          <Route path="contracts" element={<ContractsAdmin />} />
          <Route path="assignments" element={<AssignmentsAdmin />} />
          <Route path="evaluationsAI" element={<EvaluationsAI />} />
        </Route>

        <Route
          path="/agente"
          element={
            <ProtectedRoute role="agente">
              <DashboardAgent />
            </ProtectedRoute>
          }
        >
          <Route index element={<AgentHome />} />
          <Route path="contracts" element={<ContractsAgent />} />
          <Route path="assignments" element={<AssignmentsAgent />} />
          <Route path="evaluationsAI" element={<EvaluationsAI />} />
        </Route>
      </Routes>
    </>
  );
}

export default App;
