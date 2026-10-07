package com.transport.notification.sms;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
class MobileSasaMessage {
    @JsonProperty("senderID")
    private String senderId = "STS LTD";

    private String message;
    private String phones;
}
