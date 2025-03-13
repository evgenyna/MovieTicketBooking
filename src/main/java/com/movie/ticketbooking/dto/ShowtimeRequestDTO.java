package com.movie.ticketbooking.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ShowtimeRequestDTO {
    private String movieId;
    private String hallId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public ShowtimeRequestDTO() {}

    public ShowtimeRequestDTO(String movieId, String hallId, LocalDateTime startTime, LocalDateTime endTime) {
        this.movieId = movieId;
        this.hallId = hallId;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
