package com.movie.ticketbooking.dao;

import com.movie.ticketbooking.model.Hall;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface HallRepository extends JpaRepository<Hall, UUID> {
    boolean existsByNameAndTheaterId(String name, UUID theaterId);
}
