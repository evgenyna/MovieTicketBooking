package com.movie.ticketbooking.service;

import com.movie.ticketbooking.dao.HallRepository;
import com.movie.ticketbooking.model.Hall;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class HallService {

    private final HallRepository hallRepository;

    public HallService(HallRepository hallRepository) {
        this.hallRepository = hallRepository;
    }

    public List<Hall> getAllHalls() {
        return hallRepository.findAll();
    }

    public Optional<Hall> getHallById(UUID id) {
        return hallRepository.findById(id);
    }

    public Hall addHall(Hall hall) {
        if (hallRepository.existsByNameAndTheaterId(hall.getName(), hall.getTheater().getId())) {
            throw new RuntimeException("Hall with this name already exists in the theater.");
        }
        return hallRepository.save(hall);
    }

    public Optional<Hall> updateHall(UUID id, Hall hallDetails) {
        return hallRepository.findById(id).map(existingHall -> {
            existingHall.setName(hallDetails.getName());
            existingHall.setCapacity(hallDetails.getCapacity());
            return hallRepository.save(existingHall);
        });
    }

    public boolean deleteHall(UUID id) {
        if (hallRepository.existsById(id)) {
            hallRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
