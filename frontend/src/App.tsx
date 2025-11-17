import "./assets/styles/main.scss";
import { Route, Routes } from "react-router-dom";
import ProtectedRoute from "./components/ProtectedRoute";

// --- Rotte Pubbliche ---
import Home from "./pages/public/Home";
import AboutUs from "./pages/public/AboutUs";
import Contact from "./pages/public/Contact";
import MultiStepForm from "./pages/public/MultiStepForm";
import Login from "./pages/private/Login";

// --- Componenti Admin (Esistenti) ---
import DashboardAdmin from "./pages/private/adminPages/DashboardAdmin";
import AdminHome from "./pages/private/adminPages/AdminHome";
import Users from "./pages/private/adminPages/Users";
import CreateUser from "./pages/private/adminPages/CreateUser";
import ContractsAdmin from "./pages/private/adminPages/ContractsAdmin";
import AssignmentsAdmin from "./pages/private/adminPages/AssignmentsAdmin";
// Componente condiviso per le Valutazioni AI
import EvaluationsAI from "./pages/private/EvaluationsAI"; 

// --- Componenti Agente (Basati sulla tua struttura) ---
import DashboardAgent from "./pages/private/agentPages/DashboardAgent"; // Layout principale
import AgentHome from "./pages/private/agentPages/AgentHome"; // Home/Statistiche Agente
import ContractsAgent from "./pages/private/agentPages/ContractsAgent"; // Contratti Agente
import AssignmentsAgent from "./pages/private/agentPages/AssignmentsAgent"; // Incarichi Agente
// RIMOZIONE: import ValutazioniAIAgente non trovato nel percorso


function App() {
  return (
    <>
      <Routes>
        {/* --- Rotte Pubbliche --- */}
        <Route path="/" element={<Home />} />
        <Route path="/form" element={<MultiStepForm />} />
        <Route path="/about" element={<AboutUs />} />
        <Route path="/contact" element={<Contact />} />
        <Route path="/login" element={<Login />} />

        {/* ========================================================= */}
        {/* --- ROTTE ADMIN --- */}
        {/* ========================================================= */}
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
          {/* Rotta Admin usa il componente condiviso */}
          <Route path="evaluationsAI" element={<EvaluationsAI />} /> 
        </Route>

        {/* ========================================================= */}
        {/* --- ROTTE AGENTE --- */}
        {/* ========================================================= */}
        <Route
          path="/agente"
          element={
            <ProtectedRoute 
                // Il ruolo esatto definito nel tuo ProtectedRoute è "agente"
                role="agente" 
            >
              {/* Usa il layout DashboardAgent (DashboardAgent.tsx) */}
              <DashboardAgent /> 
            </ProtectedRoute>
            
          }

          
        >
          

          {/* 1. Home/Dashboard Agente */}
          <Route index element={<AgentHome />} /> 

          {/* 2. Contratti Agente */}
          <Route path="contracts" element={<ContractsAgent />} /> 

          {/* 3. Incarichi Agente */}
          <Route path="assignments" element={<AssignmentsAgent />} /> 

          {/* 4. Valutazioni AI Agente: usa il componente condiviso EvaluationsAI */}
          <Route path="evaluationsAI" element={<EvaluationsAI />} /> 
          
          {/* Le rotte Users e CreateUser NON sono definite qui. */}
        </Route>
      </Routes>
    </>
  );
}

export default App;