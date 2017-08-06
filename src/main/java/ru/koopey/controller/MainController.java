package ru.koopey.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.koopey.parse.Parse;

import javax.mail.MessagingException;
import java.io.IOException;

@RestController
@RequestMapping("/api")
public class MainController {

    @RequestMapping(value = "/parseEmailFile", method = RequestMethod.POST, produces = { "application/json;charset=UTF-8" })
    public String parseEmailFile(@RequestParam("file") MultipartFile[] msgFiles) throws IOException, MessagingException {
        Parse parse = new Parse();
        return parse.parseFiles(msgFiles).toString();
    }
}
