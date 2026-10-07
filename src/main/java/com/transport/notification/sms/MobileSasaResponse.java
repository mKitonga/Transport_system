package com.transport.notification.sms;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
class MobileSasaResponse {
    private boolean status;
    private String responseCode;
    private String message;
    private String bulkId;
}
