import PublicLayout from '../../layouts/PublicLayout';
import Header from '../../components/Header';
import StepsHome from '../../components/StepsHome';
import Stats from '../../components/Stats';
import Reasons from '../../components/Reasons';
import FAQ from '../../components/FAQ';
import CTA from '../../components/CTA';

/**
 * Home component.
 * This component represents the main landing page (homepage) of the public-facing website.
 * It structures the page content using the PublicLayout and includes various sections (components)
 * such as the header, statistics, steps, reasons, FAQ, and a call-to-action.
 * @returns {JSX.Element} The Home page view.
 */
export default function Home() {
  return (
    <>
      <PublicLayout>
        {/* Main hero section or introduction of the page. */}
        <Header />
        {/* Section displaying key statistics or metrics. */}
        <Stats />
        {/* Section outlining the process or steps for the user. */}
        <StepsHome />
        {/* Section detailing the reasons or benefits of using the service. */}
        <Reasons />
        {/* Section containing Frequently Asked Questions. */}
        <FAQ />
        {/* Call-to-Action section to encourage user engagement. */}
        <CTA />
      </PublicLayout>



































      
    </>
  );
}