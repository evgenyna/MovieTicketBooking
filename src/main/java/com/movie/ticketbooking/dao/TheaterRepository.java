package com.movie.ticketbooking.dao;

import com.movie.ticketbooking.model.Theater;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TheaterRepository extends JpaRepository<Theater, UUID> {
}
