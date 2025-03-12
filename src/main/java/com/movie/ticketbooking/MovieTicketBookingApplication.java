package com.movie.ticketbooking;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
        info = @Info(
                title = "Movie Ticket Booking API",
                version = "1.0",
                description = "API documentation for the Movie Ticket Booking System"
        )
)
public class MovieTicketBookingApplication {

    public static void main(String[] args) {
        SpringApplication.run(MovieTicketBookingApplication.class, args);
    }

}
