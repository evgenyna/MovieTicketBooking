package com.movie.ticketbooking.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TicketEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private String eventType;

    @ManyToOne
    @JoinColumn(name = "ticket_id", referencedColumnName = "id", nullable = true) // ✅ Allow null values
    private Ticket ticket;

    @Column(nullable = false)
    private LocalDateTime eventTime;

    private String details;

    public TicketEvent(String eventType, Ticket ticket, String details) {
        this.eventType = eventType;
        this.ticket = ticket;  // Can be null if the ticket is deleted
        this.eventTime = LocalDateTime.now();
        this.details = details;
    }
}
