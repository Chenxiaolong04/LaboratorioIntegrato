import { useEffect, useMemo, useState } from "react";
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
  FaX,
} from "react-icons/fa6";
import { RiCustomSize } from "react-icons/ri";
import Button from "../../../components/Button";
import InputGroup from "../../../components/InputGroup";
import { getImmobili, type Immobile } from "../../../services/api";

export default function PropertiesAdmin() {
  const [collapsed, setCollapsed] = useState(true);
  const [isEditing, setIsEditing] = useState(false);
  const [editData, setEditData] = useState<Immobile | null>(null);

  const [immobili, setImmobili] = useState<Immobile[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [selectedProperty, setSelectedProperty] = useState<Immobile | null>(
    null
  );

  const [filters, setFilters] = useState({
    city: "",
    type: "",
    state: "",
    price: "",
    surface: "",
    plus: [] as string[],
  });

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

  const getPlusArray = (p: Immobile) => {
    return [
      p.ascensore && "Ascensore",
      p.garage && "Garage",
      p.giardino && "Giardino",
      p.balcone && "Balcone",
      p.terrazzo && "Terrazzo",
      p.cantina && "Cantina",
    ].filter(Boolean) as string[];
  };

  const filteredProperties = immobili.filter((p) => {
    if (filters.city && !p.citta.includes(filters.city)) return false;
    if (filters.type && p.tipologia !== filters.type) return false;
    if (filters.state && p.condizioni !== filters.state) return false;
    if (filters.price && p.prezzo > Number(filters.price)) return false;
    if (filters.surface && p.metratura < Number(filters.surface)) return false;
    const propertyPlus = getPlusArray(p);

    if (
      filters.plus.length > 0 &&
      !filters.plus.every((pl) => propertyPlus.includes(pl))
    )
      return false;

    return true;
  });

  const isMobile = useMemo(() => window.innerWidth <= 768, []);

  useEffect(() => {
    const shouldBlockScroll =
      (isMobile && (selectedProperty || isEditing)) || (!isMobile && isEditing);

    if (shouldBlockScroll) {
      document.body.classList.add("no-scroll");
    } else {
      document.body.classList.remove("no-scroll");
    }

    return () => {
      document.body.classList.remove("no-scroll");
    };
  }, [selectedProperty, isEditing, isMobile]);

  const imageMap: Record<string, string> = {
    Appartamento:
      "https://images.unsplash.com/photo-1545324418-cc1a3fa10c00?q=80&w=735&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
    Loft: "https://plus.unsplash.com/premium_photo-1661950439212-558fa5cc82e0?q=80&w=1151&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
    Villa:
      "https://images.unsplash.com/photo-1580587771525-78b9dba3b914?q=80&w=1074&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
    Attico:
      "https://images.unsplash.com/photo-1724166483767-1a42883ccde5?q=80&w=1171&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
  };

  const getPropertyImage = (tipologia: string) => {
    return (
      imageMap[tipologia] ||
      "https://images.unsplash.com/photo-1570129477492-45c003edd2be?q=80&w=1170&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
    );
  };

  useEffect(() => {
    async function fetchData() {
      try {
        const res = await getImmobili();
        setImmobili(res.immobili);
      } catch (err: unknown) {
        if (err instanceof Error) {
          setError(err.message);
        } else {
          setError("Errore sconosciuto");
        }
      } finally {
        setLoading(false);
      }
    }
    fetchData();
  }, []);

  if (loading) return <div>Caricamento...</div>;
  if (error) return <div>Errore: {error}</div>;

  const openEditModal = (property: Immobile) => {
    setEditData({ ...property });
    setIsEditing(true);
  };

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
                    "Garage",
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
                backgroundImage: `url(${getPropertyImage(property.tipologia)})`,
                backgroundSize: "cover",
                backgroundPosition: "center",
              }}
            >
              <div className="type">
                <h4>{property.tipologia}</h4>
              </div>
              <div className="info-property">
                <div className="info-first">
                  <div>
                    <h3>{property.nomeProprietario}</h3>
                    <h4>
                      <FaLocationDot /> {property.via}, {property.citta} (
                      {property.provincia})
                    </h4>
                  </div>
                  <button
                    className="edit-btn"
                    title="Modifica dati immobile"
                    onClick={() => openEditModal(property)}
                  >
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
            <FaX />
          </button>
          <div className="details-image">
            <img
              src={getPropertyImage(selectedProperty.tipologia)}
              alt="foto immobile"
            />
          </div>

          <div className="details-content">
            <h2>{selectedProperty.tipologia}</h2>

            <p className="address">
              <FaLocationDot /> {selectedProperty.via}, {selectedProperty.citta}{" "}
              ({selectedProperty.provincia})
            </p>

            <h3>{selectedProperty.nomeProprietario}</h3>

            <div className="details-grid">
              <p>
                <strong>Email:</strong> {selectedProperty.emailProprietario}
              </p>
              <p>
                <strong>Tel:</strong> {selectedProperty.telefonoProprietario}
              </p>

              <p>
                <strong>Data di registrazione:</strong>{" "}
                {new Date(
                  selectedProperty.dataRegistrazione
                ).toLocaleDateString("it-IT")}
              </p>
              <p>
                <strong>Stato immobile:</strong>{" "}
                {selectedProperty.statoImmobile}
              </p>
              <p>
                <strong>Prezzo:</strong>{" "}
                {selectedProperty.prezzo.toLocaleString()} €
              </p>
              <p>
                <strong>Metratura:</strong> {selectedProperty.metratura} m²
              </p>
              <p>
                <strong>Piano:</strong> {selectedProperty.piano}
              </p>
              <p>
                <strong>Stanze:</strong> {selectedProperty.stanze}
              </p>
              <p>
                <strong>Bagni:</strong> {selectedProperty.bagni || 0}
              </p>
              <p>
                <strong>Riscaldamento:</strong>{" "}
                {selectedProperty.riscaldamento || "Nessuno"}
              </p>
              <p>
                <strong>Condizioni:</strong> {selectedProperty.condizioni}
              </p>
              <p>
                <strong>Agente assegnato:</strong>{" "}
                {selectedProperty.agenteAssegnato || "Nessuno"}
              </p>
              <p>
                <strong>Plus:</strong>{" "}
                {[
                  selectedProperty.ascensore && "Ascensore",
                  selectedProperty.garage && "Garage",
                  selectedProperty.giardino && "Giardino",
                  selectedProperty.balcone && "Balcone",
                  selectedProperty.terrazzo && "Terrazzo",
                  selectedProperty.cantina && "Cantina",
                ]
                  .filter(Boolean)
                  .join(", ") || "Nessuno"}
              </p>
            </div>

            <p className="description">
              <strong>Descrizione:</strong> {selectedProperty.descrizione}
            </p>

            <Button
              className="lightblu"
              onClick={() => openEditModal(selectedProperty)}
            >
              Modifica immobile
            </Button>
          </div>
        </aside>
      )}

      {isEditing && editData && (
        <div className="modal-overlay-property">
          <div className="modal-content-property">
            <button className="close-btn" onClick={() => setIsEditing(false)}>
              <FaX />
            </button>

            <div className="modal-header">
              <img
                src={getPropertyImage(editData.tipologia)}
                alt="foto immobile"
              />

              <div className="modal-title">
                <h2>Modifica immobile</h2>
                <h3>{editData.tipologia}</h3>
                <p>
                  {editData.via}, {editData.citta} ({editData.provincia})
                </p>
              </div>
            </div>

            <div className="modal-body">
              <InputGroup
                label="Nome proprietario"
                name="Nome proprietario"
                placeholder="Inserisci nuovo proprietario"
                required
                value={editData.nomeProprietario}
                onChange={(e) =>
                  setEditData({ ...editData, nomeProprietario: e.target.value })
                }
              ></InputGroup>
              <InputGroup
                label="Via"
                name="Via"
                placeholder="Inserisci nuova via"
                required
                value={editData.via}
                onChange={(e) =>
                  setEditData({ ...editData, via: e.target.value })
                }
              ></InputGroup>
              <InputGroup
                label="Città"
                name="Città"
                placeholder="Inserisci nuova città"
                required
                value={editData.citta}
                onChange={(e) =>
                  setEditData({ ...editData, citta: e.target.value })
                }
              ></InputGroup>
              <InputGroup
                label="Cap"
                name="Cap"
                placeholder="Inserisci nuovo cap"
                required
                value={editData.cap}
                onChange={(e) =>
                  setEditData({ ...editData, cap: e.target.value })
                }
              ></InputGroup>
              <InputGroup
                label="Provincia"
                name="Provincia"
                placeholder="Inserisci nuova provincia"
                required
                value={editData.provincia}
                onChange={(e) =>
                  setEditData({ ...editData, provincia: e.target.value })
                }
              ></InputGroup>
              <label>
                <p>Stato immobile *</p>
                <select
                  value={editData.condizioni}
                  onChange={(e) =>
                    setEditData({ ...editData, condizioni: e.target.value })
                  }
                >
                  <option>Nuovo</option>
                  <option>Ottimo stato</option>
                  <option>Buono</option>
                  <option>Da ristrutturare</option>
                </select>
              </label>

              <InputGroup
                label="Prezzo (€)"
                name="Prezzo"
                type="number"
                placeholder="Inserisci nuovo prezzo"
                required
                value={editData.prezzo}
                onChange={(e) =>
                  setEditData({ ...editData, prezzo: Number(e.target.value) })
                }
              ></InputGroup>

              <InputGroup
                label="Superficie (m²)"
                name="Superficie"
                type="number"
                placeholder="Inserisci nuova superficie"
                required
                value={editData.metratura}
                onChange={(e) =>
                  setEditData({
                    ...editData,
                    metratura: Number(e.target.value),
                  })
                }
              ></InputGroup>

              {/* <div className="edit-plus">
                <p>Plus *</p>
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
                        checked={editData.plus.includes(plusItem)}
                        onChange={(e) => {
                          const updated = e.target.checked
                            ? [...editData.plus, plusItem]
                            : editData.plus.filter(
                                (p: string) => p !== plusItem
                              );

                          setEditData({ ...editData, plus: updated });
                        }}
                      />
                      <span>{plusItem}</span>
                    </label>
                  ))}
                </div>
              </div> */}
            </div>

            <div className="modal-footer">
              <Button
                className="lightblu"
                onClick={() => {
                  console.log("Dati salvati:", editData);
                  setIsEditing(false);
                }}
              >
                Salva modifiche
              </Button>
            </div>
          </div>
        </div>
      )}
    </section>
  );
}
