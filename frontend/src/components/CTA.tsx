import { Link } from "react-router-dom";

/**
 * CTA (Call To Action) component.
 *
 * Renders a section prompting the user to start the free evaluation,
 * including a heading and a navigation button.
 *
 * @function CTA
 * @returns {JSX.Element} A section containing a title and a link styled as a button.
 */
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
