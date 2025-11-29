import NavbarPrivate from "../components/NavbarPrivate";
import FooterPrivate from "../components/FooterPrivate";

export default function PrivateLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <>
      <NavbarPrivate />
      <main>{children}</main>
      <FooterPrivate/>
    </>
  );
}
