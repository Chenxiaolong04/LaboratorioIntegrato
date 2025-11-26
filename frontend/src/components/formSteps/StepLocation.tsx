import React, {
  useState,
  useImperativeHandle,
  forwardRef,
  useRef,
} from "react";
import InputGroup from "../InputGroup";
import { useFormContext } from "../../context/FormContext";
import { validateAddress, type AddressSuggestion } from "../../services/api";

export interface StepLocationRef {
  validate: () => boolean;
}

interface StepLocationProps {
  error: string;
  setError: React.Dispatch<React.SetStateAction<string>>;
}

// Debounce tipizzato
function debounce<Args extends unknown[]>(
  func: (...args: Args) => void,
  delay: number
) {
  let timer: ReturnType<typeof setTimeout>;
  return (...args: Args) => {
    clearTimeout(timer);
    timer = setTimeout(() => func(...args), delay);
  };
}

const StepLocation = forwardRef<StepLocationRef, StepLocationProps>(
  ({ error, setError }, ref) => {
    const { formData, setFormData } = useFormContext();

    const [address, setAddress] = useState(formData.address || "");
    const [city, setCity] = useState(formData.city || "");
    const [cap, setCap] = useState(formData.cap || "");
    const [civico, setCivico] = useState("");

    const [suggestions, setSuggestions] = useState<AddressSuggestion[]>([]);
    const [selectedAddress, setSelectedAddress] =
      useState<AddressSuggestion | null>(null);
    const [isLoading, setIsLoading] = useState(false);

    const fetchSuggestions = async (via: string, citta: string) => {
      if (!via || !citta) {
        setSuggestions([]);
        return;
      }

      setIsLoading(true);
      try {
        const res = await validateAddress(via, citta);

        if (res.valid && res.suggestions.length > 0) {
          setSuggestions(res.suggestions);
          setError("");
        } else {
          setSuggestions([]);
          setError("Nessun indirizzo trovato. Controlla via e città");
        }
      } catch (err) {
        console.error(err);
        setError(
          "Impossibile contattare il server per la validazione dell'indirizzo"
        );
        setSuggestions([]);
      } finally {
        setIsLoading(false);
      }
    };

    const debouncedFetch = useRef(debounce(fetchSuggestions, 400)).current;

    const handleAddressChange = (e: React.ChangeEvent<HTMLInputElement>) => {
      const value = e.target.value;
      setAddress(value);
      setSelectedAddress(null);

      if (value.length > 3 && city.length > 2) {
        debouncedFetch(value, city);
      } else {
        setSuggestions([]);
      }
    };

    const handleCityChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
      const value = e.target.value;

      setCity(value);
      setSelectedAddress(null);

      setFormData((prev) => ({
        ...prev,
        city: value,
      }));

      if (value.length > 2 && address.length > 3) {
        debouncedFetch(address, value);
      } else {
        setSuggestions([]);
      }
    };

    useImperativeHandle(ref, () => ({
      validate: () => {
        if (!address) {
          setError("Inserisci l'indirizzo");
          return false;
        }
        if (!city) {
          setError("Inserisci la città");
          return false;
        }
        if (!selectedAddress) {
          setError(
            "Seleziona un indirizzo o citta validi dai suggerimenti o riscrivi l'indirizzo correttamente"
          );
          return false;
        }

        setError("");
        return true;
      },
    }));

    return (
      <div className="step">
        <h2>Dove si trova l'immobile da valutare?</h2>

        <InputGroup
          name="address"
          label="Indirizzo dell'immobile"
          type="text"
          placeholder="Es: Via Roma"
          autoComplete="street-address"
          value={address}
          required
          onChange={handleAddressChange}
        />

        <div className="form-group">
          <label htmlFor="city">Città *</label>
          <select
            id="city"
            name="city"
            value={city}
            required
            onChange={handleCityChange}
          >
            <option value="">Seleziona una città</option>
            <option value="Torino">Torino</option>
            <option value="Asti">Asti</option>
            <option value="Alessandria">Alessandria</option>
            <option value="Cuneo">Cuneo</option>
          </select>
        </div>

        {(suggestions.length > 0 || isLoading) && (
          <ul className="suggestions-list">
            {isLoading && <li className="loading-spinner"></li>}

            {!isLoading &&
              suggestions.map((s) => (
                <li
                  key={s.displayName}
                  className="suggestion-item"
                  onClick={() => {
                    const selectedCivico = s.civico || "";
                    const fullAddress =
                      s.via + (selectedCivico ? ` ${selectedCivico}` : "");

                    setSelectedAddress(s);
                    setAddress(fullAddress);
                    setCivico(selectedCivico);
                    setCap(s.cap);

                    setFormData((prev) => ({
                      ...prev,
                      address: fullAddress,
                      cap: s.cap,
                    }));

                    setSuggestions([]);
                  }}
                >
                  {s.displayName}
                </li>
              ))}
          </ul>
        )}

        {error && <p className="error-message">{error}</p>}
      </div>
    );
  }
);

export default StepLocation;
