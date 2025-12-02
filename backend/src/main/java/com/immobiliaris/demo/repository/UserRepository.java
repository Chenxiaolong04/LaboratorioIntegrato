package com.immobiliaris.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.immobiliaris.demo.entity.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // JpaRepository già include deleteById e existsById
    Optional<User> findByEmail(String email);
    
    /**
     * Trova l'ID utente tramite email
     * @param email Email dell'utente
     * @return Optional con l'utente trovato
     */
    default Long findIdByEmail(String email) {
        return findByEmail(email)
            .map(User::getIdUtente)
            .orElse(null);
    }
}
