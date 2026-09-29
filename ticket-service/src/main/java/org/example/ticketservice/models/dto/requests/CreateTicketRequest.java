package org.example.ticketservice.models.dto.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import org.example.ticketservice.models.constants.TicketStatus;
import org.example.ticketservice.models.entities.Ticket;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public record CreateTicketRequest(
        @NotBlank(message = "Passenger name is required")
        @Size(max = 255, message = "Passenger name must not exceed 255 characters")
        String passengerName,

        @NotBlank(message = "Passenger email is required")
        @Size(max = 255, message = "Passenger email must not exceed 255 characters")
        String passengerEmail,

        @NotEmpty(message = "Ticket must contain at least one item")
        List<CreateTicketDetailRequest> items
) {
        public Long getTripId() {
            if (items == null || items.isEmpty()) {
                return null;
            }
            return items.get(0).tripId();
        }

        public Collection<Object> getTicketDetails() {
            return Collections.singleton(items);
        }

        public Ticket toTicketEntity() {
            return Ticket.builder()
                    .passengerName(passengerName)
                    .passengerEmail(passengerEmail)
                    .totalAmount(items.stream().mapToDouble(item -> item.seat() * 100).sum()) // Assuming each seat costs 100
                    .status(TicketStatus.PENDING) // Assuming default status is PENDING
                    .build();
        }
}
