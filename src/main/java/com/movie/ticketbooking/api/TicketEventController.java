package com.movie.ticketbooking.api;

import com.movie.ticketbooking.model.TicketEvent;
import com.movie.ticketbooking.service.TicketEventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ticket-events")
@Tag(name = "Ticket Event API", description = "Endpoints for fetching ticket-related events")
public class TicketEventController {

    private final TicketEventService ticketEventService;

    public TicketEventController(TicketEventService ticketEventService) {
        this.ticketEventService = ticketEventService;
    }

    @GetMapping
    @Operation(summary = "Get all ticket events", description = "Retrieve a list of all ticket-related events.")
    public ResponseEntity<List<TicketEvent>> getAllTicketEvents() {
        return ResponseEntity.ok(ticketEventService.getAllTicketEvents());
    }
}
