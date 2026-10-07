package com.transport.notification.email;

import com.transport.liby.service.BaseQueuingService;
import io.micrometer.common.util.StringUtils;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Date;
import java.util.Set;

@Service
@Slf4j
public class EmailServiceImpl extends BaseQueuingService implements EmailService {
    private JavaMailSender javaMailSender;
    private String sender;
    private String emailTemplatePath;

    @Override
    public void send(Outbox outboxDTO) {
        rabbitTemplate.convertAndSend(mainQueue.getName(), outboxDTO);
    }

    @RabbitListener(queues = {"${email.outbox.queue}"})
    void consumeEmailNotification(@Payload Outbox outboxDTO) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeHelper = new MimeMessageHelper(mimeMessage, true);

        String[] to = outboxDTO.getTo().toArray(new String[0]);
        mimeHelper.setFrom(sender);
        mimeHelper.setTo(to);
        mimeHelper.setSentDate(new Date());

        //Add cc recipients
        if(!outboxDTO.getCc().isEmpty()) {
            String[] cc = outboxDTO.getCc().toArray(new String[0]);
            mimeHelper.setCc(cc);
        }

        //Add bcc recipients
        if(!outboxDTO.getBcc().isEmpty()) {
            String[] bcc = outboxDTO.getBcc().toArray(new String[0]);
            mimeHelper.setBcc(bcc);
        }

        //Add reply to address
        if(StringUtils.isNotBlank(outboxDTO.getReplyTo())){
            mimeHelper.setReplyTo(outboxDTO.getReplyTo());
        }

        mimeHelper.setSubject(outboxDTO.getSubject());

        String bodyHtml = "";
        if(!outboxDTO.getBodyHtml().isEmpty()) {
            String template = getMailTemplate();
            if(!template.isEmpty()) {
                String BODY_PLACEHOLDER_KEY = "{body}";
                bodyHtml = template.replace(BODY_PLACEHOLDER_KEY, outboxDTO.getBodyHtml());
            }else{
                bodyHtml = outboxDTO.getBodyHtml();
            }
        }

        String bodyPlainText = outboxDTO.getBodyPlainText();

        //Both html and plain text body
        if(!bodyHtml.isEmpty() && !bodyPlainText.isEmpty()) {
            mimeHelper.setText(bodyPlainText, bodyHtml);
        }

        //Html body only
        if(!bodyHtml.isEmpty() && bodyPlainText.isEmpty()) {
            mimeHelper.setText(bodyHtml, true);
        }

        //Plain text body only
        if(bodyHtml.isEmpty() && !bodyPlainText.isEmpty()) {
            mimeHelper.setText(bodyPlainText);
        }

        //Add attachments
        Set<String> attachments = outboxDTO.getAttachmentPaths();
        if(attachments != null && !attachments.isEmpty()) {
            for(String path: attachments) {
                File f = new File(path);
                mimeHelper.addAttachment(f.getName(), f);
            }
        }
        javaMailSender.send(mimeMessage);
    }

    private String getMailTemplate() {
        String template = "";
        try {
            File file = new File(emailTemplatePath);
            template = Files.readString(file.toPath(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("Failed to read email template", e);
        }
        return template;
    }

    @Value("${email.outbox.queue}")
    void setEmailQueue(String queue){
        setMainQueue(queue);
    }

    @Autowired
    public void setJavaMailSender(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Value("${spring.mail.username}")
    public void setSender(String sender) {
        this.sender = sender;
    }

    @Value("${email.template.path}")
    public void setEmailTemplatePath(String emailTemplatePath) {
        this.emailTemplatePath = emailTemplatePath;
    }
}