package com.movie.ticketbooking.dao;

import com.movie.ticketbooking.model.Showtime;
import com.movie.ticketbooking.model.Ticket;
import com.movie.ticketbooking.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, UUID> {
    boolean existsByShowtimeAndSeatNumber(Showtime showtime, int seatNumber);
    //  Add method to find all tickets by a specific user
    List<Ticket> findByUser(User user);

}
