package com.immobiliaris.demo.service;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

import com.immobiliaris.demo.dto.PropertyRequest;
import com.immobiliaris.demo.model.Property;
import com.immobiliaris.demo.repository.PropertyRepository;

@Service
public class PropertyService {

    private final PropertyRepository repository;

    public PropertyService(PropertyRepository repository){
        this.repository = repository;
    }

    public Property createProperty(PropertyRequest request) {

        Property p = new Property();

        p.setCity(request.getCity());
        p.setProvince(request.getProvince());
        p.setType(request.getType());
        p.setSquareMeters(request.getSquareMeters());
        p.setRooms(request.getRooms());
        p.setFloor(request.getFloor());
        p.setElevator(request.getElevator());
        p.setGarage(request.getGarage());
        p.setDescription(request.getDescription());            
        p.setCreatedAt(LocalDate.now());
        p.setPrice(null);

        return repository.save(p);
    }

}
