import { useState } from "react";
import { FaChevronDown, FaChevronUp } from "react-icons/fa";


interface FAQ {
    question: string;
    answer: string;
}

// Dati aggiornati del tuo progetto (domande e risposte)
const faqs: FAQ[] = [
    {
        question: "Qual è la procedura per acquistare un immobile?",
        answer: "La procedura comprende alcuni eventi fondamentali: proposta d'acquisto, eventuale contratto preliminare, stipula del rogito in presenza di un notaio, dove avviene il trasferimento ufficiale della proprietà.",
    },
    {
        question: "Quali documenti servono per la compravendita immobiliare?",
        answer: "Sono necessari diversi documenti, tra cui visura catastale, atto di provenienza, certificato di prestazione energetica, documenti di identità delle parti e planimetria aggiornata.",
    },
    {
        question: "Come avviene la trattativa sul prezzo di una casa?",
        answer: "La trattativa parte solitamente da una proposta di acquisto. Il venditore può accettare, rifiutare o controproporre. Si arriva quindi a un accordo, che può essere formalizzato tramite un contratto preliminare.",
    },
    {
        question: "Quali tasse e costi si pagano per acquistare casa?",
        answer: "All'acquisto sono legate imposte come registro, ipotecarie e catastali (con aliquote variabili in base al tipo di acquisto), spese notarili e, se previsto, costi di intermediazione. Questi variano a seconda che si tratti della prima o della seconda casa e della tipologia di venditore (privato o impresa).",
    }
];

export default function FAQ() {
    // Stato per tracciare il box aperto (indice numerico o null se chiuso)
    const [activeIndex, setActiveIndex] = useState<number | null>(null);

    // Funzione per aprire/chiudere l'elemento cliccato
    const toggleFAQ = (index: number) => {
        // Se l'indice cliccato è già attivo, chiudi (imposta a null); altrimenti apri il nuovo indice.
        setActiveIndex(activeIndex === index ? null : index);
    };

    return (
        <section className="faq">
            {/* Titoli resi visibili entrambi su mobile grazie alle modifiche in SCSS */}
            <h2 className="faq-title-mobile">FAQ's</h2>
            <h2 className="faq-title-desktop">Le risposte che ti guidano ad una vendita perfetta</h2>
            
            <div className="faq-container">
                <div className="faq-items">
                    {faqs.map((item, index) => (
                        <div
                            key={index}
                            // Aggiunge la classe 'active' quando è aperto per eventuali stili CSS
                            className={`faq-item ${activeIndex === index ? "active" : ""}`}
                            // Abilita la funzione di toggle al click
                            onClick={() => toggleFAQ(index)}
                        >
                            <div className="faq-question">
                                {/* Numerazione e domanda */}
                                <h4>{index + 1}. {item.question}</h4>
                                
                                {/* Icona condizionale: Freccia SU se aperto, Freccia GIÙ se chiuso */}
                                {activeIndex === index ? 
                                    <FaChevronUp size={20} className="icon-up" /> : 
                                    <FaChevronDown size={20} className="icon-down" />
                                }
                            </div>
                            
                            {/* Rendering condizionale della risposta: appare solo se attivo */}
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