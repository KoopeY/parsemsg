package ru.koopey.parse;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.apache.commons.io.IOUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Component;
import ru.koopey.entity.EmailAttachment;
import ru.koopey.entity.EmailResult;

@Component
public class ParseEml implements Parserable {

  private String getEmailAddress(Address address) {
    if (address instanceof InternetAddress internetAddress) {
      return internetAddress.getAddress();
    } else {
      return address.toString();
    }
  }

  @Override
  public EmailResult parseMsg(InputStream inputStream) throws IOException, MessagingException {
    final var s = Session.getDefaultInstance(new Properties());
    final var message = new MimeMessage(s, inputStream);

    final var subject = message.getSubject();
    var body = "";

    final List<EmailAttachment> attachments = new ArrayList<>();
    final var multiPart = (Multipart) message.getContent();
    for (var i = 0; i < multiPart.getCount(); i++) {
      final var part = multiPart.getBodyPart(i);
      if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
        final var encodeFileName = part.getFileName()
            .replace("=?windows-1251?B?", "")
            .replace("?=", "");
        final var valueDecoded = Base64.decodeBase64(encodeFileName);
        final var fileName = new String(valueDecoded, StandardCharsets.UTF_8);

        final var is = part.getInputStream();

        final var fileContent = Base64.encodeBase64String(IOUtils.toByteArray(is));

        attachments.add(new EmailAttachment(fileName, fileContent));
      } else {
        body = String.valueOf(part.getContent());
      }
    }

    final var sentDate = message.getSentDate().toInstant();

    final var fromEmail = Arrays.stream(message.getFrom())
        .filter(address -> address instanceof InternetAddress)
        .map(this::getEmailAddress)
        .collect(Collectors.joining(","));

    final var toEmail = Arrays.stream(message.getReplyTo())
        .filter(address -> address instanceof InternetAddress)
        .map(this::getEmailAddress)
        .collect(Collectors.joining(","));

    return new EmailResult(body, subject, fromEmail, toEmail, sentDate, attachments);
  }

  @Override
  public Extensions extension() {
    return Extensions.EML;
  }
}
