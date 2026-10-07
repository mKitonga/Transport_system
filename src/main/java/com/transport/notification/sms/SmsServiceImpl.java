package com.transport.notification.sms;

import com.transport.liby.service.BaseQueuingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
class SmsServiceImpl extends BaseQueuingService implements SmsService {
    private String smsAPiUrl;
    private String smsApiKey;
    private String smsSenderId;

    public void send(String recipient, String message) {
        List<String> recipients = new ArrayList<>();
        recipients.add(recipient);
        this.send(recipients, message);
    }

    public void send(List<String> recipients, String message) {
        SmsNotification smsNotification = new SmsNotification();
        smsNotification.setRecipients(recipients);
        smsNotification.setMessage(message);
        rabbitTemplate.convertAndSend(mainQueue.getName(), smsNotification);
    }

    @RabbitListener(queues = {"${sms.outbox.queue}"})
    void consumeSmsNotification(@Payload SmsNotification smsNotification) {
        log.info(smsNotification.getMessage());

        MobileSasaMessage sasaMessage = new MobileSasaMessage();
        sasaMessage.setSenderId(smsSenderId);
        sasaMessage.setMessage(smsNotification.getMessage());
        sasaMessage.setPhones(String.join(",", smsNotification.getRecipients()));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + smsApiKey);

        HttpEntity<MobileSasaMessage> entity = new HttpEntity<>(sasaMessage, headers);
        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<MobileSasaResponse> response = restTemplate.exchange(
                    smsAPiUrl,
                    HttpMethod.POST,
                    entity,
                    MobileSasaResponse.class
            );
            if(response.getStatusCode().isError()){
                log.error("Error sending sms: {}", response.getStatusCode());
            }
        }catch (Exception e){
            log.error("Error sending sms: ", e);
        }
    }

    @Value("${sms.outbox.queue}")
    void setSmsQueue(String queueName) {
        setMainQueue(queueName);
    }

    @Value("${sms.api.url}")
    void setSmsAPiUrl(String smsAPiUrl) {
        this.smsAPiUrl = smsAPiUrl;
    }

    @Value("${sms.api.key}")
    void setSmsApiKey(String smsApiKey) {
        this.smsApiKey = smsApiKey;
    }

    @Value("${sms.sender.id}")
    void setSmsSenderId(String smsSenderId) {
        this.smsSenderId = smsSenderId;
    }
}

