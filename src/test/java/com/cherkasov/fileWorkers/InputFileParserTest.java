package com.cherkasov.fileWorkers;

import com.cherkasov.service.Service;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

class InputFileParserTest {
    private static final String TEST_FILE_NAME = "input.txt";
    private static final Path TEST_FILE_PATH = getInputFilePath();
    private static final Charset CHARSET = StandardCharsets.UTF_8;

    @Mock
    private Service service;
    private InputFileParser target;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        target = new InputFileParser(service);
    }


    @AfterEach
    void tearDown() throws IOException {
        if (Files.exists(TEST_FILE_PATH)) {
            Files.delete(TEST_FILE_PATH);
        }
    }

    private static Path getInputFilePath() {
        try {
            URL url = InputFileParser.class.getProtectionDomain().getCodeSource().getLocation();
            Path jarPath = Paths.get(url.toURI());
            Path folderPath = jarPath.getParent();
            return folderPath.resolve(TEST_FILE_NAME);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }


    @Test
    void parseFile_ValidInputFile_CallsCorrectServiceMethods() throws IOException {
        String line1 = "u,1,1,bid";
        String line2 = "o,sell,1";
        String line3 = "q,best_bid";
        List<String> lines = Arrays.asList(line1, line2, line3);
        Files.write(TEST_FILE_PATH, lines, CHARSET);

        target.parseFile();

        verify(service).saveOrder(line1);
        verify(service).updateOrderSize(line2);
        verify(service).writeInfoInFile(line3);
        assertDoesNotThrow(() -> target.parseFile());
    }

    @Test
    void parseFile_EmptyFile_DoesNotCallServiceMethods() throws IOException {
        Files.writeString(TEST_FILE_PATH, "", CHARSET);

        target.parseFile();

        verifyNoInteractions(service);
        assertDoesNotThrow(() -> target.parseFile());
    }

    @Test
    void parseFile_InvalidLines_IgnoresInvalidLines() throws IOException {
        String validLine = "u,1,1,bid";
        String invalidLine = "invalidLine";
        List<String> lines = Arrays.asList(validLine, invalidLine);
        Files.write(TEST_FILE_PATH, lines, CHARSET);

        target.parseFile();

        verify(service).saveOrder(validLine);
        verifyNoMoreInteractions(service);
        assertDoesNotThrow(() -> target.parseFile());
    }
}
