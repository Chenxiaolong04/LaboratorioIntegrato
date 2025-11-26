import CompilaFormImg from "../assets/img/compila-form.webp";
import ValutazioneRapidaImg from "../assets/img/valutazione-rapida.webp";
import VenditaSicuraImg from "../assets/img/vendita-sicura.webp";

export default function Steps() {
  return (
    <section className="steps">
      <div className="container">
        <h2>Come funziona</h2>
        <h3>
          In soli 3 step potrai avere una valutazione completa del tuo immobile
        </h3>
        <div className="steps-container">
          <div className="step">
            <div className="step-container">
              <div className="step-info">
                <div className="step-number">
                  <p>1</p>
                </div>
                <h4 className="step-name">
                  Compila il form con i dati del tuo immobile
                </h4>
              </div>
              <div className="step-content">
                <p>Descrivi l'immobile.</p>
                <p>Semplice.</p>
                <p>Basta 1 minuto!</p>
              </div>
            </div>
            <img
              src={CompilaFormImg}
              alt="Immagine compila il form"
              title="Immagine compila il form"
            />
          </div>
          <div className="step">
            <div className="step-container">
              <div className="step-info">
                <div className="step-number">
                  <p>2</p>
                </div>
                <h4 className="step-name">Valutazione rapida e professionale</h4>
              </div>
              <div className="step-content">
                <p>
                  Entro 72 ore ottieni la stima accurata <br />
                  del tuo immobile, elaborata dal <br />
                  nostro team.
                </p>
              </div>
            </div>
            <img
              src={ValutazioneRapidaImg}
              alt="Immagine valutazione rapida"
              title="Immagine valutazione rapida"
            />
          </div>
          <div className="step">
            <div className="step-container">
              <div className="step-info">
                <div className="step-number">
                  <p>3</p>
                </div>
                <h4 className="step-name">Vendita sicura e senza pensieri</h4>
              </div>
              <div className="step-content">
                <p>
                  Affida la vendita in esclusiva <br />
                  al nostro team.
                </p>
              </div>
            </div>
            <img
              src={VenditaSicuraImg}
              alt="Immagine vendita sicura"
              title="Immagine vendita sicura"
            />
          </div>
        </div>
      </div>
    </section>
  );
}
