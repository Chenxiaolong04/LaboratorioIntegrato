import { useEffect, useState } from "react";
import {
  FaChevronRight,
  FaChevronLeft,
  FaHome,
  FaTools,
  FaPlus,
  FaEdit,
} from "react-icons/fa";
import {
  FaLocationDot,
  FaFilterCircleXmark,
  FaMoneyBill,
} from "react-icons/fa6";
import { RiCustomSize } from "react-icons/ri";
import Button from "../../../components/Button";

const fakeProperties = [
  {
    id: 1,
    type: "Appartamento",
    name: "Mario Rossi",
    address: "Via Saluzzo 8, Torino",
    state: "Nuovo",
    price: 120000,
    surface: 80,
    plus: ["Box auto", "Balcone"],
    description: "Luminoso appartamento al secondo piano con ottime finiture.",
    image: "https://picsum.photos/seed/house1/1200/800",
  },
  {
    id: 2,
    type: "Villa",
    name: "Lucia Bianchi",
    address: "Corso Vittorio Emanuele 12, Torino",
    state: "Ottimo stato",
    price: 350000,
    surface: 200,
    plus: ["Giardino", "Terrazzo"],
    description: "Villa con giardino privato e terrazzo panoramico.",
    image: "https://picsum.photos/seed/house2/1200/800",
  },
  {
    id: 3,
    type: "Loft",
    name: "Giovanni Verdi",
    address: "Via Roma 25, Asti",
    state: "Buono",
    price: 180000,
    surface: 100,
    plus: ["Ascensore"],
    description: "Loft moderno in zona centrale, ottima esposizione.",
    image: "https://picsum.photos/seed/house3/1200/800",
  },
  {
    id: 4,
    type: "Attico",
    name: "Anna Neri",
    address: "Piazza Garibaldi 3, Alessandria",
    state: "Da ristrutturare",
    price: 250000,
    surface: 150,
    plus: ["Terrazzo", "Box auto"],
    description: "Attico con terrazzo e vista panoramica, da ristrutturare.",
    image: "https://picsum.photos/seed/house4/1200/800",
  },

  {
    id: 5,
    type: "Appartamento",
    name: "Paolo Neri",
    address: "Via Po 14, Torino",
    state: "Ottimo stato",
    price: 210000,
    surface: 95,
    plus: ["Balcone"],
    description:
      "Appartamento ristrutturato recentemente in zona universitaria.",
    image: "https://picsum.photos/seed/house1/1200/800",
  },
  {
    id: 6,
    type: "Villa",
    name: "Chiara Rosa",
    address: "Via Monviso 6, Cuneo",
    state: "Buono",
    price: 420000,
    surface: 240,
    plus: ["Giardino", "Box auto"],
    description: "Villa indipendente con ampio giardino privato.",
    image: "https://picsum.photos/seed/house2/1200/800",
  },
  {
    id: 7,
    type: "Loft",
    name: "Giorgio Blu",
    address: "Via XX Settembre 9, Torino",
    state: "Nuovo",
    price: 160000,
    surface: 70,
    plus: ["Ascensore"],
    description: "Loft moderno con design industriale.",
    image: "https://picsum.photos/seed/house3/1200/800",
  },
  {
    id: 8,
    type: "Appartamento",
    name: "Sara Magni",
    address: "Via Dante 5, Asti",
    state: "Buono",
    price: 135000,
    surface: 75,
    plus: ["Terrazzo"],
    description: "Appartamento luminoso con terrazzo abitabile.",
    image: "https://picsum.photos/seed/house4/1200/800",
  },
  {
    id: 9,
    type: "Attico",
    name: "Luca Viola",
    address: "Corso Francia 220, Torino",
    state: "Ottimo stato",
    price: 370000,
    surface: 140,
    plus: ["Terrazzo", "Ascensore"],
    description: "Attico panoramico con vista sulla collina torinese.",
    image: "https://picsum.photos/seed/house1/1200/800",
  },
  {
    id: 10,
    type: "Villa",
    name: "Giulia Serra",
    address: "Via Milano 40, Alessandria",
    state: "Da ristrutturare",
    price: 280000,
    surface: 260,
    plus: ["Giardino"],
    description: "Ampia villa con potenziale, da rinnovare.",
    image: "https://picsum.photos/seed/house2/1200/800",
  },
  {
    id: 11,
    type: "Appartamento",
    name: "Davide Basile",
    address: "Via Garibaldi 18, Cuneo",
    state: "Nuovo",
    price: 190000,
    surface: 85,
    plus: ["Ascensore"],
    description: "Appartamento moderno in stabile di nuova costruzione.",
    image: "https://picsum.photos/seed/house3/1200/800",
  },
  {
    id: 12,
    type: "Loft",
    name: "Martina Gallo",
    address: "Corso Laghi 30, Avigliana",
    state: "Buono",
    price: 165000,
    surface: 90,
    plus: ["Box auto"],
    description: "Loft spazioso vicino al centro storico.",
    image: "https://picsum.photos/seed/house4/1200/800",
  },
  {
    id: 13,
    type: "Appartamento",
    name: "Stefano Moretti",
    address: "Via Nizza 120, Torino",
    state: "Ottimo stato",
    price: 230000,
    surface: 100,
    plus: ["Balcone", "Ascensore"],
    description: "Appartamento in zona comoda ai servizi.",
    image: "https://picsum.photos/seed/house1/1200/800",
  },
  {
    id: 14,
    type: "Attico",
    name: "Francesca Riva",
    address: "Via Alfieri 3, Asti",
    state: "Buono",
    price: 260000,
    surface: 130,
    plus: ["Terrazzo"],
    description: "Attico centrale con ampia terrazza.",
    image: "https://picsum.photos/seed/house2/1200/800",
  },
  {
    id: 15,
    type: "Villa",
    name: "Alessandro De Luca",
    address: "Via Cascina 17, Cuneo",
    state: "Ottimo stato",
    price: 400000,
    surface: 230,
    plus: ["Giardino", "Box auto"],
    description: "Villa elegante immersa nel verde.",
    image: "https://picsum.photos/seed/house3/1200/800",
  },
  {
    id: 16,
    type: "Appartamento",
    name: "Elisa Romano",
    address: "Corso Galileo Ferraris 55, Torino",
    state: "Nuovo",
    price: 270000,
    surface: 110,
    plus: ["Ascensore", "Balcone"],
    description: "Ampio appartamento vicino al Politecnico.",
    image: "https://picsum.photos/seed/house4/1200/800",
  },
  {
    id: 17,
    type: "Loft",
    name: "Marco Gialli",
    address: "Via Cavour 10, Alessandria",
    state: "Da ristrutturare",
    price: 120000,
    surface: 85,
    plus: ["Ascensore"],
    description: "Loft open space da personalizzare.",
    image: "https://picsum.photos/seed/house1/1200/800",
  },
  {
    id: 18,
    type: "Villa",
    name: "Simona Righi",
    address: "Strada del Bosco 4, Asti",
    state: "Buono",
    price: 380000,
    surface: 210,
    plus: ["Giardino", "Terrazzo"],
    description: "Villa con vista panoramica sulle colline.",
    image: "https://picsum.photos/seed/house2/1200/800",
  },
  {
    id: 19,
    type: "Appartamento",
    name: "Tommaso Ricci",
    address: "Via Madama Cristina 80, Torino",
    state: "Ottimo stato",
    price: 175000,
    surface: 70,
    plus: ["Balcone"],
    description: "Bilocale completamente ristrutturato.",
    image: "https://picsum.photos/seed/house3/1200/800",
  },
  {
    id: 20,
    type: "Attico",
    name: "Sofia Leone",
    address: "Via San Secondo 9, Torino",
    state: "Nuovo",
    price: 410000,
    surface: 160,
    plus: ["Terrazzo", "Ascensore"],
    description: "Attico di lusso con finiture moderne.",
    image: "https://picsum.photos/seed/house4/1200/800",
  },
];

export default function PropertiesAdmin() {
  const [collapsed, setCollapsed] = useState(true);

  const [filters, setFilters] = useState({
    city: "",
    type: "",
    state: "",
    price: "",
    surface: "",
    plus: [] as string[],
  });

  const [selectedProperty, setSelectedProperty] = useState<
    (typeof fakeProperties)[0] | null
  >(null);

  const resetFilters = () =>
    setFilters({
      city: "",
      type: "",
      state: "",
      price: "",
      surface: "",
      plus: [],
    });

  const getIconColor = (value: string | string[]) =>
    Array.isArray(value)
      ? value.length > 0
        ? "#348AA7"
        : "#333"
      : value
      ? "#348AA7"
      : "#333";

  const filteredProperties = fakeProperties.filter((p) => {
    if (filters.city && !p.address.includes(filters.city)) return false;
    if (filters.type && p.type !== filters.type) return false;
    if (filters.state && p.state !== filters.state) return false;
    if (filters.price && p.price > Number(filters.price)) return false;
    if (filters.surface && p.surface < Number(filters.surface)) return false;
    if (
      filters.plus.length > 0 &&
      !filters.plus.every((pl) => p.plus.includes(pl))
    )
      return false;

    return true;
  });

  useEffect(() => {
    if (selectedProperty) {
      document.body.classList.add("no-scroll");
    } else {
      document.body.classList.remove("no-scroll");
    }
  }, [selectedProperty]);

  return (
    <section className="properties-admin">
      <aside className={`sidebar ${collapsed ? "collapsed" : ""}`}>
        <button
          title="Espandi menu filtri"
          className="collapse-btn"
          onClick={() => setCollapsed(!collapsed)}
        >
          {collapsed ? (
            <FaChevronRight size={20} />
          ) : (
            <FaChevronLeft size={20} />
          )}
        </button>

        <div className="sidebar-title">
          <h3>Filtri</h3>
        </div>

        <div className="sidebar-filters">
          {/* Filtri come prima */}
          <div className="filter-container">
            <FaLocationDot size={30} color={getIconColor(filters.city)} />
            <div className="filter-info">
              <label>
                <h3>Città</h3>
                <select
                  value={filters.city}
                  onChange={(e) =>
                    setFilters({ ...filters, city: e.target.value })
                  }
                >
                  <option value="">Tutte</option>
                  <option value="Torino">Torino</option>
                  <option value="Asti">Asti</option>
                  <option value="Alessandria">Alessandria</option>
                  <option value="Cuneo">Cuneo</option>
                </select>
              </label>
            </div>
          </div>
          <div className="filter-container">
            <FaHome size={30} color={getIconColor(filters.type)} />
            <div className="filter-info">
              <label>
                <h3>Tipo</h3>
                <select
                  value={filters.type}
                  onChange={(e) =>
                    setFilters({ ...filters, type: e.target.value })
                  }
                >
                  <option value="">Tutti</option>
                  <option>Appartamento</option>
                  <option>Villa</option>
                  <option>Loft</option>
                  <option>Attico</option>
                </select>
              </label>
            </div>
          </div>
          <div className="filter-container">
            <FaTools size={30} color={getIconColor(filters.state)} />
            <div className="filter-info">
              <label>
                <h3>Stato</h3>
                <select
                  value={filters.state}
                  onChange={(e) =>
                    setFilters({ ...filters, state: e.target.value })
                  }
                >
                  <option value="">Tutti</option>
                  <option>Nuovo</option>
                  <option>Ottimo stato</option>
                  <option>Buono</option>
                  <option>Da ristrutturare</option>
                </select>
              </label>
            </div>
          </div>
          <div className="filter-container">
            <FaMoneyBill size={30} color={getIconColor(filters.price)} />
            <div className="filter-info">
              <label>
                <h3>Prezzo max</h3>
                <select
                  value={filters.price}
                  onChange={(e) =>
                    setFilters({ ...filters, price: e.target.value })
                  }
                >
                  <option value="">Tutti</option>
                  <option value="100000">100.000€</option>
                  <option value="150000">150.000€</option>
                  <option value="200000">200.000€</option>
                  <option value="300000">300.000€</option>
                </select>
              </label>
            </div>
          </div>
          <div className="filter-container">
            <RiCustomSize size={30} color={getIconColor(filters.surface)} />
            <div className="filter-info">
              <label>
                <h3>Superficie min</h3>
                <select
                  value={filters.surface}
                  onChange={(e) =>
                    setFilters({ ...filters, surface: e.target.value })
                  }
                >
                  <option value="">Tutte</option>
                  <option value="50">50 m²</option>
                  <option value="80">80 m²</option>
                  <option value="100">100 m²</option>
                  <option value="150">150 m²</option>
                </select>
              </label>
            </div>
          </div>
          <div className="filter-container">
            <FaPlus size={30} color={getIconColor(filters.plus)} />
            <div className="filter-info">
              <label>
                <h3>Plus</h3>

                <div className="checkbox-group">
                  {[
                    "Box auto",
                    "Giardino",
                    "Balcone",
                    "Terrazzo",
                    "Ascensore",
                  ].map((plusItem) => (
                    <label key={plusItem} className="checkbox-item">
                      <input
                        type="checkbox"
                        checked={filters.plus.includes(plusItem)}
                        onChange={(e) => {
                          const updated = e.target.checked
                            ? [...filters.plus, plusItem]
                            : filters.plus.filter((p) => p !== plusItem);

                          setFilters({ ...filters, plus: updated });
                        }}
                      />
                      <span>{plusItem}</span>
                    </label>
                  ))}
                </div>
              </label>
            </div>
          </div>
        </div>

        <button
          title="Rimuovi tutti i filtri"
          className="remove-filters"
          onClick={resetFilters}
        >
          <FaFilterCircleXmark size={30} color="red" />
        </button>
      </aside>

      <div
        className="properties"
        style={{
          marginRight: !selectedProperty ? 0 : undefined,
        }}
      >
        <div className="properties-container">
          {filteredProperties.map((property) => (
            <div
              key={property.id}
              className="card-property"
              style={{
                backgroundImage: `url(${property.image})`,
                backgroundSize: "cover",
                backgroundPosition: "center",
              }}
            >
              <div className="type">
                <h4>{property.type}</h4>
              </div>
              <div className="info-property">
                <div className="info-first">
                  <div>
                    <h3>{property.name}</h3>
                    <h4>
                      <FaLocationDot /> {property.address}
                    </h4>
                  </div>
                  <button className="edit-btn" title="Modifica dati immobile">
                    <FaEdit size={24} />
                  </button>
                </div>
                <Button
                  onClick={() => setSelectedProperty(property)}
                  className="lightblu"
                >
                  Dettagli
                </Button>
              </div>
            </div>
          ))}
        </div>
      </div>

      {/* Sidebar dettagli immobile */}
      {selectedProperty && (
        <aside className="property-details-sidebar">
          <button
            title="Chiudi dettagli"
            className="close-details"
            onClick={() => setSelectedProperty(null)}
          >
            ✕
          </button>
          <div className="details-image">
            <img src={selectedProperty.image} alt={selectedProperty.name} />
          </div>

          <div className="details-content">
            <h2>{selectedProperty.type}</h2>

            <h3>{selectedProperty.name}</h3>

            <p className="address">
              <FaLocationDot /> {selectedProperty.address}
            </p>

            <div className="details-grid">
              <p>
                <strong>Stato:</strong> {selectedProperty.state}
              </p>
              <p>
                <strong>Prezzo:</strong>{" "}
                {selectedProperty.price.toLocaleString()} €
              </p>
              <p>
                <strong>Superficie:</strong> {selectedProperty.surface} m²
              </p>
              <p>
                <strong>Plus:</strong> {selectedProperty.plus.join(", ")}
              </p>
            </div>

            <p className="description">{selectedProperty.description}</p>

            <Button className="lightblu">Modifica immobile</Button>
          </div>
        </aside>
      )}
    </section>
  );
}
