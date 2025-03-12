package com.movie.ticketbooking.model;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "showtime_id", nullable = false)
    private Showtime showtime;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private int seatNumber;

    // Default constructor (required by JPA)
    public Ticket() {
    }

    // Correct constructor with UUID
    public Ticket(UUID id, Showtime showtime, User user, int seatNumber) {
        this.id = id;
        this.showtime = showtime;
        this.user = user;
        this.seatNumber = seatNumber;
    }

    // Alternative constructor without UUID (JPA will auto-generate it)
    public Ticket(Showtime showtime, User user, int seatNumber) {
        this.showtime = showtime;
        this.user = user;
        this.seatNumber = seatNumber;
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Showtime getShowtime() {
        return showtime;
    }

    public void setShowtime(Showtime showtime) {
        this.showtime = showtime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(int seatNumber) {
        this.seatNumber = seatNumber;
    }
}
