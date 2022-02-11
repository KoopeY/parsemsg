package ru.koopey.parse;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import javax.mail.MessagingException;
import org.apache.poi.hsmf.exceptions.ChunkNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.koopey.entity.EmailResult;

@Service
public class Parse {

  private final List<Parserable> parsers;

  public Parse(List<Parserable> parsers) {
    this.parsers = parsers;
  }

  public EmailResult parseFiles(MultipartFile[] files) throws IOException, ChunkNotFoundException, MessagingException {

    if (files.length > 0) {
      String[] fileFormat = Optional.ofNullable(files[0].getOriginalFilename())
          .map(str -> str.split("\\."))
          .orElseThrow(FileNotFoundException::new);

      InputStream is = new ByteArrayInputStream(files[0].getBytes());

      final Optional<Parserable> parser = parsers.stream()
          .filter(p -> p.extension().extension().equalsIgnoreCase(fileFormat[fileFormat.length - 1]))
          .findFirst();

      if (parser.isPresent()) {
        return parser.get().parseMsg(is);
      }
    }

    throw new FileNotFoundException();
  }
}
