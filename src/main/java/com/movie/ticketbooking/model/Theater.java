package com.movie.ticketbooking.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Theater {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String name;
    private String location;

    @OneToMany(mappedBy = "theater", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonIgnore  // Prevents infinite recursion
    private List<Hall> halls = new ArrayList<>();

    // Constructor with halls for DatabaseInitializer
    public Theater(String name, String location, List<Hall> halls) {
        this.name = name;
        this.location = location;
        //this.halls = halls != null ? halls : new ArrayList<>();
    }
}
