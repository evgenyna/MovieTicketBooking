package com.movie.ticketbooking.service;

import com.movie.ticketbooking.dao.ShowtimeRepository;
import com.movie.ticketbooking.model.Showtime;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ShowtimeService {

    private final ShowtimeRepository showtimeRepository;

    public ShowtimeService(ShowtimeRepository showtimeRepository) {
        this.showtimeRepository = showtimeRepository;
    }

    // Get all showtimes
    public List<Showtime> getAllShowtimes() {
        return showtimeRepository.findAll();
    }

    // Get a showtime by UUID
    public Optional<Showtime> getShowtimeById(UUID id) {
        return showtimeRepository.findById(id);
    }

    // Create a new showtime
    public Showtime saveShowtime(Showtime showtime) {
        return showtimeRepository.save(showtime);
    }

    // Delete a showtime
    public boolean deleteShowtime(UUID id) {
        if (showtimeRepository.existsById(id)) {
            showtimeRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
