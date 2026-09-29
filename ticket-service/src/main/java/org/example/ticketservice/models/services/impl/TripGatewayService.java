package org.example.ticketservice.models.services.impl;

import lombok.RequiredArgsConstructor;
import org.example.ticketservice.clients.TripClient;
import org.example.ticketservice.models.dto.responses.TripResponse;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TripGatewayService {
    private final TripClient tripClient;

    public TripResponse getTripById(Long tripId) {
        throw new UnsupportedOperationException();
    }

}
