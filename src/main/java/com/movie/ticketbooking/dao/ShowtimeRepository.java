package com.movie.ticketbooking.dao;


import com.movie.ticketbooking.model.Showtime;
import com.movie.ticketbooking.model.Hall;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ShowtimeRepository extends JpaRepository<Showtime, UUID> {
    // Find all showtimes linked to a list of halls in a theater
    List<Showtime> findAllByHallIn(List<Hall> halls);
}