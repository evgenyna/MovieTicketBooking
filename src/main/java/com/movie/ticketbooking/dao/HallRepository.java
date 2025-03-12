package com.movie.ticketbooking.dao;

import com.movie.ticketbooking.model.Hall;
import com.movie.ticketbooking.model.Theater;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

public interface HallRepository extends JpaRepository<Hall, UUID> {
    boolean existsByNameAndTheaterId(String name, UUID theaterId);
    // Find all halls belonging to a given theater
    List<Hall> findByTheater(Theater theater);
}
