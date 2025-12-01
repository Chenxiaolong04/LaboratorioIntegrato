/**
 * Loader component.
 *
 * Displays a full-screen loading overlay with a logo and a loading bar.
 * Typically used while waiting for data fetching or page transitions.
 *
 * @function Loader
 * @returns {JSX.Element} A loader overlay element with logo and animated bar.
 */
export default function Loader() {
  return (
    <div className="loader-overlay">
      <div className="loader-content">
        <img src='./logo.svg' alt="Logo" className="loader-logo" />
        <div className="loader-bar"></div>
      </div>
    </div>
  );
}
