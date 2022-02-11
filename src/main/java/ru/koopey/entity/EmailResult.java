package ru.koopey.entity;

import java.util.Calendar;
import java.util.List;

public record EmailResult(String subject, String body, String fromEmail, String toEmail, Calendar date,
                          List<EmailAttachment> attachments) {}
