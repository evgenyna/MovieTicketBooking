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
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;

    public DatabaseInitializer(
            MovieRepository movieRepository,
            TheaterRepository theaterRepository,
            HallRepository hallRepository,
            ShowtimeRepository showtimeRepository,
            TicketRepository ticketRepository,
            UserRepository userRepository)
    {
        this.movieRepository = movieRepository;
        this.theaterRepository = theaterRepository;
        this.hallRepository = hallRepository;
        this.showtimeRepository = showtimeRepository;
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void initDatabase() {
        insertTheatersAndHalls();
        insertUsers();
        insertMovies();
        insertShowtimes();
        insertTickets();
    }

    private void insertTheatersAndHalls() {
        if (theaterRepository.count() == 0) {
            List<Theater> theaters = new ArrayList<>();

            for (int t = 1; t <= 5; t++) {
                Theater theater = new Theater("Theater " + t, "Location " + t, new ArrayList<>());

                // Create 10 halls for each theater
                for (int h = 1; h <= 10; h++) {
                    Hall hall = new Hall("Hall " + h, 150+h*10, theater);
                    theater.getHalls().add(hall); // Add hall to theater
                }

                theaters.add(theater);
            }

            theaterRepository.saveAll(theaters);
            System.out.println("Inserted Theaters & Halls into the database.");
        }
    }


    private void insertUsers() {
        if (userRepository.count() == 0) {
            List<User> users = List.of(
                    new User("Alice Johnson", "alice@example.com"),
                    new User("Bob Smith", "bob@example.com"),
                    new User("Charlie Davis", "charlie@example.com"),
                    new User("David White", "david@example.com"),
                    new User("Emma Wilson", "emma@example.com"),
                    new User("Frank Thomas", "frank@example.com"),
                    new User("Grace Harris", "grace@example.com"),
                    new User("Henry Martin", "henry@example.com"),
                    new User("Isabella Lee", "isabella@example.com"),
                    new User("Jack Brown", "jack@example.com")
            );
            userRepository.saveAll(users);
            System.out.println(" Inserted 10 Users into the database.");
        }
    }

    private void insertMovies() {
        if (movieRepository.count() == 0) {
            List<Movie> movies = List.of(
                    new Movie("Inception", "Sci-Fi", 148, 8.8, 2010),
                    new Movie("The Dark Knight", "Action", 152, 9.0, 2008),
                    new Movie("Interstellar", "Sci-Fi", 169, 8.6, 2014),
                    new Movie("Titanic", "Romance", 195, 7.8, 1997),
                    new Movie("Avatar", "Sci-Fi", 162, 7.9, 2009),
                    new Movie("The Matrix", "Sci-Fi", 136, 8.7, 1999),
                    new Movie("Gladiator", "Action", 155, 8.5, 2000),
                    new Movie("The Godfather", "Crime", 175, 9.2, 1972),
                    new Movie("Forrest Gump", "Drama", 142, 8.8, 1994),
                    new Movie("The Shawshank Redemption", "Drama", 144, 9.3, 1994)
            );
            movieRepository.saveAll(movies);
            System.out.println(" Inserted 10 Movies into the database.");
        }
    }

    private void insertShowtimes() {
        if (showtimeRepository.count() == 0) {
            List<Movie> movies = movieRepository.findAll();
            List<Hall> halls = hallRepository.findAll();
            List<Theater> theaters = theaterRepository.findAll();

            List<Showtime> showtimes = new ArrayList<>();

            LocalDateTime now = LocalDateTime.now();
            LocalDateTime twoMonthsLater = now.plusMonths(2);

            while (now.isBefore(twoMonthsLater)) {
                for (Movie movie : movies) {
                    for (Hall hall : halls) {
                        Theater theater = hall.getTheater();
                        LocalDateTime startTime = now;
                        LocalDateTime endTime = startTime.plusHours(2); // Assuming 2-hour movie duration

                        showtimes.add(new Showtime(movie, theater, hall, startTime, endTime));
                    }
                }
                now = now.plusDays(1); // Move to the next day
            }

            showtimeRepository.saveAll(showtimes);
            System.out.println(" Inserted Showtimes for the Next 2 Months into the database.");
        }
    }

    private void insertTickets() {
        if (ticketRepository.count() == 0) {
            List<User> users = userRepository.findAll();
            List<Showtime> showtimes = showtimeRepository.findAll();

            List<Ticket> tickets = new ArrayList<>();

            for (User user : users) {
                List<Showtime> userShowtimes = new ArrayList<>();
                int ticketsToCreate = Math.min(5, showtimes.size()); // Up to 5 tickets per user

                for (int i = 0; i < ticketsToCreate; i++) {
                    Showtime randomShowtime = getNonOverlappingShowtime(userShowtimes, showtimes);

                    if (randomShowtime != null) {
                        Hall hall = randomShowtime.getHall();
                        int seatNumber = getRandomSeat(hall.getCapacity());

                        tickets.add(new Ticket(randomShowtime, user, seatNumber));
                        userShowtimes.add(randomShowtime); // Add to user's showtimes to prevent overlap
                    }
                }
            }

            ticketRepository.saveAll(tickets);
            System.out.println(" Inserted up to 5 tickets per user for non-overlapping showtimes.");
        }
    }

    /**
     * Get a random non-overlapping showtime for a user.
     */
    private Showtime getNonOverlappingShowtime(List<Showtime> userShowtimes, List<Showtime> allShowtimes) {
        List<Showtime> availableShowtimes = allShowtimes.stream()
                .filter(showtime -> userShowtimes.stream()
                        .noneMatch(existing -> isOverlapping(existing, showtime))) // Ensure no time overlap
                .toList();

        return availableShowtimes.isEmpty() ? null : availableShowtimes.get((int) (Math.random() * availableShowtimes.size()));
    }

    /**
     * Check if two showtimes overlap.
     */
    private boolean isOverlapping(Showtime s1, Showtime s2) {
        return (s1.getStartTime().isBefore(s2.getEndTime()) && s2.getStartTime().isBefore(s1.getEndTime()));
    }

    /**
     * Generate a random seat number within hall capacity.
     */
    private int getRandomSeat(int capacity) {
        return (int) (Math.random() * capacity) + 1;
    }


}
