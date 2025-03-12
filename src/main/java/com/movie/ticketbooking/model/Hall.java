package com.movie.ticketbooking.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Hall {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String name;
    private int capacity;

    @ManyToOne
    @JoinColumn(name = "theater_id", nullable = false)
    @JsonIgnore // Hides "theater" field in JSON
    private Theater theater;

    @OneToMany(mappedBy = "hall", cascade = CascadeType.ALL, orphanRemoval = true) // Cascade delete
    @JsonIgnore // Hides "showtimes" field in JSON
    private List<Showtime> showtimes;

    public Hall(String name, int capacity, Theater theater) {
        this.name = name;
        this.capacity = capacity;
        this.theater = theater;
    }
}
