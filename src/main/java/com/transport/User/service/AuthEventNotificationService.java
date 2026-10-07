package com.transport.User.service;

import com.transport.User.entity.User;
import com.transport.liby.service.FormatUtil;
import com.transport.liby.service.Message;
import com.transport.notification.email.EmailService;
import com.transport.notification.email.Outbox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Set;

@Service
public class AuthEventNotificationService {
    private EmailService emailService;

    public void notifyOnPasswordChange(User user) {
        if (user.getEmail() == null) {
            return;
        }
        Outbox outbox = new Outbox();
        outbox.setTo(Set.of(user.getEmail()));
        outbox.setSubject(Message.get("password.change.email.subject"));
        String body = String.format(Message.get("password.change.email.html.body"),
                user.getName(),
                FormatUtil.getHumanReadableDateTime(LocalDateTime.now())
        );
        outbox.setBodyHtml(body);
        emailService.send(outbox);
    }

    @Autowired
    public void setEmailService(EmailService emailService) {
        this.emailService = emailService;
    }
}