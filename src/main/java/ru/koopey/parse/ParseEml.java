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
    Session s = Session.getDefaultInstance(new Properties());
    MimeMessage message = new MimeMessage(s, inputStream);

    String subject = message.getSubject();
    String body = "";

    List<EmailAttachment> attachments = new ArrayList<>();
    Multipart multiPart = (Multipart) message.getContent();
    for (int i = 0; i < multiPart.getCount(); i++) {
      BodyPart part = multiPart.getBodyPart(i);
      if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
        String encodeFileName = part.getFileName()
            .replace("=?windows-1251?B?", "")
            .replace("?=", "");
        byte[] valueDecoded = Base64.decodeBase64(encodeFileName);
        String fileName = new String(valueDecoded, StandardCharsets.UTF_8);

        InputStream is = part.getInputStream();

        String base64 = Base64.encodeBase64String(IOUtils.toByteArray(is));

        attachments.add(new EmailAttachment(fileName, base64));
      } else {
        body = String.valueOf(part.getContent());
      }
    }

    Calendar sentDate = Calendar.getInstance();
    sentDate.setTime(message.getSentDate());

    String fromEmail = Arrays.stream(message.getFrom())
        .filter(address -> address instanceof InternetAddress)
        .map(this::getEmailAddress)
        .collect(Collectors.joining(","));

    String toEmail = Arrays.stream(message.getReplyTo())
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
