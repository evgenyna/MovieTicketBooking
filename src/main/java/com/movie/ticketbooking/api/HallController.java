package com.movie.ticketbooking.api;

import com.movie.ticketbooking.model.Hall;
import com.movie.ticketbooking.model.Theater;
import com.movie.ticketbooking.service.HallService;
import com.movie.ticketbooking.service.TheaterService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/halls")
@Tag(name = "Halls API", description = "Manage Halls")
public class HallController {

    private final HallService hallService;
    private final TheaterService theaterService; // Needed to fetch theaters when creating halls

    public HallController(HallService hallService, TheaterService theaterService) {
        this.hallService = hallService;
        this.theaterService = theaterService;
    }

    // Get all halls
    @GetMapping
    public List<Hall> getAllHalls() {
        return hallService.getAllHalls();
    }

    // Get hall by ID
    @GetMapping("/{id}")
    public ResponseEntity<Hall> getHallById(@PathVariable String id) {
        try {
            UUID uuid = UUID.fromString(id);
            return hallService.getHallById(uuid)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Create a new hall
    @PostMapping
    public ResponseEntity<?> createHall(@RequestBody Hall hall) {
        try {
            return ResponseEntity.ok(hallService.addHall(hall));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Create a new hall in a specific theater
    @PostMapping("/theater/{theaterId}")
    public ResponseEntity<?> createHallInTheater(@PathVariable String theaterId, @RequestBody Hall hall) {
        try {
            UUID uuid = UUID.fromString(theaterId); // ✅ Convert to UUID
            Optional<Theater> theater = theaterService.getTheaterById(uuid); // ✅ Fetch using UUID

            if (theater.isPresent()) {
                hall.setTheater(theater.get());
                return ResponseEntity.ok(hallService.addHall(hall));
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid theater ID format.");
        }
    }

    // Update a hall
    @PutMapping("/{id}")
    public ResponseEntity<Hall> updateHall(@PathVariable String id, @RequestBody Hall hallDetails) {
        try {
            UUID uuid = UUID.fromString(id);
            return hallService.updateHall(uuid, hallDetails)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Delete a hall
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHall(@PathVariable String id) {
        try {
            UUID uuid = UUID.fromString(id);
            return hallService.deleteHall(uuid)
                    ? ResponseEntity.noContent().build()
                    : ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
