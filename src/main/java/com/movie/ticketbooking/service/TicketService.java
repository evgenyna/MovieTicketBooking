package com.movie.ticketbooking.service;

import com.movie.ticketbooking.dao.TicketEventRepository;
import com.movie.ticketbooking.dao.TicketRepository;
import com.movie.ticketbooking.model.Showtime;
import com.movie.ticketbooking.model.Ticket;
import com.movie.ticketbooking.model.TicketEvent;
import com.movie.ticketbooking.model.User;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TicketService {
    private static final Logger logger = LoggerFactory.getLogger(TicketService.class);

    private final TicketRepository ticketRepository;
    private final TicketEventRepository ticketEventRepository;  // ✅ Inject TicketEventRepository

    public TicketService(TicketRepository ticketRepository, TicketEventRepository ticketEventRepository) {
        this.ticketRepository = ticketRepository;
        this.ticketEventRepository = ticketEventRepository;  // ✅ Assign TicketEventRepository
    }

    //  Get all tickets
    public List<Ticket> getAllTickets() {
        logger.info("Fetching all tickets from the database.");
        return ticketRepository.findAll();
    }

    //  Get all tickets for a specific user
    public List<Ticket> getTicketsByUser(User user) {
        logger.info("Fetching all tickets for user: {}", user.getId());
        return ticketRepository.findByUser(user);
    }

    //  Get ticket by ID
    public Optional<Ticket> getTicketById(UUID id) {
        logger.info("Fetching ticket with ID: {}", id);
        return ticketRepository.findById(id);
    }

    public Ticket bookTicket(Showtime showtime, User user, int seatNumber, double price) {
        logger.info("Attempting to book a ticket for User: {}, Showtime: {}, Seat: {}, Price: {}",
                user.getId(), showtime.getId(), seatNumber, price);

        if (seatNumber <= 0 || seatNumber > showtime.getHall().getCapacity()) {
            logger.warn("Invalid seat number {}. Must be between 1 and {}",
                    seatNumber, showtime.getHall().getCapacity());
            throw new RuntimeException("Invalid seat number! Must be between 1 and " +
                    showtime.getHall().getCapacity());
        }

        if (LocalDateTime.now().isAfter(showtime.getStartTime().minusHours(3))) {
            logger.warn("Booking failed. Cannot book tickets within 3 hours of showtime.");
            throw new RuntimeException("Cannot book tickets within 3 hours of showtime.");
        }

        if (ticketRepository.existsByShowtimeAndSeatNumber(showtime, seatNumber)) {
            logger.warn("Booking failed. Seat {} is already booked for Showtime: {}",
                    seatNumber, showtime.getId());
            throw new RuntimeException("Seat already booked for this showtime.");
        }

        // Check if user has an overlapping booking
        List<Ticket> overlappingTickets = ticketRepository.findOverlappingTickets(
                user, showtime.getStartTime(), showtime.getEndTime());

        if (!overlappingTickets.isEmpty()) {
            logger.warn("Booking failed. User {} already has a ticket in overlapping time: {}",
                    user.getId(), overlappingTickets.get(0).getShowtime().getId());
            throw new RuntimeException("You already have a booking during this time.");
        }

        // Save the ticket if no conflicts
        Ticket ticket = new Ticket(showtime, user, seatNumber, price);
        Ticket savedTicket = ticketRepository.save(ticket);

        // ✅ Log the ticket booking event
        TicketEvent event = new TicketEvent("ticket_booked", savedTicket, "User booked a ticket.");
        ticketEventRepository.save(event);
        logger.info("✅ Event logged: ticket_booked for ticket ID: {}", savedTicket.getId());
        logger.info("Ticket booked successfully. Ticket ID: {}, Price: {}", savedTicket.getId(), price);

        return savedTicket;
    }

    @Transactional
    public boolean cancelTicket(UUID ticketId) {
        logger.info("Attempting to cancel ticket with ID: {}", ticketId);

        Optional<Ticket> ticketOptional = ticketRepository.findById(ticketId);
        if (ticketOptional.isPresent()) {
            Ticket ticket = ticketOptional.get();

            if (LocalDateTime.now().isAfter(ticket.getShowtime().getStartTime().minusHours(3))) {
                logger.warn("Cancellation failed. Cannot cancel tickets within 3 hours of showtime.");
                throw new RuntimeException("Cannot cancel tickets within 3 hours of showtime.");
            }

            // ✅ Extract details (avoid using full Ticket object to prevent Hibernate errors)
            UUID ticketRefId = ticket.getId();
            String ticketDetails = "User canceled ticket for showtime: " + ticket.getShowtime().getId();

            // ✅ Delete the ticket first (to avoid transient object errors)
            ticketRepository.delete(ticket);
            ticketRepository.flush(); // Force immediate deletion

            // ✅ Now log the event separately after deletion
            TicketEvent event = new TicketEvent("ticket_deleted", null, ticketDetails);
            ticketEventRepository.save(event);
            logger.info("✅ Event logged: ticket_deleted for ticket ID: {}", ticketRefId);

            return true;
        } else {
            logger.warn("Cancellation failed. Ticket with ID {} not found.", ticketId);
            return false;
        }
    }


    //  Change a ticket seat
    public Ticket changeSeat(UUID ticketId, int newSeatNumber) {
        logger.info("Attempting to change seat for ticket ID: {} to seat: {}", ticketId, newSeatNumber);
        Optional<Ticket> ticketOptional = ticketRepository.findById(ticketId);

        if (ticketOptional.isPresent()) {
            Ticket ticket = ticketOptional.get();
            Showtime showtime = ticket.getShowtime();

            if (newSeatNumber <= 0 || newSeatNumber > showtime.getHall().getCapacity()) {
                logger.warn("Seat change failed. Seat number {} is out of valid range.", newSeatNumber);
                throw new RuntimeException("Invalid seat number! Must be between 1 and " + showtime.getHall().getCapacity());
            }

            if (LocalDateTime.now().isAfter(showtime.getStartTime().minusHours(3))) {
                logger.warn("Seat change failed. Cannot change seat within 3 hours of showtime.");
                throw new RuntimeException("Cannot change seat within 3 hours of showtime.");
            }

            if (ticketRepository.existsByShowtimeAndSeatNumber(showtime, newSeatNumber)) {
                logger.warn("Seat change failed. Seat {} is already booked for Showtime: {}", newSeatNumber, showtime.getId());
                throw new RuntimeException("Seat already booked for this showtime.");
            }

            ticket.setSeatNumber(newSeatNumber);
            Ticket updatedTicket = ticketRepository.save(ticket);

            // ✅ Log the seat change event
            TicketEvent event = new TicketEvent("ticket_seat_changed", updatedTicket, "User changed seat to " + newSeatNumber);
            ticketEventRepository.save(event);
            logger.info(" Seat change successful. Ticket ID: {} now has seat {}", updatedTicket.getId(), newSeatNumber);

            return updatedTicket;
        } else {
            logger.warn("Seat change failed. Ticket with ID {} not found.", ticketId);
            throw new RuntimeException("Ticket not found.");
        }
    }
}
