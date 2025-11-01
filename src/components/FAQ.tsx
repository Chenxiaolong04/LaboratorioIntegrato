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
        question: "Domanda neg giwgiweigwnig?",
        answer: "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Phasellus commodo id ipsum ut placerat. Donec a commodo mauris. Nam mattis aliquet enim, placerat accumsan lectus consequat at. Cras leo nibh, ultricies vitae ultricies sit amet, ornare nec mi. Mauris luctus a mi sed egestas.",
    },
    {
        question: "Domanda neg giwgiweigwnig?",
        answer: "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
    },
    {
        question: "Domanda neg giwgiweigwnig?",
        answer: "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
    },
    {
        question: "Domanda neg giwgiweigwnig?",
        answer: "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
    },
    {
        question: "Domanda neg giwgiweigwnig?",
        answer: "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
    },
    {
        question: "Domanda neg giwgiweigwnig?",
        answer: "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
    },
];

export default function FAQ() {
    const [activeIndex, setActiveIndex] = useState<number | null>(0);

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
                                {activeIndex === index ? <FaChevronUp /> : <FaChevronDown />}
                            </div>
                            {activeIndex === index && (
                                <p className="faq-answer">{item.answer}</p>
                            )}
                        </div>
                    ))}
                </div>
                <div className="faq-contact">
                    <img src={TestImage} alt="" />
                    <p>Hai altre domande?</p>
                    <Button className="lightblu">Contattaci</Button>
                </div>
            </div>
        </section>
    );
}
