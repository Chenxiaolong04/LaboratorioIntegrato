import { useState } from "react";
import { FaChevronDown, FaChevronUp } from "react-icons/fa";

interface FAQ {
  question: string;
  answer: string;
}

const faqs: FAQ[] = [
  {
    question: "Qual è la procedura per acquistare un immobile?",
    answer:
      "La procedura comprende alcuni eventi fondamentali: proposta d'acquisto, eventuale contratto preliminare, stipula del rogito in presenza di un notaio, dove avviene il trasferimento ufficiale della proprietà.",
  },
  {
    question: "Quali documenti servono per la compravendita immobiliare?",
    answer:
      "Sono necessari diversi documenti, tra cui visura catastale, atto di provenienza, certificato di prestazione energetica, documenti di identità delle parti e planimetria aggiornata.",
  },
  {
    question: "Come avviene la trattativa sul prezzo di una casa?",
    answer:
      "La trattativa parte solitamente da una proposta di acquisto. Il venditore può accettare, rifiutare o controproporre. Si arriva quindi a un accordo, che può essere formalizzato tramite un contratto preliminare.",
  },
  {
    question: "Quali tasse e costi si pagano per acquistare casa?",
    answer:
      "All'acquisto sono legate imposte come registro, ipotecarie e catastali (con aliquote variabili in base al tipo di acquisto), spese notarili e, se previsto, costi di intermediazione. Questi variano a seconda che si tratti della prima o della seconda casa e della tipologia di venditore (privato o impresa).",
  },
];

export default function FAQ() {
  const [activeIndex, setActiveIndex] = useState<number | null>(null);

  const toggleFAQ = (index: number) => {
    setActiveIndex(activeIndex === index ? null : index);
  };

  return (
    <section className="faq">
      <h2 className="faq-title-mobile">FAQ's</h2>
      <h2 className="faq-title-desktop">
        Le risposte che ti guidano ad una vendita perfetta
      </h2>

      <div className="faq-container">
        <div className="faq-items">
          {faqs.map((item, index) => (
            <div
              key={index}
              className={`faq-item ${activeIndex === index ? "active" : ""}`}
              onClick={() => toggleFAQ(index)}
            >
              <div className="faq-question">
                <h4>
                  {index + 1}. {item.question}
                </h4>

                {activeIndex === index ? (
                  <FaChevronUp size={20} className="icon-up" />
                ) : (
                  <FaChevronDown size={20} className="icon-down" />
                )}
              </div>

              {activeIndex === index && (
                <p className="faq-answer">{item.answer}</p>
              )}
            </div>
          ))}
        </div>
      </div>
    </section>
  );
}
