package com.movie.ticketbooking.service;

import com.movie.ticketbooking.dao.TheaterRepository;
import com.movie.ticketbooking.model.Theater;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TheaterService {
    private final TheaterRepository theaterRepository;

    public TheaterService(TheaterRepository theaterRepository) {
        this.theaterRepository = theaterRepository;
    }

    public List<Theater> getAllTheaters() {
        return theaterRepository.findAll();
    }

    public Optional<Theater> getTheaterById(UUID id) {  //Accepts UUID correctly
        return theaterRepository.findById(id);
    }

    public Optional<Theater> getTheaterById(String id) {
        try {
            UUID uuid = UUID.fromString(id); // Convert String to UUID
            return theaterRepository.findById(uuid);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid UUID format for Theater ID: " + id);
        }
    }

    public Optional<Theater> updateTheater(UUID id, Theater theaterDetails) {
        return theaterRepository.findById(id).map(existingTheater -> {
            existingTheater.setName(theaterDetails.getName());
            existingTheater.setLocation(theaterDetails.getLocation());
            return theaterRepository.save(existingTheater);
        });
    }

    public Theater addTheater(Theater theater) {
        return theaterRepository.save(theater);
    }

    public boolean deleteTheater(UUID id) {
        if (theaterRepository.existsById(id)) {
            theaterRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public void deleteTheater(String id) {
        try {
            UUID uuid = UUID.fromString(id); // Convert String to UUID
            theaterRepository.deleteById(uuid);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid UUID format for Theater ID: " + id);
        }
    }
}
