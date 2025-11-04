-- Script SQL per creare la tabella users e inserire dati di esempio
-- Database: agenziaimmobiliare

USE agenziaimmobiliare;

-- Crea la tabella users
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    enabled BOOLEAN NOT NULL DEFAULT TRUE
);

-- Inserisci utenti di esempio
-- Password per admin: adminpass (BCrypt hash)
-- Password per agent: agentpass (BCrypt hash)
INSERT INTO users (email, password, role, enabled) VALUES
('admin@example.com', '$2a$10$X5wFWtxB8y7Kg7X5wFWtxB8y7Kg7X5wFWtxB8y7Kg7X5wFWtxB8y7K', 'ADMIN', TRUE),
('agent@example.com', '$2a$10$Y6xGXuyC9z8Lh8Y6xGXuyC9z8Lh8Y6xGXuyC9z8Lh8Y6xGXuyC9z8L', 'AGENT', TRUE);

-- NOTA: Le password sopra sono esempi generici di hash BCrypt.
-- Per generare le tue password cifrate, usa uno dei seguenti metodi:
-- 1. Online: https://bcrypt-generator.com/
-- 2. In Java (Spring Boot):
--    System.out.println(new BCryptPasswordEncoder().encode("tuaPassword"));
-- 3. Usando lo script Java incluso sotto (vedi generate-bcrypt.java)

-- Verifica gli utenti inseriti
SELECT * FROM users;
