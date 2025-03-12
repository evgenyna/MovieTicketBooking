package com.movie.ticketbooking.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Theater {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String name;
    private String location;

    @OneToMany(mappedBy = "theater", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<Hall> halls;

    public Theater(String name, String location, List<Hall> halls) {
        this.name = name;
        this.location = location;
        this.halls = halls;
    }
}

