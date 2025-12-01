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

/**
 * StepPlus Component
 *
 * @description
 * This component represents the optional features step in a multi-step form.
 * Users can select various external spaces or additional amenities related to the property.
 * The selected values are stored locally as component state and synchronized with the global
 * form data through `FormContext`. This step does not require validation, and therefore the
 * exposed `validate()` method always returns `true`.
 *
 * @component
 * @param {any} props - No props are required for this component.
 * @param {React.Ref<StepPlusRef>} ref - A forwarded ref exposing the `validate` function.
 * @returns {JSX.Element} The JSX structure for the extra features selection step.
 */
const StepPlus = forwardRef<StepPlusRef>((props, ref) => {
  const { formData, setFormData } = useFormContext();

  const [selectedFeatures, setSelectedFeatures] = useState<string[]>(
    formData.features || []
  );

  /**
   * Toggles the selection of a feature. If the feature is already selected, it is removed.
   * Otherwise, it is added to the selected features array. Updates both component state
   * and global form state.
   *
   * @param {string} feature - The feature to toggle.
   * @returns {void}
   */
  const toggleFeature = (feature: string): void => {
    let updated: string[];

    if (selectedFeatures.includes(feature)) {
      updated = selectedFeatures.filter((f) => f !== feature);
    } else {
      updated = [...selectedFeatures, feature];
    }

    setSelectedFeatures(updated);
    setFormData((prev) => ({ ...prev, features: updated }));
  };

  /**
   * Exposes the validate method to parent components through ref.
   *
   * @function validate
   * @description
   * This step does not require any validation, so this function always returns `true`.
   *
   * @returns {boolean} Always `true`.
   */
  useImperativeHandle(ref, () => ({
    validate: () => true,
  }));

  return (
    <div className="step">
      <h2>Spazi esterni e plus</h2>
      <p>Select the features if available</p>

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
