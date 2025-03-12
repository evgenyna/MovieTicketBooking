package com.movie.ticketbooking.api;

import com.movie.ticketbooking.model.Theater;
import com.movie.ticketbooking.service.TheaterService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/theaters")
@Tag(name = "Theater API", description = "Endpoints for managing theaters")
public class TheaterController {

    private final TheaterService theaterService;

    public TheaterController(TheaterService theaterService) {
        this.theaterService = theaterService;
    }

    // Get all theaters
    @GetMapping
    @Operation(summary = "Get all theaters", description = "Retrieve a list of all available theaters.")
    public ResponseEntity<List<Theater>> getAllTheaters() {
        return ResponseEntity.ok(theaterService.getAllTheaters());
    }

    // Get a specific theater by ID
    @GetMapping("/{theaterId}")
    @Operation(summary = "Get a theater by ID", description = "Retrieve details of a specific theater by its ID.")
    public ResponseEntity<?> getTheaterById(@PathVariable String theaterId) {
        try {
            UUID uuid = UUID.fromString(theaterId);
            Optional<Theater> theater = theaterService.getTheaterById(uuid);

            return theater.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid theater ID format.");
        }
    }

    // Create a new theater
    @PostMapping
    @Operation(summary = "Create a new theater", description = "Add a new theater to the system.")
    public ResponseEntity<Theater> createTheater(@RequestBody Theater theater) {
        Theater savedTheater = theaterService.addTheater(theater);
        return ResponseEntity.ok(savedTheater);
    }

    // Update an existing theater
    @PutMapping("/{theaterId}")
    @Operation(summary = "Update a theater", description = "Modify an existing theater's details using its ID.")
    public ResponseEntity<?> updateTheater(@PathVariable String theaterId, @RequestBody Theater theaterDetails) {
        try {
            UUID uuid = UUID.fromString(theaterId);
            Optional<Theater> updatedTheater = theaterService.updateTheater(uuid, theaterDetails);

            return updatedTheater.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid theater ID format.");
        }
    }

    // Delete a theater
    @DeleteMapping("/{theaterId}")
    @Operation(summary = "Delete a theater", description = "Remove a theater from the system using its ID.")
    public ResponseEntity<?> deleteTheater(@PathVariable String theaterId) {
        try {
            UUID uuid = UUID.fromString(theaterId);
            boolean deleted = theaterService.deleteTheater(uuid);

            if (deleted) {
                return ResponseEntity.ok("Theater deleted successfully.");
            } else {
                return ResponseEntity.notFound().build();
            }

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid theater ID format.");
        }
    }
}
