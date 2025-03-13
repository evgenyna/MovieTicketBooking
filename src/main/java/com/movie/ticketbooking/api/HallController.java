package com.movie.ticketbooking.api;

import com.movie.ticketbooking.dto.HallRequestDTO;
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
@Tag(name = "Hall API", description = "Endpoints for managing halls")
public class HallController {

    private final HallService hallService;
    private final TheaterService theaterService;

    public HallController(HallService hallService, TheaterService theaterService) {
        this.hallService = hallService;
        this.theaterService = theaterService;
    }

    @GetMapping
    @Operation(summary = "Get all halls", description = "Retrieve a list of all available halls.")
    public ResponseEntity<List<Hall>> getAllHalls() {
        return ResponseEntity.ok(hallService.getAllHalls());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a hall by ID", description = "Retrieve details of a specific hall by its ID.")
    public ResponseEntity<?> getHallById(@PathVariable String id) {
        try {
            UUID uuid = UUID.fromString(id);
            return hallService.getHallById(uuid)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid hall ID format.");
        }
    }

    @PostMapping
    @Operation(summary = "Create a new hall within theater", description = "Add a new hall to the theater.")
    public ResponseEntity<?> createHall(@RequestBody HallRequestDTO hallRequest) {
        try {
            // Find Theater by ID
            Optional<Theater> theaterOptional = theaterService.getTheaterById(hallRequest.getTheaterId());

            if (theaterOptional.isEmpty()) {
                return ResponseEntity.badRequest().body("Invalid theater ID.");
            }

            // Create and save Hall
            Hall hall = new Hall(hallRequest.getName(), hallRequest.getCapacity(), theaterOptional.get());
            return ResponseEntity.ok(hallService.addHall(hall));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid theater ID format.");
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing hall by id", description = "Updating Hall in system.")
    public ResponseEntity<Hall> updateHall(@PathVariable String id, @RequestBody HallRequestDTO hallDetails) {
        try {
            UUID uuid = UUID.fromString(id);
            return hallService.updateHall(uuid, hallDetails)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a hall by id", description = "Deleting Hall in system.")
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


//
//    // Create a new hall
//    @PostMapping
//    @Operation(summary = "Create a new hall", description = "Add a new hall to the system.")
//    public ResponseEntity<?> createHall(@RequestBody Hall hall) {
//        try {
//            return ResponseEntity.ok(hallService.addHall(hall));
//        } catch (RuntimeException e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }
//
//    // Create a new hall in a specific theater
//    @PostMapping("/theater/{theaterId}")
//    @Operation(summary = "Create a hall in a theater", description = "Add a new hall inside a specific theater.")
//    public ResponseEntity<?> createHallInTheater(@PathVariable String theaterId, @RequestBody Hall hall) {
//        try {
//            UUID uuid = UUID.fromString(theaterId);
//            Optional<Theater> theater = theaterService.getTheaterById(uuid);
//
//            if (theater.isPresent()) {
//                hall.setTheater(theater.get());
//                return ResponseEntity.ok(hallService.addHall(hall));
//            } else {
//                return ResponseEntity.notFound().build();
//            }
//        } catch (IllegalArgumentException e) {
//            return ResponseEntity.badRequest().body("Invalid theater ID format.");
//        }
//    }
//
//    // Update a hall
//    @PutMapping("/{id}")
//    @Operation(summary = "Update a hall", description = "Modify an existing hall's details using its ID.")
//    public ResponseEntity<?> updateHall(@PathVariable String id, @RequestBody Hall hallDetails) {
//        try {
//            UUID uuid = UUID.fromString(id);
//            return hallService.updateHall(uuid, hallDetails)
//                    .map(ResponseEntity::ok)
//                    .orElse(ResponseEntity.notFound().build());
//        } catch (IllegalArgumentException e) {
//            return ResponseEntity.badRequest().body("Invalid hall ID format.");
//        }
//    }
//
//    // Delete a hall
//    @DeleteMapping("/{id}")
//    @Operation(summary = "Delete a hall", description = "Remove a hall from the system using its ID.")
//    public ResponseEntity<?> deleteHall(@PathVariable String id) {
//        try {
//            UUID uuid = UUID.fromString(id);
//            return hallService.deleteHall(uuid)
//                    ? ResponseEntity.noContent().build()
//                    : ResponseEntity.notFound().build();
//        } catch (IllegalArgumentException e) {
//            return ResponseEntity.badRequest().body("Invalid hall ID format.");
//        }
//    }
}
