package ru.koopey.parse;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import javax.mail.MessagingException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.koopey.entity.EmailResult;

@ExtendWith(MockitoExtension.class)
public class ParseEmlTest {
  private final ParseEml parser = new ParseEml();

  @Test
  public void shouldParseEmail() throws IOException, MessagingException {
    final URL resource = getClass().getClassLoader().getResource("email.eml");
    InputStream is = new FileInputStream(resource.getFile());

    final EmailResult emailResult = parser.parseMsg(is);
    assertEquals("xxxx@xxxx.com", emailResult.fromEmail());
    assertEquals("xxxx@xxxx.com", emailResult.toEmail());
    assertEquals("Another PDF with Unicode chars in it", emailResult.body());
  }
}
