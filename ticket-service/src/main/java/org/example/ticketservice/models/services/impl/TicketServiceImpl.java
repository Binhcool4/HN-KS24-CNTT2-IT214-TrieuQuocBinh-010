package org.example.ticketservice.models.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.ticketservice.models.constants.TicketStatus;
import org.example.ticketservice.models.dto.requests.CreateTicketRequest;
import org.example.ticketservice.models.dto.responses.TicketDetailResponse;
import org.example.ticketservice.models.dto.responses.TicketResponse;
import org.example.ticketservice.models.entities.Ticket;
import org.example.ticketservice.models.entities.TicketDetail;
import org.example.ticketservice.models.repositories.TicketDetailRepository;
import org.example.ticketservice.models.repositories.TicketRepository;
import org.example.ticketservice.models.services.TicketService;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.lang.module.ResolvedModule;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl<ResolvedItem> implements TicketService {
    private static final String TICKET_NOT_FOUND = "Ticket not found";
    private final TicketRepository ticketRepository;
    private final TicketDetailRepository ticketDetailRepository;
    private final TripGatewayService tripGatewayService;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Override
    @Transactional
    public TicketResponse createTicket(CreateTicketRequest request) {
     List<ResolvedItem> resolvedItems = request.items().stream()
                .map(item -> (ResolvedItem) tripGatewayService.getTripById(item.tripId()))
                .toList();

     double total = resolvedItems.stream()
                .mapToDouble(item -> item.seat() * 100) // Assuming each seat costs 100
                .sum();

     Ticket ticket = TicketRepository.save(Ticket.builder()
                .passengerName(request.passengerName())
                .passengerEmail(request.passengerEmail())
                .totalAmount(total)
                .status(TicketStatus.PENDING) // Assuming default status is PENDING
                .build());

     List<TicketDetail> ticketDetails = resolvedItems.stream()
                .map(item -> TicketDetail.builder()
                        .ticket(ticket)
                        .tripId(item.tripId())
                        .seat(item.seat())
                        .build())
                .toList();

     List<TicketDetail> savedDetails = ticketDetailRepository.saveAll(ticketDetails);
     List<TicketDetailResponse> itemResponses = savedDetails.stream()
                .map(detail -> new TicketDetailResponse(detail.getTripId(), detail.getSeat()))
                .toList();

     return new TicketResponse(ticket.getId(), ticket.getPassengerName(), ticket.getPassengerEmail(), itemResponses, ticket.getTotalAmount());
        }
}
