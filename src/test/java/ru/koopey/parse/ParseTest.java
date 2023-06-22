package ru.koopey.parse;

import org.apache.poi.hsmf.exceptions.ChunkNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import ru.koopey.entity.EmailResult;

import javax.mail.MessagingException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.List;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class ParseTest {


    @Test
    void shouldParseFiles() throws ChunkNotFoundException, MessagingException, IOException {
        var fileContent = "content".getBytes();
        var file = new MockMultipartFile(
            "test.eml",
            "test.eml",
            "contentType",
                fileContent
        );
        var parsed = new EmailResult("subject", "body", "from@email.com", "to@email.com", Instant.now(), emptyList());

        var parserable = mock(Parserable.class);
        var parse = new Parse(List.of(parserable));

        given(parserable.extension()).willReturn(Extensions.EML);
        given(parserable.parseMsg(any(InputStream.class))).willReturn(parsed);

        var result = parse.parseFiles(new MultipartFile[]{file});

        assertThat(result).isEqualTo(parsed);
    }

    @Test
    void shouldRaiseExceptionIfNoFiles() throws ChunkNotFoundException, MessagingException, IOException {
        var parserable = mock(Parserable.class);
        var parse = new Parse(List.of(parserable));

        assertThatThrownBy(() -> parse.parseFiles(new MultipartFile[]{}))
            .isInstanceOf(FileNotFoundException.class);
    }

    @Test
    void shouldRaiseExceptionIfNoParsersWasFound() throws ChunkNotFoundException, MessagingException, IOException {
        var parserable = mock(Parserable.class);
        var fileContent = "content".getBytes();
        var file = new MockMultipartFile(
                "test.txt",
                "test.txt",
                "contentType",
                fileContent
        );
        given(parserable.extension()).willReturn(Extensions.EML);
        var parse = new Parse(List.of(parserable));

        assertThatThrownBy(() -> parse.parseFiles(new MultipartFile[]{file}))
                .isInstanceOf(FileNotFoundException.class);
    }
}