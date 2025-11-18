package com.immobiliaris.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.immobiliaris.demo.entity.User;

import java.util.Optional;

@Repository
<<<<<<< HEAD
public interface UserRepository extends JpaRepository<User, Integer> {
=======
public interface UserRepository extends JpaRepository<User, Long> {
>>>>>>> main
    Optional<User> findByEmail(String email);
}
