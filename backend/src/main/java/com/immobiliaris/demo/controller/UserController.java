package com.immobiliaris.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.immobiliaris.demo.entity.User;
import com.immobiliaris.demo.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;

    // Registrazione nuovo utente (solo ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/register")
    public User registerUser(@RequestBody User user) {
        return userService.registerUser(user, user.getTipoUtente().getIdTipo());
    }

    // Aggiornamento utente
    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User user) {
        return userService.updateUser(id, user);
    }

    // Lista utenti
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    // Recupero singolo utente
    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }
}
