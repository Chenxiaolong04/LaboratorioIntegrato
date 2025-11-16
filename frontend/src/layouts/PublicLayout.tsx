import NavbarPublic from "../components/NavbarPublic";
import Footer from "../components/Footer";

export default function PublicLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <>
      <NavbarPublic />
      <main>{children}</main>
      <Footer />
    </>
  );
}
