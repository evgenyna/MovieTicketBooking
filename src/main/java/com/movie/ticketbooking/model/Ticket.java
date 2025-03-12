package com.movie.ticketbooking.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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

    // Alternative constructor (JPA will auto-generate ID)
    public Ticket(Showtime showtime, User user, int seatNumber) {
        this.showtime = showtime;
        this.user = user;
        this.seatNumber = seatNumber;
    }
}
