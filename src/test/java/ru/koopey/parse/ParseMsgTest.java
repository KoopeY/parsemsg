package ru.koopey.parse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import javax.mail.MessagingException;
import org.apache.poi.hsmf.exceptions.ChunkNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.koopey.entity.EmailResult;

@ExtendWith(MockitoExtension.class)
public class ParseMsgTest {
  private final ParseMsg parser = new ParseMsg();

  @Test
  public void shouldParseEmail() throws IOException, MessagingException, ChunkNotFoundException {
    final URL resource = getClass().getClassLoader().getResource("email.msg");
    InputStream is = new FileInputStream(resource.getFile());

    final EmailResult emailResult = parser.parseMsg(is);
    assertEquals("time2talk@online-convert.com", emailResult.fromEmail());
    assertEquals("time2talk@online-convert.com", emailResult.toEmail());
    assertFalse(emailResult.body().isBlank());
  }
}
