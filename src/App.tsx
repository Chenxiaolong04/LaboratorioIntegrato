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
import SalesAdmin from "./pages/private/adminPages/SalesAdmin";
import AssignmentsAdmin from "./pages/private/adminPages/AssignmentsAdmin";
import EvaluationsAI from "./pages/private/EvaluationsAI";

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
            <ProtectedRoute>
              <DashboardAdmin />
            </ProtectedRoute>
          }
        >
          <Route index element={<AdminHome />} />
          <Route path="create-user" element={<CreateUser />} />
          <Route path="sales" element={<SalesAdmin />} />
          <Route path="assignments" element={<AssignmentsAdmin />} />
          <Route path="evaluationsAI" element={<EvaluationsAI />} />
        </Route>
      </Routes>
    </>
  );
}

export default App;
