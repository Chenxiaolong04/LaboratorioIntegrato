/**
 * Stats component.
 *
 * Displays a section with key company statistics, including total investments,
 * properties managed, satisfied customers, and investment success rate.
 *
 * @function Stats
 * @returns {JSX.Element} A section containing four statistic cards.
 */
export default function Stats() {
  return (
    <section className="stats">
      <div className="container">
        <div className="stat">
          <h3>€500M+</h3>
          <p>Investimenti totali</p>
        </div>
        <div className="stat">
          <h3>10.000+</h3>
          <p>Proprietà gestite</p>
        </div>
        <div className="stat">
          <h3>100.000+</h3>
          <p>Persone soddisfatte</p>
        </div>
        <div className="stat">
          <h3>98%</h3>
          <p>Investimenti totali</p>
        </div>
      </div>
    </section>
  );
}
