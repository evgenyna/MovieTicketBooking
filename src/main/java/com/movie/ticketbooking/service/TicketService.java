package com.movie.ticketbooking.service;

import com.movie.ticketbooking.dao.TicketRepository;
import com.movie.ticketbooking.model.Showtime;
import com.movie.ticketbooking.model.Ticket;
import com.movie.ticketbooking.model.User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TicketService {
    private final TicketRepository ticketRepository;

    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    public Optional<Ticket> getTicketById(UUID id) {
        return ticketRepository.findById(id);
    }

    public Ticket bookTicket(Showtime showtime, User user, int seatNumber) {
        LocalDateTime showtimeStart = showtime.getStartTime();

        if (LocalDateTime.now().isAfter(showtimeStart.minusHours(3))) {
            throw new RuntimeException("❌ Cannot book tickets within 3 hours of showtime.");
        }

        if (ticketRepository.existsByShowtimeAndSeatNumber(showtime, seatNumber)) {
            throw new RuntimeException("❌ Seat already booked for this showtime.");
        }

        Ticket ticket = new Ticket(showtime, user, seatNumber);
        return ticketRepository.save(ticket);
    }

    public boolean cancelTicket(UUID ticketId) {
        Optional<Ticket> ticketOptional = ticketRepository.findById(ticketId);

        if (ticketOptional.isPresent()) {
            Ticket ticket = ticketOptional.get();
            LocalDateTime showtimeStart = ticket.getShowtime().getStartTime();

            if (LocalDateTime.now().isAfter(showtimeStart.minusHours(3))) {
                throw new RuntimeException("Cannot cancel tickets within 3 hours of showtime.");
            }

            ticketRepository.delete(ticket);
            return true;
        } else {
            return false;  // Return false if ticket not found
        }
    }

}