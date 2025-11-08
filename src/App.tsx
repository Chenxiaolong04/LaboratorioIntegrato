import "./assets/styles/main.scss";
import Home from "./pages/public/Home";
import { Route, Routes } from "react-router-dom";
import AboutUs from "./pages/public/AboutUs";
import Contact from "./pages/public/Contact";
import MultiStepForm from "./pages/public/MultiStepForm";
import Login from "./pages/private/Login";

function App() {
  return (
    <>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/form" element={<MultiStepForm />} />
        <Route path="/about" element={<AboutUs />} />
        <Route path="/contact" element={<Contact />} />
        <Route path="/login" element={<Login />} />
      </Routes>
    </>
  );
}

export default App;
