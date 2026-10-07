package com.transport.notification.sms;

import java.io.IOException;
import java.util.List;

public interface SmsService {
    void send(String recipient, String message);
    void send(List<String> recipients, String message) throws IOException;
}
