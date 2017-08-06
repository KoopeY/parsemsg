package ru.koopey.parse;

import org.apache.poi.hsmf.MAPIMessage;
import org.apache.poi.hsmf.datatypes.AttachmentChunks;
import org.apache.tomcat.util.codec.binary.Base64;
import ru.koopey.entity.EmailAttachment;
import ru.koopey.entity.EmailResult;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class ParseMsg implements IMessage {

    public ParseMsg() {
    }

    @Override
    public EmailResult parseMsg(InputStream inputStream) throws Exception {
        EmailResult emailResult = new EmailResult();
        MAPIMessage msg = new MAPIMessage(inputStream);
        msg.guess7BitEncoding();

        emailResult.setSubject(msg.getSubject());
        emailResult.setBody(msg.getTextBody());
        emailResult.setFromEmail(msg.getRecipientEmailAddress());
        emailResult.setToEmail(msg.getRecipientEmailAddress());
        emailResult.setDate(msg.getMessageDate());

        for (AttachmentChunks attachment : msg.getAttachmentFiles()) {
            byte[] fileContent = attachment.attachData.getValue();
            String fileName = attachment.attachLongFileName.toString();
            String base64 = new String(Base64.encodeBase64(fileContent), StandardCharsets.US_ASCII);

            emailResult.addEmailAttachment(new EmailAttachment(fileName, base64));
        }

        return emailResult;
    }
}
