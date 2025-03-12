package com.movie.ticketbooking.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Showtime {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

    @ManyToOne
    @JoinColumn(name = "theater_id", nullable = false)
    private Theater theater;

    @ManyToOne
    @JoinColumn(name = "hall_id", nullable = false)
    private Hall hall;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    // Constructor without UUID (JPA will auto-generate ID)
    public Showtime(Movie movie, Theater theater, Hall hall, LocalDateTime startTime) {
        this.movie = movie;
        this.theater = theater;
        this.hall = hall;
        this.startTime = startTime;
        this.endTime = startTime.plusHours(2); // Default to 2-hour movie if not specified
    }

    // Constructor with explicit endTime
    public Showtime(Movie movie, Theater theater, Hall hall, LocalDateTime startTime, LocalDateTime endTime) {
        this.movie = movie;
        this.theater = theater;
        this.hall = hall;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
