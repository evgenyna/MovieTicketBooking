package com.movie.ticketbooking.api;

import com.movie.ticketbooking.model.Movie;
import com.movie.ticketbooking.service.MovieService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/movies")
@Tag(name = "Movies API", description = "Endpoints for managing movies")
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    // Get all movies
    @GetMapping
    @Operation(summary = "Get all movies", description = "Retrieve a list of all available movies.")
    public ResponseEntity<List<Movie>> getAllMovies() {
        return ResponseEntity.ok(movieService.getAllMovies());
    }

    // Get movie by ID
    @GetMapping("/{id}")
    @Operation(summary = "Get a movie by ID", description = "Retrieve details of a specific movie by its ID.")
    public ResponseEntity<?> getMovieById(@PathVariable String id) {
        try {
            UUID uuid = UUID.fromString(id);
            return movieService.getMovieById(uuid)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid movie ID format.");
        }
    }

    // Create a new movie
    @PostMapping
    @Operation(summary = "Create a new movie", description = "Add a new movie to the system.")
    public ResponseEntity<Movie> createMovie(@RequestBody Movie movie) {
        return ResponseEntity.ok(movieService.saveMovie(movie));
    }

    // Update a movie
    @PutMapping("/{id}")
    @Operation(summary = "Update a movie", description = "Modify an existing movie's details using its ID.")
    public ResponseEntity<?> updateMovie(@PathVariable String id, @RequestBody Movie movieDetails) {
        try {
            UUID uuid = UUID.fromString(id);
            return movieService.updateMovie(uuid, movieDetails)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid movie ID format.");
        }
    }

    // Delete a movie
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a movie", description = "Remove a movie from the system using its ID. (Next update: will work with ticket events)")
    public ResponseEntity<?> deleteMovie(@PathVariable String id) {
        try {
            UUID uuid = UUID.fromString(id);
            return movieService.deleteMovie(uuid)
                    ? ResponseEntity.noContent().build()
                    : ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid movie ID format.");
        }
    }
}
