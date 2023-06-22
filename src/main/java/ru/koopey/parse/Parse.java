package ru.koopey.parse;

import org.apache.poi.hsmf.exceptions.ChunkNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.koopey.entity.EmailResult;

import javax.mail.MessagingException;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class Parse {

  private final List<Parserable> parsers;

  public Parse(List<Parserable> parsers) {
    this.parsers = parsers;
  }

  public EmailResult parseFiles(MultipartFile[] files) throws IOException, ChunkNotFoundException, MessagingException {

    if (files.length > 0) {
      final var fileFormat = Optional.ofNullable(files[0].getOriginalFilename())
          .map(str -> str.split("\\."))
          .orElseThrow(FileNotFoundException::new);

      final var inputStream = new ByteArrayInputStream(files[0].getBytes());

      final var parser = parsers.stream()
          .filter(p -> p.extension().extension().equalsIgnoreCase(fileFormat[fileFormat.length - 1]))
          .findFirst();

      if (parser.isPresent()) {
        return parser.get().parseMsg(inputStream);
      }
    }

    throw new FileNotFoundException();
  }
}
