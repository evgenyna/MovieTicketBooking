package com.movie.ticketbooking.dao;

import com.movie.ticketbooking.model.Showtime;
import com.movie.ticketbooking.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, UUID> {
    boolean existsByShowtimeAndSeatNumber(Showtime showtime, int seatNumber);
}
