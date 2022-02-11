package ru.koopey.parse;

import java.io.IOException;
import java.io.InputStream;
import javax.mail.MessagingException;
import org.apache.poi.hsmf.exceptions.ChunkNotFoundException;
import ru.koopey.entity.EmailResult;

public interface IMessage {
  EmailResult parseMsg(InputStream inputStream) throws IOException, ChunkNotFoundException, MessagingException;
}
