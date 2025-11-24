package com.immobiliaris.demo.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.immobiliaris.demo.repository.UtenteJpaRepository;
import com.immobiliaris.demo.entity.Utente;
import java.util.List;

@RestController
@RequestMapping("/api/utenti")
@CrossOrigin(origins = "*")
public class UtenteApiController {
    
    @Autowired
    private UtenteJpaRepository repository;

    @GetMapping("/all")
    public Object getAll() {
        List<Utente> list = repository.findAll();
        if(list.size() == 0) {
            return new Response("error", "Nessun utente trovato", null);
        }
        return new Response("success", "Utenti caricati", list);
    }

    @PostMapping("/save")
    public Object save(@RequestBody Utente utente) {
        Utente saved = repository.save(utente);
        return new Response("success", "Utente salvato", saved);
    }

    @GetMapping("/{id}")
    public Object getById(@PathVariable Long id) {
        var utente = repository.findById(id);
        if(utente.isEmpty()) {
            return new Response("error", "Utente non trovato", null);
        }
        return new Response("success", "Utente trovato", utente.get());
    }

    @DeleteMapping("/{id}")
    public Object delete(@PathVariable Long id) {
        repository.deleteById(id);
        return new Response("success", "Utente eliminato", null);
    }

    static class Response {
        public String status;
        public String message;
        public Object data;

        public Response(String status, String message, Object data) {
            this.status = status;
            this.message = message;
            this.data = data;
        }
    }
}
