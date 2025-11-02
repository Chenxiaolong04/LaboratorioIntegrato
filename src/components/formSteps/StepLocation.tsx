import React, { useEffect } from "react";
import TestImage from "../../assets/img/test-image.jpg";
import InputGroup from "../InputGroup";
import { useState } from "react";

const StepLocation: React.FC = () => {
  // const [locationData, setLocationData] = useState({});
  const [address, setAddress] = useState("");
  const [cap, setCap] = useState(0);
  const [provincia, setProvincia] = useState("");

  // bisogna creare il context LocationData per condividerlo con gli altri step e per mandare alla fine al backend un unico oggetto
  // useEffect(() => {
  //   setLocationData({ address, cap, provincia });
  // }, [address, cap, provincia]);

  return (
    <div className="step">
      <h2>Dove si trova l'immobile da valutare?</h2>

      <InputGroup
        label="Indirizzo dell'immobile"
        type="text"
        placeholder="Es: Via Roma"
        autoComplete="street-address"
        onChange={(e) => setAddress(e.target.value)}
      />
      <InputGroup
        label="CAP"
        type="number"
        placeholder="Es: 10015"
        autoComplete="postal-code"
        onChange={(e) => setAddress(e.target.value)}
      />
      <InputGroup
        label="Provincia"
        type="text"
        placeholder="Es: Torino"
        autoComplete="address-level1"
        onChange={(e) => setAddress(e.target.value)}
      />

      <img src={TestImage} alt="" />
    </div>
  );
};

export default StepLocation;
