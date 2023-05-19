package com.cherkasov.fileWorkers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class OutputFileWriter {
    private static final String FILE_NAME = "output.txt";
    private static final Path FILE_PATH = Paths.get(FILE_NAME);

    public void writeDataToFile(String data) {
        try {
            if (!Files.exists(FILE_PATH)) {
                Files.createFile(FILE_PATH);
            }
            Files.writeString(FILE_PATH, data, StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
