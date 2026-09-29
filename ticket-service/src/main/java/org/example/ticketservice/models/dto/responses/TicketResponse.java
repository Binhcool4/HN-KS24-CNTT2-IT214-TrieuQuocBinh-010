package org.example.ticketservice.models.dto.responses;

import org.example.ticketservice.models.constants.TicketStatus;
import org.example.ticketservice.models.entities.Ticket;

import java.util.List;

public record TicketResponse(
        Long id,
        String passengerName,
        String passengerEmail,
        Double totalAmount,
        TicketStatus status,
        List<TicketDetailResponse> details
) {
    public static TicketResponse fromTicketEntity(Ticket ticket, List<Object> ticketDetails) {
        List<TicketDetailResponse> detailResponses = ticketDetails.stream()
                .map(detail -> (TicketDetailResponse) detail)
                .toList();

        return new TicketResponse(
                ticket.getId(),
                ticket.getPassengerName(),
                ticket.getPassengerEmail(),
                ticket.getTotalAmount(),
                ticket.getStatus(),
                detailResponses
        );
    }
}
