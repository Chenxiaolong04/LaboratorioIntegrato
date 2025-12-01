import NavbarPublic from "../components/NavbarPublic";
import Footer from "../components/Footer";

/**
 * PublicLayout component.
 *
 * Wraps public pages with a navbar and footer.
 * Used for routes accessible without authentication.
 *
 * @param {Object} props - Component props
 * @param {React.ReactNode} props.children - The page content to render inside the layout
 * @returns {JSX.Element} The layout with navbar, main content, and footer
 */
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
