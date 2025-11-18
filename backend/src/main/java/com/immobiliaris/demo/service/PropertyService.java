package com.immobiliaris.demo.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.immobiliaris.demo.dto.PropertyRequest;
import com.immobiliaris.demo.model.Property;
import com.immobiliaris.demo.model.StatoImmobile;
import com.immobiliaris.demo.model.User;
import com.immobiliaris.demo.repository.PropertyRepository;
import com.immobiliaris.demo.repository.StatoImmobileRepository;
import com.immobiliaris.demo.repository.UserRepository;

@Service
public class PropertyService {

    private final PropertyRepository repository;
    private final StatoImmobileRepository statoImmobileRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

    @Value("${app.mail.from:darkshadowsean@gmail.com}")
    private String adminEmail;
    
    @Value("${app.default.stato-immobile-id:1}")
    private Integer defaultStatoId;
    
    @Value("${app.default.user-id:1}")
    private Long defaultUserId;

    public PropertyService(PropertyRepository repository, 
                          StatoImmobileRepository statoImmobileRepository,
                          UserRepository userRepository,
                          EmailService emailService){
        this.repository = repository;
        this.statoImmobileRepository = statoImmobileRepository;
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    public Property createProperty(PropertyRequest request) {

        Property p = new Property();

        p.setCity(request.getCity());
        p.setProvince(request.getProvince());
        p.setType(request.getType());
        p.setSquareMeters(request.getSquareMeters() != null ? request.getSquareMeters() : 0);
        p.setStreet(request.getStreet());
        p.setCondizioni(request.getCondizioni());
        p.setRooms(request.getRooms() != null ? request.getRooms() : 0);
        p.setBathrooms(request.getBathrooms() != null ? request.getBathrooms() : 0);
        p.setRiscaldamento(request.getRiscaldamento());
        p.setFloor(request.getFloor() != null ? request.getFloor() : 0);
        p.setElevator(request.getElevator() != null ? request.getElevator() : false);
        p.setGarage(request.getGarage() != null ? request.getGarage() : false);
        p.setGiardino(request.getGiardino() != null ? request.getGiardino() : false);
        p.setBalcone(request.getBalcone() != null ? request.getBalcone() : false);
        p.setTerrazzo(request.getTerrazzo() != null ? request.getTerrazzo() : false);
        p.setCantina(request.getCantina() != null ? request.getCantina() : false);
        p.setDescription(request.getDescription());            
        p.setCreatedAt(LocalDate.now());
        p.setPrice(null); // Il prezzo verrà calcolato successivamente dall'AI
        
        // Imposta stato immobile di default (ID 1 = "In Valutazione")
        StatoImmobile defaultStato = statoImmobileRepository.findById(defaultStatoId)
            .orElseThrow(() -> new RuntimeException("Stato immobile non trovato (ID: " + defaultStatoId + ")"));
        p.setStatoImmobile(defaultStato);
        
        // Imposta utente di sistema per richieste pubbliche (ID 1 = utente "Sistema")
        User systemUser = userRepository.findById(defaultUserId)
            .orElseThrow(() -> new RuntimeException("Utente di sistema non trovato (ID: " + defaultUserId + ")"));
        p.setUtente(systemUser);

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
