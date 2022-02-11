package ru.koopey.parse;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import org.apache.commons.io.IOUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import ru.koopey.entity.EmailAttachment;
import ru.koopey.entity.EmailResult;

public class ParseEml implements IMessage {

  public ParseEml() {

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
        byte[] valueDecoded = Base64.decodeBase64(encodeFileName.getBytes());
        String fileName = new String(valueDecoded, "windows-1251");

        InputStream is = part.getInputStream();

        String base64 = new String(Base64.encodeBase64(IOUtils.toByteArray(is)), StandardCharsets.US_ASCII);

        attachments.add(new EmailAttachment(fileName, base64));
      } else {
        body = String.valueOf(part.getContent());
      }
    }

    Calendar sentDate = Calendar.getInstance();
    sentDate.setTime(message.getSentDate());

    String fromEmail = "";
    String toEmail = "";

    if (message.getFrom().length > 0) {
      fromEmail = message.getFrom()[0].toString();
    }
    if (message.getReplyTo().length > 0) {
      toEmail = message.getReplyTo()[0].toString();
    }

    return new EmailResult(body, subject, fromEmail, toEmail, sentDate, attachments);
  }
}
