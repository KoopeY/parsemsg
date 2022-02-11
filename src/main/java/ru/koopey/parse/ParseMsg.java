package ru.koopey.parse;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import org.apache.poi.hsmf.MAPIMessage;
import org.apache.poi.hsmf.exceptions.ChunkNotFoundException;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Component;
import ru.koopey.entity.EmailAttachment;
import ru.koopey.entity.EmailResult;

@Component
public class ParseMsg implements Parserable {

  @Override
  public EmailResult parseMsg(InputStream inputStream) throws IOException, ChunkNotFoundException {
    MAPIMessage msg = new MAPIMessage(inputStream);
    msg.guess7BitEncoding();

    var attachments = Arrays.stream(msg.getAttachmentFiles())
        .map(attachment ->
            new EmailAttachment(
                attachment.getAttachLongFileName().getValue(),
                new String(Base64.encodeBase64(attachment.getAttachData().getValue()), StandardCharsets.US_ASCII)
            )
        )
        .toList();

    return new EmailResult(
        msg.getSubject(),
        msg.getTextBody(),
        msg.getRecipientEmailAddress(),
        msg.getRecipientEmailAddress(),
        msg.getMessageDate(),
        attachments
    );
  }

  @Override
  public Extensions extension() {
    return Extensions.MSG;
  }
}
