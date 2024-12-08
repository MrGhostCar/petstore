package com.home.petstore.pet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PetService {
    @Autowired PetRepository petRepository;

    public boolean doesPetExist (Long petId) {
        return petRepository.existsById(petId);
    }
}
