package ru.koopey.parse;

import org.springframework.web.multipart.MultipartFile;
import ru.koopey.entity.EmailResult;

import javax.mail.MessagingException;
import java.io.*;

public class Parse {

    public Parse() {
    }

    public EmailResult parseFiles(MultipartFile[] file) throws MessagingException, IOException {

        EmailResult emailResult = new EmailResult();
        IMessage parseMessage;
        if (file.length > 0) {
            String[] fileFormat = file[0].getOriginalFilename().split("\\.");

            InputStream is = new ByteArrayInputStream(file[0].getBytes());

            switch(fileFormat[fileFormat.length - 1].toLowerCase()) {
                case "msg":
                    parseMessage = new ParseMsg();
                    break;
                default:
                    parseMessage = new ParseEml();
                    break;
            }

            try {
                emailResult = parseMessage.parseMsg(is);
            } catch(Exception e) {
                System.out.println("Error while parse msg " + e.getMessage());
            }

        } else {
            System.out.println("file.length = 0");
        }

        return emailResult;
    }
}
