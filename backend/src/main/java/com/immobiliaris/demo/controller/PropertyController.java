package com.immobiliaris.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.immobiliaris.demo.dto.PropertyRequest;
import com.immobiliaris.demo.model.Property;
import com.immobiliaris.demo.service.PropertyService;

import java.util.List;

@RestController
@RequestMapping("/api/property")
@CrossOrigin(origins = "http://localhost:3000")
public class PropertyController {
    
    private final PropertyService service;

    public PropertyController (PropertyService service){
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> addProperty (@RequestBody PropertyRequest request){
        Property propertyCreating = service.createProperty(request);
        return ResponseEntity.ok(propertyCreating);
    }

    @GetMapping
    public ResponseEntity<List<Property>> getAllProperties(){
        List<Property> properties = service.getAllProperties();
        return ResponseEntity.ok(properties);
    }
    
}
