package com.immobiliaris.demo.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.immobiliaris.demo.dto.PropertyRequest;
import com.immobiliaris.demo.model.Property;
import com.immobiliaris.demo.repository.PropertyRepository;

@Service
public class PropertyService {

    private final PropertyRepository repository;
    private final EmailService emailService;

    @Value("${app.admin.email:admin@immobiliaris.com}")
    private String adminEmail;

    public PropertyService(PropertyRepository repository, EmailService emailService){
        this.repository = repository;
        this.emailService = emailService;
    }

    public Property createProperty(PropertyRequest request) {

        Property p = new Property();

        p.setCity(request.getCity());
        p.setProvince(request.getProvince());
        p.setType(request.getType());
        p.setSquareMeters(request.getSquareMeters());
        p.setStreet(request.getStreet());
        p.setRooms(request.getRooms());
        p.setBathrooms(request.getBathrooms());
        p.setFloor(request.getFloor());
        p.setElevator(request.getElevator());
        p.setGarage(request.getGarage());
        p.setDescription(request.getDescription());            
        p.setCreatedAt(LocalDate.now());
        p.setPrice(null);

        Property savedProperty = repository.save(p);

        // Invia email di conferma all'utente
        if (request.getContactEmail() != null && !request.getContactEmail().isEmpty()) {
            emailService.sendConfirmationEmail(
                request.getContactEmail(),
                request.getContactName(),
                savedProperty
            );
        }

        // Invia notifica all'admin
        emailService.sendInternalNotification(
            adminEmail,
            savedProperty,
            request.getContactName(),
            request.getContactEmail(),
            request.getContactPhone()
        );

        return savedProperty;
    }

    public List<Property> getAllProperties() {
        return repository.findAll();
    }

}
