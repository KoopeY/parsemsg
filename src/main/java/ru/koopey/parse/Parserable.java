package ru.koopey.parse;

import org.apache.poi.hsmf.exceptions.ChunkNotFoundException;
import ru.koopey.entity.EmailResult;

import javax.mail.MessagingException;
import java.io.IOException;
import java.io.InputStream;

public interface Parserable {

  EmailResult parseMsg(InputStream inputStream) throws IOException, ChunkNotFoundException, MessagingException;

  Extensions extension();
}
