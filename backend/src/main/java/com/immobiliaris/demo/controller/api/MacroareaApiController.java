package com.immobiliaris.demo.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.immobiliaris.demo.repository.MacroareaUrbanaRepository;
import com.immobiliaris.demo.entity.MacroareaUrbana;
import java.util.List;

@RestController
@RequestMapping("/api/macroaree")
@CrossOrigin(origins = "*")
public class MacroareaApiController {
    @Autowired
    private MacroareaUrbanaRepository repository;

    @GetMapping("/all")
    public Object getAll() {
        List<MacroareaUrbana> list = repository.findAll();
        if(list.isEmpty()) {
            return new Response("error", "Nessuna macroarea trovata", null);
        }
        return new Response("success", "Macroaree caricate", list);
    }

    @PostMapping("/save")
    public Object save(@RequestBody MacroareaUrbana macroarea) {
        MacroareaUrbana saved = repository.save(macroarea);
        return new Response("success", "Macroarea salvata", saved);
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