import NavbarPublic from "../components/NavbarPublic";
import Footer from "../components/Footer";

export default function PublicLayout({ children }: { children: React.ReactNode }) {

  return (
    <>
      <NavbarPublic/>
      {children}
      <Footer />
    </>
  );
}
