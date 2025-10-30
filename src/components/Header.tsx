import HeaderImage from '../assets/img/test-image.jpg';

export default function Header() {
  return (
    <header>
      <h1>Vendi casa in modo semplice e veloce</h1>
      <h2>Scopri quanto vale la tua casa in pochi clic, Immobiliaris ti offre una valutazione gratuita in 72 ore.</h2>
      <button>Inizia la valutazione</button>
      <img src={HeaderImage} alt="Immagine header" />
    </header>
  );
}