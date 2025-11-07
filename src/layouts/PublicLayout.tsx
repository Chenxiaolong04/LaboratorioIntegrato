import { useLocation } from "react-router-dom";
import Navbar from "../components/Navbar";
import Footer from "../components/Footer";

export default function PublicLayout({ children }: { children: React.ReactNode }) {
  const location = useLocation();
  const hideFooter = location.pathname === "/login"; // controlla la route

  return (
    <>
      <Navbar />
      {children}
      {!hideFooter && <Footer />} {/* mostra solo se non è la login */}
    </>
  );
}
