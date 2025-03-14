package com.movie.ticketbooking.dao;

import com.movie.ticketbooking.model.Showtime;
import com.movie.ticketbooking.model.Ticket;
import com.movie.ticketbooking.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, UUID> {
    boolean existsByShowtimeAndSeatNumber(Showtime showtime, int seatNumber);
    //  Add method to find all tickets by a specific user
    List<Ticket> findByUser(User user);
    // ✅ Find tickets for the user that have overlapping showtimes
    @Query("SELECT t FROM Ticket t WHERE t.user = :user AND " +
            "t.showtime.startTime < :endTime AND t.showtime.endTime > :startTime")
    List<Ticket> findOverlappingTickets(@Param("user") User user,
                                        @Param("startTime") LocalDateTime startTime,
                                        @Param("endTime") LocalDateTime endTime);

}
