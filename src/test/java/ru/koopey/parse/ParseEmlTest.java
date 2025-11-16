package ru.koopey.parse;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.mail.MessagingException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class ParseEmlTest {

  private final ParseEml parser = new ParseEml();

  @Test
  public void parsesEmail() throws IOException, MessagingException {
    var resource = getClass().getClassLoader().getResource("email.eml");
    InputStream is = new FileInputStream(resource.getFile());

    var emailResult = parser.parseMsg(is);
    assertThat(emailResult.fromEmail()).isEqualTo("xxxx@xxxx.com");
    assertThat(emailResult.toEmail()).isEqualTo("xxxx@xxxx.com");
    assertThat(emailResult.body()).isEqualTo("Another PDF with Unicode chars in it");
  }
}
