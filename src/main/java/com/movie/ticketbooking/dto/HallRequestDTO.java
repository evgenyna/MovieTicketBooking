package com.movie.ticketbooking.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class HallRequestDTO {
    private String name;
    private int capacity;
    private UUID theaterId; //Accept theater ID instead of full Theater object
}
