package ru.koopey.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.io.File;
import java.nio.file.Files;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class EmailControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testParseEmailFile() throws Exception {
        var emailFile = new File("src/test/resources/email.eml");
        var file = new MockMultipartFile("file", "email.eml", "", Files.readAllBytes(emailFile.toPath()));
        mockMvc.perform(multipart("/email/parse").file(file))
            .andDo(print())
            .andExpect(status().isOk());
    }
}
