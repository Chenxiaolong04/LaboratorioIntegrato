import { Link } from "react-router-dom";

export default function CTA() {
    return (
        <section className="cta-section">
            <div className="container">
                <h2>Ti abbiamo convinto?</h2>
                <Link to={'/form'} className="btn lightblu">Inizia ora la valutazione gratuita</Link>
            </div>
        </section>
    );
}