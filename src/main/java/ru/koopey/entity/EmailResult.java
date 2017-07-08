package ru.koopey.entity;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class EmailResult {
    private String subject;
    private String body;
    private String fromEmail;
    private String toEmail;
    private Calendar date;
    private List<EmailAttachment> emailAttachmentList = new ArrayList<>();

    public EmailResult() {}

    public EmailResult(String subject, String body, String fromEmail, String toEmail, Calendar date) {
        this.subject = subject;
        this.body = body;
        this.fromEmail = fromEmail;
        this.toEmail = toEmail;
        this.date = date;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setFromEmail(String fromEmail) {
        this.fromEmail = fromEmail;
    }

    public void setToEmail(String toEmail) {
        this.toEmail = toEmail;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public void addEmailAttachment(EmailAttachment emailAttachment) {
        this.emailAttachmentList.add(emailAttachment);
    }

    public String getSubject() {
        return subject;
    }

    public String getBody() {
        return body;
    }

    public String getFromEmail() {
        return fromEmail;
    }

    public String getToEmail() {
        return toEmail;
    }

    public Calendar getDate() {
        return date;
    }

    public Object[] getEmailAttachmentList() {
        return emailAttachmentList.toArray();
    }

    public String toString() {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            return objectMapper.writeValueAsString(this);
        } catch(Exception e) {
            System.out.println("Error while make json EmailResult " + e.getMessage());
        }

        return super.toString();
    }
}
