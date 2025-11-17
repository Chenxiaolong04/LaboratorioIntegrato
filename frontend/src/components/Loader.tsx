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
