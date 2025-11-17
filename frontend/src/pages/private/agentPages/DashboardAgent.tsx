import { Outlet, Link, useLocation } from "react-router-dom"; 
import PrivateLayout from "../../../layouts/PrivateLayout";
import { useAuth } from "../../../context/AuthContext";

export default function DashboardAgent() {
  const { user } = useAuth();
  const location = useLocation();

  // Definiamo i link usando emoji per le icone
  const navLinks = [
    { to: "/agente", label: "Dashboard", icon: "" },
    { to: "assignments", label: "I miei Incarichi", icon: "" },
    { to: "contracts", label: "I miei Contratti", icon: "" },
    { to: "evaluationsAI", label: "Valutazioni AI", icon: "" },
  ];

  return (
    <PrivateLayout>
      <div className="flex min-h-screen bg-gray-50">
        
        {/* --- Sidebar di Navigazione Agente (Design Migliorato) --- */}
        <nav className="w-64 bg-white border-r border-gray-200 flex flex-col shadow-lg">
          
          <div className="p-6 border-b border-gray-200">
            <h1 className="text-2xl font-bold text-indigo-700">AREA AGENTE</h1>
          </div>
          
          <ul className="flex flex-col p-4 space-y-2">
            {navLinks.map((link) => {
              // Determina se il link è attivo per lo stile
              const isActive = location.pathname === link.to || (link.to !== "/agente" && location.pathname.startsWith("/agente/" + link.to));

              return (
                <li key={link.to}>
                  <Link
                    to={link.to}
                    className={`flex items-center space-x-3 p-3 rounded-xl transition duration-200 ease-in-out font-medium 
                      ${isActive 
                        ? 'bg-indigo-100 text-indigo-700 shadow-sm' // Stile attivo
                        : 'text-gray-600 hover:bg-gray-100' // Stile inattivo
                      }`
                    }
                  >
                    {/* Utilizzo della prop 'icon' come stringa emoji */}
                    <span className="text-lg">{link.icon}</span> 
                    <span>{link.label}</span>
                  </Link>
                </li>
              );
            })}
          </ul>

          <div className="mt-auto p-4 text-center border-t border-gray-200">
             <p className="text-sm text-gray-400">© 2025 Azienda. Tutti i diritti riservati.</p>
          </div>

        </nav>

        {/* --- Contenuto Principale --- */}
        <div className="flex-1 flex flex-col overflow-hidden">
          
          <header className="bg-white shadow-md p-4 flex justify-between items-center z-10">
            <h1 className="text-3xl font-semibold text-gray-800">
              Bentornato, <span className="text-indigo-600">{user?.name || 'Agente'}</span>!
            </h1>
            {/* Qui puoi aggiungere un avatar o un pulsante Logout */}
          </header>

          <main className="flex-1 overflow-y-auto p-8">
            <Outlet />
          </main>
        </div>
      </div>
    </PrivateLayout>
  );
}