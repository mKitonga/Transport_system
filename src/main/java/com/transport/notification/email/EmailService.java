package com.transport.notification.email;

public interface EmailService {
    void send(Outbox outboxDTO);
}
