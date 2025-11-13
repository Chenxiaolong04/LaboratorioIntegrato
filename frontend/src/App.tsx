import "./assets/styles/main.scss";
import Home from "./pages/public/Home";
import { Route, Routes } from "react-router-dom";
import AboutUs from "./pages/public/AboutUs";
import Contact from "./pages/public/Contact";
import MultiStepForm from "./pages/public/MultiStepForm";
import Login from "./pages/private/Login";
import DashboardAdmin from "./pages/private/adminPages/DashboardAdmin";
import ProtectedRoute from "./components/ProtectedRoute";
import CreateUser from "./pages/private/adminPages/CreateUser";
import AdminHome from "./pages/private/adminPages/AdminHome";
import AssignmentsAdmin from "./pages/private/adminPages/AssignmentsAdmin";
import EvaluationsAI from "./pages/private/EvaluationsAI";
import ContractsAdmin from "./pages/private/adminPages/ContractsAdmin";

function App() {
  return (
    <>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/form" element={<MultiStepForm />} />
        <Route path="/about" element={<AboutUs />} />
        <Route path="/contact" element={<Contact />} />
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
          <Route path="create-user" element={<CreateUser />} />
          <Route path="contracts" element={<ContractsAdmin />} />
          <Route path="assignments" element={<AssignmentsAdmin />} />
          <Route path="evaluationsAI" element={<EvaluationsAI />} />
        </Route>
      </Routes>
    </>
  );
}

export default App;
