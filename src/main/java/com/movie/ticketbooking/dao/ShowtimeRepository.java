package com.movie.ticketbooking.dao;

import com.movie.ticketbooking.model.Hall;
import com.movie.ticketbooking.model.Showtime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ShowtimeRepository extends JpaRepository<Showtime, UUID> {

    //  Fetch all showtimes for a given movie ID
    List<Showtime> findAllByMovieId(UUID movieId);

    //  Find all halls where a specific movie is screening
    @Query("SELECT DISTINCT s.hall FROM Showtime s WHERE s.movie.id = :movieId")
    List<Hall> findHallsByMovieId(UUID movieId);

    //  Get all showtimes for a list of halls
    @Query("SELECT s FROM Showtime s WHERE s.hall IN :halls")
    List<Showtime> findAllByHalls(@Param("halls") List<Hall> halls);
}
