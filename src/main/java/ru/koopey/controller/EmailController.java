package ru.koopey.controller;

import java.io.IOException;
import javax.mail.MessagingException;
import org.apache.poi.hsmf.exceptions.ChunkNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.koopey.parse.Parse;

@RestController
@RequestMapping("/email")
public class EmailController {

  private final Parse parse;

  public EmailController(Parse parse) {
    this.parse = parse;
  }

  @PostMapping(value = "/parse", produces = {"application/json;charset=UTF-8"})
  public String parseEmailFile(@RequestParam("file") MultipartFile[] msgFiles)
      throws IOException, ChunkNotFoundException, MessagingException {
    return parse.parseFiles(msgFiles).toString();
  }
}


