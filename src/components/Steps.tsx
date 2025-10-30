import Step1Image from "../assets/img/test-image.jpg";

export default function Steps() {
  return (
    <section className="steps">
      <h2>Come funziona</h2>
      <h3>
        In soli 3 step potrai avere una valutazione completa del tuo immobile
      </h3>
      <div>
        <div className="step">
          <div className="step-info">
            <div className="step-number">
              <p>1</p>
            </div>
            <h4 className="step-name">
              Compila il form con i dati del tuo immobile
            </h4>
          </div>
          <div className="step-content">
            <p>
              Descrivi la tua casa indicandone le caratteristiche: sua
              geolocalizzazione, la superficie, il piano, il numero dei locali,
              lo stato dell'immobile, la presenza di spazi esterni (balcone,
              terrazzo, giardino) e tutto ciò che possa contribuire a definirne
              in modo preciso il valore. Compilare il nostro form è veloce e
              intuitivo, impiegherai solo 1 minuto.
            </p>
          </div>
          <img src={Step1Image} alt="" />
        </div>
        <div className="step">
          <div className="step-info">
            <div className="step-number">
              <p>2</p>
            </div>
            <h4 className="step-name">Ricevi la valutazione entro 72 ore</h4>
          </div>
          <div className="step-content">
            <p>
              Il nostro team analizza le informazioni inserite e confronta il
              tuo immobile con dati di mercato aggiornati nella tua zona. Entro
              72 ore riceverai una valutazione realistica del valore della tua
              casa, basata su criteri professionali e trasparenti.
            </p>
          </div>
          <img src={Step1Image} alt="" />
        </div>
        <div className="step">
          <div className="step-info">
            <div className="step-number">
              <p>3</p>
            </div>
            <h4 className="step-name">Vendi con noi in esclusiva</h4>
          </div>
          <div className="step-content">
            <p>
              Dopo la valutazione, ti proponiamo una collaborazione in
              esclusiva: un contratto chiaro, senza sorprese, per affidare la
              vendita a un’agenzia che segue ogni fase, dalla promozione
              dell’immobile fino alla firma del rogito, in modo sicuro e senza
              stress.
            </p>
          </div>
          <img src={Step1Image} alt="" />
        </div>
      </div>
    </section>
  );
}
