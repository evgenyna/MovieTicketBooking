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
//@Tag(name = "Theater API", description = "Manage Theaters")
public class TheaterController {

    private final TheaterService theaterService;

    public TheaterController(TheaterService theaterService) {
        this.theaterService = theaterService;
    }

    // ✅ Get all theaters
    @GetMapping
    @Tag(name = "Theater API", description = "Get all theaters")
    public ResponseEntity<List<Theater>> getAllTheaters() {
        return ResponseEntity.ok(theaterService.getAllTheaters());
    }

    // ✅ Get a specific theater by ID
    @GetMapping("/{theaterId}")
    public ResponseEntity<?> getTheaterById(@PathVariable String theaterId) {
        try {
            UUID uuid = UUID.fromString(theaterId); // ✅ Convert to UUID
            Optional<Theater> theater = theaterService.getTheaterById(uuid);

            return theater.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid theater ID format.");
        }
    }

    // ✅ Create a new theater
    @PostMapping
    public ResponseEntity<Theater> createTheater(@RequestBody Theater theater) {
        Theater savedTheater = theaterService.addTheater(theater);
        return ResponseEntity.ok(savedTheater);
    }

    // ✅ Update an existing theater
    @PutMapping("/{theaterId}")
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

    // ✅ Delete a theater
    @DeleteMapping("/{theaterId}")
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

//package com.movie.ticketbooking.api;
//
//import com.movie.ticketbooking.model.Theater;
//import com.movie.ticketbooking.service.TheaterService;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.Optional;
//import java.util.UUID;
//
//@RestController
//@RequestMapping("/api/theaters")
//public class TheaterController {
//
//    private final TheaterService theaterService;
//
//    public TheaterController(TheaterService theaterService) {
//        this.theaterService = theaterService;
//    }
//
//    @GetMapping
//    public List<Theater> getAllTheaters() {
//        return theaterService.getAllTheaters();
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<Theater> getTheaterById(@PathVariable UUID id) {
//        Optional<Theater> theater = theaterService.getTheaterById(id);
//        return theater.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
//    }
//
//    @PostMapping
//    public Theater createTheater(@RequestBody Theater theater) {
//        return theaterService.addTheater(theater);
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<Theater> updateTheater(@PathVariable UUID id, @RequestBody Theater theaterDetails) {
//        Optional<Theater> updatedTheater = theaterService.updateTheater(id, theaterDetails);
//        return updatedTheater.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteTheater(@PathVariable UUID id) {
//        boolean deleted = theaterService.deleteTheater(id);
//        return deleted ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
//    }
//}
