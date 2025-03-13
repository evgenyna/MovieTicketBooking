package com.movie.ticketbooking.service;

import com.movie.ticketbooking.dao.ShowtimeRepository;
import com.movie.ticketbooking.dto.ShowtimeRequestDTO;
import com.movie.ticketbooking.model.Showtime;
import com.movie.ticketbooking.model.Movie;
import com.movie.ticketbooking.model.Hall;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ShowtimeService {

    private final ShowtimeRepository showtimeRepository;

    public ShowtimeService(ShowtimeRepository showtimeRepository) {
        this.showtimeRepository = showtimeRepository;
    }

    //  Get all showtimes
    public List<Showtime> getAllShowtimes() {
        return showtimeRepository.findAll();
    }

    //  Get showtime by ID
    public Optional<Showtime> getShowtimeById(UUID id) {
        return showtimeRepository.findById(id);
    }

    //  Add a new showtime
    public Showtime addShowtime(Showtime showtime) {
        return showtimeRepository.save(showtime);
    }

    //  Update an existing showtime
    public Optional<Showtime> updateShowtime(UUID id, ShowtimeRequestDTO showtimeDTO, Movie movie, Hall hall) {
        return showtimeRepository.findById(id).map(existingShowtime -> {
            existingShowtime.setMovie(movie);
            existingShowtime.setHall(hall);
            existingShowtime.setTheater(hall.getTheater()); // Ensure correct theater from Hall
            existingShowtime.setStartTime(showtimeDTO.getStartTime());
            existingShowtime.setEndTime(showtimeDTO.getEndTime());

            return showtimeRepository.save(existingShowtime);
        });
    }

    //  Delete showtime by ID
    public boolean deleteShowtime(UUID id) {
        if (showtimeRepository.existsById(id)) {
            showtimeRepository.deleteById(id);
            return true;
        }
        return false;
    }

    //  Get only halls where a specific movie is screening
    public List<Hall> getHallsByMovie(UUID movieId) {
        List<Showtime> showtimes = showtimeRepository.findAllByMovieId(movieId);

        // Extract unique halls
        return showtimes.stream()
                .map(Showtime::getHall)
                .distinct()
                .collect(Collectors.toList());
    }
}
