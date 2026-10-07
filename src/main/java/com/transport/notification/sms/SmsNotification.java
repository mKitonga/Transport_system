package com.transport.notification.sms;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
class SmsNotification {
    private List<String> recipients;
    private String message;
}
