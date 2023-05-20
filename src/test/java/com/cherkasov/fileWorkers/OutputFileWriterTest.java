package com.cherkasov.fileWorkers;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

public class OutputFileWriterTest {
    private static final String TEST_FILE_NAME = "output.txt";
    private static final Path TEST_FILE_PATH = Paths.get(TEST_FILE_NAME);

    private OutputFileWriter target;

    @BeforeEach
    void setUp() {
        target = new OutputFileWriter();
    }

    @AfterEach
    void tearDown() throws IOException {
        if (Files.exists(TEST_FILE_PATH)) {
            Files.delete(TEST_FILE_PATH);
        }
    }

    @Test
    void writeDataToFile_NullData_ShouldNotCreateFile() {
        target.writeDataToFile(null);

        assertFalse(Files.exists(TEST_FILE_PATH));
    }

    @Test
    void writeDataToFile_NullData_ShouldNotWriteToFile() throws IOException {
        String initialData = "Initial data";
        Files.writeString(TEST_FILE_PATH, initialData);

        target.writeDataToFile(null);

        assertTrue(Files.exists(TEST_FILE_PATH));
        assertEquals(initialData, Files.readString(TEST_FILE_PATH));
        assertDoesNotThrow(() -> target.writeDataToFile(null));
    }


    @Test
    void writeDataToFile_NonNullData_ShouldCreateFileAndWriteData() throws IOException {
        String data = "Test data";

        target.writeDataToFile(data);

        assertTrue(Files.exists(TEST_FILE_PATH));
        assertEquals(data, Files.readString(TEST_FILE_PATH));
        assertDoesNotThrow(() -> target.writeDataToFile(data));
    }

    @Test
    void writeDataToFile_AppendData_ShouldAppendDataToFile() throws IOException {
        String initialData = "Initial data";
        String appendedData = "Appended data";
        Files.writeString(TEST_FILE_PATH, initialData);

        target.writeDataToFile(appendedData);

        assertTrue(Files.exists(TEST_FILE_PATH));
        assertEquals(initialData + appendedData, Files.readString(TEST_FILE_PATH));
        assertDoesNotThrow(() -> target.writeDataToFile(appendedData));
    }
}
