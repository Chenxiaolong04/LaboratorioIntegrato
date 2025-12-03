import NavbarPrivate from "../components/NavbarPrivate";

export default function PrivateLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <>
      <NavbarPrivate />
      <main>{children}</main>
    </>
  );
}
