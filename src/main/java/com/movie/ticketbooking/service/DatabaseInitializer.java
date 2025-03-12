package com.movie.ticketbooking.service;

import com.movie.ticketbooking.dao.*;
import com.movie.ticketbooking.model.*;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class DatabaseInitializer {

    private final MovieRepository movieRepository;
    private final TheaterRepository theaterRepository;
    private final HallRepository hallRepository;
    private final ShowtimeRepository showtimeRepository;
    private final UserRepository userRepository;

    public DatabaseInitializer(MovieRepository movieRepository, TheaterRepository theaterRepository, HallRepository hallRepository, ShowtimeRepository showtimeRepository, UserRepository userRepository) {
        this.movieRepository = movieRepository;
        this.theaterRepository = theaterRepository;
        this.hallRepository = hallRepository;
        this.showtimeRepository = showtimeRepository;
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void initDatabase() {
        insertTheatersAndHalls();
        insertUsers();
        insertMovies();
        insertShowtimes();
    }

    private void insertTheatersAndHalls() {
        if (theaterRepository.count() == 0) {
            List<Theater> theaters = new ArrayList<>();

            for (int t = 1; t <= 3; t++) {
                List<Hall> halls = new ArrayList<>();
                Theater theater = new Theater("Theater " + t, "Location " + t, halls);

                // Create 3 halls for each theater
                for (int h = 1; h <= 3; h++) {
                    halls.add(new Hall("Hall " + h, 150, theater));
                }

                theaters.add(theater);
            }

            theaterRepository.saveAll(theaters);
            System.out.println("✅ Inserted Theaters & Halls into the database.");
        }
    }

    private void insertUsers() {
        if (userRepository.count() == 0) {
            List<User> users = List.of(
                    new User("Alice Johnson", "alice@example.com"),
                    new User("Bob Smith", "bob@example.com")
            );
            userRepository.saveAll(users);
            System.out.println("✅ Inserted Users into the database.");
        }
    }

    private void insertMovies() {
        if (movieRepository.count() == 0) {
            List<Movie> movies = List.of(
                    new Movie("Inception", "Sci-Fi", 148, 8.8, 2010),
                    new Movie("The Dark Knight", "Action", 152, 9.0, 2008)
            );
            movieRepository.saveAll(movies);
            System.out.println("✅ Inserted Movies into the database.");
        }
    }

    private void insertShowtimes() {
        if (showtimeRepository.count() == 0) {
            List<Movie> movies = movieRepository.findAll();
            List<Hall> halls = hallRepository.findAll();
            List<Theater> theaters = theaterRepository.findAll();

            List<Showtime> showtimes = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                Movie movie = movies.get(i % movies.size());
                Hall hall = halls.get(i % halls.size());
                Theater theater = hall.getTheater(); // Each hall belongs to a theater

                LocalDateTime startTime = LocalDateTime.now().plusDays(i);
                LocalDateTime endTime = startTime.plusHours(2); // Assuming 2-hour movie duration

                showtimes.add(new Showtime(movie, theater, hall, startTime, endTime));
            }

            showtimeRepository.saveAll(showtimes);
            System.out.println("✅ Inserted 10 showtimes into the database.");
        }
    }
}
