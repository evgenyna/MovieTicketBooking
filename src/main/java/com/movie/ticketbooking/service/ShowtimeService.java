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

    public List<Showtime> getAllShowtimes() {
        return showtimeRepository.findAll();
    }

    public Optional<Showtime> getShowtimeById(UUID id) {
        return showtimeRepository.findById(id);
    }
}
