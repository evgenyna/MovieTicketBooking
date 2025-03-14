package com.movie.ticketbooking.service;

import com.movie.ticketbooking.dao.TicketEventRepository;
import com.movie.ticketbooking.model.TicketEvent;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketEventService {

    private final TicketEventRepository ticketEventRepository;

    public TicketEventService(TicketEventRepository ticketEventRepository) {
        this.ticketEventRepository = ticketEventRepository;
    }

    public List<TicketEvent> getAllTicketEvents() {
        return ticketEventRepository.findAll();
    }
}
