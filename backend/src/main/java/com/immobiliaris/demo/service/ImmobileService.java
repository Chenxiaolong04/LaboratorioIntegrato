package com.immobiliaris.demo.service;

import com.immobiliaris.demo.model.Immobile;
import com.immobiliaris.demo.repository.ImmobileRepository;

public class ImmobileService {

    private final ImmobileRepository repository;

    public ImmobileService(ImmobileRepository repository){
        this.repository = repository;
    }

    public Immobile salvaImmobile(Immobile immobile){
        return repository.save(immobile);

    }

}
