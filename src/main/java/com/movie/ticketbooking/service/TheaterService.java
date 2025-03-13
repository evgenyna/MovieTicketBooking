package com.movie.ticketbooking.service;

import com.movie.ticketbooking.dao.TheaterRepository;
import com.movie.ticketbooking.dao.ShowtimeRepository;
import com.movie.ticketbooking.dao.HallRepository;
import com.movie.ticketbooking.model.Theater;
import com.movie.ticketbooking.model.Showtime;
import com.movie.ticketbooking.model.Hall;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TheaterService {
    private final TheaterRepository theaterRepository;
    private final ShowtimeRepository showtimeRepository;
    private final HallRepository hallRepository;

    public TheaterService(TheaterRepository theaterRepository, ShowtimeRepository showtimeRepository, HallRepository hallRepository) {
        this.theaterRepository = theaterRepository;
        this.showtimeRepository = showtimeRepository;
        this.hallRepository = hallRepository;
    }

    public List<Theater> getAllTheaters() {
        return theaterRepository.findAll();
    }

    public Optional<Theater> getTheaterById(UUID id) {
        return theaterRepository.findById(id);
    }

    public Theater addTheater(Theater theater) {
        return theaterRepository.save(theater);
    }

    public Optional<Theater> updateTheater(UUID id, Theater theaterDetails) {
        return theaterRepository.findById(id).map(existingTheater -> {
            existingTheater.setName(theaterDetails.getName());
            existingTheater.setLocation(theaterDetails.getLocation());
            return theaterRepository.save(existingTheater);
        });
    }

    /**
     * **Cascade Delete Fix**
     * - Deletes all `Showtimes` before deleting `Halls`
     * - Then deletes `Theater`
     */
    @Transactional
    public boolean deleteTheater(UUID id) {
        Optional<Theater> theaterOptional = theaterRepository.findById(id);
        if (theaterOptional.isPresent()) {
            Theater theater = theaterOptional.get();

            // Step 1: Get all halls of this theater
            List<Hall> halls = hallRepository.findByTheater(theater);

            // Step 2: Get all showtimes related to these halls
            List<Showtime> showtimes = showtimeRepository.findAllByHalls (halls);
            showtimeRepository.deleteAll(showtimes);

            // Step 3: Delete all halls
            hallRepository.deleteAll(halls);

            // Step 4: Delete the theater itself
            theaterRepository.delete(theater);
            return true;
        }
        return false;
    }

    @Transactional
    public void deleteTheater(String id) {
        try {
            UUID uuid = UUID.fromString(id);
            deleteTheater(uuid);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid UUID format for Theater ID: " + id);
        }
    }
}
