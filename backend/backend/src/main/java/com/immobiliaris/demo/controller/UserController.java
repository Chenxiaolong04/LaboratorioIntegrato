package com.immobiliaris.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.immobiliaris.demo.entity.User;
import com.immobiliaris.demo.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin
public class UserController {
    // Elimina utente per ID (solo ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUserById(id);
    }

    @Autowired
    private UserService userService;
    
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    // Registrazione nuovo utente (solo ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/register")
    public User registerUser(@RequestBody User user) {
        logger.info("JSON ricevuto: {}", user);
        logger.info("TipoUtente: {}", user.getTipoUtente());
        return userService.registerUser(user, user.getTipoUtente().getIdTipo());
    }

    // Recupera profilo personale (solo ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/profile")
    public User getOwnProfile(Authentication authentication) {
        String email = authentication.getName();
        return userService.getUserByEmail(email);
    }

    // Modifica profilo personale (solo ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/profile")
    public User updateOwnProfile(@RequestBody User updatedUser, Authentication authentication) {
        String email = authentication.getName();
        return userService.updateOwnProfile(email, updatedUser);
    }

    // Modifica utente per ID (solo ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public User updateUserAsAdmin(@PathVariable Long id, @RequestBody User updatedUser) {
        return userService.updateUserAsAdmin(id, updatedUser);
    }

    // Lista utenti (solo ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    // Recupero singolo utente (solo ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }
}
