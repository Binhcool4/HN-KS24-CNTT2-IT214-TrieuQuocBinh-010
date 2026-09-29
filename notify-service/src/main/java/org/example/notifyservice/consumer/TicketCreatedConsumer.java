package org.example.notifyservice.consumer;

import org.example.notifyservice.service.EmailService;

public class TicketCreatedConsumer {
    private final EmailService emailService;

    public TicketCreatedConsumer(EmailService emailService) {
        this.emailService = emailService;
    }

    public void consume(String email) {
        throw new UnsupportedOperationException();
    }
}
