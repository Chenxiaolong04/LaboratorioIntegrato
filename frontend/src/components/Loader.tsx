import Logo from "../assets/img/logo.svg";

export default function Loader() {
  return (
    <div className="loader-overlay">
      <div className="loader-content">
        <img src={Logo} alt="Logo" className="loader-logo" />
        <div className="loader-bar"></div>
      </div>
    </div>
  );
}
