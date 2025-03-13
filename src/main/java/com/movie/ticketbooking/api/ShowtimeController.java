package com.movie.ticketbooking.api;

import com.movie.ticketbooking.dto.ShowtimeRequestDTO;
import com.movie.ticketbooking.model.Hall;
import com.movie.ticketbooking.model.Showtime;
import com.movie.ticketbooking.model.Movie;
import com.movie.ticketbooking.service.ShowtimeService;
import com.movie.ticketbooking.service.MovieService;
import com.movie.ticketbooking.service.HallService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/showtimes")
@Tag(name = "Showtimes API", description = "Manage Showtimes")
public class ShowtimeController {

    private final ShowtimeService showtimeService;
    private final MovieService movieService;
    private final HallService hallService;

    public ShowtimeController(ShowtimeService showtimeService, MovieService movieService, HallService hallService) {
        this.showtimeService = showtimeService;
        this.movieService = movieService;
        this.hallService = hallService;
    }

    //  Get all showtimes
    @GetMapping
    @Operation(summary = "Get all showtimes", description = "Retrieve a list of all scheduled showtimes.")
    public ResponseEntity<List<Showtime>> getAllShowtimes() {
        return ResponseEntity.ok(showtimeService.getAllShowtimes());
    }

    //  Get a specific showtime by ID
    @GetMapping("/{showtimeId}")
    @Operation(summary = "Get a showtime by ID", description = "Retrieve details of a specific showtime by its ID.")
    public ResponseEntity<?> getShowtimeById(@PathVariable String showtimeId) {
        try {
            UUID uuid = UUID.fromString(showtimeId);
            Optional<Showtime> showtime = showtimeService.getShowtimeById(uuid);
            return showtime.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid UUID format.");
        }
    }

    //  Get only halls where a specific movie is screening
    @GetMapping("/halls/{movieId}")
    @Operation(summary = "Get halls screening a movie", description = "Retrieve only the halls where a given movie is currently being shown.")
    public ResponseEntity<List<Hall>> getHallsScreeningMovie(@PathVariable String movieId) {
        try {
            UUID uuid = UUID.fromString(movieId);
            List<Hall> halls = showtimeService.getHallsByMovie(uuid);
            return ResponseEntity.ok(halls);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    //  Create a new showtime
    @PostMapping
    @Operation(summary = "Create a new showtime", description = "Schedule a new movie showtime in a specific hall.")
    public ResponseEntity<?> createShowtime(@RequestBody ShowtimeRequestDTO requestDTO) {
        try {
            UUID movieId = UUID.fromString(requestDTO.getMovieId());
            UUID hallId = UUID.fromString(requestDTO.getHallId());

            Optional<Movie> movie = movieService.getMovieById(movieId);
            Optional<Hall> hall = hallService.getHallById(hallId);

            if (movie.isEmpty() || hall.isEmpty()) {
                return ResponseEntity.badRequest().body("Invalid Movie or Hall ID.");
            }

            Showtime newShowtime = new Showtime(
                    movie.get(),
                    hall.get().getTheater(),
                    hall.get(),
                    requestDTO.getStartTime(),
                    requestDTO.getEndTime()
            );

            return ResponseEntity.ok(showtimeService.addShowtime(newShowtime));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid UUID format.");
        }
    }

    //  Update an existing showtime
    @PutMapping("/{showtimeId}")
    @Operation(summary = "Update a showtime", description = "Modify the details of an existing showtime.")
    public ResponseEntity<?> updateShowtime(@PathVariable String showtimeId, @RequestBody ShowtimeRequestDTO requestDTO) {
        try {
            UUID uuid = UUID.fromString(showtimeId);
            UUID movieId = UUID.fromString(requestDTO.getMovieId());
            UUID hallId = UUID.fromString(requestDTO.getHallId());

            Optional<Movie> movie = movieService.getMovieById(movieId);
            Optional<Hall> hall = hallService.getHallById(hallId);

            if (movie.isEmpty() || hall.isEmpty()) {
                return ResponseEntity.badRequest().body("Invalid Movie or Hall ID.");
            }

            return showtimeService.updateShowtime(uuid, requestDTO, movie.get(), hall.get())
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid UUID format.");
        }
    }

    //  Delete a showtime
    @DeleteMapping("/{showtimeId}")
    @Operation(summary = "Delete a showtime", description = "Remove a showtime from the schedule.")
    public ResponseEntity<?> deleteShowtime(@PathVariable String showtimeId) {
        try {
            UUID uuid = UUID.fromString(showtimeId);
            boolean deleted = showtimeService.deleteShowtime(uuid);
            if (deleted) {
                return ResponseEntity.ok("Showtime deleted successfully.");
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid UUID format.");
        }
    }
}
