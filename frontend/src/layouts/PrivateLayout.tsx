import NavbarPrivate from "../components/NavbarPrivate";
import FooterPrivate from "../components/FooterPrivate";

/**
 * PrivateLayout component.
 *
 * Wraps private pages with a navbar and footer.
 * Used for authenticated user routes.
 *
 * @param {Object} props - Component props
 * @param {React.ReactNode} props.children - The page content to render inside the layout
 * @returns {JSX.Element} The layout with navbar, main content, and footer
 */
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
