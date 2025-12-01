import React from "react";

/**
 * Static data for the footer.
 */
const CURRENT_YEAR = new Date().getFullYear();
const BRAND_NAME = "Immobiliaris";

/**
 * FooterPrivate component.
 *
 * Renders a simple private footer with a logo and brand name.
 * Styled using inline CSS for layout and responsiveness.
 *
 * @function FooterPrivate
 * @returns {React.JSX.Element} The footer element for authenticated/private pages.
 */
export default function FooterPrivate(): React.JSX.Element {

  const footerStyle: React.CSSProperties = {
    backgroundColor: '#000c1e',
    color: '#ffffff',
    padding: '4.5rem',           // padding base
    display: 'flex',
    justifyContent: 'space-between',
    alignItems: 'center',
    width: '100%',
    boxSizing: 'border-box',
    borderTop: '1px solid rgba(255, 255, 255, 0.1)',
    flexWrap: 'wrap',            // permette di andare a capo su mobile
    gap: '1rem'                  // distanza tra elementi quando vanno a capo
  };

  const logoContainerStyle: React.CSSProperties = {
    display: 'flex',
    alignItems: 'center',
    gap: '0.75rem',
    fontSize: '1.2rem',
    fontWeight: '700',
    color: '#ffffff',
    textDecoration: 'none'
  };

  const logoImageStyle: React.CSSProperties = {
    width: '50px',
    height: '50px',
  };

  return (
    <footer style={footerStyle}>
      <div style={logoContainerStyle}>
        <img 
          src="./logo.svg" 
          alt={`Logo ${BRAND_NAME}`} 
          style={logoImageStyle}
        />
        <span>{BRAND_NAME}</span>
      </div>
    </footer>
  );
}
