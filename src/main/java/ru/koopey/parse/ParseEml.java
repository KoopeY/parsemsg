package ru.koopey.parse;

import com.sun.mail.util.BASE64DecoderStream;
import org.apache.commons.io.IOUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import ru.koopey.entity.EmailAttachment;
import ru.koopey.entity.EmailResult;

import javax.mail.BodyPart;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.Properties;

public class ParseEml implements IMessage {

    public ParseEml() {

    }

    @Override
    public EmailResult parseMsg(InputStream inputStream) throws Exception {
        EmailResult emailResult = new EmailResult();

        Session s = Session.getDefaultInstance(new Properties());
        MimeMessage message = new MimeMessage(s, inputStream);

        String subject = message.getSubject();
        String body = "";

        Multipart multiPart = (Multipart) message.getContent();
        for (int i = 0; i < multiPart.getCount(); i++) {
            BodyPart part = multiPart.getBodyPart(i);
            if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
                String encodeFileName = part.getFileName()
                        .replace("=?windows-1251?B?", "")
                        .replace("?=", "");
                byte[] valueDecoded= Base64.decodeBase64(encodeFileName.getBytes());
                String fileName = new String(valueDecoded, "windows-1251");

                InputStream is = part.getInputStream();

                /*File file = new File(fileName);
                FileOutputStream fos = new FileOutputStream(file);
                byte[] buf = new byte[4096];
                int bytesRead;
                while((bytesRead = is.read(buf))!=-1) {
                    fos.write(buf, 0, bytesRead);
                }
                fos.close();*/
                String base64 = new String(Base64.encodeBase64(IOUtils.toByteArray(is)), StandardCharsets.US_ASCII);

                emailResult.addEmailAttachment(new EmailAttachment(fileName, base64));
            } else {
                body = String.valueOf(part.getContent());
            }
        }

        Calendar sentDate = Calendar.getInstance();
        sentDate.setTime(message.getSentDate());

        String fromEmail = message.getFrom()[0].toString();
        String toEmail = message.getReplyTo()[0].toString();

        fromEmail = fromEmail.substring(fromEmail.indexOf("<") + 1, fromEmail.indexOf(">"));
        toEmail = toEmail.substring(toEmail.indexOf("<") + 1, toEmail.indexOf(">"));

        emailResult.setBody(body);
        emailResult.setSubject(subject);
        emailResult.setFromEmail(fromEmail);
        emailResult.setToEmail(toEmail);
        emailResult.setDate(sentDate);

        return emailResult;
    }
}
