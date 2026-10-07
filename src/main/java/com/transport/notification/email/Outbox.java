package com.transport.notification.email;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class Outbox {
    private Set<String> to;
    private Set<String> cc;
    private Set<String> bcc;
    private String replyTo;
    private String subject;
    private String bodyHtml;
    private String bodyPlainText;
    private Set<String> attachmentPaths;

    public Outbox(){}


    public Set<String> getTo() {
        return to == null ? new HashSet<>() : to;
    }

    public void addTo(String to) {
        if(this.to == null){
            this.to = new HashSet<>();
        }
        this.to.add(to);
    }

    public Set<String> getCc() {
        return cc == null ? new HashSet<>(): cc;
    }

    public void addCc(String cc){
        if(this.cc == null){
            this.cc = new HashSet<>();
        }
        this.cc.add(cc);
    }

    public Set<String> getBcc() {
        return bcc == null ? new HashSet<>() : bcc;
    }

    public void addBcc(String bcc){
        if(this.bcc == null){
            this.bcc = new HashSet<>();
        }
        this.bcc.add(bcc);
    }

    public String getBodyHtml() {
        return bodyHtml == null
                ? ""
                : bodyHtml.trim();
    }

    public String getBodyPlainText() {
        return bodyPlainText == null
                ? ""
                : bodyPlainText.trim();
    }

    public Set<String> getAttachmentPaths() {
        return attachmentPaths == null
                ? new HashSet<>()
                : attachmentPaths;
    }

    public void addToAttachments(Set<String> attachmentPaths) {
        Set<String> currentPaths = getAttachmentPaths();
        currentPaths.addAll(attachmentPaths);
        setAttachmentPaths(currentPaths);
    }

    public void addToAttachments(String attachmentPath){
        Set<String> currentPaths = getAttachmentPaths();
        currentPaths.add(attachmentPath);
        setAttachmentPaths(currentPaths);
    }
}
