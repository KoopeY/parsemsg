package ru.koopey.parse;

import org.apache.poi.hsmf.exceptions.ChunkNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class ParseMsgTest {
  private final ParseMsg parser = new ParseMsg();

  @Test
  public void shouldParseEmail() throws IOException, ChunkNotFoundException {
    var resource = getClass().getClassLoader().getResource("email.msg");
    InputStream is = new FileInputStream(resource.getFile());

    var emailResult = parser.parseMsg(is);
    assertThat(emailResult.fromEmail()).isEqualTo("time2talk@online-convert.com");
    assertThat(emailResult.toEmail()).isEqualTo("time2talk@online-convert.com");
    assertThat(emailResult.body().isBlank()).isFalse();
  }
}
