import { useState } from "react";
import TestImage from "../assets/img/test-image.jpg";
import { FaChevronDown, FaChevronUp } from "react-icons/fa";
import Button from "./Button";

interface FAQ {
    question: string;
    answer: string;
}

const faqs: FAQ[] = [
    {
        question: "1. Cosa devo indicare per la posizione della casa?",
        answer: "Indica zona, quartiere e vicinanza a servizi utili (mezzi, scuole, parchi). Queste informazioni aumentano subito il valore percepito.",
    },
    {
        question: "2. Come specificare correttamente la metratura?",
        answer: "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
    },
    {
        question: "3. Va indicato se l’edificio ha un ascensore?",
        answer: "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
    },
    {
        question: "4. Devo menzionare le spese condominiali?",
        answer: "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
    }
];

export default function FAQ() {
    const [activeIndex, setActiveIndex] = useState<number | null>(null);

    const toggleFAQ = (index: number) => {
        setActiveIndex(activeIndex === index ? null : index);
    };

    return (
        <section className="faq">
            <h2>Le risposte che ti guidano alla vendita perfetta</h2>

            <div className="faq-container">
                <div className="faq-items">
                    {faqs.map((item, index) => (
                        <div
                            key={index}
                            className={`faq-item ${activeIndex === index ? "active" : ""}`}
                            onClick={() => toggleFAQ(index)}
                        >
                            <div className="faq-question">
                                <h4>{item.question}</h4>
                                {activeIndex === index ? <FaChevronUp size={20}/> : <FaChevronDown size={20}/>}
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
