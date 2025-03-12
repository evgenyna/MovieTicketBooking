package com.movie.ticketbooking.api;

import com.movie.ticketbooking.model.Ticket;
import com.movie.ticketbooking.model.Showtime;
import com.movie.ticketbooking.model.User;
import com.movie.ticketbooking.service.TicketService;
import com.movie.ticketbooking.service.ShowtimeService;
import com.movie.ticketbooking.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/tickets")
@Tag(name = "Tickets API", description = "Manage Tickets")
public class TicketController {

    private final TicketService ticketService;
    private final ShowtimeService showtimeService;
    private final UserService userService;

    public TicketController(TicketService ticketService, ShowtimeService showtimeService, UserService userService) {
        this.ticketService = ticketService;
        this.showtimeService = showtimeService;
        this.userService = userService;
    }

    // ✅ Get all tickets
    @GetMapping
    public ResponseEntity<List<Ticket>> getAllTickets() {
        return ResponseEntity.ok(ticketService.getAllTickets());
    }

    // ✅ Get a specific ticket by ID
    @GetMapping("/{ticketId}")
    public ResponseEntity<?> getTicketById(@PathVariable String ticketId) {
        try {
            UUID uuid = UUID.fromString(ticketId);  // ✅ Convert String to UUID
            Optional<Ticket> ticket = ticketService.getTicketById(uuid);

            return ticket.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid ticket ID format.");
        }
    }

    // ✅ Book a new ticket
    @PostMapping("/book")
    public ResponseEntity<?> bookTicket(
            @RequestParam String showtimeId,
            @RequestParam String userId,
            @RequestParam int seatNumber
    ) {
        try {
            UUID showtimeUUID = UUID.fromString(showtimeId);
            UUID userUUID = UUID.fromString(userId);

            Optional<Showtime> showtime = showtimeService.getShowtimeById(showtimeUUID);
            Optional<User> user = userService.getUserById(userUUID);

            if (showtime.isEmpty() || user.isEmpty()) {
                return ResponseEntity.badRequest().body("Invalid Showtime or User ID.");
            }

            Ticket bookedTicket = ticketService.bookTicket(showtime.get(), user.get(), seatNumber);
            return ResponseEntity.ok(bookedTicket);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid UUID format.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ✅ Cancel a ticket
    @DeleteMapping("/{ticketId}")
    public ResponseEntity<?> cancelTicket(@PathVariable String ticketId) {
        try {
            UUID uuid = UUID.fromString(ticketId);
            boolean isCancelled = ticketService.cancelTicket(uuid);

            if (isCancelled) {
                return ResponseEntity.ok("Ticket cancelled successfully.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ticket not found.");
            }

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid ticket ID format.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage()); // Return error messages for business logic failures
        }
    }
}
