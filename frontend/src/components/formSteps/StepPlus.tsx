import { useState, forwardRef, useImperativeHandle } from "react";
import { useFormContext } from "../../context/FormContext";
import CardInput from "../CardInput";
import BalconeImg from "../../assets/img/form-img/balcone.webp";
import TerrazzoImg from "../../assets/img/form-img/terrazzo.webp";
import GiardinoImg from "../../assets/img/form-img/giardino.webp";
import GarageImg from "../../assets/img/form-img/garage.webp";
import AscensoreImg from "../../assets/img/form-img/ascensore.webp";
import CantinaImg from "../../assets/img/form-img/cantina.webp";

export interface StepPlusRef {
  validate: () => boolean;
}

const FEATURES = [
  { text: "Balcone", img: BalconeImg },
  { text: "Terrazzo", img: TerrazzoImg },
  { text: "Giardino privato", img: GiardinoImg },
  { text: "Box garage", img: GarageImg },
  { text: "Ascensore", img: AscensoreImg },
  { text: "Cantina", img: CantinaImg },
];

const StepPlus = forwardRef<StepPlusRef>((props, ref) => {
  const { formData, setFormData } = useFormContext();

  const [selectedFeatures, setSelectedFeatures] = useState<string[]>(
    formData.features || []
  );

  const toggleFeature = (feature: string) => {
    let updated: string[];

    if (selectedFeatures.includes(feature)) {
      updated = selectedFeatures.filter((f) => f !== feature);
    } else {
      updated = [...selectedFeatures, feature];
    }

    setSelectedFeatures(updated);
    setFormData((prev) => ({ ...prev, features: updated }));
  };

  useImperativeHandle(ref, () => ({
    // Questo step non ha validazioni
    validate: () => true,
  }));

  return (
    <div className="step">
      <h2>Spazi esterni e plus</h2>
      <p>Seleziona se presenti</p>

      <div className="card-group-input">
        {FEATURES.map(({ text, img }) => (
          <CardInput
            key={text}
            text={text}
            img={img}
            isActive={selectedFeatures.includes(text)}
            onClick={() => toggleFeature(text)}
          />
        ))}
      </div>
    </div>
  );
});

export default StepPlus;
