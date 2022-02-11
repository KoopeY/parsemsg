package ru.koopey.parse;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import javax.mail.MessagingException;
import org.apache.poi.hsmf.exceptions.ChunkNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.koopey.entity.EmailResult;

@Service
public class Parse {

  public EmailResult parseFiles(MultipartFile[] files) throws IOException, ChunkNotFoundException, MessagingException {

    IMessage parseMessage;
    if (files.length > 0) {
      String[] fileFormat = Optional.ofNullable(files[0].getOriginalFilename())
          .map(str -> str.split("\\."))
          .orElseThrow(FileNotFoundException::new);

      InputStream is = new ByteArrayInputStream(files[0].getBytes());

      if ("msg".equalsIgnoreCase(fileFormat[fileFormat.length - 1])) {
        parseMessage = new ParseMsg();
      } else {
        parseMessage = new ParseEml();
      }

      return parseMessage.parseMsg(is);
    } else {
      throw new FileNotFoundException();
    }
  }
}
