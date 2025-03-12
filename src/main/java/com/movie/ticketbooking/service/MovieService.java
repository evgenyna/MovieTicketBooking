package com.movie.ticketbooking.service;

import com.movie.ticketbooking.dao.MovieRepository;
import com.movie.ticketbooking.model.Movie;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class MovieService {

    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    // Get all movies
    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    // Get movie by ID
    public Optional<Movie> getMovieById(UUID id) {
        return movieRepository.findById(id);
    }

    // Save a new movie
    public Movie saveMovie(Movie movie) {
        return movieRepository.save(movie);
    }

    // Update movie details
    public Optional<Movie> updateMovie(UUID id, Movie movieDetails) {
        return movieRepository.findById(id).map(existingMovie -> {
            existingMovie.setTitle(movieDetails.getTitle());
            existingMovie.setGenre(movieDetails.getGenre());
            existingMovie.setDuration(movieDetails.getDuration());
            existingMovie.setRating(movieDetails.getRating());
            existingMovie.setReleaseYear(movieDetails.getReleaseYear());
            return movieRepository.save(existingMovie);
        });
    }

    // Delete a movie
    public boolean deleteMovie(UUID id) {
        if (movieRepository.existsById(id)) {
            movieRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
