package ru.koopey.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.koopey.parse.Parse;

import javax.mail.MessagingException;
import java.io.IOException;

@RestController
public class MainController {

    @RequestMapping(method = RequestMethod.POST)
    public String parseEmailFile(@RequestParam("file") MultipartFile[] msgFiles) throws IOException, MessagingException {
        Parse parse = new Parse();
        return parse.parseFiles(msgFiles).toString();
    }
}
