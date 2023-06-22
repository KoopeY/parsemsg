package ru.koopey.entity;

import java.time.Instant;
import java.util.List;

public record EmailResult(String subject, String body,
                          String fromEmail, String toEmail,
                          Instant date, List<EmailAttachment> attachments) {
}
